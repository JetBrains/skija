package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class TextBlob extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public TextBlob(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    /** 
     * Returns conservative bounding box. Uses Paint associated with each glyph to
     * determine glyph bounds, and unions all bounds. Returned bounds may be
     * larger than the bounds of all glyphs in runs.
     *
     * @return  conservative bounding box
     */
    public Rect getBounds() {
        Stats.onNativeCall();
        return _nBounds(_ptr);
    }

    /**
     * Returns a non-zero value unique among all text blobs.
     *
     * @return  identifier for TextBlob
     */
    public int getUniqueId() {
        Stats.onNativeCall();
        return _nGetUniqueId(_ptr);
    }

    /**
     * <p>Returns the number of intervals that intersect bounds.
     * bounds describes a pair of lines parallel to the text advance.
     * The return array size is a multiple of two, and is at most twice the number of glyphs in
     * the the blob.</p>
     *
     * <p>Runs within the blob that contain SkRSXform are ignored when computing intercepts.</p>
     *
     * @param lowerBound lower line parallel to the advance
     * @param upperBound upper line parallel to the advance
     * @param paint      specifies stroking, PathEffect that affects the result; may be null
     * @return           intersections; may be null
     */
    @Nullable
    public float[] getIntercepts(float lowerBound, float upperBound, @Nullable Paint paint) {
        Stats.onNativeCall();
        return _nGetIntercepts(_ptr, lowerBound, upperBound, Native.getPtr(paint));
    }

    /**
     * Returns a TextBlob built from a single run of text with x-positions and a single y value.
     * Returns null if glyphs is empty.
     *
     * @param glyphs  glyphs drawn
     * @param xpos    array of x-positions, must contain values for all of the glyphs.
     * @param ypos    shared y-position for each glyph, to be paired with each xpos.
     * @param font    Font used for this run
     * @return        new TextBlob or null
     */
    public static TextBlob makeFromPosH(short[] glyphs, float[] xpos, float ypos, Font font) {
        assert glyphs.length == xpos.length : "glyphs.length " + glyphs.length + " != xpos.length " + xpos.length;
        Stats.onNativeCall();
        long ptr = _nMakeFromPosH(glyphs, xpos, ypos, Native.getPtr(font));
        return ptr == 0 ? null : new TextBlob(ptr);
    }

    /**
     * Returns a TextBlob built from a single run of text with positions.
     * Returns null if glyphs is empty.
     *
     * @param glyphs  glyphs drawn
     * @param pos     array of positions, must contain values for all of the glyphs.
     * @param font    Font used for this run
     * @return        new TextBlob or null
     */
    public static TextBlob makeFromPos(short[] glyphs, Point[] pos, Font font) {
        assert glyphs.length == pos.length : "glyphs.length " + glyphs.length + " != pos.length " + pos.length;
        float[] floatPos = new float[pos.length * 2];
        for (int i = 0; i < pos.length; ++i) {
            floatPos[i * 2]     = pos[i]._x;
            floatPos[i * 2 + 1] = pos[i]._y;
        }
        Stats.onNativeCall();
        long ptr = _nMakeFromPos(glyphs, floatPos, Native.getPtr(font));
        return ptr == 0 ? null : new TextBlob(ptr);
    }

    public static TextBlob makeFromRSXform(short[] glyphs, RSXform[] xform, Font font) {
        assert glyphs.length == xform.length : "glyphs.length " + glyphs.length + " != xform.length " + xform.length;
        float[] floatXform = new float[xform.length * 4];
        for (int i = 0; i < xform.length; ++i) {
            floatXform[i * 4]     = xform[i]._scos;
            floatXform[i * 4 + 1] = xform[i]._ssin;
            floatXform[i * 4 + 2] = xform[i]._tx;
            floatXform[i * 4 + 3] = xform[i]._ty;
        }
        Stats.onNativeCall();
        long ptr = _nMakeFromRSXform(glyphs, floatXform, Native.getPtr(font));
        return ptr == 0 ? null : new TextBlob(ptr);
    }

    public Data serializeToData() {
        Stats.onNativeCall();
        return new Data(_nSerializeToData(_ptr));
    }

    @Nullable
    public static TextBlob makeFromData(Data data) {
        Stats.onNativeCall();
        long ptr = _nMakeFromData(Native.getPtr(data));
        return ptr == 0 ? null : new TextBlob(ptr);
    }


    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native Rect _nBounds(long ptr);
    @ApiStatus.Internal public static native int  _nGetUniqueId(long ptr);
    @ApiStatus.Internal public static native float[]  _nGetIntercepts(long ptr, float lower, float upper, long paintPtr);
    @ApiStatus.Internal public static native long _nMakeFromPosH(short[] glyphs, float[] xpos, float ypos, long fontPtr);
    @ApiStatus.Internal public static native long _nMakeFromPos(short[] glyphs, float[] pos, long fontPtr);
    @ApiStatus.Internal public static native long _nMakeFromRSXform(short[] glyphs, float[] xform, long fontPtr);
    @ApiStatus.Internal public static native long _nSerializeToData(long ptr /*, SkSerialProcs */);
    @ApiStatus.Internal public static native long _nMakeFromData(long dataPtr /*, SkDeserialProcs */);
}