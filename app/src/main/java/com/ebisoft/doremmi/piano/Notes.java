package com.ebisoft.doremmi.piano;

import java.util.Locale;

/** Note names and MIDI numbers. Black keys use flat names to match the sample files. */
public final class Notes {
    public static final int C3 = 48;
    public static final int C4 = 60;
    public static final int C5 = 72;
    public static final int C6 = 84;

    private static final String[] NAMES =
            {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    private static final boolean[] BLACK =
            {false, true, false, true, false, false, true, false, true, false, true, false};
    private static final String LETTERS = "CDEFGAB";

    private Notes() {
    }

    /** Parses names such as "C4", "Db4" or "F#3" into a MIDI number. */
    public static int midi(String name) {
        char letter = Character.toUpperCase(name.charAt(0));
        int pitchClass = new int[]{0, 2, 4, 5, 7, 9, 11}[LETTERS.indexOf(letter)];
        int i = 1;
        if (i < name.length() && name.charAt(i) == 'b') {
            pitchClass--;
            i++;
        } else if (i < name.length() && name.charAt(i) == '#') {
            pitchClass++;
            i++;
        }
        int octave = Integer.parseInt(name.substring(i));
        return (octave + 1) * 12 + pitchClass;
    }

    public static int[] midi(String... names) {
        int[] result = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = midi(names[i]);
        }
        return result;
    }

    public static String name(int midi) {
        return NAMES[midi % 12] + octave(midi);
    }

    /** Letter name without octave, for key labels. */
    public static String letter(int midi) {
        return NAMES[midi % 12];
    }

    public static int octave(int midi) {
        return midi / 12 - 1;
    }

    public static boolean isBlack(int midi) {
        return BLACK[midi % 12];
    }

    /** Name for screen readers, e.g. "D flat 4". */
    public static String spokenName(int midi) {
        String n = NAMES[midi % 12];
        String spoken = n.length() == 2 ? n.charAt(0) + " flat" : n;
        return String.format(Locale.US, "%s %d", spoken, octave(midi));
    }

    /**
     * Diatonic staff position (C0 = 0, D0 = 1 ...). Used to place notes on a staff; accidentals
     * share the position of their natural.
     */
    public static int staffPosition(int midi) {
        int[] fromPitchClass = {0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6};
        return octave(midi) * 7 + fromPitchClass[midi % 12];
    }
}
