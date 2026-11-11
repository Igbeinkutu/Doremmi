package com.ebisoft.doremmi.data;

import static com.ebisoft.doremmi.data.LessonStep.image;
import static com.ebisoft.doremmi.data.LessonStep.notation;
import static com.ebisoft.doremmi.data.LessonStep.text;
import static com.ebisoft.doremmi.notation.Score.Duration.HALF;
import static com.ebisoft.doremmi.notation.Score.Duration.QUARTER;

import androidx.annotation.Nullable;

import com.ebisoft.doremmi.R;
import com.ebisoft.doremmi.notation.Score;
import com.ebisoft.doremmi.notation.Score.Clef;

import java.util.Arrays;
import java.util.List;

/** Lesson content. */
public final class Lessons {

    private static List<Lesson> lessons;

    private Lessons() {
    }

    public static synchronized List<Lesson> all() {
        if (lessons == null) {
            lessons = Arrays.asList(lesson1(), lesson2(), lesson3(), lesson4(), lesson5());
        }
        return lessons;
    }

    @Nullable
    public static Lesson get(int number) {
        for (Lesson lesson : all()) {
            if (lesson.number == number) {
                return lesson;
            }
        }
        return null;
    }

    @Nullable
    public static Lesson next(int number) {
        return get(number + 1);
    }

    private static Lesson lesson1() {
        return new Lesson(1, R.string.lesson1_title, R.string.lesson1_desc, Arrays.asList(
                text(R.string.l1_s1),
                text(R.string.l1_s2),
                text(R.string.l1_s3),
                text(R.string.l1_s4),
                text(R.string.l1_s5),
                text(R.string.l1_s6),
                text(R.string.l1_s7),
                text(R.string.l1_s8).highlight("C4"),
                text(R.string.l1_s9).highlight("D4"),
                text(R.string.l1_s10).highlight("E4"),
                text(R.string.l1_s11).highlight("F4"),
                text(R.string.l1_s12).highlight("G4"),
                text(R.string.l1_s13).highlight("A4"),
                text(R.string.l1_s14).highlight("B4"),
                text(R.string.l1_s15).highlight("C5"),
                text(R.string.l1_s16),
                text(R.string.l1_s17),
                text(R.string.l1_s18),
                text(R.string.l1_s19),
                text(R.string.l1_s20),
                text(R.string.l1_s21),
                text(R.string.l1_s22),
                text(R.string.l1_s23),
                text(R.string.l1_s24),
                text(R.string.l1_s25),
                text(R.string.l1_s26),
                text(R.string.l1_s27).highlight("Db4"),
                text(R.string.l1_s28).highlight("Db4"),
                text(R.string.l1_s29).highlight("Eb4"),
                text(R.string.l1_s30),
                image(R.drawable.c_major_scale_piano, R.string.l1_img_c_major_scale),
                text(R.string.l1_s32),
                text(R.string.l1_s33),
                image(R.drawable.sitting_and_finger_image, R.string.l1_img_sitting)
        ), Quizzes.LESSON1, R.raw.clap1);
    }

    private static Lesson lesson2() {
        Score song1 = Score.staff(Clef.TREBLE).time(4, 4)
                .note("C4", QUARTER, "C", "1").note("D4", QUARTER, "D", "2").note("E4", HALF, "E", "3")
                .bar()
                .note("C4", QUARTER, "C", null).note("D4", QUARTER, "D", null).note("E4", HALF, "E", null)
                .bar()
                .note("C4", QUARTER, "I", null).note("D4", QUARTER, "can", null)
                .note("E4", QUARTER, "play", null).note("D4", QUARTER, null, null)
                .finalBar()
                .build();
        Score song2 = Score.staff(Clef.TREBLE)
                .note("C4", QUARTER, "C", null).note("D4", QUARTER, "D", null).note("E4", HALF, "E", null)
                .bar()
                .note("E4", QUARTER, "E", null).note("D4", QUARTER, "D", null).note("C4", HALF, "C", null)
                .bar()
                .note("E4", QUARTER, "E", null).note("D4", QUARTER, "D", null).note("C4", HALF, "C", null)
                .finalBar()
                .build();
        Score song3 = Score.staff(Clef.TREBLE)
                .note("C4", QUARTER, "Up", null).note("D4", QUARTER, "and", null)
                .note("E4", QUARTER, "down", null).note("D4", QUARTER, "to", null)
                .bar()
                .note("C4", QUARTER, "Mid-", null).note("C4", QUARTER, "dle", null)
                .note("C4", HALF, "C.", null)
                .finalBar()
                .build();

        return new Lesson(2, R.string.lesson2_title, R.string.lesson2_desc, Arrays.asList(
                text(R.string.l2_s1),
                image(R.drawable.introducing_piano_fingering, R.string.l2_img_fingering),
                image(R.drawable.c_pentascale, R.string.l2_img_pentascale),
                text(R.string.l2_s4),
                image(R.drawable.right_hand_fingering_piano, R.string.l2_img_right_hand)
                        .guided("C4", "D4", "E4", "F4", "G4"),
                text(R.string.l2_s6),
                image(R.drawable.left_hand_fingering_piano, R.string.l2_img_left_hand)
                        .guided("C3", "D3", "E3", "F3", "G3"),
                text(R.string.l2_s8),
                text(R.string.l2_s9),
                text(R.string.l2_s10),
                text(R.string.l2_s11),
                notation(Score.staff(Clef.NONE).build(), R.string.l2_notation_staff),
                text(R.string.l2_s13),
                text(R.string.l2_s14),
                text(R.string.l2_s15),
                text(R.string.l2_s16),
                notation(Score.staff(Clef.TREBLE).build(), R.string.l2_notation_treble),
                text(R.string.l2_s18),
                notation(Score.staff(Clef.BASS).build(), R.string.l2_notation_bass),
                text(R.string.l2_s20),
                text(R.string.l2_s21),
                image(R.drawable.treble_staff_names, R.string.l2_img_treble_names),
                text(R.string.l2_s23),
                text(R.string.l2_s24),
                text(R.string.l2_s25),
                text(R.string.l2_s26),
                text(R.string.l2_s27),
                image(R.drawable.ledger_lines, R.string.l2_img_ledger),
                text(R.string.l2_s29).highlight("C4"),
                text(R.string.l2_s30).highlight("C4"),
                text(R.string.l2_s31),
                text(R.string.l2_s32),
                notation(song1, R.string.l2_notation_song1)
                        .guided("C4", "D4", "E4", "C4", "D4", "E4", "C4", "D4", "E4", "D4"),
                notation(song2, R.string.l2_notation_song2)
                        .guided("C4", "D4", "E4", "E4", "D4", "C4", "E4", "D4", "C4"),
                notation(song3, R.string.l2_notation_song3)
                        .guided("C4", "D4", "E4", "D4", "C4", "C4", "C4"),
                text(R.string.l2_s36),
                image(R.drawable.c_major_scale_fingering, R.string.l2_img_c_major_fingering)
                        .guided("C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5"),
                text(R.string.l2_s38),
                image(R.drawable.song_treble4_4, R.string.l2_img_song_treble)
        ), null, R.raw.crowd_applause);
    }

    private static Lesson lesson3() {
        Score leftHand = Score.staff(Clef.BASS)
                .note("C3", QUARTER, null, "5").note("D3", QUARTER, null, "4")
                .note("E3", QUARTER, null, "3").note("F3", QUARTER, null, "2")
                .note("G3", QUARTER, null, "1")
                .build();

        return new Lesson(3, R.string.lesson3_title, R.string.lesson3_desc, Arrays.asList(
                text(R.string.l3_s1),
                text(R.string.l3_s2),
                image(R.drawable.bass_staff, R.string.l3_img_bass_staff),
                text(R.string.l3_s4),
                text(R.string.l3_s5),
                text(R.string.l3_s6),
                text(R.string.l3_s7),
                text(R.string.l3_s8),
                notation(leftHand, R.string.l3_notation_left_hand)
                        .guided("C3", "D3", "E3", "F3", "G3"),
                image(R.drawable.left_hand_five1, R.string.l3_img_left_five1),
                image(R.drawable.left_hand_five2, R.string.l3_img_left_five2),
                text(R.string.l3_s12),
                text(R.string.l3_s13),
                image(R.drawable.grand_staff, R.string.l3_img_grand_staff),
                text(R.string.l3_s15),
                image(R.drawable.grandstaff, R.string.l3_img_grandstaff_piano)
        ), null, R.raw.clap1);
    }

    private static Lesson lesson4() {
        return new Lesson(4, R.string.lesson4_title, R.string.lesson4_desc, Arrays.asList(
                text(R.string.l4_s1),
                image(R.drawable.notes_image1, R.string.l4_img_notes1),
                image(R.drawable.notes_image2, R.string.l4_img_notes2),
                image(R.drawable.note_tree, R.string.l4_img_note_tree),
                text(R.string.l4_s5),
                image(R.drawable.bar_line_measures, R.string.l4_img_bar_lines),
                text(R.string.l4_s7),
                image(R.drawable.time_signatures, R.string.l4_img_time_signatures),
                text(R.string.l4_s9),
                image(R.drawable.song_treble4_4, R.string.l4_img_song_treble),
                text(R.string.l4_s11),
                text(R.string.l4_s12),
                image(R.drawable.time_sig_explanation1, R.string.l4_img_time_sig1),
                image(R.drawable.time_sig_explanation2, R.string.l4_img_time_sig2)
        ), null, R.raw.clap1);
    }

    private static Lesson lesson5() {
        return new Lesson(5, R.string.lesson5_title, R.string.lesson5_desc, Arrays.asList(
                text(R.string.l5_s1),
                text(R.string.l5_s2),
                text(R.string.l5_s3),
                text(R.string.l5_s4),
                text(R.string.l5_s5)
        ), null, R.raw.clap1);
    }
}
