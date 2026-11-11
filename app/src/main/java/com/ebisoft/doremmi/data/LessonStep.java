package com.ebisoft.doremmi.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.ebisoft.doremmi.notation.Score;
import com.ebisoft.doremmi.piano.Notes;

/**
 * One page of a lesson: text, an image or notation, plus optional piano keys to highlight
 * (shown together) or to play as a guided sequence (shown one at a time).
 */
public final class LessonStep {
    @StringRes public final int text;
    @DrawableRes public final int image;
    @Nullable public final Score notation;
    @StringRes public final int mediaDescription;
    private int[] highlighted = new int[0];
    private int[] guided = new int[0];

    private LessonStep(@StringRes int text, @DrawableRes int image, @Nullable Score notation,
                       @StringRes int mediaDescription) {
        this.text = text;
        this.image = image;
        this.notation = notation;
        this.mediaDescription = mediaDescription;
    }

    public static LessonStep text(@StringRes int text) {
        return new LessonStep(text, 0, null, 0);
    }

    public static LessonStep image(@DrawableRes int image, @StringRes int description) {
        return new LessonStep(0, image, null, description);
    }

    public static LessonStep notation(Score score, @StringRes int description) {
        return new LessonStep(0, 0, score, description);
    }

    public LessonStep highlight(String... notes) {
        highlighted = Notes.midi(notes);
        return this;
    }

    public LessonStep guided(String... notes) {
        guided = Notes.midi(notes);
        return this;
    }

    public int[] highlightedNotes() {
        return highlighted.clone();
    }

    public int[] guidedNotes() {
        return guided.clone();
    }

    public boolean hasMedia() {
        return image != 0 || notation != null;
    }
}
