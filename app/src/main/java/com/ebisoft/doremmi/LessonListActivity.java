package com.ebisoft.doremmi;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.data.Lesson;
import com.ebisoft.doremmi.data.Lessons;
import com.ebisoft.doremmi.data.ProgressRepository;
import com.ebisoft.doremmi.ui.MenuCardAdapter;
import com.ebisoft.doremmi.ui.MenuItem;

import java.util.ArrayList;
import java.util.List;

/** Lists every lesson with its progress so learners can start, resume or revisit one. */
public class LessonListActivity extends BaseActivity {
    private final MenuCardAdapter adapter = new MenuCardAdapter();
    private ProgressRepository progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupToolbar(R.string.section_lessons);
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
        for (Lesson lesson : Lessons.all()) {
            int number = lesson.number;
            boolean done = progress.isLessonComplete(number);
            MenuItem item = new MenuItem(done ? R.drawable.ic_check_circle : R.drawable.ic_school,
                    getString(lesson.title))
                    .overline(getString(R.string.lesson_number, number))
                    .description(getString(lesson.description))
                    .onClick(() -> startActivity(LessonActivity.intent(this, number)));
            if (progress.hasStartedLesson(number)) {
                int step = progress.getLessonStep(number) + 1;
                item.meta(getString(R.string.lesson_status_in_progress, step, lesson.stepCount()), 0)
                        .progress(step, lesson.stepCount());
            } else if (done) {
                item.meta(getString(R.string.lesson_status_done), R.drawable.ic_check_circle);
            } else {
                item.meta(getString(R.string.lesson_status_not_started), 0);
            }
            if (lesson.quizId != null) {
                String quizId = lesson.quizId;
                item.action(getString(R.string.lesson_practice_quiz),
                        () -> startActivity(QuizActivity.intent(this, quizId)));
            }
            items.add(item);
        }
        adapter.submit(items);
    }
}
