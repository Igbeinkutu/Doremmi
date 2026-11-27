package com.ebisoft.doremmi.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.ebisoft.doremmi.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;

/** Bottom sheet shown when a lesson or game finishes, offering clear next steps. */
public final class ResultSheet {
    private final Activity activity;
    private final BottomSheetDialog dialog;
    private final View content;

    @SuppressLint("InflateParams")
    private ResultSheet(Activity activity) {
        this.activity = activity;
        dialog = new BottomSheetDialog(activity);
        content = activity.getLayoutInflater().inflate(R.layout.sheet_result, null, false);
        dialog.setContentView(content);
        dialog.setCanceledOnTouchOutside(false);
        BottomSheetBehavior<?> behavior = dialog.getBehavior();
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(new DefaultLifecycleObserver() {
                @Override
                public void onDestroy(@NonNull LifecycleOwner owner) {
                    dialog.setOnCancelListener(null);
                    dialog.dismiss();
                }
            });
        }
    }

    public static ResultSheet with(Activity activity) {
        return new ResultSheet(activity);
    }

    public ResultSheet icon(@DrawableRes int icon, @AttrRes int tintAttr) {
        ImageView view = content.findViewById(R.id.result_icon);
        view.setImageResource(icon);
        view.setImageTintList(ColorStateList.valueOf(MaterialColors.getColor(view, tintAttr)));
        return this;
    }

    public ResultSheet title(CharSequence title) {
        ((TextView) content.findViewById(R.id.result_title)).setText(title);
        return this;
    }

    public ResultSheet score(CharSequence score) {
        return text(R.id.result_score, score);
    }

    public ResultSheet message(CharSequence message) {
        return text(R.id.result_message, message);
    }

    public ResultSheet highlight(@Nullable CharSequence highlight) {
        return text(R.id.result_highlight, highlight);
    }

    public ResultSheet primary(CharSequence label, Runnable action) {
        return button(R.id.result_primary, label, action);
    }

    public ResultSheet secondary(CharSequence label, Runnable action) {
        return button(R.id.result_secondary, label, action);
    }

    public ResultSheet tertiary(CharSequence label, Runnable action) {
        return button(R.id.result_tertiary, label, action);
    }

    public ResultSheet onCancel(Runnable action) {
        dialog.setOnCancelListener(d -> action.run());
        return this;
    }

    public void show() {
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    private ResultSheet text(int id, @Nullable CharSequence value) {
        TextView view = content.findViewById(id);
        view.setText(value);
        view.setVisibility(value == null ? View.GONE : View.VISIBLE);
        return this;
    }

    private ResultSheet button(int id, CharSequence label, Runnable action) {
        MaterialButton button = content.findViewById(id);
        button.setText(label);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            dialog.dismiss();
            action.run();
        });
        return this;
    }
}
