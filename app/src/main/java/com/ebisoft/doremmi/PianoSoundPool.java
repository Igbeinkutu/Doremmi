package com.ebisoft.doremmi;

import android.media.AudioAttributes;
import android.media.SoundPool;

final class PianoSoundPool {
    private PianoSoundPool() {
    }

    static SoundPool create() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        return new SoundPool.Builder()
                .setMaxStreams(99)
                .setAudioAttributes(attributes)
                .build();
    }
}
