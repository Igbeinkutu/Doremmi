package com.ebisoft.doremmi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.ebisoft.doremmi.data.Lesson;
import com.ebisoft.doremmi.data.LessonStep;
import com.ebisoft.doremmi.data.Lessons;
import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.data.Quiz;
import com.ebisoft.doremmi.data.Quizzes;
import com.ebisoft.doremmi.notation.NotationView;
import com.ebisoft.doremmi.ui.LessonStepAdapter;
import com.ebisoft.doremmi.ui.ResultSheet;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Shows any lesson as swipeable steps above a playable keyboard. */
public class LessonActivity extends PianoBaseActivity {
    private static final String EXTRA_LESSON = "lesson";

    private Lesson lesson;
    private ProgressRepository progress;
    private MaterialToolbar toolbar;
    private ViewPager2 pager;
    private LinearProgressIndicator progressBar;
    private MaterialButton previousButton;
    private MaterialButton nextButton;
    private TextView guidedHint;

    private int[] guided = new int[0];
    private int guidedIndex;

    public static Intent intent(Context context, int lessonNumber) {
        return new Intent(context, LessonActivity.class).putExtra(EXTRA_LESSON, lessonNumber);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lesson = Lessons.get(getIntent().getIntExtra(EXTRA_LESSON, 1));
        if (lesson == null) {
            finish();
            return;
        }
        progress = new ProgressRepository(this);
        setContentView(R.layout.activity_lesson);
        toolbar = setupToolbar(lesson.title);
        setupPiano();

        progressBar = findViewById(R.id.lesson_progress);
        previousButton = findViewById(R.id.step_previous);
        nextButton = findViewById(R.id.step_next);
        guidedHint = findViewById(R.id.guided_hint);
        pager = findViewById(R.id.step_pager);
        pager.setAdapter(new LessonStepAdapter(lesson.steps, this::openMedia));
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                onStepChanged(position);
            }
        });

        previousButton.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() - 1));
        nextButton.setOnClickListener(v -> {
            int current = pager.getCurrentItem();
            if (current >= lesson.stepCount() - 1) {
                completeLesson();
            } else {
                pager.setCurrentItem(current + 1);
            }
        });

        if (savedInstanceState == null) {
            int start = progress.isLessonComplete(lesson.number) ? 0
                    : Math.min(progress.getLessonStep(lesson.number), lesson.stepCount() - 1);
            pager.setCurrentItem(start, false);
        }
        onStepChanged(pager.getCurrentItem());
    }

    private void onStepChanged(int position) {
        int total = lesson.stepCount();
        toolbar.setSubtitle(getString(R.string.lesson_toolbar_subtitle, lesson.number, position + 1, total));
        progressBar.setProgressCompat((position + 1) * 1000 / total, true);
        previousButton.setEnabled(position > 0);
        boolean last = position == total - 1;
        nextButton.setIconResource(last ? R.drawable.ic_check_circle : R.drawable.ic_chevron_right);
        nextButton.setContentDescription(getString(last ? R.string.action_finish : R.string.action_next));
        if (!progress.isLessonComplete(lesson.number)) {
            progress.saveLessonStep(lesson.number, position);
        }
        applyStepToPiano(lesson.steps.get(position));
    }

    private void applyStepToPiano(LessonStep step) {
        guided = step.guidedNotes();
        guidedIndex = 0;
        if (guided.length > 0) {
            piano.ensureVisible(toList(guided));
            piano.setHighlightedNotes(Collections.singletonList(guided[0]));
            guidedHint.setText(R.string.lesson_guided_hint);
            guidedHint.setVisibility(View.VISIBLE);
            return;
        }
        guidedHint.setVisibility(View.GONE);
        List<Integer> highlighted = toList(step.highlightedNotes());
        if (!highlighted.isEmpty()) {
            piano.ensureVisible(highlighted);
        }
        piano.setHighlightedNotes(highlighted);
    }

    @Override
    protected void onPianoKeyPressed(int midi) {
        super.onPianoKeyPressed(midi);
        if (guidedIndex >= guided.length || midi != guided[guidedIndex]) {
            return;
        }
        guidedIndex++;
        if (guidedIndex == guided.length) {
            piano.clearHighlights();
            guidedHint.setText(R.string.lesson_guided_done);
        } else {
            piano.setHighlightedNotes(Collections.singletonList(guided[guidedIndex]));
        }
    }

    private void completeLesson() {
        progress.markLessonComplete(lesson.number);
        playCompletionSound();

        Lesson next = Lessons.next(lesson.number);
        Quiz quiz = lesson.quizId == null ? null : Quizzes.get(lesson.quizId);
        ResultSheet sheet = ResultSheet.with(this)
                .icon(R.drawable.ic_trophy, androidx.appcompat.R.attr.colorPrimary)
                .title(getString(R.string.lesson_complete_title, lesson.number))
                .message(next == null ? getString(R.string.lesson_complete_last_message)
                        : getString(R.string.lesson_complete_message, getString(lesson.title)));

        if (quiz != null) {
            sheet.primary(getString(R.string.action_take_quiz), () -> {
                startActivity(QuizActivity.intent(this, quiz.id));
                finish();
            });
            if (next != null) {
                sheet.secondary(getString(R.string.action_next_lesson), () -> openLesson(next));
            }
        } else if (next != null) {
            sheet.primary(getString(R.string.action_next_lesson), () -> openLesson(next));
        } else {
            sheet.primary(getString(R.string.home_games_action), () -> {
                startActivity(new Intent(this, GamesActivity.class));
                finish();
            });
        }
        sheet.tertiary(getString(R.string.action_back_to_lessons), this::backToLessons).show();
    }

    private void openLesson(Lesson next) {
        startActivity(intent(this, next.number));
        finish();
    }

    private void backToLessons() {
        Intent intent = new Intent(this, LessonListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void playCompletionSound() {
        MediaPlayer player = MediaPlayer.create(getApplicationContext(), lesson.completionSound);
        if (player != null) {
            player.setOnCompletionListener(MediaPlayer::release);
            player.start();
        }
    }

    private void openMedia(LessonStep step) {
        Dialog dialog = new Dialog(this, R.style.Theme_Doremmi_FullScreenDialog);
        dialog.setContentView(R.layout.dialog_media);
        ImageView image = dialog.findViewById(R.id.media_image);
        NotationView notation = dialog.findViewById(R.id.media_notation);
        if (step.notation != null) {
            image.setVisibility(View.GONE);
            notation.setScore(step.notation);
            notation.setContentDescription(getString(step.mediaDescription));
        } else {
            notation.setVisibility(View.GONE);
            image.setImageResource(step.image);
            image.setContentDescription(getString(step.mediaDescription));
        }
        dialog.findViewById(R.id.media_close).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            boolean night = (getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
            WindowInsetsControllerCompat controller =
                    WindowCompat.getInsetsController(window, window.getDecorView());
            controller.setAppearanceLightStatusBars(!night);
            controller.setAppearanceLightNavigationBars(!night);
        }
    }

    private static List<Integer> toList(int[] notes) {
        List<Integer> list = new ArrayList<>(notes.length);
        for (int note : notes) {
            list.add(note);
        }
        return list;
    }
}
