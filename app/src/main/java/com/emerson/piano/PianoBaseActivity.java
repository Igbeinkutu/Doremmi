package com.emerson.piano;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public abstract class PianoBaseActivity extends AppCompatActivity {
    protected AudioRecordingHelper recordingHelper;
    private boolean pendingRecordStart;
    private Button pendingRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordingHelper = new AudioRecordingHelper(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        super.setContentView(layoutResID);
        applySystemBarInsets();
        maybeShowLandscapeHint();
    }

    @Override
    protected void onDestroy() {
        if (recordingHelper != null) {
            recordingHelper.release();
        }
        super.onDestroy();
    }

    protected boolean beginRecordingIfPermitted(Button recordButton) {
        if (recordingHelper.hasRecordPermission()) {
            return recordingHelper.startRecording();
        }
        pendingRecordStart = true;
        pendingRecordButton = recordButton;
        recordingHelper.requestRecordPermission();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != AudioRecordingHelper.REQUEST_RECORD_AUDIO) {
            return;
        }
        boolean granted = grantResults.length > 0
                && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED;
        if (granted && pendingRecordStart && pendingRecordButton != null) {
            pendingRecordStart = false;
            if (recordingHelper.startRecording()) {
                pendingRecordButton.setText(R.string.record_stop);
                Toast.makeText(this, R.string.recording_audio, Toast.LENGTH_SHORT).show();
            }
        } else if (!granted) {
            pendingRecordStart = false;
            Toast.makeText(this, R.string.record_permission_denied, Toast.LENGTH_LONG).show();
        }
    }

    private void applySystemBarInsets() {
        View content = findViewById(android.R.id.content);
        if (!(content instanceof ViewGroup)) {
            return;
        }
        View root = ((ViewGroup) content).getChildAt(0);
        if (root == null) {
            return;
        }
        final int initialLeft = root.getPaddingLeft();
        final int initialTop = root.getPaddingTop();
        final int initialRight = root.getPaddingRight();
        final int initialBottom = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(
                    initialLeft + bars.left,
                    initialTop + bars.top,
                    initialRight + bars.right,
                    initialBottom + bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void maybeShowLandscapeHint() {
        Configuration config = getResources().getConfiguration();
        boolean largeScreen = config.smallestScreenWidthDp >= 600;
        boolean portrait = config.orientation == Configuration.ORIENTATION_PORTRAIT;
        if (!largeScreen || !portrait) {
            return;
        }
        View content = findViewById(android.R.id.content);
        if (!(content instanceof FrameLayout)) {
            return;
        }
        TextView banner = new TextView(this);
        banner.setText(R.string.rotate_landscape_hint);
        banner.setTextColor(0xFFFFFFFF);
        banner.setBackgroundColor(0xCC000000);
        banner.setGravity(Gravity.CENTER);
        int padding = (int) (12 * getResources().getDisplayMetrics().density);
        banner.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP);
        ((FrameLayout) content).addView(banner, params);
    }
}
