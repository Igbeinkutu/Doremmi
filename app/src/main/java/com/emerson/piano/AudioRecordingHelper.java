package com.emerson.piano;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

final class AudioRecordingHelper {
    static final int REQUEST_RECORD_AUDIO = 1001;

    private final Activity activity;
    private final File outputFile;
    private MediaRecorder mediaRecorder;
    private MediaPlayer playbackPlayer;

    AudioRecordingHelper(Activity activity) {
        this.activity = activity;
        File dir = activity.getExternalFilesDir(null);
        if (dir == null) {
            dir = activity.getFilesDir();
        }
        this.outputFile = new File(dir, "tempRecord.3gp");
    }

    boolean hasRecordPermission() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    void requestRecordPermission() {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO);
    }

    boolean startRecording() {
        if (!hasRecordPermission()) {
            requestRecordPermission();
            return false;
        }
        stopPlayback();
        releaseRecorder();
        mediaRecorder = createRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            return true;
        } catch (IOException | RuntimeException e) {
            releaseRecorder();
            Toast.makeText(activity, R.string.recording_failed, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void stopRecording() {
        if (mediaRecorder == null) {
            return;
        }
        try {
            mediaRecorder.stop();
        } catch (RuntimeException ignored) {
            // stop() throws if start() failed or no data was recorded.
        }
        releaseRecorder();
    }

    void playRecording() throws IOException {
        if (!outputFile.exists()) {
            Toast.makeText(activity, R.string.recording_missing, Toast.LENGTH_SHORT).show();
            return;
        }
        stopPlayback();
        playbackPlayer = new MediaPlayer();
        playbackPlayer.setDataSource(outputFile.getAbsolutePath());
        playbackPlayer.prepare();
        playbackPlayer.start();
    }

    void release() {
        stopRecording();
        stopPlayback();
    }

    @SuppressWarnings("deprecation")
    private MediaRecorder createRecorder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new MediaRecorder(activity);
        }
        return new MediaRecorder();
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void stopPlayback() {
        if (playbackPlayer != null) {
            playbackPlayer.release();
            playbackPlayer = null;
        }
    }
}
