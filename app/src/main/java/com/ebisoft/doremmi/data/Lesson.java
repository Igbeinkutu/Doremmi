package com.ebisoft.doremmi.data;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import java.util.Collections;
import java.util.List;

public final class Lesson {
    public final int number;
    @StringRes public final int title;
    @StringRes public final int description;
    public final List<LessonStep> steps;
    @Nullable public final String quizId;
    @RawRes public final int completionSound;

    Lesson(int number, @StringRes int title, @StringRes int description, List<LessonStep> steps,
           @Nullable String quizId, @RawRes int completionSound) {
        this.number = number;
        this.title = title;
        this.description = description;
        this.steps = Collections.unmodifiableList(steps);
        this.quizId = quizId;
        this.completionSound = completionSound;
    }

    public int stepCount() {
        return steps.size();
    }
}
