package com.ebisoft.doremmi;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.data.Lessons;
import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.data.Quiz;
import com.ebisoft.doremmi.data.Quizzes;
import com.ebisoft.doremmi.ui.MenuCardAdapter;
import com.ebisoft.doremmi.ui.MenuItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/** Shows lesson completion and the best and latest score for every quiz. */
public class ScoresActivity extends BaseActivity {
    private final MenuCardAdapter adapter = new MenuCardAdapter();
    private ProgressRepository progress;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupToolbar(R.string.scores_title);
        progress = new ProgressRepository(this);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<MenuItem> items = new ArrayList<>();
        int total = Lessons.all().size();
        int done = progress.completedLessonCount(total);
        items.add(new MenuItem(R.drawable.ic_school, getString(R.string.scores_lessons_header))
                .description(getString(R.string.scores_lessons_progress, done, total))
                .progress(done, total)
                .onClick(() -> startActivity(new android.content.Intent(this, LessonListActivity.class))));

        for (Quiz quiz : Quizzes.all()) {
            ProgressRepository.ScoreRecord record = progress.getScore(quiz.id);
            MenuItem item = new MenuItem(quiz.icon, getString(quiz.title))
                    .onClick(() -> startActivity(QuizActivity.intent(this, quiz.id)));
            if (record == null) {
                item.description(getString(R.string.game_not_played));
            } else {
                int max = record.max > 0 ? record.max : quiz.maxScore();
                item.description(getString(R.string.scores_best, record.best, max))
                        .meta(record.lastPlayedAt > 0
                                ? getString(R.string.scores_last, record.last, max, whenPlayed(record.lastPlayedAt))
                                : getString(R.string.scores_last_no_date, record.last, max), 0)
                        .progress(record.best, max);
            }
            items.add(item);
        }
        adapter.submit(items);
    }

    private CharSequence whenPlayed(long time) {
        long now = System.currentTimeMillis();
        if (now - time < DateUtils.MINUTE_IN_MILLIS) {
            return getString(R.string.scores_just_now);
        }
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            confirmReset();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmReset() {
        new MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_delete)
                .setTitle(R.string.scores_reset_title)
                .setMessage(R.string.scores_reset_message)
                .setPositiveButton(R.string.scores_reset_confirm, (d, w) -> {
                    progress.reset();
                    refresh();
                    Snackbar.make(list, R.string.scores_reset_done, Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }
}
