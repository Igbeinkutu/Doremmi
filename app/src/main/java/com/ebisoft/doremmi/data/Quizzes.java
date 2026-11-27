package com.ebisoft.doremmi.data;

import static com.ebisoft.doremmi.data.QuizQuestion.res;
import static com.ebisoft.doremmi.data.QuizQuestion.text;
import static com.ebisoft.doremmi.notation.Score.Duration.EIGHTH;
import static com.ebisoft.doremmi.notation.Score.Duration.HALF;
import static com.ebisoft.doremmi.notation.Score.Duration.QUARTER;
import static com.ebisoft.doremmi.notation.Score.Duration.SIXTEENTH;
import static com.ebisoft.doremmi.notation.Score.Duration.WHOLE;

import androidx.annotation.Nullable;

import com.ebisoft.doremmi.R;
import com.ebisoft.doremmi.notation.Score;
import com.ebisoft.doremmi.notation.Score.Clef;

import java.util.Arrays;
import java.util.List;

/** Quiz content. Answer keys match the original games. */
public final class Quizzes {
    public static final String NAME_THAT_NOTE = "name_that_note";
    public static final String NOTE_MATHS = "note_maths";
    public static final String IDENTIFY_NOTE = "identify_note";
    public static final String WORD_MATCH = "word_match";
    public static final String LESSON1 = "lesson1_quiz";

    private static List<Quiz> all;

    private Quizzes() {
    }

    public static synchronized List<Quiz> all() {
        if (all == null) {
            all = Arrays.asList(nameThatNote(), noteMaths(), identifyNote(), wordMatch(), lesson1());
        }
        return all;
    }

    /** Quizzes shown on the Games screen. */
    public static List<Quiz> games() {
        return all().subList(0, 4);
    }

    @Nullable
    public static Quiz get(String id) {
        for (Quiz quiz : all()) {
            if (quiz.id.equals(id)) {
                return quiz;
            }
        }
        return null;
    }

    private static Score treble(String note) {
        return Score.staff(Clef.TREBLE).note(note, QUARTER).build();
    }

    private static Score bass(String note) {
        return Score.staff(Clef.BASS).note(note, QUARTER).build();
    }

    private static Score word(Clef clef, String... notes) {
        return Score.staff(clef).notes(WHOLE, notes).build();
    }

    private static Quiz nameThatNote() {
        int p = R.string.prompt_note_value;
        return new Quiz(NAME_THAT_NOTE, R.string.quiz_name_that_note, R.string.quiz_name_that_note_desc,
                R.drawable.ic_music_note, 20, 11, Arrays.asList(
                res(p, Score.singleNote(WHOLE), 0,
                        R.string.note_semibreve, R.string.note_quarter, R.string.note_quaver, R.string.note_minim),
                res(p, Score.singleNote(QUARTER), 2,
                        R.string.note_semibreve, R.string.note_quaver, R.string.note_crotchet, R.string.note_minim),
                res(p, Score.singleNote(SIXTEENTH), 0,
                        R.string.note_semiquaver, R.string.note_quaver, R.string.note_semibreve, R.string.note_minim),
                res(p, Score.singleNote(EIGHTH), 2,
                        R.string.note_semiquaver, R.string.note_crotchet, R.string.note_quaver, R.string.note_minim),
                res(p, Score.singleNote(HALF), 3,
                        R.string.note_semiquaver, R.string.note_quaver, R.string.note_demisemiquaver, R.string.note_minim)
        ));
    }

    private static Quiz noteMaths() {
        int p = R.string.prompt_note_maths;
        return new Quiz(NOTE_MATHS, R.string.quiz_note_maths, R.string.quiz_note_maths_desc,
                R.drawable.ic_calculate, 20, 11, Arrays.asList(
                text(p, Score.equation(HALF, EIGHTH), 2, "2", "3", "4", "5"),
                text(p, Score.equation(WHOLE, EIGHTH), 3, "4", "6", "12", "8"),
                text(p, Score.equation(WHOLE, QUARTER), 3, "1", "2", "3", "4"),
                text(p, Score.equation(WHOLE, HALF), 0, "2", "4", "6", "8"),
                text(p, Score.equation(HALF, QUARTER), 0, "2", "4", "1", "3"),
                text(p, Score.equation(QUARTER, EIGHTH), 0, "2", "4", "1", "3")
        ));
    }

    private static Quiz identifyNote() {
        int p = R.string.prompt_identify_note;
        return new Quiz(IDENTIFY_NOTE, R.string.quiz_identify_note, R.string.quiz_identify_note_desc,
                R.drawable.ic_search, 30, 20, Arrays.asList(
                text(p, treble("C5"), 2, "A", "B", "C", "D"),
                text(p, treble("E5"), 0, "E", "B", "C", "D"),
                text(p, bass("F3"), 2, "A", "G", "F", "B"),
                text(p, bass("G3"), 2, "E", "C", "G", "D"),
                text(p, treble("F4"), 1, "A", "F", "G", "B"),
                text(p, treble("A4"), 0, "A", "B", "D", "F"),
                text(p, bass("C3"), 3, "B", "G", "D", "C"),
                text(p, Score.staff(Clef.BASS).note("B3", WHOLE).build(), 1, "A", "B", "D", "G")
        ));
    }

    private static Quiz wordMatch() {
        int p = R.string.prompt_word_match;
        return new Quiz(WORD_MATCH, R.string.quiz_word_match, R.string.quiz_word_match_desc,
                R.drawable.ic_text_fields, 50, 20, Arrays.asList(
                text(p, word(Clef.BASS, "A3", "C4", "E3"), 3, "BEE", "CAB", "EGG", "ACE"),
                text(p, word(Clef.BASS, "F3", "E3", "D3"), 1, "EGG", "FED", "ACE", "CAFE"),
                text(p, word(Clef.TREBLE, "B4", "E5", "A4", "D5"), 2, "FEED", "CAFE", "BEAD", "EGG"),
                text(p, word(Clef.BASS, "E3", "G3", "G3"), 0, "EGG", "FED", "CAB", "HEAD"),
                text(p, word(Clef.BASS, "C3", "A3", "G3", "E3"), 2, "EDGE", "CAFE", "CAGE", "BEAD"),
                text(p, word(Clef.TREBLE, "E4", "D4", "G4", "E4"), 1, "BEED", "EDGE", "EGG", "ACE"),
                text(p, word(Clef.TREBLE, "C4", "A4", "F4", "E4"), 0, "CAFE", "CAGE", "ACE", "EDGE"),
                text(p, word(Clef.TREBLE, "C5", "A4", "G4", "E4"), 3, "FEED", "CAFE", "BEAD", "CAGE"),
                text(p, word(Clef.BASS, "C3", "A3", "B3"), 2, "EGG", "FED", "CAB", "CAD"),
                text(p, word(Clef.BASS, "B3", "E3", "E3"), 2, "CAB", "BAD", "BEE", "BED"),
                text(p, word(Clef.BASS, "B3", "E3", "G3"), 3, "CAB", "BAD", "BEE", "BEG"),
                text(p, word(Clef.BASS, "D3", "A3", "D3"), 2, "CAB", "BAD", "DAD", "BED")
        ));
    }

    private static Quiz lesson1() {
        return new Quiz(LESSON1, R.string.quiz_lesson1, R.string.quiz_lesson1_desc,
                R.drawable.ic_help, 0, 11, Arrays.asList(
                text(R.string.prompt_identify_note, treble("C5"), 2, "A", "B", "C", "D"),
                text(R.string.prompt_identify_note, treble("E5"), 0, "E", "B", "C", "D"),
                text(R.string.prompt_word_match, word(Clef.TREBLE, "C4", "A4", "F4", "E4"), 0,
                        "CAFE", "CAGE", "ACE", "EDGE"),
                text(R.string.prompt_identify_note, treble("F4"), 1, "A", "F", "G", "B"),
                text(R.string.prompt_identify_note, treble("A4"), 0, "A", "B", "D", "F")
        ));
    }
}
