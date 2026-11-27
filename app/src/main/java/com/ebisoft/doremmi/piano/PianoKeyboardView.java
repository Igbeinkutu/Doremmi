package com.ebisoft.doremmi.piano;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;

import com.ebisoft.doremmi.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A multi-touch piano keyboard drawn on a canvas. Key widths follow the view width, so the same
 * view works on phones and tablets. The visible window covers whole octaves between C3 and C6.
 */
public class PianoKeyboardView extends View {

    public interface OnKeyPressListener {
        void onKeyPressed(int midi);
    }

    public interface OnRangeChangeListener {
        void onRangeChanged(int lowest, int highest);
    }

    private static final int NO_NOTE = -1;
    private static final int MAX_OCTAVES = 3;

    private static final class Key {
        final int midi;
        final boolean black;
        final RectF rect = new RectF();

        Key(int midi) {
            this.midi = midi;
            this.black = Notes.isBlack(midi);
        }
    }

    private final List<Key> whiteKeys = new ArrayList<>();
    private final List<Key> blackKeys = new ArrayList<>();
    private final List<Key> allKeys = new ArrayList<>();
    private final SparseIntArray pointerNotes = new SparseIntArray();
    private final SparseIntArray pressCounts = new SparseIntArray();
    private final Set<Integer> highlighted = new HashSet<>();

    private final Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint whitePressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint blackPressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highlightLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint framePaint = new Paint();
    private final RectF drawRect = new RectF();

    private final float cornerRadius;
    private final float frameHeight;
    private final float minWhiteKeyWidth;
    private final KeyboardAccessibilityHelper accessibilityHelper;

    private int requestedOctaves;
    private int octaves;
    private int startNote = Notes.C4;
    private boolean showLabels;
    private OnKeyPressListener keyPressListener;
    private OnRangeChangeListener rangeChangeListener;

    public PianoKeyboardView(Context context) {
        this(context, null);
    }

    public PianoKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        float density = getResources().getDisplayMetrics().density;
        cornerRadius = 6 * density;
        frameHeight = 6 * density;
        minWhiteKeyWidth = getResources().getDimension(R.dimen.piano_min_white_key_width);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PianoKeyboardView);
        requestedOctaves = a.getInt(R.styleable.PianoKeyboardView_octaves,
                getResources().getInteger(R.integer.piano_octaves));
        showLabels = a.getBoolean(R.styleable.PianoKeyboardView_showKeyLabels, true);
        a.recycle();
        requestedOctaves = Math.max(1, Math.min(MAX_OCTAVES, requestedOctaves));
        octaves = requestedOctaves;

        whitePaint.setColor(ContextCompat.getColor(context, R.color.piano_white_key));
        whitePressedPaint.setColor(ContextCompat.getColor(context, R.color.piano_white_key_pressed));
        blackPaint.setColor(ContextCompat.getColor(context, R.color.piano_black_key));
        blackPressedPaint.setColor(ContextCompat.getColor(context, R.color.piano_black_key_pressed));
        highlightPaint.setColor(ContextCompat.getColor(context, R.color.piano_highlight));
        framePaint.setColor(ContextCompat.getColor(context, R.color.piano_frame));
        borderPaint.setColor(ContextCompat.getColor(context, R.color.piano_key_border));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(Math.max(1f, density));
        labelPaint.setColor(ContextCompat.getColor(context, R.color.piano_key_label));
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(getResources().getDimension(R.dimen.piano_label_text));
        highlightLabelPaint.set(labelPaint);
        highlightLabelPaint.setColor(ContextCompat.getColor(context, R.color.piano_black_key));
        highlightLabelPaint.setFakeBoldText(true);

        setContentDescription(context.getString(R.string.piano_keyboard_description));
        setHapticFeedbackEnabled(true);
        accessibilityHelper = new KeyboardAccessibilityHelper(this);
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);
        buildKeys();
    }

    public void setOnKeyPressListener(@Nullable OnKeyPressListener listener) {
        keyPressListener = listener;
    }

    public void setOnRangeChangeListener(@Nullable OnRangeChangeListener listener) {
        rangeChangeListener = listener;
        notifyRange();
    }

    public int getLowestVisibleNote() {
        return startNote;
    }

    public int getHighestVisibleNote() {
        return startNote + 12 * octaves;
    }

    public boolean canShiftDown() {
        return startNote - 12 >= PianoSoundBank.LOWEST;
    }

    public boolean canShiftUp() {
        return startNote + 12 * (octaves + 1) <= PianoSoundBank.HIGHEST;
    }

    public void shiftOctave(int delta) {
        setStartNote(startNote + 12 * delta);
    }

    /** Sets the lowest visible C. The value is clamped to the playable range. */
    public void setStartNote(int note) {
        int clamped = clampStart(note - Math.floorMod(note, 12));
        if (clamped == startNote) {
            return;
        }
        releaseAll();
        startNote = clamped;
        buildKeys();
        notifyRange();
    }

    /** Moves the window, if needed, so that every given note is on screen. */
    public void ensureVisible(@NonNull Collection<Integer> notes) {
        if (notes.isEmpty()) {
            return;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int n : notes) {
            min = Math.min(min, n);
            max = Math.max(max, n);
        }
        if (min >= getLowestVisibleNote() && max <= getHighestVisibleNote()) {
            return;
        }
        setStartNote(min);
    }

    public void setHighlightedNotes(@NonNull Collection<Integer> notes) {
        highlighted.clear();
        highlighted.addAll(notes);
        invalidate();
        accessibilityHelper.invalidateRoot();
    }

    public void clearHighlights() {
        highlighted.clear();
        invalidate();
        accessibilityHelper.invalidateRoot();
    }

    private int clampStart(int start) {
        int maxStart = PianoSoundBank.HIGHEST - 12 * octaves;
        return Math.max(PianoSoundBank.LOWEST, Math.min(maxStart, start));
    }

    private void notifyRange() {
        if (rangeChangeListener != null) {
            rangeChangeListener.onRangeChanged(getLowestVisibleNote(), getHighestVisibleNote());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int usable = w - getPaddingLeft() - getPaddingRight();
        int fitting = (int) ((usable / minWhiteKeyWidth - 1) / 7);
        int newOctaves = Math.max(1, Math.min(requestedOctaves, fitting));
        if (newOctaves != octaves) {
            octaves = newOctaves;
            startNote = clampStart(startNote);
            notifyRange();
        }
        buildKeys();
    }

    private void buildKeys() {
        whiteKeys.clear();
        blackKeys.clear();
        allKeys.clear();
        int highest = getHighestVisibleNote();
        for (int midi = startNote; midi <= highest; midi++) {
            Key key = new Key(midi);
            (key.black ? blackKeys : whiteKeys).add(key);
            allKeys.add(key);
        }
        layoutKeys();
        invalidate();
        accessibilityHelper.invalidateRoot();
    }

    private void layoutKeys() {
        float left = getPaddingLeft();
        float top = getPaddingTop() + frameHeight;
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float bottom = getHeight() - getPaddingBottom();
        if (width <= 0 || whiteKeys.isEmpty()) {
            return;
        }
        float whiteWidth = width / whiteKeys.size();
        float blackWidth = whiteWidth * 0.6f;
        float blackBottom = top + (bottom - top) * 0.62f;
        int whiteIndex = 0;
        for (Key key : allKeys) {
            if (key.black) {
                float centre = left + whiteIndex * whiteWidth;
                key.rect.set(centre - blackWidth / 2f, top, centre + blackWidth / 2f, blackBottom);
            } else {
                key.rect.set(left + whiteIndex * whiteWidth, top,
                        left + (whiteIndex + 1) * whiteWidth, bottom);
                whiteIndex++;
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getPaddingTop() + frameHeight, framePaint);
        canvas.save();
        canvas.clipRect(getPaddingLeft(), getPaddingTop() + frameHeight,
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        for (Key key : whiteKeys) {
            drawKey(canvas, key);
        }
        for (Key key : blackKeys) {
            drawKey(canvas, key);
        }
        canvas.restore();
    }

    private void drawKey(Canvas canvas, Key key) {
        boolean pressed = pressCounts.get(key.midi, 0) > 0;
        Paint fill;
        if (highlighted.contains(key.midi) && !pressed) {
            fill = highlightPaint;
        } else if (key.black) {
            fill = pressed ? blackPressedPaint : blackPaint;
        } else {
            fill = pressed ? whitePressedPaint : whitePaint;
        }
        drawRect.set(key.rect);
        drawRect.top -= cornerRadius;
        if (!key.black) {
            drawRect.inset(borderPaint.getStrokeWidth() / 2f, 0);
        }
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, fill);
        if (!key.black) {
            canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, borderPaint);
            if (showLabels) {
                String label = key.midi % 12 == 0 ? Notes.name(key.midi) : Notes.letter(key.midi);
                float y = key.rect.bottom - labelPaint.getTextSize() * 0.8f;
                canvas.drawText(label, key.rect.centerX(), y,
                        fill == highlightPaint ? highlightLabelPaint : labelPaint);
            }
        }
    }

    // region Touch handling

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                // fall through
            case MotionEvent.ACTION_POINTER_DOWN: {
                int index = event.getActionIndex();
                trackPointer(event.getPointerId(index), event.getX(index), event.getY(index));
                return true;
            }
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    trackPointer(event.getPointerId(i), event.getX(i), event.getY(i));
                }
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                releasePointer(event.getPointerId(event.getActionIndex()));
                return true;
            case MotionEvent.ACTION_UP:
                releasePointer(event.getPointerId(event.getActionIndex()));
                performClick();
                return true;
            case MotionEvent.ACTION_CANCEL:
                releaseAll();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void trackPointer(int pointerId, float x, float y) {
        int note = noteAt(x, y);
        int previous = pointerNotes.get(pointerId, NO_NOTE);
        if (note == previous) {
            return;
        }
        if (previous != NO_NOTE) {
            releaseNote(previous);
        }
        pointerNotes.put(pointerId, note);
        if (note != NO_NOTE) {
            pressNote(note);
        }
    }

    private void releasePointer(int pointerId) {
        int note = pointerNotes.get(pointerId, NO_NOTE);
        pointerNotes.delete(pointerId);
        if (note != NO_NOTE) {
            releaseNote(note);
        }
    }

    private void releaseAll() {
        pointerNotes.clear();
        pressCounts.clear();
        invalidate();
    }

    private void pressNote(int note) {
        int count = pressCounts.get(note, 0);
        pressCounts.put(note, count + 1);
        if (count == 0) {
            performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            if (keyPressListener != null) {
                keyPressListener.onKeyPressed(note);
            }
        }
        invalidate();
    }

    private void releaseNote(int note) {
        int count = pressCounts.get(note, 0) - 1;
        if (count <= 0) {
            pressCounts.delete(note);
        } else {
            pressCounts.put(note, count);
        }
        invalidate();
    }

    private int noteAt(float x, float y) {
        Key key = keyAt(x, y);
        return key == null ? NO_NOTE : key.midi;
    }

    @Nullable
    private Key keyAt(float x, float y) {
        for (Key key : blackKeys) {
            if (key.rect.contains(x, y)) {
                return key;
            }
        }
        for (Key key : whiteKeys) {
            if (key.rect.contains(x, y)) {
                return key;
            }
        }
        return null;
    }

    // endregion

    // region Accessibility

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return accessibilityHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return accessibilityHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        accessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    private final class KeyboardAccessibilityHelper extends ExploreByTouchHelper {
        private final Rect bounds = new Rect();

        KeyboardAccessibilityHelper(View host) {
            super(host);
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {
            Key key = keyAt(x, y);
            return key == null ? INVALID_ID : key.midi;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            for (Key key : allKeys) {
                virtualViewIds.add(key.midi);
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId,
                                                    @NonNull AccessibilityNodeInfoCompat node) {
            Key key = findKey(virtualViewId);
            String description = Notes.spokenName(virtualViewId);
            if (highlighted.contains(virtualViewId)) {
                description = description + ", " + getContext().getString(R.string.lesson_guided_hint);
            }
            node.setContentDescription(description);
            node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK);
            if (key != null) {
                key.rect.roundOut(bounds);
            } else {
                bounds.set(0, 0, 1, 1);
            }
            node.setBoundsInParent(bounds);
        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action,
                                                        @Nullable Bundle arguments) {
            if (action == AccessibilityNodeInfoCompat.ACTION_CLICK) {
                if (keyPressListener != null) {
                    keyPressListener.onKeyPressed(virtualViewId);
                }
                sendEventForVirtualView(virtualViewId,
                        android.view.accessibility.AccessibilityEvent.TYPE_VIEW_CLICKED);
                return true;
            }
            return false;
        }

        @Nullable
        private Key findKey(int midi) {
            for (Key key : allKeys) {
                if (key.midi == midi) {
                    return key;
                }
            }
            return null;
        }
    }

    // endregion
}
