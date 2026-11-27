package com.ebisoft.doremmi.ui;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

/** Content for one card in a {@link MenuCardAdapter} list. */
public final class MenuItem {
    @DrawableRes final int icon;
    final CharSequence title;
    @Nullable CharSequence overline;
    @Nullable CharSequence description;
    @Nullable CharSequence meta;
    @DrawableRes int metaIcon;
    int progress = -1;
    int progressMax = 100;
    @Nullable CharSequence actionLabel;
    @Nullable Runnable onAction;
    @Nullable Runnable onClick;

    public MenuItem(@DrawableRes int icon, CharSequence title) {
        this.icon = icon;
        this.title = title;
    }

    public MenuItem overline(@Nullable CharSequence overline) {
        this.overline = overline;
        return this;
    }

    public MenuItem description(@Nullable CharSequence description) {
        this.description = description;
        return this;
    }

    public MenuItem meta(@Nullable CharSequence meta, @DrawableRes int icon) {
        this.meta = meta;
        this.metaIcon = icon;
        return this;
    }

    public MenuItem progress(int value, int max) {
        this.progress = value;
        this.progressMax = Math.max(1, max);
        return this;
    }

    public MenuItem action(CharSequence label, Runnable action) {
        this.actionLabel = label;
        this.onAction = action;
        return this;
    }

    public MenuItem onClick(Runnable action) {
        this.onClick = action;
        return this;
    }
}
