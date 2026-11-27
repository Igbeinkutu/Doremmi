package com.ebisoft.doremmi.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebisoft.doremmi.R;
import com.ebisoft.doremmi.data.LessonStep;
import com.ebisoft.doremmi.notation.NotationView;

import java.util.List;

/** Pages of a lesson for a ViewPager2. */
public final class LessonStepAdapter extends RecyclerView.Adapter<LessonStepAdapter.Holder> {

    public interface OnMediaClickListener {
        void onMediaClick(LessonStep step);
    }

    private final List<LessonStep> steps;
    private final OnMediaClickListener mediaClickListener;

    public LessonStepAdapter(List<LessonStep> steps, OnMediaClickListener mediaClickListener) {
        this.steps = steps;
        this.mediaClickListener = mediaClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson_step, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LessonStep step = steps.get(position);
        boolean hasMedia = step.hasMedia();
        holder.text.setVisibility(hasMedia ? View.GONE : View.VISIBLE);
        holder.media.setVisibility(hasMedia ? View.VISIBLE : View.GONE);
        if (!hasMedia) {
            holder.text.setText(step.text);
            holder.media.setOnClickListener(null);
            return;
        }
        String description = holder.itemView.getContext().getString(step.mediaDescription);
        holder.media.setContentDescription(description + ". "
                + holder.itemView.getContext().getString(R.string.action_open_full_screen));
        holder.media.setOnClickListener(v -> mediaClickListener.onMediaClick(step));
        if (step.notation != null) {
            holder.image.setVisibility(View.GONE);
            holder.image.setImageDrawable(null);
            holder.notation.setVisibility(View.VISIBLE);
            holder.notation.setScore(step.notation);
        } else {
            holder.notation.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(step.image);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static final class Holder extends RecyclerView.ViewHolder {
        final TextView text;
        final View media;
        final ImageView image;
        final NotationView notation;

        Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.step_text);
            media = itemView.findViewById(R.id.step_media);
            image = itemView.findViewById(R.id.step_image);
            notation = itemView.findViewById(R.id.step_notation);
        }
    }
}
