package org.jetbrains.skija;

import java.util.function.Consumer;
import java.util.function.LongConsumer;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Font extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public Font(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public Font(long ptr, boolean managed) {
        super(ptr, _FinalizerHolder.PTR, managed);
    }

    /**
     * Returns Font initialized with default values
     */
    public Font() {
        this(_nMakeDefault());
        Stats.onNativeCall();
    }

    /**
     * Returns Font with Typeface and default size
     *
     * @param typeface  typeface and style used to draw and measure text
     */
    public Font(@NotNull Typeface typeface) {
        this(_nMakeTypeface(typeface._ptr));
        Stats.onNativeCall();
    }

    /**
     * @param typeface  typeface and style used to draw and measure text
     * @param size      typographic size of the text
     */
    public Font(@NotNull Typeface typeface, float size) {
        this(_nMakeTypefaceSize(typeface._ptr, size));
        Stats.onNativeCall();
    }

    /**
     * Constructs Font with default values with Typeface and size in points,
     * horizontal scale, and horizontal skew. Horizontal scale emulates condensed
     * and expanded fonts. Horizontal skew emulates oblique fonts.
     *
     * @param typeface  typeface and style used to draw and measure text
     * @param size      typographic size of the text
     * @param scaleX    text horizonral scale
     * @param skewX     additional shear on x-axis relative to y-axis
     */
    public Font(@NotNull Typeface typeface, float size, float scaleX, float skewX) {
        this(_nMakeTypefaceSizeScaleSkew(typeface._ptr, size, scaleX, skewX));
        Stats.onNativeCall();
    }

    /**
     * Compares fonts, and returns true if they are equivalent.
     * May return false if Typeface has identical contents but different pointers.
     */
    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        return _nEquals(_ptr, Native.getPtr(other));
    }

    public static Font makeClone(long ptr) {
        Stats.onNativeCall();
        return new Font(_nMakeClone(ptr));
    }

    /**
     * If true, instructs the font manager to always hint glyphs.
     * Returned value is only meaningful if platform uses FreeType as the font manager.
     *
     * @return  true if all glyphs are hinted
    */
    public boolean isAutoHintingForced() {
        Stats.onNativeCall();
        return _nIsAutoHintingForced(_ptr);
    }

    /**
     * @return  true if font engine may return glyphs from font bitmaps instead of from outlines
     */
    public boolean areBitmapsEmbedded() {
        Stats.onNativeCall();
        return _nAreBitmapsEmbedded(_ptr);
    }

    /**
     * @return  true if glyphs may be drawn at sub-pixel offsets
     */
    public boolean isSubpixel() {
        Stats.onNativeCall();
        return _nIsSubpixel(_ptr);
    }

    /**
     * @return  true if font and glyph metrics are requested to be linearly scalable
     */
    public boolean areMetricsLinear() {
        Stats.onNativeCall();
        return _nAreMetricsLinear(_ptr);
    }

    /**
     * Returns true if bold is approximated by increasing the stroke width when creating glyph
     * bitmaps from outlines.
     *
     * @return  true if bold is approximated through stroke width
     */
    public boolean isEmboldened() {
        Stats.onNativeCall();
        return _nIsEmboldened(_ptr);
    }

    /**
     * Returns true if baselines will be snapped to pixel positions when the current transformation
     * matrix is axis aligned.
     *
     * @return  true if baselines may be snapped to pixels
     */
    public boolean isBaselineSnapped() {
        Stats.onNativeCall();
        return _nIsBaselineSnapped(_ptr);
    }

    /**
     * Sets whether to always hint glyphs. If forceAutoHinting is set, instructs the font manager to always hint glyphs. Only affects platforms that use FreeType as the font manager.
     *
     * @param value  setting to always hint glyphs
     * @return       this
     */
    public Font setAutoHintingForced(boolean value) {
        Stats.onNativeCall();
        _nSetAutoHintingForced(_ptr, value);
        return this;
    }

    /**
     * Requests, but does not require, to use bitmaps in fonts instead of outlines.
     *
     * @param value  setting to use bitmaps in fonts
     * @return       this
     */
    public Font setBitmapsEmbedded(boolean value) {
        Stats.onNativeCall();
        _nSetBitmapsEmbedded(_ptr, value);
        return this;
    }

    /**
     * Requests, but does not require, that glyphs respect sub-pixel positioning.
     *
     * @param value  setting for sub-pixel positioning 
     * @return       this
     */
    public Font setSubpixel(boolean value) {
        Stats.onNativeCall();
        _nSetSubpixel(_ptr, value);
        return this;
    }

    /**
     * Requests, but does not require, linearly scalable font and glyph metrics.
     *
     * For outline fonts 'true' means font and glyph metrics should ignore hinting and rounding.
     * Note that some bitmap formats may not be able to scale linearly and will ignore this flag.
     *
     * @param value  setting for linearly scalable font and glyph metrics.
     * @return       this
     */
    public Font setMetricsLinear(boolean value) {
        Stats.onNativeCall();
        _nSetMetricsLinear(_ptr, value);
        return this;
    }

    /**
     * Increases stroke width when creating glyph bitmaps to approximate a bold typeface.
     *
     * @param value  setting for bold approximation
     * @return       this
     */
    public Font setEmboldened(boolean value) {
        Stats.onNativeCall();
        _nSetEmboldened(_ptr, value);
        return this;
    }

    /**
     * Requests that baselines be snapped to pixels when the current transformation matrix is axis
     * aligned.
     * 
     * @param value  setting for baseline snapping to pixels
     * @return       this
     */
    public Font setBaselineSnapped(boolean value) {
        Stats.onNativeCall();
        _nSetBaselineSnapped(_ptr, value);
        return this;
    }

    /**
     * Whether edge pixels draw opaque or with partial transparency.
     */
    public FontEdging getEdging() {
        Stats.onNativeCall();
        return FontEdging.values()[_nGetEdging(_ptr)];
    }

    /**
     * Requests, but does not require, that edge pixels draw opaque or with
     * partial transparency.
     */
    public Font setEdging(FontEdging value) {
        Stats.onNativeCall();
        _nSetEdging(_ptr, value.ordinal());
        return this;
    }

    /**
     * @return  level of glyph outline adjustment
     */
    public FontHinting getHinting() {
        Stats.onNativeCall();
        return FontHinting.values()[_nGetHinting(_ptr)];
    }

    /**
     * Sets level of glyph outline adjustment. Does not check for valid values of hintingLevel.
     */
    public Font setHinting(FontHinting value) {
        Stats.onNativeCall();
        _nSetHinting(_ptr, value.ordinal());
        return this;
    }

    /**
     * Returns a font with the same attributes of this font, but with the specified size.
     */
    public Font makeWithSize(float size) {
        return new Font(getTypeface(), size, getScaleX(), getSkewX());
    }

    /**
     * @return {@link Typeface} if set, or null
     */ 
    @Nullable
    public Typeface getTypeface() {
        Stats.onNativeCall();
        long ptr = _nGetTypeface(_ptr);
        return ptr == 0 ? null : new Typeface(ptr);
    }

    /**
     * @return {@link Typeface} if set, or the default typeface.
     */
    @NotNull
    public Typeface getTypefaceOrDefault() {
        Stats.onNativeCall();
        return new Typeface(_nGetTypefaceOrDefault(_ptr));
    }

    /**
     * @return  text size in points
     */
    public float getSize() {
        Stats.onNativeCall();
        return _nGetSize(_ptr);
    }

    /**
     * @return  text scale on x-axis. Default value is 1
     */
    public float getScaleX() {
        Stats.onNativeCall();
        return _nGetScaleX(_ptr);
    }

    /**
     * @return  text skew on x-axis. Default value is 0
     */
    public float getSkewX() {
        Stats.onNativeCall();
        return _nGetSkewX(_ptr);
    }

    /**
     * Sets Typeface to typeface. Pass null to use the default typeface.
     */
    public Font setTypeface(@Nullable Typeface typeface) {
        Stats.onNativeCall();
        _nSetTypeface(_ptr, Native.getPtr(typeface));
        return this;
    }

    /**
     * Sets text size in points. Has no effect if value is not greater than or equal to zero.
     */
    public Font setSize(float value) {
        Stats.onNativeCall();
        _nSetSize(_ptr, value);
        return this;
    }

    /**
     * Sets text scale on x-axis. Default value is 1.
     */
    public Font setScaleX(float value) {
        Stats.onNativeCall();
        _nSetScaleX(_ptr, value);
        return this;
    }

    /**
     * Sets text skew on x-axis. Default value is 0.
     */
    public Font setSkewX(float value) {
        Stats.onNativeCall();
        _nSetSkewX(_ptr, value);
        return this;
    }

    /**
     *  Converts text into glyph indices.
     *
     *  @return  the corresponding glyph ids for each character.
     */
    public short[] getStringGlyphs(String s) {
        return getUTF32Glyphs(s.codePoints().toArray());
    }

    /**
     *  Given an array of UTF32 character codes, return their corresponding glyph IDs.
     *
     *  @return  the corresponding glyph IDs for each character.
     */
    public short[] getUTF32Glyphs(int[] uni) {
        Stats.onNativeCall();
        return _nGetUTF32Glyphs(_ptr, uni);
    }

    /**
     * @return  the glyph that corresponds to the specified unicode code-point (in UTF32 encoding). If the unichar is not supported, returns 0
     */
    public short getUTF32Glyph(int unichar) {
        Stats.onNativeCall();
        return _nGetUTF32Glyph(_ptr, unichar);
    }

    /**
     * @return  number of glyphs represented by text
     */
    public int getStringGlyphsCount(String s) {
        Stats.onNativeCall();
        return _nGetStringGlyphsCount(_ptr, s);
    }

    /**
     * @return  the bounding box of text
     */
    public Rect measureText(String s) {
        return measureText(s, null);
    }

    /**
     * @param p  stroke width or PathEffect may modify the advance with
     * @return   the bounding box of text
     */
    public Rect measureText(String s, Paint p) {
        Stats.onNativeCall();
        return _nMeasureText(_ptr, s, Native.getPtr(p));
    }

    public float measureTextWidth(String s) {
        Stats.onNativeCall();
        return measureTextWidth(s, null);
    }

    public float measureTextWidth(String s, Paint p) {
        Stats.onNativeCall();
        return _nMeasureTextWidth(_ptr, s, Native.getPtr(p));
    }

    /**
     * Retrieves the advances for each glyph
     */
    public float[] getWidths(short[] glyphs) {
        Stats.onNativeCall();
        return _nGetWidths(_ptr, glyphs);
    }

    /**
     * Retrieves the bounds for each glyph
     */
    public Rect[] getBounds(short[] glyphs) {
        return getBounds(glyphs, null);
    }

    /**
     * Retrieves the bounds for each glyph
     */
    public Rect[] getBounds(short[] glyphs, Paint p) {
        Stats.onNativeCall();
        return _nGetBounds(_ptr, glyphs, Native.getPtr(p));
    }

    /**
     * Retrieves the positions for each glyph.
     */
    public Point[] getPositions(short[] glyphs) {
        Stats.onNativeCall();
        return _nGetPositions(_ptr, glyphs, 0, 0);
    }    

    /**
     * Retrieves the positions for each glyph, beginning at the specified origin.
     */
    public Point[] getPositions(short[] glyphs, Point offset) {
        Stats.onNativeCall();
        return _nGetPositions(_ptr, glyphs, offset._x, offset._y);
    }

    /**
     * Retrieves the x-positions for each glyph.
     */
    public float[] getXPositions(short[] glyphs) {
        Stats.onNativeCall();
        return _nGetXPositions(_ptr, glyphs, 0);
    }    

    /**
     * Retrieves the x-positions for each glyph, beginning at the specified origin.
     */
    public float[] getXPositions(short[] glyphs, float offset) {
        Stats.onNativeCall();
        return _nGetXPositions(_ptr, glyphs, offset);
    }

    /**
     * If the glyph has an outline, returns it. The glyph outline may be empty.
     * Degenerate contours in the glyph outline will be skipped. If glyph is described by a bitmap, returns null.
     */
    @Nullable
    public Path getPath(short glyph) {
        Stats.onNativeCall();
        long ptr = _nGetPath(_ptr, glyph);
        return ptr == 0 ? null : new Path(ptr);
    }

    /**
     * Return glyph outlines, some of which might be null.
     */
    public Path[] getPaths(short[] glyphs) {
        Stats.onNativeCall();
        return _nGetPaths(_ptr, glyphs);
    }

    /**
     * Returns FontMetrics associated with Typeface. Results are scaled by text size but does not take into account
     * dimensions required by text scale, text skew, fake bold, style stroke, and {@link PathEffect}.
     */
    public FontMetrics getMetrics() {
        Stats.onNativeCall();
        return _nGetMetrics(_ptr);
    }

    /**
     * Returns the recommended spacing between lines: the sum of metrics descent, ascent, and leading.
     * Result is scaled by text size but does not take into account dimensions required by stroking and SkPathEffect.
     */
    public float getSpacing() {
        Stats.onNativeCall();
        return _nGetSpacing(_ptr);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long    _nGetFinalizer();
    @ApiStatus.Internal public static native long    _nMakeDefault();
    @ApiStatus.Internal public static native long    _nMakeTypeface(long typefacePtr);
    @ApiStatus.Internal public static native long    _nMakeTypefaceSize(long typefacePtr, float size);
    @ApiStatus.Internal public static native long    _nMakeTypefaceSizeScaleSkew(long typefacePtr, float size, float scaleX, float skewX);
    @ApiStatus.Internal public static native long    _nMakeClone(long ptr);
    @ApiStatus.Internal public static native boolean _nEquals(long ptr, long otherPtr);
    @ApiStatus.Internal public static native boolean _nIsAutoHintingForced(long ptr);
    @ApiStatus.Internal public static native boolean _nAreBitmapsEmbedded(long ptr);
    @ApiStatus.Internal public static native boolean _nIsSubpixel(long ptr);
    @ApiStatus.Internal public static native boolean _nAreMetricsLinear(long ptr);
    @ApiStatus.Internal public static native boolean _nIsEmboldened(long ptr);
    @ApiStatus.Internal public static native boolean _nIsBaselineSnapped(long ptr);
    @ApiStatus.Internal public static native void    _nSetAutoHintingForced(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nSetBitmapsEmbedded(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nSetSubpixel(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nSetMetricsLinear(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nSetEmboldened(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nSetBaselineSnapped(long ptr, boolean value);
    @ApiStatus.Internal public static native int     _nGetEdging(long ptr);
    @ApiStatus.Internal public static native void    _nSetEdging(long ptr, int value);
    @ApiStatus.Internal public static native int     _nGetHinting(long ptr);
    @ApiStatus.Internal public static native void    _nSetHinting(long ptr, int value);
    @ApiStatus.Internal public static native long    _nGetTypeface(long ptr);
    @ApiStatus.Internal public static native long    _nGetTypefaceOrDefault(long ptr);
    @ApiStatus.Internal public static native float   _nGetSize(long ptr);
    @ApiStatus.Internal public static native float   _nGetScaleX(long ptr);
    @ApiStatus.Internal public static native float   _nGetSkewX(long ptr);
    @ApiStatus.Internal public static native void    _nSetTypeface(long ptr, long typefacePtr);
    @ApiStatus.Internal public static native void    _nSetSize(long ptr, float value);
    @ApiStatus.Internal public static native void    _nSetScaleX(long ptr, float value);
    @ApiStatus.Internal public static native void    _nSetSkewX(long ptr, float value);
    @ApiStatus.Internal public static native short[] _nGetStringGlyphs(long ptr, String str);
    @ApiStatus.Internal public static native short   _nGetUTF32Glyph(long ptr, int uni);
    @ApiStatus.Internal public static native short[] _nGetUTF32Glyphs(long ptr, int[] uni);
    @ApiStatus.Internal public static native int     _nGetStringGlyphsCount(long ptr, String str);
    @ApiStatus.Internal public static native Rect    _nMeasureText(long ptr, String str, long paintPtr);
    @ApiStatus.Internal public static native float   _nMeasureTextWidth(long ptr, String str, long paintPtr); 
    @ApiStatus.Internal public static native float[] _nGetWidths(long ptr, short[] glyphs);
    @ApiStatus.Internal public static native Rect[]  _nGetBounds(long ptr, short[] glyphs, long paintPtr);
    @ApiStatus.Internal public static native Point[] _nGetPositions(long ptr, short[] glyphs, float x, float y);
    @ApiStatus.Internal public static native float[] _nGetXPositions(long ptr, short[] glyphs, float x);
    @ApiStatus.Internal public static native long    _nGetPath(long ptr, short glyph);
    @ApiStatus.Internal public static native Path[]  _nGetPaths(long ptr, short[] glyphs);
    @ApiStatus.Internal public static native FontMetrics _nGetMetrics(long ptr);
    @ApiStatus.Internal public static native float   _nGetSpacing(long ptr);
}