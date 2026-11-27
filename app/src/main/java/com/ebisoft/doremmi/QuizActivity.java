package com.ebisoft.doremmi;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ebisoft.doremmi.data.Lesson;
import com.ebisoft.doremmi.data.Lessons;
import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.data.Quiz;
import com.ebisoft.doremmi.data.QuizQuestion;
import com.ebisoft.doremmi.data.Quizzes;
import com.ebisoft.doremmi.notation.NotationView;
import com.ebisoft.doremmi.piano.Notes;
import com.ebisoft.doremmi.piano.PianoSoundBank;
import com.ebisoft.doremmi.ui.ResultSheet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

/** Runs any multiple-choice quiz with instant feedback, an optional timer and saved scores. */
public class QuizActivity extends BaseActivity {
    private static final String EXTRA_QUIZ = "quiz";
    private static final String STATE_INDEX = "index";
    private static final String STATE_CORRECT = "correct";
    private static final String STATE_REMAINING = "remaining";
    private static final String STATE_SELECTED = "selected";
    private static final String STATE_FINISHED = "finished";
    private static final long ADVANCE_DELAY_MS = 1200;

    private static final int[] CORRECT_CHORD = Notes.midi("C5", "E5", "G5");
    private static final int[] INCORRECT_CHORD = Notes.midi("C3", "Db3");

    private Quiz quiz;
    private ProgressRepository progress;
    private PianoSoundBank soundBank;
    private int applauseSound;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable advance = this::nextQuestion;

    private MaterialToolbar toolbar;
    private LinearProgressIndicator quizProgress;
    private NotationView notation;
    private TextView prompt;
    private TextView feedback;
    private View timerContainer;
    private CircularProgressIndicator timerProgress;
    private TextView timerText;
    private final MaterialButton[] options = new MaterialButton[4];
    private ColorStateList optionBackground;
    private ColorStateList optionStroke;
    private ColorStateList optionText;

    private int index;
    private int correctCount;
    private int selected = -1;
    private long remainingMs;
    private boolean finished;
    private CountDownTimer timer;

    public static Intent intent(Context context, String quizId) {
        return new Intent(context, QuizActivity.class).putExtra(EXTRA_QUIZ, quizId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra(EXTRA_QUIZ);
        quiz = id == null ? null : Quizzes.get(id);
        if (quiz == null) {
            finish();
            return;
        }
        progress = new ProgressRepository(this);
        soundBank = new PianoSoundBank(this);
        applauseSound = soundBank.loadEffect(R.raw.clap1);

        setContentView(R.layout.activity_quiz);
        toolbar = setupToolbar(quiz.title);
        quizProgress = findViewById(R.id.quiz_progress);
        notation = findViewById(R.id.quiz_notation);
        prompt = findViewById(R.id.quiz_prompt);
        feedback = findViewById(R.id.quiz_feedback);
        timerContainer = findViewById(R.id.timer_container);
        timerProgress = findViewById(R.id.timer_progress);
        timerText = findViewById(R.id.timer_text);
        int[] optionIds = {R.id.option_1, R.id.option_2, R.id.option_3, R.id.option_4};
        for (int i = 0; i < options.length; i++) {
            final int choice = i;
            options[i] = findViewById(optionIds[i]);
            options[i].setOnClickListener(v -> onOptionSelected(choice));
        }
        optionBackground = options[0].getBackgroundTintList();
        optionStroke = options[0].getStrokeColor();
        optionText = options[0].getTextColors();

        quizProgress.setMax(quiz.questions.size());
        if (quiz.timeLimitSeconds > 0) {
            timerContainer.setVisibility(View.VISIBLE);
            timerProgress.setMax(quiz.timeLimitSeconds);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmExit();
            }
        });

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(STATE_INDEX);
            correctCount = savedInstanceState.getInt(STATE_CORRECT);
            remainingMs = savedInstanceState.getLong(STATE_REMAINING);
            selected = savedInstanceState.getInt(STATE_SELECTED, -1);
            finished = savedInstanceState.getBoolean(STATE_FINISHED);
        } else {
            remainingMs = quiz.timeLimitSeconds * 1000L;
        }

        if (quiz.timeLimitSeconds > 0) {
            updateTimer(remainingMs);
        }
        if (finished) {
            showQuestion(Math.min(index, quiz.questions.size() - 1));
            showResults();
        } else {
            showQuestion(index);
            if (selected >= 0) {
                revealAnswer(selected, false);
                handler.postDelayed(advance, ADVANCE_DELAY_MS);
            }
        }
    }

    private void showQuestion(int position) {
        QuizQuestion question = quiz.questions.get(position);
        toolbar.setSubtitle(getString(R.string.game_question_counter, position + 1, quiz.questions.size()));
        quizProgress.setProgressCompat(position, true);
        notation.setScore(question.notation);
        prompt.setText(question.prompt);
        feedback.setText(null);
        for (int i = 0; i < options.length; i++) {
            MaterialButton button = options[i];
            boolean exists = i < question.optionCount();
            button.setVisibility(exists ? View.VISIBLE : View.GONE);
            if (exists) {
                button.setText(question.option(this, i));
            }
            resetOption(button);
        }
    }

    private void resetOption(MaterialButton button) {
        button.setBackgroundTintList(optionBackground);
        button.setStrokeColor(optionStroke);
        button.setTextColor(optionText);
        button.setIcon(null);
    }

    private void onOptionSelected(int choice) {
        if (selected >= 0 || finished) {
            return;
        }
        selected = choice;
        boolean correct = revealAnswer(choice, true);
        if (correct) {
            correctCount++;
        }
        handler.postDelayed(advance, ADVANCE_DELAY_MS);
    }

    /** Colours the chosen and correct answers. Returns whether the choice was right. */
    private boolean revealAnswer(int choice, boolean withSound) {
        QuizQuestion question = quiz.questions.get(index);
        boolean correct = choice == question.correctIndex;
        styleOption(options[question.correctIndex], R.color.feedback_correct_container,
                R.color.feedback_correct, R.drawable.ic_check_circle);
        if (!correct) {
            styleOption(options[choice], R.color.feedback_incorrect_container,
                    R.color.feedback_incorrect, R.drawable.ic_cancel);
        }
        feedback.setTextColor(ContextCompat.getColor(this,
                correct ? R.color.feedback_correct : R.color.feedback_incorrect));
        feedback.setText(correct ? getString(R.string.game_correct)
                : getString(R.string.game_incorrect, question.option(this, question.correctIndex)));
        if (withSound) {
            soundBank.play(correct ? CORRECT_CHORD : INCORRECT_CHORD);
        }
        return correct;
    }

    private void styleOption(MaterialButton button, int containerColor, int accentColor, int icon) {
        int accent = ContextCompat.getColor(this, accentColor);
        button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, containerColor)));
        button.setStrokeColor(ColorStateList.valueOf(accent));
        button.setIconResource(icon);
        button.setIconTint(ColorStateList.valueOf(accent));
        button.setTextColor(MaterialColors.getColor(button, com.google.android.material.R.attr.colorOnSurface));
    }

    private void nextQuestion() {
        selected = -1;
        index++;
        if (index >= quiz.questions.size()) {
            finishQuiz();
        } else {
            showQuestion(index);
        }
    }

    // region Timer

    private void startTimer() {
        if (quiz.timeLimitSeconds <= 0 || finished || timer != null) {
            return;
        }
        updateTimer(remainingMs);
        timer = new CountDownTimer(remainingMs, 250) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMs = millisUntilFinished;
                updateTimer(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                remainingMs = 0;
                timer = null;
                updateTimer(0);
                onTimeUp();
            }
        }.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void updateTimer(long millis) {
        int seconds = (int) Math.ceil(millis / 1000.0);
        timerText.setText(String.valueOf(seconds));
        timerProgress.setProgressCompat(seconds, true);
        timerContainer.setContentDescription(
                getResources().getQuantityString(R.plurals.game_time_remaining, seconds, seconds));
    }

    private void onTimeUp() {
        handler.removeCallbacks(advance);
        feedback.setTextColor(ContextCompat.getColor(this, R.color.feedback_incorrect));
        feedback.setText(R.string.game_times_up);
        finishQuiz();
    }

    // endregion

    private void finishQuiz() {
        if (finished) {
            return;
        }
        finished = true;
        stopTimer();
        handler.removeCallbacks(advance);
        quizProgress.setProgressCompat(quiz.questions.size(), true);
        int score = correctCount * Quiz.POINTS_PER_QUESTION;
        boolean newBest = progress.recordScore(quiz.id, score, quiz.maxScore());
        if (score >= quiz.passScore) {
            soundBank.playEffect(applauseSound);
        }
        showResults(newBest);
    }

    private void showResults() {
        showResults(false);
    }

    private void showResults(boolean newBest) {
        int score = correctCount * Quiz.POINTS_PER_QUESTION;
        int total = quiz.questions.size();
        boolean passed = score >= quiz.passScore;
        ResultSheet sheet = ResultSheet.with(this)
                .icon(passed ? R.drawable.ic_trophy : R.drawable.ic_replay,
                        passed ? androidx.appcompat.R.attr.colorPrimary : androidx.appcompat.R.attr.colorError)
                .title(getString(passed ? R.string.game_result_pass_title : R.string.game_result_fail_title))
                .score(score + "/" + quiz.maxScore())
                .message(getString(passed ? R.string.game_result_pass_message
                        : R.string.game_result_fail_message, correctCount, total))
                .highlight(newBest ? getString(R.string.game_new_best) : null)
                .primary(getString(R.string.action_retry), this::restart)
                .onCancel(this::finish);

        if (Quizzes.LESSON1.equals(quiz.id)) {
            Lesson next = Lessons.get(2);
            if (next != null && passed) {
                sheet.secondary(getString(R.string.action_next_lesson), () -> {
                    startActivity(LessonActivity.intent(this, next.number));
                    finish();
                });
            }
            sheet.tertiary(getString(R.string.action_back_to_lessons), this::finish);
        } else {
            sheet.secondary(getString(R.string.action_view_scores), () -> {
                startActivity(new Intent(this, ScoresActivity.class));
                finish();
            });
            sheet.tertiary(getString(R.string.action_back_to_games), this::finish);
        }
        sheet.show();
    }

    private void restart() {
        index = 0;
        correctCount = 0;
        selected = -1;
        finished = false;
        remainingMs = quiz.timeLimitSeconds * 1000L;
        showQuestion(0);
        startTimer();
    }

    private void confirmExit() {
        if (finished || (index == 0 && selected < 0)) {
            finish();
            return;
        }
        stopTimer();
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.game_exit_title)
                .setMessage(R.string.game_exit_message)
                .setPositiveButton(R.string.game_exit_confirm, (d, w) -> finish())
                .setNegativeButton(R.string.game_exit_stay, (d, w) -> startTimer())
                .setOnCancelListener(d -> startTimer())
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_INDEX, index);
        outState.putInt(STATE_CORRECT, correctCount);
        outState.putLong(STATE_REMAINING, remainingMs);
        outState.putInt(STATE_SELECTED, selected);
        outState.putBoolean(STATE_FINISHED, finished);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(advance);
        stopTimer();
        if (soundBank != null) {
            soundBank.release();
        }
        super.onDestroy();
    }
}
