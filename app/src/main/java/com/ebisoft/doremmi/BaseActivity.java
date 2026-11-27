package com.ebisoft.doremmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * Shared chrome for every screen: edge-to-edge drawing, system bar insets and a toolbar with
 * an up arrow.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        applySystemBarInsets();
    }

    protected MaterialToolbar setupToolbar(@StringRes int titleRes) {
        return setupToolbar(getString(titleRes));
    }

    protected MaterialToolbar setupToolbar(CharSequence title) {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeActionContentDescription(R.string.navigate_up);
        }
        return toolbar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    /** Returns to the existing home screen instead of stacking a new one. */
    protected void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
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
}
