package com.ebisoft.doremmi.data;

import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import com.ebisoft.doremmi.R;

import java.util.Arrays;
import java.util.List;

public final class Song {
    @StringRes public final int title;
    @StringRes public final int composer;
    @RawRes public final int audio;

    private Song(@StringRes int title, @StringRes int composer, @RawRes int audio) {
        this.title = title;
        this.composer = composer;
        this.audio = audio;
    }

    public static List<Song> catalog() {
        return Arrays.asList(
                new Song(R.string.song_alla_turca, R.string.composer_mozart, R.raw.alla_turca),
                new Song(R.string.song_arabesque1, R.string.composer_debussy, R.raw.arabesque1),
                new Song(R.string.song_autumn, R.string.composer_piano_solo, R.raw.autumn),
                new Song(R.string.song_autumn_arrangement, R.string.composer_piano_solo, R.raw.autumn_piano_arrangement),
                new Song(R.string.song_minuet_in_g, R.string.composer_beethoven, R.raw.beethoven_minuet_in_g),
                new Song(R.string.song_ode_to_joy, R.string.composer_beethoven, R.raw.beethoven_symphony9_4_ode_to_joy_piano_solo),
                new Song(R.string.song_prelude_c, R.string.composer_bach, R.raw.book1_prelude01),
                new Song(R.string.song_brahms_lullaby, R.string.composer_brahms, R.raw.brahms_lullaby_wiegenlied_piano),
                new Song(R.string.song_canon_in_d, R.string.composer_pachelbel, R.raw.canon_in_d),
                new Song(R.string.song_fur_elise, R.string.composer_beethoven, R.raw.fur_elise),
                new Song(R.string.song_arietta, R.string.composer_grieg, R.raw.grieg_lyric_piece_op12_no1_arietta),
                new Song(R.string.song_hornpipe, R.string.composer_handel, R.raw.handel_water_music_hornpipe_piano),
                new Song(R.string.song_haydn_sonata, R.string.composer_haydn, R.raw.haydn_piano_sonata_31_1),
                new Song(R.string.song_minuet_k2, R.string.composer_mozart, R.raw.mozart_minuet_k2),
                new Song(R.string.song_sonata_k333, R.string.composer_mozart, R.raw.mozart_piano_sonata_k333_movement1),
                new Song(R.string.song_symphony40, R.string.composer_mozart, R.raw.mozart_symphony40_1_piano_solo),
                new Song(R.string.song_notebook_minuet4, R.string.composer_bach, R.raw.notebook2_4_minuet_by_bach),
                new Song(R.string.song_notebook_minuet7, R.string.composer_bach, R.raw.notebook2_7_minuet),
                new Song(R.string.song_notebook_march_beethoven, R.string.composer_beethoven, R.raw.notebook2_16_march_beethoven),
                new Song(R.string.song_notebook_march_bach, R.string.composer_bach, R.raw.notebook2_16_march_bach),
                new Song(R.string.song_pachelbel_arranged, R.string.composer_pachelbel, R.raw.pachelbels_canon_arranged),
                new Song(R.string.song_traumerei, R.string.composer_schumann, R.raw.reverie_traumerei),
                new Song(R.string.song_peacherine, R.string.composer_joplin, R.raw.scott_joplin_peacherine_rag),
                new Song(R.string.song_sonata_in_c, R.string.composer_piano_solo, R.raw.sonata_in_c),
                new Song(R.string.song_sugar_plum, R.string.composer_tchaikovsky, R.raw.sugar_plum_fairy_piano),
                new Song(R.string.song_blue_danube, R.string.composer_strauss, R.raw.the_blue_danube_piano),
                new Song(R.string.song_bridal_chorus, R.string.composer_wagner, R.raw.wagner_bridal_chorus)
        );
    }
}
