package com.ebisoft.doremmi;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.data.Quiz;
import com.ebisoft.doremmi.data.Quizzes;
import com.ebisoft.doremmi.ui.MenuCardAdapter;
import com.ebisoft.doremmi.ui.MenuItem;

import java.util.ArrayList;
import java.util.List;

/** Lists the note games with their time limits and personal bests. */
public class GamesActivity extends BaseActivity {
    private final MenuCardAdapter adapter = new MenuCardAdapter();
    private ProgressRepository progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupToolbar(R.string.section_games);
        progress = new ProgressRepository(this);
        RecyclerView list = findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<MenuItem> items = new ArrayList<>();
        for (Quiz quiz : Quizzes.games()) {
            ProgressRepository.ScoreRecord record = progress.getScore(quiz.id);
            MenuItem item = new MenuItem(quiz.icon, getString(quiz.title))
                    .overline(getString(R.string.game_round_length, quiz.timeLimitSeconds))
                    .description(getString(quiz.description))
                    .onClick(() -> startActivity(QuizActivity.intent(this, quiz.id)));
            if (record != null) {
                item.meta(getString(R.string.game_best_score, record.best, quiz.maxScore()), R.drawable.ic_trophy)
                        .progress(record.best, quiz.maxScore());
            } else {
                item.meta(getString(R.string.game_not_played), 0);
            }
            items.add(item);
        }
        adapter.submit(items);
    }
}
