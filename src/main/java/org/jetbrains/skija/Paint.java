package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Paint extends Managed {
    public Paint() {
        super(_nMake(), _finalizerPtr);
        Stats.onNativeCall();
    }

    @ApiStatus.Internal
    public Paint(long ptr, boolean managed) {
        super(ptr, _finalizerPtr, managed);
    }

    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        return _nEquals(_ptr, Native.getPtr(other));
    }

    public boolean isAntiAlias() {
        Stats.onNativeCall();
        return _nIsAntiAlias(_ptr);
    }

    public Paint setAntiAlias(boolean value) {
        Stats.onNativeCall();
        _nSetAntiAlias(_ptr, value);
        return this;
    }

    public PaintMode getMode() {
        Stats.onNativeCall();
        return PaintMode.values()[_nGetMode(_ptr)];
    }

    public Paint setMode(PaintMode style) {
        Stats.onNativeCall();
        _nSetMode(_ptr, style.ordinal());
        return this;
    }

    public int getColor() {
        Stats.onNativeCall();
        return _nGetColor(_ptr);
    }

    public Paint setColor(int color) {
        Stats.onNativeCall();
        _nSetColor(_ptr, color);
        return this;
    }

    /**
     * Retrieves alpha and RGB, unpremultiplied, as four floating point values. RGB are
     * extended sRGB values (sRGB gamut, and encoded with the sRGB transfer function).
     *
     * @return  unpremultiplied RGBA
     */
    public Color4f getColor4f() {
        Stats.onNativeCall();
        return _nGetColor4f(_ptr);
    }

    /**
     * Sets alpha and RGB used when stroking and filling. The color is four floating
     * point values, unpremultiplied. The color values are interpreted as being in sRGB.
     *
     * @param color       unpremultiplied RGBA
     * @return            this
     */
    public Paint setColor4f(Color4f color) {
        return setColor4f(color, null);
    }

    /**
     * Sets alpha and RGB used when stroking and filling. The color is four floating
     * point values, unpremultiplied. The color values are interpreted as being in
     * the colorSpace. If colorSpace is nullptr, then color is assumed to be in the
     * sRGB color space.
     *
     * @param color       unpremultiplied RGBA
     * @param colorSpace  SkColorSpace describing the encoding of color
     * @return            this
     */
    public Paint setColor4f(Color4f color, ColorSpace colorSpace) {
        Stats.onNativeCall();
        _nSetColor4f(_ptr, color.getR(), color.getG(), color.getB(), color.getA(), Native.getPtr(colorSpace));
        return this;
    }

    public float getStrokeWidth() {
        Stats.onNativeCall();
        return _nGetStrokeWidth(_ptr);
    }

    public Paint setStrokeWidth(float width) {
        Stats.onNativeCall();
        _nSetStrokeWidth(_ptr, width);
        return this;
    }

    public float getStrokeMiter() {
        Stats.onNativeCall();
        return _nGetStrokeMiter(_ptr);
    }

    public Paint setStrokeMiter(float limit) {
        Stats.onNativeCall();
        _nSetStrokeMiter(_ptr, limit);
        return this;
    }

    public PaintStrokeCap getStrokeCap() {
        Stats.onNativeCall();
        return PaintStrokeCap.values()[_nGetStrokeCap(_ptr)];
    }

    public Paint setStrokeCap(PaintStrokeCap cap) {
        Stats.onNativeCall();
        _nSetStrokeCap(_ptr, cap.ordinal());
        return this;
    }

    public PaintStrokeJoin getStrokeJoin() {
        Stats.onNativeCall();
        return PaintStrokeJoin.values()[_nGetStrokeJoin(_ptr)];
    }

    public Paint setStrokeJoin(PaintStrokeJoin join) {
        Stats.onNativeCall();
        _nSetStrokeJoin(_ptr, join.ordinal());
        return this;
    }

    public Path getFillPath(Path src) {
        return getFillPath(src, null, 1);
    }

    public Path getFillPath(Path src, Rect cull, float resScale) {
        Stats.onNativeCall();
        if (cull == null)
            return new Path(_nGetFillPath(_ptr, Native.getPtr(src), resScale));
        else
            return new Path(_nGetFillPathCull(_ptr, Native.getPtr(src), cull._left, cull._top, cull._right, cull._bottom, resScale));
    }

    /**
     * @return  {@link Shader} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refShader">https://fiddle.skia.org/c/@Paint_refShader</a>
     */
    public Shader getShader() {
        Stats.onNativeCall();
        long shaderPtr = _nGetShader(_ptr);
        return shaderPtr == 0 ? null : new Shader(shaderPtr);
    }

    /**
     * @param shader  how geometry is filled with color; if null, color is used instead
     *
     * @see <a href="https://fiddle.skia.org/c/@Color_Filter_Methods">https://fiddle.skia.org/c/@Color_Filter_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setShader">https://fiddle.skia.org/c/@Paint_setShader</a>
     */
    public Paint setShader(Shader shader) {
        Stats.onNativeCall();
        _nSetShader(_ptr, Native.getPtr(shader));
        return this;
    }

    /**
     * @return  {@link ColorFilter} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refColorFilter">https://fiddle.skia.org/c/@Paint_refColorFilter</a>
     */
    public ColorFilter getColorFilter() {
        Stats.onNativeCall();
        long colorFilterPtr = _nGetColorFilter(_ptr);
        return colorFilterPtr == 0 ? null : new ColorFilter(colorFilterPtr);
    }

    /**
     * @param colorFilter {@link ColorFilter} to apply to subsequent draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Blend_Mode_Methods">https://fiddle.skia.org/c/@Blend_Mode_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setColorFilter">https://fiddle.skia.org/c/@Paint_setColorFilter</a>
     */
    public Paint setColorFilter(ColorFilter colorFilter) {
        Stats.onNativeCall();
        _nSetColorFilter(_ptr, Native.getPtr(colorFilter));
        return this;
    }

    public BlendMode getBlendMode() {
        Stats.onNativeCall();
        return BlendMode.values()[_nGetBlendMode(_ptr)];
    }

    public Paint setBlendMode(BlendMode mode) {
        Stats.onNativeCall();
        _nSetBlendMode(_ptr, mode.ordinal());
        return this;
    }

    /**
     * @return  {@link PathEffect} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refPathEffect">https://fiddle.skia.org/c/@Paint_refPathEffect</a>
     */
    public PathEffect getPathEffect() {
        Stats.onNativeCall();
        long pathEffectPtr = _nGetPathEffect(_ptr);
        return pathEffectPtr == 0 ? null : new PathEffect(pathEffectPtr);
    }

    /**
     * @param p  replace {@link Path} with a modification when drawn
     *
     * @see <a href="https://fiddle.skia.org/c/@Mask_Filter_Methods">https://fiddle.skia.org/c/@Mask_Filter_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setPathEffect">https://fiddle.skia.org/c/@Paint_setPathEffect</a>
     */
    public Paint setPathEffect(PathEffect p) {
        Stats.onNativeCall();
        _nSetPathEffect(_ptr, Native.getPtr(p));
        return this;
    }

    /**
     * @return  {@link MaskFilter} if previously set, null otherwise
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refMaskFilter">https://fiddle.skia.org/c/@Paint_refMaskFilter</a>
     */
    public MaskFilter getMaskFilter() {
        Stats.onNativeCall();
        long maskFilterPtr = _nGetMaskFilter(_ptr);
        return maskFilterPtr == 0 ? null : new MaskFilter(maskFilterPtr);
    }

    /**
     * @param maskFilter  modifies clipping mask generated from drawn geometry
     * @return            this
     *
     * @see  <a href="https://fiddle.skia.org/c/@Paint_setMaskFilter">https://fiddle.skia.org/c/@Paint_setMaskFilter</a>
     * @see  <a href="https://fiddle.skia.org/c/@Typeface_Methods">https://fiddle.skia.org/c/@Typeface_Methods</a>
     */
    public Paint setMaskFilter(MaskFilter maskFilter) {
        Stats.onNativeCall();
        _nSetMaskFilter(_ptr, Native.getPtr(maskFilter));
        return this;
    }

    /**
     * @return  {@link ImageFilter} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refImageFilter">https://fiddle.skia.org/c/@Paint_refImageFilter</a>
     */
    public ImageFilter getImageFilter() {
        Stats.onNativeCall();
        long imageFilterPtr = _nGetImageFilter(_ptr);
        return imageFilterPtr == 0 ? null : new ImageFilter(imageFilterPtr);
    }

    /**
     * @param imageFilter  how SkImage is sampled when transformed
     *
     * @see <a href="https://fiddle.skia.org/c/@Draw_Looper_Methods">https://fiddle.skia.org/c/@Draw_Looper_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setImageFilter">https://fiddle.skia.org/c/@Paint_setImageFilter</a>
     */
    public Paint setImageFilter(ImageFilter imageFilter) {
        Stats.onNativeCall();
        _nSetImageFilter(_ptr, Native.getPtr(imageFilter));
        return this;
    }

    public FilterQuality getFilterQuality() {
        Stats.onNativeCall();
        return FilterQuality.values()[_nGetFilterQuality(_ptr)];
    }

    public Paint setFilterQuality(FilterQuality filterQuality) {
        Stats.onNativeCall();
        _nSetFilterQuality(_ptr, filterQuality.ordinal());
        return this;
    }

    public static final  long  _finalizerPtr = _nGetFinalizer();
    public static native long  _nMake();
    public static native long  _nGetFinalizer();
    public static native boolean _nEquals(long ptr, long otherPtr);
    public static native boolean _nIsAntiAlias(long ptr);
    public static native void  _nSetAntiAlias(long ptr, boolean value);
    public static native int   _nGetMode(long ptr);
    public static native void  _nSetMode(long ptr, int value);
    public static native int   _nGetColor(long ptr);
    public static native void  _nSetColor(long ptr, int argb);
    public static native Color4f _nGetColor4f(long ptr);
    public static native void  _nSetColor4f(long ptr, float r, float g, float b, float a, long colorSpacePtr);
    public static native float _nGetStrokeWidth(long ptr);
    public static native void  _nSetStrokeWidth(long ptr, float value);
    public static native float _nGetStrokeMiter(long ptr);
    public static native void  _nSetStrokeMiter(long ptr, float value);
    public static native int   _nGetStrokeCap(long ptr);
    public static native void  _nSetStrokeCap(long ptr, int value);
    public static native int   _nGetStrokeJoin(long ptr);
    public static native void  _nSetStrokeJoin(long ptr, int value);
    public static native long  _nGetFillPath(long ptr, long path, float resScale);
    public static native long  _nGetFillPathCull(long ptr, long path, float left, float top, float right, float bottom, float resScale);
    public static native long  _nGetShader(long ptr);
    public static native void  _nSetShader(long ptr, long shaderPtr);
    public static native long  _nGetColorFilter(long ptr);
    public static native void  _nSetColorFilter(long ptr, long colorFilterPtr);
    public static native int   _nGetBlendMode(long ptr);
    public static native void  _nSetBlendMode(long ptr, int mode);
    public static native long  _nGetPathEffect(long ptr);
    public static native void  _nSetPathEffect(long ptr, long pathEffectPtr);
    public static native long  _nGetMaskFilter(long ptr);
    public static native void  _nSetMaskFilter(long ptr, long filterPtr);
    public static native long  _nGetImageFilter(long ptr);
    public static native void  _nSetImageFilter(long ptr, long filterPtr);
    public static native int   _nGetFilterQuality(long ptr);
    public static native void  _nSetFilterQuality(long ptr, int quality);
}

