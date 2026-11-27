package com.ebisoft.doremmi.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

/** Vertical list of icon cards used by the lessons, games and scores screens. */
public final class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.Holder> {
    private final List<MenuItem> items = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void submit(List<MenuItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {
        private final MaterialCardView card;
        private final ImageView icon;
        private final TextView overline;
        private final TextView title;
        private final TextView description;
        private final View metaRow;
        private final ImageView metaIcon;
        private final TextView meta;
        private final LinearProgressIndicator progress;
        private final MaterialButton action;

        Holder(View view) {
            super(view);
            card = (MaterialCardView) view;
            icon = view.findViewById(R.id.item_icon);
            overline = view.findViewById(R.id.item_overline);
            title = view.findViewById(R.id.item_title);
            description = view.findViewById(R.id.item_description);
            metaRow = view.findViewById(R.id.item_meta_row);
            metaIcon = view.findViewById(R.id.item_meta_icon);
            meta = view.findViewById(R.id.item_meta);
            progress = view.findViewById(R.id.item_progress);
            action = view.findViewById(R.id.item_action);
        }

        void bind(MenuItem item) {
            icon.setImageResource(item.icon);
            setText(overline, item.overline);
            title.setText(item.title);
            setText(description, item.description);

            metaRow.setVisibility(item.meta == null ? View.GONE : View.VISIBLE);
            meta.setText(item.meta);
            if (item.metaIcon != 0) {
                metaIcon.setImageResource(item.metaIcon);
                metaIcon.setVisibility(View.VISIBLE);
            } else {
                metaIcon.setVisibility(View.GONE);
            }

            if (item.progress >= 0) {
                progress.setVisibility(View.VISIBLE);
                progress.setMax(item.progressMax);
                progress.setProgress(item.progress);
            } else {
                progress.setVisibility(View.GONE);
            }

            if (item.actionLabel != null && item.onAction != null) {
                action.setVisibility(View.VISIBLE);
                action.setText(item.actionLabel);
                action.setOnClickListener(v -> item.onAction.run());
            } else {
                action.setVisibility(View.GONE);
                action.setOnClickListener(null);
            }

            if (item.onClick != null) {
                card.setOnClickListener(v -> item.onClick.run());
            } else {
                card.setOnClickListener(null);
                card.setClickable(false);
            }
        }

        private static void setText(TextView view, @Nullable CharSequence text) {
            view.setText(text);
            view.setVisibility(text == null ? View.GONE : View.VISIBLE);
        }
    }
}
