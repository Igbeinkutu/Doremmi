package com.ebisoft.doremmi.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

/** Home dashboard: a header, a "continue learning" card and a grid of section tiles. */
public final class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTINUE = 1;
    public static final int TYPE_TILE = 2;
    private static final int FIXED_ITEMS = 2;

    public static final class Tile {
        @DrawableRes final int icon;
        @StringRes final int title;
        @StringRes final int description;
        final Runnable onClick;

        public Tile(@DrawableRes int icon, @StringRes int title, @StringRes int description, Runnable onClick) {
            this.icon = icon;
            this.title = title;
            this.description = description;
            this.onClick = onClick;
        }
    }

    public static final class ContinueCard {
        final CharSequence title;
        final CharSequence subtitle;
        final CharSequence actionLabel;
        final int progress;
        final int max;
        final Runnable onClick;

        /** Pass a negative progress to hide the progress bar. */
        public ContinueCard(CharSequence title, CharSequence subtitle, CharSequence actionLabel,
                            int progress, int max, Runnable onClick) {
            this.title = title;
            this.subtitle = subtitle;
            this.actionLabel = actionLabel;
            this.progress = progress;
            this.max = max;
            this.onClick = onClick;
        }
    }

    private final List<Tile> tiles;
    @Nullable private ContinueCard continueCard;

    public HomeAdapter(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public void setContinueCard(ContinueCard card) {
        continueCard = card;
        notifyItemChanged(1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return position == 1 ? TYPE_CONTINUE : TYPE_TILE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_home_header, parent, false)) {
                };
            case TYPE_CONTINUE:
                return new ContinueHolder(inflater.inflate(R.layout.item_home_continue, parent, false));
            default:
                return new TileHolder(inflater.inflate(R.layout.item_home_tile, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContinueHolder) {
            ((ContinueHolder) holder).bind(continueCard);
        } else if (holder instanceof TileHolder) {
            ((TileHolder) holder).bind(tiles.get(position - FIXED_ITEMS));
        }
    }

    @Override
    public int getItemCount() {
        return tiles.size() + FIXED_ITEMS;
    }

    static final class ContinueHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView subtitle;
        private final LinearProgressIndicator progress;
        private final MaterialButton action;

        ContinueHolder(View view) {
            super(view);
            title = view.findViewById(R.id.continue_title);
            subtitle = view.findViewById(R.id.continue_subtitle);
            progress = view.findViewById(R.id.continue_progress);
            action = view.findViewById(R.id.continue_action);
        }

        void bind(@Nullable ContinueCard card) {
            itemView.setVisibility(card == null ? View.INVISIBLE : View.VISIBLE);
            if (card == null) {
                return;
            }
            title.setText(card.title);
            subtitle.setText(card.subtitle);
            action.setText(card.actionLabel);
            action.setOnClickListener(v -> card.onClick.run());
            itemView.setOnClickListener(v -> card.onClick.run());
            if (card.progress >= 0) {
                progress.setVisibility(View.VISIBLE);
                progress.setMax(Math.max(1, card.max));
                progress.setProgress(card.progress);
            } else {
                progress.setVisibility(View.GONE);
            }
        }
    }

    static final class TileHolder extends RecyclerView.ViewHolder {
        private final ImageView icon;
        private final TextView title;
        private final TextView description;

        TileHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.tile_icon);
            title = view.findViewById(R.id.tile_title);
            description = view.findViewById(R.id.tile_description);
        }

        void bind(Tile tile) {
            icon.setImageResource(tile.icon);
            title.setText(tile.title);
            description.setText(tile.description);
            itemView.setOnClickListener(v -> tile.onClick.run());
        }
    }
}
