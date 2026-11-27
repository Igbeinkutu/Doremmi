package com.ebisoft.doremmi.notation;

import androidx.annotation.Nullable;

import com.ebisoft.doremmi.piano.Notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A small, immutable piece of music notation that {@link NotationView} can draw. */
public final class Score {

    public enum Mode { STAFF, SINGLE_NOTE, EQUATION }

    public enum Clef { NONE, TREBLE, BASS }

    public enum Duration {
        WHOLE(0), HALF(0), QUARTER(0), EIGHTH(1), SIXTEENTH(2);

        final int flags;

        Duration(int flags) {
            this.flags = flags;
        }

        boolean hollow() {
            return this == WHOLE || this == HALF;
        }

        boolean hasStem() {
            return this != WHOLE;
        }
    }

    public static final class Element {
        public enum Type { NOTE, BAR, FINAL_BAR }

        public final Type type;
        public final int midi;
        public final Duration duration;
        @Nullable public final String label;
        @Nullable public final String fingering;

        Element(Type type, int midi, Duration duration, @Nullable String label,
                @Nullable String fingering) {
            this.type = type;
            this.midi = midi;
            this.duration = duration;
            this.label = label;
            this.fingering = fingering;
        }
    }

    public final Mode mode;
    public final Clef clef;
    public final int timeTop;
    public final int timeBottom;
    public final List<Element> elements;
    /** Left- and right-hand note values for {@link Mode#EQUATION} and {@link Mode#SINGLE_NOTE}. */
    public final Duration primary;
    public final Duration secondary;

    private Score(Builder b) {
        mode = b.mode;
        clef = b.clef;
        timeTop = b.timeTop;
        timeBottom = b.timeBottom;
        elements = Collections.unmodifiableList(new ArrayList<>(b.elements));
        primary = b.primary;
        secondary = b.secondary;
    }

    public boolean hasLabels() {
        for (Element e : elements) {
            if (e.label != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFingerings() {
        for (Element e : elements) {
            if (e.fingering != null) {
                return true;
            }
        }
        return false;
    }

    public static Builder staff(Clef clef) {
        Builder b = new Builder(Mode.STAFF);
        b.clef = clef;
        return b;
    }

    /** One large note with no staff, used to show what a note value looks like. */
    public static Score singleNote(Duration duration) {
        Builder b = new Builder(Mode.SINGLE_NOTE);
        b.primary = duration;
        return b.build();
    }

    /** "lhs = [ ] rhs's": how many of one note value fit into another. */
    public static Score equation(Duration lhs, Duration rhs) {
        Builder b = new Builder(Mode.EQUATION);
        b.primary = lhs;
        b.secondary = rhs;
        return b.build();
    }

    public static final class Builder {
        private final Mode mode;
        private Clef clef = Clef.NONE;
        private int timeTop;
        private int timeBottom;
        private Duration primary = Duration.QUARTER;
        private Duration secondary = Duration.QUARTER;
        private final List<Element> elements = new ArrayList<>();

        private Builder(Mode mode) {
            this.mode = mode;
        }

        public Builder time(int top, int bottom) {
            timeTop = top;
            timeBottom = bottom;
            return this;
        }

        public Builder note(String name, Duration duration) {
            return note(name, duration, null, null);
        }

        public Builder note(String name, Duration duration, @Nullable String label,
                            @Nullable String fingering) {
            elements.add(new Element(Element.Type.NOTE, Notes.midi(name), duration, label, fingering));
            return this;
        }

        public Builder notes(Duration duration, String... names) {
            for (String name : names) {
                note(name, duration);
            }
            return this;
        }

        public Builder bar() {
            elements.add(new Element(Element.Type.BAR, 0, Duration.QUARTER, null, null));
            return this;
        }

        public Builder finalBar() {
            elements.add(new Element(Element.Type.FINAL_BAR, 0, Duration.QUARTER, null, null));
            return this;
        }

        public Score build() {
            return new Score(this);
        }
    }
}
