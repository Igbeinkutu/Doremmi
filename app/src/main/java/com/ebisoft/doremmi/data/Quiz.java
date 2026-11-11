package com.ebisoft.doremmi.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import java.util.Collections;
import java.util.List;

public final class Quiz {
    public static final int POINTS_PER_QUESTION = 5;

    public final String id;
    @StringRes public final int title;
    @StringRes public final int description;
    @DrawableRes public final int icon;
    /** Zero means untimed. */
    public final int timeLimitSeconds;
    /** Minimum points needed to pass. */
    public final int passScore;
    public final List<QuizQuestion> questions;

    Quiz(String id, @StringRes int title, @StringRes int description, @DrawableRes int icon,
         int timeLimitSeconds, int passScore, List<QuizQuestion> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.timeLimitSeconds = timeLimitSeconds;
        this.passScore = passScore;
        this.questions = Collections.unmodifiableList(questions);
    }

    public int maxScore() {
        return questions.size() * POINTS_PER_QUESTION;
    }
}
