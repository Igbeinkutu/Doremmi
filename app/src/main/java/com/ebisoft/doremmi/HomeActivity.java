package com.ebisoft.doremmi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.data.Lesson;
import com.ebisoft.doremmi.data.Lessons;
import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.ui.HomeAdapter;

import java.util.Arrays;

/** Launcher dashboard with a shortcut back into the current lesson and a tile per section. */
public class HomeActivity extends BaseActivity {
    private HomeAdapter adapter;
    private ProgressRepository progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progress = new ProgressRepository(this);

        adapter = new HomeAdapter(Arrays.asList(
                new HomeAdapter.Tile(R.drawable.ic_school, R.string.section_lessons,
                        R.string.section_lessons_desc, () -> open(LessonListActivity.class)),
                new HomeAdapter.Tile(R.drawable.ic_piano, R.string.section_free_play,
                        R.string.section_free_play_desc, () -> open(FreeStyleActivity.class)),
                new HomeAdapter.Tile(R.drawable.ic_library_music, R.string.section_songs,
                        R.string.section_songs_desc, () -> open(SongsActivity.class)),
                new HomeAdapter.Tile(R.drawable.ic_extension, R.string.section_games,
                        R.string.section_games_desc, () -> open(GamesActivity.class)),
                new HomeAdapter.Tile(R.drawable.ic_trophy, R.string.section_scores,
                        R.string.section_scores_desc, () -> open(ScoresActivity.class))));

        int columns = getResources().getInteger(R.integer.home_grid_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columns);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) == HomeAdapter.TYPE_TILE ? 1 : columns;
            }
        });
        RecyclerView grid = findViewById(R.id.home_grid);
        grid.setLayoutManager(layoutManager);
        grid.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setContinueCard(buildContinueCard());
    }

    private HomeAdapter.ContinueCard buildContinueCard() {
        Lesson resume = Lessons.get(progress.getLastLesson());
        if (resume == null || !progress.hasStartedLesson(resume.number)) {
            resume = null;
            for (Lesson lesson : Lessons.all()) {
                if (progress.hasStartedLesson(lesson.number)) {
                    resume = lesson;
                    break;
                }
            }
        }
        if (resume != null) {
            int number = resume.number;
            int step = progress.getLessonStep(number) + 1;
            return new HomeAdapter.ContinueCard(
                    getString(R.string.home_continue_title),
                    getString(R.string.home_continue_subtitle, getString(resume.title), step,
                            resume.stepCount()),
                    getString(R.string.home_continue_action),
                    step, resume.stepCount(),
                    () -> startActivity(LessonActivity.intent(this, number)));
        }

        Lesson next = null;
        for (Lesson lesson : Lessons.all()) {
            if (!progress.isLessonComplete(lesson.number)) {
                next = lesson;
                break;
            }
        }
        if (next == null) {
            return new HomeAdapter.ContinueCard(
                    getString(R.string.home_all_done_title),
                    getString(R.string.home_all_done_subtitle),
                    getString(R.string.home_games_action),
                    -1, 0,
                    () -> open(GamesActivity.class));
        }

        int number = next.number;
        boolean firstTime = progress.completedLessonCount(Lessons.all().size()) == 0;
        return new HomeAdapter.ContinueCard(
                getString(firstTime ? R.string.home_start_title : R.string.home_continue_title),
                firstTime ? getString(R.string.home_start_subtitle)
                        : getString(R.string.lesson_number, number) + " · " + getString(next.title),
                getString(R.string.home_start_action),
                -1, 0,
                () -> startActivity(LessonActivity.intent(this, number)));
    }

    private void open(Class<?> screen) {
        startActivity(new Intent(this, screen));
    }
}
