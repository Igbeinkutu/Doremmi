package com.ebisoft.doremmi;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ebisoft.doremmi.piano.Notes;
import com.ebisoft.doremmi.piano.PianoKeyboardView;
import com.ebisoft.doremmi.piano.PianoSoundBank;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

/** Base for screens with a playable keyboard: sounds, octave controls and recording. */
public abstract class PianoBaseActivity extends BaseActivity {
    protected AudioRecordingHelper recordingHelper;
    protected PianoSoundBank soundBank;
    protected PianoKeyboardView piano;

    private MaterialButton recordButton;
    private MaterialButton playRecordingButton;
    private MaterialButton octaveDownButton;
    private MaterialButton octaveUpButton;
    private TextView rangeLabel;
    private boolean recording;
    private boolean pendingRecordStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordingHelper = new AudioRecordingHelper(this);
        soundBank = new PianoSoundBank(this);
    }

    /** Call after setContentView. Expects the layout to include view_piano_controls and a piano. */
    protected void setupPiano() {
        piano = findViewById(R.id.piano);
        piano.setOnKeyPressListener(this::onPianoKeyPressed);

        octaveDownButton = findViewById(R.id.octave_down);
        octaveUpButton = findViewById(R.id.octave_up);
        rangeLabel = findViewById(R.id.octave_range);
        recordButton = findViewById(R.id.record);
        playRecordingButton = findViewById(R.id.play_recording);

        octaveDownButton.setOnClickListener(v -> piano.shiftOctave(-1));
        octaveUpButton.setOnClickListener(v -> piano.shiftOctave(1));
        piano.setOnRangeChangeListener(this::onRangeChanged);
        recordButton.setOnClickListener(v -> toggleRecording());
        playRecordingButton.setOnClickListener(v -> playRecording());
        playRecordingButton.setEnabled(false);

        maybeShowLandscapeHint();
    }

    protected void onPianoKeyPressed(int midi) {
        soundBank.play(midi);
    }

    private void onRangeChanged(int lowest, int highest) {
        rangeLabel.setText(getString(R.string.octave_range, Notes.name(lowest), Notes.name(highest)));
        octaveDownButton.setEnabled(piano.canShiftDown());
        octaveUpButton.setEnabled(piano.canShiftUp());
    }

    private void toggleRecording() {
        if (recording) {
            stopRecording();
            Toast.makeText(this, R.string.recording_stopped, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!recordingHelper.hasRecordPermission()) {
            pendingRecordStart = true;
            recordingHelper.requestRecordPermission();
            return;
        }
        startRecording();
    }

    private void startRecording() {
        if (recordingHelper.startRecording()) {
            recording = true;
            updateRecordButton();
            Toast.makeText(this, R.string.recording_audio, Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        recordingHelper.stopRecording();
        recording = false;
        playRecordingButton.setEnabled(true);
        updateRecordButton();
    }

    private void updateRecordButton() {
        recordButton.setIconResource(recording ? R.drawable.ic_stop : R.drawable.ic_mic);
        recordButton.setText(recording ? R.string.record_stop : R.string.record);
        recordButton.setContentDescription(getString(recording ? R.string.record_stop : R.string.record));
    }

    private void playRecording() {
        try {
            recordingHelper.playRecording();
        } catch (IOException e) {
            Toast.makeText(this, R.string.recording_missing, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != AudioRecordingHelper.REQUEST_RECORD_AUDIO) {
            return;
        }
        boolean granted = grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (granted && pendingRecordStart) {
            startRecording();
        } else if (!granted) {
            Toast.makeText(this, R.string.record_permission_denied, Toast.LENGTH_LONG).show();
        }
        pendingRecordStart = false;
    }

    private void maybeShowLandscapeHint() {
        Configuration config = getResources().getConfiguration();
        if (config.orientation != Configuration.ORIENTATION_PORTRAIT) {
            return;
        }
        View root = findViewById(android.R.id.content);
        Snackbar.make(root, R.string.rotate_landscape_hint, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        if (recording) {
            stopRecording();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        recordingHelper.release();
        soundBank.release();
        super.onDestroy();
    }
}
