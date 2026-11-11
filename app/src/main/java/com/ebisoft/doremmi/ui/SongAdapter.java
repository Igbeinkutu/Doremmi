package com.ebisoft.doremmi.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.R;
import com.ebisoft.doremmi.data.Song;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/** Song list that marks the song currently loaded in the player. */
public final class SongAdapter extends RecyclerView.Adapter<SongAdapter.Holder> {
    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    private final List<Song> songs;
    private final OnSongClickListener listener;
    private int activePosition = RecyclerView.NO_POSITION;
    private boolean playing;

    public SongAdapter(List<Song> songs, OnSongClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    public void setActive(int position, boolean isPlaying) {
        int previous = activePosition;
        activePosition = position;
        playing = isPlaying;
        if (previous != RecyclerView.NO_POSITION) {
            notifyItemChanged(previous);
        }
        if (position != RecyclerView.NO_POSITION && position != previous) {
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Song song = songs.get(position);
        Context context = holder.itemView.getContext();
        boolean active = position == activePosition;
        holder.title.setText(song.title);
        holder.composer.setText(song.composer);
        holder.icon.setImageResource(active ? R.drawable.ic_graphic_eq : R.drawable.ic_music_note);
        holder.state.setImageResource(active && playing ? R.drawable.ic_pause : R.drawable.ic_play);
        holder.card.setStrokeWidth(active
                ? context.getResources().getDimensionPixelSize(R.dimen.card_selected_stroke) : 0);
        holder.card.setContentDescription(context.getString(
                active && playing ? R.string.songs_pause_item : R.string.songs_play_item,
                context.getString(song.title), context.getString(song.composer)));
        holder.card.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onSongClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {
        final MaterialCardView card;
        final ImageView icon;
        final ImageView state;
        final TextView title;
        final TextView composer;

        Holder(View view) {
            super(view);
            card = (MaterialCardView) view;
            icon = view.findViewById(R.id.song_icon);
            state = view.findViewById(R.id.song_state);
            title = view.findViewById(R.id.song_title);
            composer = view.findViewById(R.id.song_composer);
        }
    }
}
