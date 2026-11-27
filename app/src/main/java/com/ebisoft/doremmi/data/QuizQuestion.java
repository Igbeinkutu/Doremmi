package com.ebisoft.doremmi.data;

import android.content.Context;

import androidx.annotation.StringRes;

import com.ebisoft.doremmi.notation.Score;

/** A multiple-choice question about a piece of notation. Options are literal text or string ids. */
public final class QuizQuestion {
    @StringRes public final int prompt;
    public final Score notation;
    private final String[] optionText;
    private final int[] optionRes;
    public final int correctIndex;

    private QuizQuestion(@StringRes int prompt, Score notation, String[] optionText, int[] optionRes,
                         int correctIndex) {
        this.prompt = prompt;
        this.notation = notation;
        this.optionText = optionText;
        this.optionRes = optionRes;
        this.correctIndex = correctIndex;
    }

    static QuizQuestion text(@StringRes int prompt, Score notation, int correctIndex,
                             String... options) {
        return new QuizQuestion(prompt, notation, options, null, correctIndex);
    }

    static QuizQuestion res(@StringRes int prompt, Score notation, int correctIndex,
                            int... options) {
        return new QuizQuestion(prompt, notation, null, options, correctIndex);
    }

    public int optionCount() {
        return optionText != null ? optionText.length : optionRes.length;
    }

    public String option(Context context, int index) {
        return optionText != null ? optionText[index] : context.getString(optionRes[index]);
    }
}
