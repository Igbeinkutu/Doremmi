package com.ebisoft.doremmi.piano;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.SparseIntArray;

import androidx.annotation.RawRes;

import com.ebisoft.doremmi.R;

/** Loads every piano sample once and plays notes by MIDI number. */
public final class PianoSoundBank {
    public static final int LOWEST = Notes.C3;
    public static final int HIGHEST = Notes.C6;

    private static final int[] SAMPLES = {
            R.raw.c3, R.raw.db3, R.raw.d3, R.raw.eb3, R.raw.e3, R.raw.f3,
            R.raw.gb3, R.raw.g3, R.raw.ab3, R.raw.a3, R.raw.bb3, R.raw.b3,
            R.raw.c4, R.raw.db4, R.raw.d4, R.raw.eb4, R.raw.e4, R.raw.f4,
            R.raw.gb4, R.raw.g4, R.raw.ab4, R.raw.a4, R.raw.bb4, R.raw.b4,
            R.raw.c5, R.raw.db5, R.raw.d5, R.raw.eb5, R.raw.e5, R.raw.f5,
            R.raw.gb5, R.raw.g5, R.raw.ab5, R.raw.a5, R.raw.bb5, R.raw.b5,
            R.raw.c6,
    };

    private final Context context;
    private final SoundPool soundPool;
    private final SparseIntArray soundIds = new SparseIntArray();

    public PianoSoundBank(Context context) {
        this.context = context.getApplicationContext();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(16)
                .setAudioAttributes(attributes)
                .build();
        for (int i = 0; i < SAMPLES.length; i++) {
            soundIds.put(LOWEST + i, soundPool.load(this.context, SAMPLES[i], 1));
        }
    }

    public void play(int midi) {
        int id = soundIds.get(midi, 0);
        if (id != 0) {
            soundPool.play(id, 1f, 1f, 1, 0, 1f);
        }
    }

    public void play(int... notes) {
        for (int note : notes) {
            play(note);
        }
    }

    /** Loads a non-piano sound such as applause. Returns an id for {@link #playEffect(int)}. */
    public int loadEffect(@RawRes int resId) {
        return soundPool.load(context, resId, 1);
    }

    public void playEffect(int effectId) {
        if (effectId != 0) {
            soundPool.play(effectId, 1f, 1f, 1, 0, 1f);
        }
    }

    public void release() {
        soundPool.release();
    }
}
