package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class TextBlobBuilder extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public TextBlobBuilder(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    /**
     * Constructs empty TextBlobBuilder. By default, TextBlobBuilder has no runs.
     *
     * @see <a href="https://fiddle.skia.org/c/@TextBlobBuilder_empty_constructor">https://fiddle.skia.org/c/@TextBlobBuilder_empty_constructor</a>
     */
    public TextBlobBuilder() {
        this(_nMake());
        Stats.onNativeCall();
    }

    /** 
     * <p>Returns TextBlob built from runs of glyphs added by builder. Returned
     * TextBlob is immutable; it may be copied, but its contents may not be altered.
     * Returns null if no runs of glyphs were added by builder.</p>
     *
     * <p>Resets TextBlobBuilder to its initial empty state, allowing it to be
     * reused to build a new set of runs.</p>
     *
     * @return  SkTextBlob or null
     *
     * @see <a href="https://fiddle.skia.org/c/@TextBlobBuilder_make">https://fiddle.skia.org/c/@TextBlobBuilder_make</a>
     */
    @Nullable
    public TextBlob build() {
        try {
            Stats.onNativeCall();
            long ptr = _nBuild(_ptr);
            return ptr == 0 ? null : new TextBlob(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /** 
     * Glyphs are positioned on a baseline at (x, y), using font metrics to
     * determine their relative placement.
     *
     * @param font    Font used for this run
     * @param text    Text to append in this run
     * @param x       horizontal offset within the blob
     * @param y       vertical offset within the blob
     * @return        this
     */
    public TextBlobBuilder appendRun(Font font, String text, float x, float y) {
        return appendRun(font, font.getStringGlyphs(text), x, y, null);
    }

    /** 
     * <p>Glyphs are positioned on a baseline at (x, y), using font metrics to
     * determine their relative placement.</p>
     *
     * <p>bounds defines an optional bounding box, used to suppress drawing when TextBlob
     * bounds does not intersect Surface bounds. If bounds is null, TextBlob bounds
     * is computed from (x, y) and glyphs metrics.</p>
     *
     * @param font    Font used for this run
     * @param text    Text to append in this run
     * @param x       horizontal offset within the blob
     * @param y       vertical offset within the blob
     * @param bounds  optional run bounding box
     * @return        this
     */
    public TextBlobBuilder appendRun(Font font, String text, float x, float y, @Nullable Rect bounds) {
        return appendRun(font, font.getStringGlyphs(text), x, y, bounds);
    }

    /** 
     * <p>Glyphs are positioned on a baseline at (x, y), using font metrics to
     * determine their relative placement.</p>
     *
     * <p>bounds defines an optional bounding box, used to suppress drawing when TextBlob
     * bounds does not intersect Surface bounds. If bounds is null, TextBlob bounds
     * is computed from (x, y) and glyphs metrics.</p>
     *
     * @param font    Font used for this run
     * @param glyphs  glyphs in this run
     * @param x       horizontal offset within the blob
     * @param y       vertical offset within the blob
     * @param bounds  optional run bounding box
     * @return        this
     */
    public TextBlobBuilder appendRun(Font font, short[] glyphs, float x, float y, @Nullable Rect bounds) {
        try {
            Stats.onNativeCall();
            _nAppendRun(_ptr, Native.getPtr(font), glyphs, x, y, bounds);
            return this;
        } finally {
            Reference.reachabilityFence(font);
        }
    }

    /** 
     * <p>Glyphs are positioned on a baseline at y, using x-axis positions from xs.</p>
     *
     * @param font    Font used for this run
     * @param glyphs  glyphs in this run
     * @param xs      horizontal positions on glyphs within the blob
     * @param y       vertical offset within the blob
     * @return        this
     */
    public TextBlobBuilder appendRunPosH(Font font, short[] glyphs, float[] xs, float y) {
        return appendRunPosH(font, glyphs, xs, y, null);
    }

    /** 
     * <p>Glyphs are positioned on a baseline at y, using x-axis positions from xs.</p>
     *
     * <p>bounds defines an optional bounding box, used to suppress drawing when TextBlob
     * bounds does not intersect Surface bounds. If bounds is null, TextBlob bounds
     * is computed from (x, y) and glyphs metrics.</p>
     *
     * @param font    Font used for this run
     * @param glyphs  glyphs in this run
     * @param xs      horizontal positions of glyphs within the blob
     * @param y       vertical offset within the blob
     * @param bounds  optional run bounding box
     * @return        this
     */
    public TextBlobBuilder appendRunPosH(Font font, short[] glyphs, float[] xs, float y, @Nullable Rect bounds) {
        try {
            assert glyphs.length == xs.length : "glyphs.length " + glyphs.length + " != xs.length " + xs.length;
            Stats.onNativeCall();
            _nAppendRunPosH(_ptr, Native.getPtr(font), glyphs, xs, y, bounds);
            return this;
        } finally {
            Reference.reachabilityFence(font);
        }
    }

    /** 
     * <p>Glyphs are positioned at positions from pos.</p>
     *
     * @param font    Font used for this run
     * @param glyphs  glyphs in this run
     * @param pos     positions of glyphs within the blob
     * @return        this
     */
    public TextBlobBuilder appendRunPos(Font font, short[] glyphs, Point[] pos) {
        return appendRunPos(font, glyphs, pos, null);
    }

    /** 
     * <p>Glyphs are positioned at positions from pos.</p>
     *
     * <p>bounds defines an optional bounding box, used to suppress drawing when TextBlob
     * bounds does not intersect Surface bounds. If bounds is null, TextBlob bounds
     * is computed from (x, y) and glyphs metrics.</p>
     *
     * @param font    Font used for this run
     * @param glyphs  glyphs in this run
     * @param pos     positions of glyphs within the blob
     * @param bounds  optional run bounding box
     * @return        this
     */
    public TextBlobBuilder appendRunPos(Font font, short[] glyphs, Point[] pos, @Nullable Rect bounds) {
        try {
            assert glyphs.length == pos.length : "glyphs.length " + glyphs.length + " != pos.length " + pos.length;
            float[] floatPos = new float[pos.length * 2];
            for (int i = 0; i < pos.length; ++i) {
                floatPos[i * 2]     = pos[i]._x;
                floatPos[i * 2 + 1] = pos[i]._y;
            }
            Stats.onNativeCall();
            _nAppendRunPos(_ptr, Native.getPtr(font), glyphs, floatPos, bounds);
            return this;
        } finally {
            Reference.reachabilityFence(font);
        }
    }

    public TextBlobBuilder appendRunRSXform(Font font, short[] glyphs, RSXform[] xform) {
        try {
            assert glyphs.length == xform.length : "glyphs.length " + glyphs.length + " != xform.length " + xform.length;
            float[] floatXform = new float[xform.length * 4];
            for (int i = 0; i < xform.length; ++i) {
                floatXform[i * 4]     = xform[i]._scos;
                floatXform[i * 4 + 1] = xform[i]._ssin;
                floatXform[i * 4 + 2] = xform[i]._tx;
                floatXform[i * 4 + 3] = xform[i]._ty;
            }
            Stats.onNativeCall();
            _nAppendRunRSXform(_ptr, Native.getPtr(font), glyphs, floatXform);
            return this;
        } finally {
            Reference.reachabilityFence(font);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMake();
    @ApiStatus.Internal public static native long _nBuild(long ptr);
    @ApiStatus.Internal public static native void _nAppendRun(long ptr, long fontPtr, short[] glyphs, float x, float y, Rect bounds);
    @ApiStatus.Internal public static native void _nAppendRunPosH(long ptr, long fontPtr, short[] glyphs, float[] xs, float y, Rect bounds);
    @ApiStatus.Internal public static native void _nAppendRunPos(long ptr, long fontPtr, short[] glyphs, float[] pos, Rect bounds);
    @ApiStatus.Internal public static native void _nAppendRunRSXform(long ptr, long fontPtr, short[] glyphs, float[] xform);
}