package com.ebisoft.doremmi;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.data.Song;
import com.ebisoft.doremmi.ui.SongAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;

/** Classical piano pieces with a persistent mini player. */
public class SongsActivity extends BaseActivity {
    private static final long PROGRESS_INTERVAL_MS = 500;

    private final List<Song> songs = Song.catalog();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SongAdapter adapter;
    private RecyclerView list;
    private View playerBar;
    private TextView playerTitle;
    private TextView elapsed;
    private TextView duration;
    private SeekBar seekBar;
    private MaterialButton playPause;

    private MediaPlayer player;
    private boolean prepared;
    private int current = RecyclerView.NO_POSITION;

    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (player != null && prepared) {
                int position = player.getCurrentPosition();
                seekBar.setProgress(position);
                elapsed.setText(formatTime(position));
            }
            handler.postDelayed(this, PROGRESS_INTERVAL_MS);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        setupToolbar(R.string.songs_title);

        list = findViewById(R.id.list);
        playerBar = findViewById(R.id.player_bar);
        playerTitle = findViewById(R.id.player_title);
        elapsed = findViewById(R.id.player_elapsed);
        duration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.player_seek);
        playPause = findViewById(R.id.player_play_pause);

        adapter = new SongAdapter(songs, this::onSongClick);
        list.setAdapter(adapter);

        playPause.setOnClickListener(v -> togglePlayback());
        findViewById(R.id.player_stop).setOnClickListener(v -> stopPlayback());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                if (fromUser && player != null && prepared) {
                    player.seekTo(progress);
                    elapsed.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
    }

    private void onSongClick(int position) {
        if (position == current && player != null) {
            togglePlayback();
        } else {
            startSong(position);
        }
    }

    private void startSong(int position) {
        releasePlayer();
        current = position;
        Song song = songs.get(position);
        playerTitle.setText(song.title);
        elapsed.setText(formatTime(0));
        duration.setText(null);
        seekBar.setProgress(0);
        playerBar.setVisibility(View.VISIBLE);

        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        player.setOnPreparedListener(mp -> {
            prepared = true;
            seekBar.setMax(mp.getDuration());
            duration.setText(formatTime(mp.getDuration()));
            mp.start();
            updatePlaybackUi();
        });
        player.setOnCompletionListener(mp -> stopPlayback());
        player.setOnErrorListener((mp, what, extra) -> {
            stopPlayback();
            Snackbar.make(list, R.string.songs_play_error, Snackbar.LENGTH_LONG).show();
            return true;
        });
        try {
            player.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/" + song.audio));
            player.prepareAsync();
        } catch (IOException | IllegalStateException e) {
            stopPlayback();
            Snackbar.make(list, R.string.songs_play_error, Snackbar.LENGTH_LONG).show();
            return;
        }
        updatePlaybackUi();
    }

    private void togglePlayback() {
        if (player == null || !prepared) {
            return;
        }
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
        updatePlaybackUi();
    }

    private void stopPlayback() {
        releasePlayer();
        current = RecyclerView.NO_POSITION;
        playerBar.setVisibility(View.GONE);
        updatePlaybackUi();
    }

    private void releasePlayer() {
        handler.removeCallbacks(progressUpdater);
        if (player != null) {
            player.release();
            player = null;
        }
        prepared = false;
    }

    private void updatePlaybackUi() {
        boolean playing = player != null && (!prepared || player.isPlaying());
        playPause.setIconResource(playing ? R.drawable.ic_pause : R.drawable.ic_play);
        playPause.setContentDescription(getString(playing ? R.string.action_pause : R.string.action_play));
        adapter.setActive(current, playing);
        handler.removeCallbacks(progressUpdater);
        if (playing && prepared) {
            handler.post(progressUpdater);
        }
    }

    private static String formatTime(int millis) {
        return DateUtils.formatElapsedTime(millis / 1000);
    }

    @Override
    protected void onStop() {
        if (player != null && prepared && player.isPlaying()) {
            player.pause();
            updatePlaybackUi();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }
}
