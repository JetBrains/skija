package org.jetbrains.skija.paragraph;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class Paragraph extends Managed {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public ManagedString _text;

    @Override
    public void close() {
        if (_text != null) {
            _text.close();
            _text = null;
        }
        super.close();
    }
    
    public float getMaxWidth() {
        try {
            Stats.onNativeCall();
            return _nGetMaxWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getHeight() {
        try {
            Stats.onNativeCall();
            return _nGetHeight(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getMinIntrinsicWidth() {
        try {
            Stats.onNativeCall();
            return _nGetMinIntrinsicWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getMaxIntrinsicWidth() {
        try {
            Stats.onNativeCall();
            return _nGetMaxIntrinsicWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getAlphabeticBaseline() {
        try {
            Stats.onNativeCall();
            return _nGetAlphabeticBaseline(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getIdeographicBaseline() {
        try {
            Stats.onNativeCall();
            return _nGetIdeographicBaseline(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getLongestLine() {
        try {
            Stats.onNativeCall();
            return _nGetLongestLine(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean didExceedMaxLines() {
        try {
            Stats.onNativeCall();
            return _nDidExceedMaxLines(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paragraph layout(float width) {
        Stats.onNativeCall();
        _nLayout(_ptr, width);
        return this;
    }

    public Paragraph paint(Canvas canvas, float x, float y) {
        try {
            Stats.onNativeCall();
            _nPaint(_ptr, Native.getPtr(canvas), x, y);
            return this;
        } finally {
            Reference.reachabilityFence(canvas);
        }
    }

    /**
     * Returns a vector of bounding boxes that enclose all text between
     * start and end char indices, including start and excluding end.
     */
    public TextBox[] getRectsForRange(int start, int end, RectHeightMode rectHeightMode, RectWidthMode rectWidthMode) {
        try {
            Stats.onNativeCall();
            return _nGetRectsForRange(_ptr, start, end, rectHeightMode.ordinal(), rectWidthMode.ordinal());
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public TextBox[] getRectsForPlaceholders() {
        try {
            Stats.onNativeCall();
            return _nGetRectsForPlaceholders(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public PositionWithAffinity getGlyphPositionAtCoordinate(float dx, float dy) {
        try {
            Stats.onNativeCall();
            int res = _nGetGlyphPositionAtCoordinate(_ptr, dx, dy);
            if (res >= 0)
                return new PositionWithAffinity(res, Affinity.DOWNSTREAM);
            else
                return new PositionWithAffinity(-res-1, Affinity.UPSTREAM);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public IRange getWordBoundary(int offset) {
        try {
            Stats.onNativeCall();
            return IRange._makeFromLong(_nGetWordBoundary(_ptr, offset));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public LineMetrics[] getLineMetrics() {
        try {
            if (_text == null)
                return new LineMetrics[0];
            Stats.onNativeCall();
            return _nGetLineMetrics(_ptr, Native.getPtr(_text));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(_text);
        }
    }

    public long getLineNumber() {
        try {
            Stats.onNativeCall();
            return _nGetLineNumber(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paragraph markDirty() {
        Stats.onNativeCall();
        _nMarkDirty(_ptr);
        return this;
    }

    public int getUnresolvedGlyphsCount() {
        try {
            Stats.onNativeCall();
            return _nGetUnresolvedGlyphsCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paragraph updateAlignment(Alignment alignment) {
        Stats.onNativeCall();
        _nUpdateAlignment(_ptr, alignment.ordinal());
        return this;
    }

    // public Paragraph updateText(int from, String text) {
    //     Stats.onNativeCall();
    //     _nUpdateText(_ptr, from, text);
    //     // TODO: update _text
    //     return this;
    // }

    public Paragraph updateFontSize(int from, int to, float size) {
        try {
            if (_text != null) {
                Stats.onNativeCall();
                _nUpdateFontSize(_ptr, from, to, size, Native.getPtr(_text));
            }
            return this;
        } finally {
            Reference.reachabilityFence(_text);
        }
    }

    public Paragraph updateForegroundPaint(int from, int to, Paint paint) {
        try {
            if (_text != null) {
                Stats.onNativeCall();
                _nUpdateForegroundPaint(_ptr, from, to, Native.getPtr(paint), Native.getPtr(_text));
            }
            return this;
        } finally {
            Reference.reachabilityFence(paint);
            Reference.reachabilityFence(_text);
        }
    }

    public Paragraph updateBackgroundPaint(int from, int to, Paint paint) {
        try {
            if (_text != null) {
                Stats.onNativeCall();
                _nUpdateBackgroundPaint(_ptr, from, to, Native.getPtr(paint), Native.getPtr(_text));
            }
            return this;
        } finally {
            Reference.reachabilityFence(paint);
            Reference.reachabilityFence(_text);
        }
    }

    @ApiStatus.Internal
    public Paragraph(long ptr, ManagedString text) {
        super(ptr, _FinalizerHolder.PTR);
        Stats.onNativeCall();
        _text = text;
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

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
    public static native TextBox[] _nGetRectsForRange(long ptr, int start, int end, int rectHeightMode, int rectWidthMode);
    public static native TextBox[] _nGetRectsForPlaceholders(long ptr);
    public static native int   _nGetGlyphPositionAtCoordinate(long ptr, float dx, float dy);
    public static native long  _nGetWordBoundary(long ptr, int offset);
    public static native LineMetrics[] _nGetLineMetrics(long ptr, long textPtr);
    public static native long  _nGetLineNumber(long ptr);
    public static native void  _nMarkDirty(long ptr);
    public static native int   _nGetUnresolvedGlyphsCount(long ptr);
    public static native void  _nUpdateAlignment(long ptr, int Align);
    // public static native void  _nUpdateText(long ptr, int from, String text);
    public static native void  _nUpdateFontSize(long ptr, int from, int to, float size, long textPtr);
    public static native void  _nUpdateForegroundPaint(long ptr, int from, int to, long paintPtr, long textPtr);
    public static native void  _nUpdateBackgroundPaint(long ptr, int from, int to, long paintPtr, long textPtr);
}
