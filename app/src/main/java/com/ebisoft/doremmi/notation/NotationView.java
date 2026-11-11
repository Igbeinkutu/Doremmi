package com.ebisoft.doremmi.notation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.ebisoft.doremmi.R;
import com.ebisoft.doremmi.piano.Notes;
import com.google.android.material.color.MaterialColors;

/**
 * Draws a {@link Score} as vector graphics, scaled to the view size so notation stays sharp on
 * any screen. Sizes below are expressed in staff spaces (the gap between two staff lines).
 */
public class NotationView extends View {

    private static final String TREBLE_CLEF = "\uD834\uDD1E";
    private static final String BASS_CLEF = "\uD834\uDD22";

    private static final float NOTE_WIDTH = 3.2f;
    private static final float BAR_WIDTH = 1.6f;
    private static final float CLEF_WIDTH = 4.2f;
    private static final float TIME_WIDTH = 2.6f;
    private static final float EDGE = 1.0f;
    private static final float EMPTY_STAFF_WIDTH = 16f;
    private static final float STEM_LENGTH = 3.5f;

    private final Paint inkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clefPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint accentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final RectF oval = new RectF();
    private final Rect bounds = new Rect();

    @Nullable private Score score;

    public NotationView(Context context) {
        this(context, null);
    }

    public NotationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int ink = MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurface);
        int accent = MaterialColors.getColor(this, androidx.appcompat.R.attr.colorPrimary);
        inkPaint.setColor(ink);
        inkPaint.setStyle(Paint.Style.FILL);
        strokePaint.setColor(ink);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeCap(Paint.Cap.BUTT);
        clefPaint.setColor(ink);
        Typeface music = isInEditMode() ? null : ResourcesCompat.getFont(context, R.font.noto_music);
        if (music != null) {
            clefPaint.setTypeface(music);
        }
        textPaint.setColor(ink);
        textPaint.setTextAlign(Paint.Align.CENTER);
        accentPaint.setColor(accent);
        accentPaint.setStyle(Paint.Style.STROKE);
        accentPaint.setStrokeJoin(Paint.Join.ROUND);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setScore(@Nullable Score score) {
        this.score = score;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            int desired = (int) (width * 0.4f);
            height = heightMode == MeasureSpec.AT_MOST ? Math.min(desired, height) : desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (score == null) {
            return;
        }
        float w = getWidth() - getPaddingLeft() - getPaddingRight();
        float h = getHeight() - getPaddingTop() - getPaddingBottom();
        if (w <= 0 || h <= 0) {
            return;
        }
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        switch (score.mode) {
            case SINGLE_NOTE:
                drawSingleNote(canvas, w, h);
                break;
            case EQUATION:
                drawEquation(canvas, w, h);
                break;
            default:
                drawStaff(canvas, w, h);
                break;
        }
        canvas.restore();
    }

    // region Staff mode

    private int bottomLinePosition() {
        // Treble bottom line is E4; bass bottom line is G2.
        return score.clef == Score.Clef.BASS ? Notes.staffPosition(Notes.midi("G2"))
                : Notes.staffPosition(Notes.midi("E4"));
    }

    private void drawStaff(Canvas canvas, float w, float h) {
        int bottom = bottomLinePosition();
        boolean hasClef = score.clef != Score.Clef.NONE;
        boolean hasTime = score.timeTop > 0;

        float units = EDGE * 2;
        if (hasClef) units += CLEF_WIDTH;
        if (hasTime) units += TIME_WIDTH;
        int noteCount = 0;
        for (Score.Element e : score.elements) {
            units += e.type == Score.Element.Type.NOTE ? NOTE_WIDTH : BAR_WIDTH;
            if (e.type == Score.Element.Type.NOTE) noteCount++;
        }
        boolean fullWidth = noteCount == 0;
        if (fullWidth) {
            units = Math.max(units, EMPTY_STAFF_WIDTH);
        }

        // Vertical room above the top line and below the bottom line, in spaces.
        float above = hasClef ? 1.8f : 0.8f;
        float below = hasClef ? 1.8f : 0.8f;
        for (Score.Element e : score.elements) {
            if (e.type != Score.Element.Type.NOTE) continue;
            int r = Notes.staffPosition(e.midi) - bottom;
            boolean stemUp = r < 4;
            float headTop = (r - 8) / 2f + 0.6f;
            float headBottom = -r / 2f + 0.6f;
            float stemTop = e.duration.hasStem() && stemUp ? (r + 7 - 8) / 2f : headTop;
            float stemBottom = e.duration.hasStem() && !stemUp ? (7 - r) / 2f : headBottom;
            above = Math.max(above, Math.max(headTop, stemTop) + 0.4f);
            below = Math.max(below, Math.max(headBottom, stemBottom) + 0.4f);
        }
        float fingeringRoom = score.hasFingerings() ? 2.0f : 0f;
        float labelRoom = score.hasLabels() ? 2.2f : 0f;
        float totalHeight = 4 + above + below + fingeringRoom + labelRoom;

        float s = Math.min(h / totalHeight, w / units);
        float contentWidth = fullWidth ? w : units * s;
        float x = (w - contentWidth) / 2f;
        float topLine = (h - totalHeight * s) / 2f + (above + fingeringRoom) * s;
        float bottomLine = topLine + 4 * s;
        float staffEnd = x + contentWidth;

        strokePaint.setStrokeWidth(Math.max(1f, s * 0.1f));
        for (int i = 0; i < 5; i++) {
            float y = topLine + i * s;
            canvas.drawLine(x, y, staffEnd, y, strokePaint);
        }

        float cursor = x + EDGE * s;
        if (hasClef) {
            drawClef(canvas, cursor, topLine, s);
            cursor += CLEF_WIDTH * s;
        }
        if (hasTime) {
            drawTimeSignature(canvas, cursor + TIME_WIDTH * s / 2f, topLine, s);
            cursor += TIME_WIDTH * s;
        }

        float fingeringY = topLine - above * s - fingeringRoom * s * 0.25f;
        float labelY = bottomLine + below * s + labelRoom * s * 0.75f;
        textPaint.setTextSize(s * 1.4f);
        textPaint.setTypeface(Typeface.DEFAULT);

        for (Score.Element e : score.elements) {
            switch (e.type) {
                case NOTE: {
                    float cx = cursor + NOTE_WIDTH * s / 2f;
                    int r = Notes.staffPosition(e.midi) - bottom;
                    drawStaffNote(canvas, cx, bottomLine, r, e.duration, s);
                    if (e.fingering != null) {
                        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                        canvas.drawText(e.fingering, cx, fingeringY, textPaint);
                        textPaint.setTypeface(Typeface.DEFAULT);
                    }
                    if (e.label != null) {
                        canvas.drawText(e.label, cx, labelY, textPaint);
                    }
                    cursor += NOTE_WIDTH * s;
                    break;
                }
                case BAR: {
                    float bx = cursor + BAR_WIDTH * s / 2f;
                    strokePaint.setStrokeWidth(s * 0.12f);
                    canvas.drawLine(bx, topLine, bx, bottomLine, strokePaint);
                    cursor += BAR_WIDTH * s;
                    break;
                }
                case FINAL_BAR: {
                    float bx = cursor + BAR_WIDTH * s * 0.75f;
                    strokePaint.setStrokeWidth(s * 0.12f);
                    canvas.drawLine(bx - s * 0.6f, topLine, bx - s * 0.6f, bottomLine, strokePaint);
                    canvas.drawRect(bx - s * 0.2f, topLine, bx + s * 0.25f, bottomLine, inkPaint);
                    cursor += BAR_WIDTH * s;
                    break;
                }
            }
        }
    }

    private void drawClef(Canvas canvas, float left, float topLine, float s) {
        boolean treble = score.clef == Score.Clef.TREBLE;
        String glyph = treble ? TREBLE_CLEF : BASS_CLEF;
        float targetHeight = treble ? 7.0f * s : 3.3f * s;
        float targetTop = treble ? topLine - 1.5f * s : topLine - 0.05f * s;
        clefPaint.setTextSize(4 * s);
        clefPaint.getTextBounds(glyph, 0, glyph.length(), bounds);
        if (bounds.height() == 0) {
            return;
        }
        clefPaint.setTextSize(4 * s * targetHeight / bounds.height());
        clefPaint.getTextBounds(glyph, 0, glyph.length(), bounds);
        canvas.drawText(glyph, left - bounds.left, targetTop - bounds.top, clefPaint);
    }

    private void drawTimeSignature(Canvas canvas, float cx, float topLine, float s) {
        textPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
        textPaint.setTextSize(s * 2.7f);
        String top = String.valueOf(score.timeTop);
        String bottom = String.valueOf(score.timeBottom);
        textPaint.getTextBounds(top, 0, top.length(), bounds);
        float digitHeight = bounds.height();
        float middle = topLine + 2 * s;
        canvas.drawText(top, cx, middle - (2 * s - digitHeight) / 2f, textPaint);
        canvas.drawText(bottom, cx, topLine + 4 * s - (2 * s - digitHeight) / 2f, textPaint);
        textPaint.setTypeface(Typeface.DEFAULT);
    }

    /** @param r staff position relative to the bottom line, in half-spaces. */
    private void drawStaffNote(Canvas canvas, float cx, float bottomLine, int r,
                               Score.Duration duration, float s) {
        float y = bottomLine - r * s / 2f;
        strokePaint.setStrokeWidth(Math.max(1f, s * 0.12f));
        float ledgerHalf = s * 1.1f;
        for (int k = -2; k >= r; k -= 2) {
            float ly = bottomLine - k * s / 2f;
            canvas.drawLine(cx - ledgerHalf, ly, cx + ledgerHalf, ly, strokePaint);
        }
        for (int k = 10; k <= r; k += 2) {
            float ly = bottomLine - k * s / 2f;
            canvas.drawLine(cx - ledgerHalf, ly, cx + ledgerHalf, ly, strokePaint);
        }
        drawNote(canvas, cx, y, duration, r < 4, s);
    }

    // endregion

    // region Note glyphs

    /** Draws a notehead centred on (cx, cy) with stem and flags. */
    private void drawNote(Canvas canvas, float cx, float cy, Score.Duration duration,
                          boolean stemUp, float s) {
        float headWidth = duration == Score.Duration.WHOLE ? 1.6f * s : 1.25f * s;
        float headHeight = 0.9f * s;
        canvas.save();
        canvas.rotate(duration == Score.Duration.WHOLE ? 0 : -20, cx, cy);
        oval.set(cx - headWidth / 2f, cy - headHeight / 2f, cx + headWidth / 2f, cy + headHeight / 2f);
        if (!duration.hollow()) {
            canvas.drawOval(oval, inkPaint);
        } else {
            int layer = canvas.saveLayer(oval.left - s, oval.top - s, oval.right + s, oval.bottom + s, null);
            canvas.drawOval(oval, inkPaint);
            float innerW = duration == Score.Duration.WHOLE ? headWidth * 0.42f : headWidth * 0.68f;
            float innerH = duration == Score.Duration.WHOLE ? headHeight * 0.72f : headHeight * 0.42f;
            canvas.rotate(duration == Score.Duration.WHOLE ? 50 : 0, cx, cy);
            oval.set(cx - innerW / 2f, cy - innerH / 2f, cx + innerW / 2f, cy + innerH / 2f);
            canvas.drawOval(oval, clearPaint);
            canvas.restoreToCount(layer);
        }
        canvas.restore();

        if (!duration.hasStem()) {
            return;
        }
        float stemWidth = Math.max(1f, s * 0.13f);
        float halfHead = headWidth / 2f * 0.94f;
        float stemX = stemUp ? cx + halfHead - stemWidth / 2f : cx - halfHead + stemWidth / 2f;
        float stemEnd = stemUp ? cy - STEM_LENGTH * s : cy + STEM_LENGTH * s;
        float stemStart = stemUp ? cy - headHeight * 0.15f : cy + headHeight * 0.15f;
        canvas.drawRect(stemX - stemWidth / 2f, Math.min(stemStart, stemEnd),
                stemX + stemWidth / 2f, Math.max(stemStart, stemEnd), inkPaint);

        float direction = stemUp ? 1f : -1f;
        float flagX = stemX;
        for (int i = 0; i < duration.flags; i++) {
            float y0 = stemEnd + direction * i * 0.9f * s;
            path.reset();
            path.moveTo(flagX, y0);
            path.cubicTo(flagX + 0.15f * s, y0 + direction * 0.9f * s,
                    flagX + 1.35f * s, y0 + direction * 1.2f * s,
                    flagX + 0.85f * s, y0 + direction * 2.7f * s);
            path.cubicTo(flagX + 1.0f * s, y0 + direction * 1.7f * s,
                    flagX + 0.45f * s, y0 + direction * 1.35f * s,
                    flagX, y0 + direction * 1.15f * s);
            path.close();
            canvas.drawPath(path, inkPaint);
        }
    }

    // endregion

    // region Single note and equation modes

    private void drawSingleNote(Canvas canvas, float w, float h) {
        float s = Math.min(h / 6f, w / 4f);
        float cx = w / 2f - (score.primary.hasStem() ? 0.4f * s : 0);
        float cy = score.primary.hasStem() ? h / 2f + 1.6f * s : h / 2f;
        drawNote(canvas, cx, cy, score.primary, true, s);
    }

    private void drawEquation(Canvas canvas, float w, float h) {
        float s = Math.min(h / 6.5f, w / 14f);
        float total = 13f * s;
        float x = (w - total) / 2f;
        float cy = h / 2f;
        float noteY = cy + 1.4f * s;

        drawNote(canvas, x + 1.2f * s, noteY, score.primary, true, s);

        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(s * 2.2f);
        textPaint.getTextBounds("=", 0, 1, bounds);
        canvas.drawText("=", x + 4.0f * s, cy + bounds.height() / 2f, textPaint);

        float boxLeft = x + 5.4f * s;
        float boxSize = 3.0f * s;
        accentPaint.setStrokeWidth(s * 0.2f);
        oval.set(boxLeft, cy - boxSize / 2f, boxLeft + boxSize, cy + boxSize / 2f);
        canvas.drawRoundRect(oval, s * 0.6f, s * 0.6f, accentPaint);

        drawNote(canvas, x + 10.0f * s, noteY, score.secondary, true, s);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(s * 1.8f);
        canvas.drawText("’s", x + 12.3f * s, cy + 1.8f * s, textPaint);
    }

    // endregion
}
