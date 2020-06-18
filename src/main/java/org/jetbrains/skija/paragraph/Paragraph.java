package org.jetbrains.skija.paragraph;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.Internal;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class Paragraph extends Managed {
    public enum Affinity {
        UPSTREAM,
        DOWNSTREAM
    }

    public enum RectHeightStyle {

        /** Provide tight bounding boxes that fit heights per run. */
        TIGHT,

        /**
         * The height of the boxes will be the maximum height of all runs in the
         * line. All rects in the same line will be the same height.
         */
        MAX,

        /**
         * Extends the top and/or bottom edge of the bounds to fully cover any line
         * spacing. The top edge of each line should be the same as the bottom edge
         * of the line above. There should be no gaps in vertical coverage given any
         * ParagraphStyle line_height.
         *
         * The top and bottom of each rect will cover half of the
         * space above and half of the space below the line.
         */
        INCLUDE_LINE_SPACING_MIDDLE,

        /** The line spacing will be added to the top of the rect. */
        INCLUDE_LINE_SPACING_TOP,

        /** The line spacing will be added to the bottom of the rect. */
        INCLUDE_LINE_SPACING_BOTTOM,

        STRUT
    }

    public enum RectWidthStyle {

        /** Provide tight bounding boxes that fit widths to the runs of each line independently. */
        TIGHT,

        /** Extends the width of the last rect of each line to match the position of the widest rect over all the lines. */
        MAX
    }

    public enum TextDirection {
        RTL,
        LTR
    }

    @Data
    public static class PositionWithAffinity {
        public final int _position;
        public final Affinity _affinity;
    }

    @AllArgsConstructor
    @Data
    public static class TextBox {
        public final Rect _rect;
        public final TextDirection _direction;

        public TextBox(float l, float t, float r, float b, int direction) {
            this(Rect.makeLTRB(l, t, r, b), TextDirection.values()[direction]);
        }
    }

    public enum TextAlign {
        LEFT,
        RIGHT,
        CENTER,
        JUSTIFY,
        START,
        END,
    }

    public float getMaxWidth() {
        Stats.onNativeCall();
        return _nGetMaxWidth(_ptr);
    }

    public float getHeight() {
        Stats.onNativeCall();
        return _nGetHeight(_ptr);
    }

    public float getMinIntrinsicWidth() {
        Stats.onNativeCall();
        return _nGetMinIntrinsicWidth(_ptr);
    }

    public float getMaxIntrinsicWidth() {
        Stats.onNativeCall();
        return _nGetMaxIntrinsicWidth(_ptr);
    }

    public float getAlphabeticBaseline() {
        Stats.onNativeCall();
        return _nGetAlphabeticBaseline(_ptr);
    }

    public float getIdeographicBaseline() {
        Stats.onNativeCall();
        return _nGetIdeographicBaseline(_ptr);
    }

    public float getLongestLine() {
        Stats.onNativeCall();
        return _nGetLongestLine(_ptr);
    }

    public boolean didExceedMaxLines() {
        Stats.onNativeCall();
        return _nDidExceedMaxLines(_ptr);
    }

    public Paragraph layout(float width) {
        Stats.onNativeCall();
        _nLayout(_ptr, width);
        return this;
    }

    public Paragraph paint(Canvas canvas, float x, float y) {
        Stats.onNativeCall();
        _nPaint(_ptr, Native.getPtr(canvas), x, y);
        return this;
    }

    /**
     * Returns a vector of bounding boxes that enclose all text between
     * start and end glyph indexes, including start and excluding end.
     */
    public TextBox[] getRectsForRange(int start, int end, RectHeightStyle rectHeightStyle, RectWidthStyle rectWidthStyle) {
        Stats.onNativeCall();
        return _nGetRectsForRange(_ptr, start, end, rectHeightStyle.ordinal(), rectWidthStyle.ordinal());
    }

    public TextBox[] getRectsForPlaceholders() {
        Stats.onNativeCall();
        return _nGetRectsForPlaceholders(_ptr);
    }

    public PositionWithAffinity getGlyphPositionAtCoordinate(float dx, float dy) {
        Stats.onNativeCall();
        int res = _nGetGlyphPositionAtCoordinate(_ptr, dx, dy);
        if (res >= 0)
            return new PositionWithAffinity(res, Affinity.DOWNSTREAM);
        else
            return new PositionWithAffinity(-res-1, Affinity.UPSTREAM);
    }

    public IRange getWordBoundary(int offset) {
        Stats.onNativeCall();
        long l = _nGetWordBoundary(_ptr, offset);
        return new IRange((int) (l >>> 32), (int) (l & 0xFFFFFFFF));
    }

    public LineMetrics[] getLineMetrics() {
        Stats.onNativeCall();
        return _nGetLineMetrics(_ptr);
    }

    public long getLineNumber() {
        Stats.onNativeCall();
        return _nGetLineNumber(_ptr);
    }

    public Paragraph markDirty() {
        Stats.onNativeCall();
        _nMarkDirty(_ptr);
        return this;
    }

    public int getUnresolvedGlyphsCount() {
        Stats.onNativeCall();
        return _nGetUnresolvedGlyphsCount(_ptr);
    }

    public Paragraph updateTextAlign(TextAlign align) {
        Stats.onNativeCall();
        _nUpdateTextAlign(_ptr, align.ordinal());
        return this;
    }

    public Paragraph updateText(int from, String text) {
        Stats.onNativeCall();
        _nUpdateText(_ptr, from, text);
        return this;
    }

    public Paragraph updateFontSize(int from, int to, float size) {
        Stats.onNativeCall();
        _nUpdateFontSize(_ptr, from, to, size);
        return this;
    }

    public Paragraph updateForegroundPaint(int from, int to, Paint paint) {
        Stats.onNativeCall();
        _nUpdateForegroundPaint(_ptr, from, to, Native.getPtr(paint));
        return this;
    }

    public Paragraph updateBackgroundPaint(int from, int to, Paint paint) {
        Stats.onNativeCall();
        _nUpdateBackgroundPaint(_ptr, from, to, Native.getPtr(paint));
        return this;
    }

    @Internal
    public Paragraph(long ptr) {
        super(ptr, _finalizerPtr); Stats.onNativeCall();
    }

    public static final  long  _finalizerPtr = _nGetFinalizer();
    public static native long  _nGetFinalizer();
    public static native float _nGetMaxWidth(long ptr);
    public static native float _nGetHeight(long ptr);
    public static native float _nGetMinIntrinsicWidth(long ptr);
    public static native float _nGetMaxIntrinsicWidth(long ptr);
    public static native float _nGetAlphabeticBaseline(long ptr);
    public static native float _nGetIdeographicBaseline(long ptr);
    public static native float _nGetLongestLine(long ptr);
    public static native boolean _nDidExceedMaxLines(long ptr);
    public static native void  _nLayout(long ptr, float width);
    public static native long  _nPaint(long ptr, long canvasPtr, float x, float y);
    public static native TextBox[] _nGetRectsForRange(long ptr, int start, int end, int rectHeightStyle, int rectWidthStyle);
    public static native TextBox[] _nGetRectsForPlaceholders(long ptr);
    public static native int   _nGetGlyphPositionAtCoordinate(long ptr, float dx, float dy);
    public static native long  _nGetWordBoundary(long ptr, int offset);
    public static native LineMetrics[] _nGetLineMetrics(long ptr);
    public static native long  _nGetLineNumber(long ptr);
    public static native void  _nMarkDirty(long ptr);
    public static native int _nGetUnresolvedGlyphsCount(long ptr);
    public static native void  _nUpdateTextAlign(long ptr, int textAlign);
    public static native void  _nUpdateText(long ptr, int from, String text);
    public static native void  _nUpdateFontSize(long ptr, int from, int to, float size);
    public static native void  _nUpdateForegroundPaint(long ptr, int from, int to, long paintPtr);
    public static native void  _nUpdateBackgroundPaint(long ptr, int from, int to, long paintPtr);
}
