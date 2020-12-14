package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Paint extends Managed {
    static { Library.staticLoad(); }
    
    public Paint() {
        super(_nMake(), _FinalizerHolder.PTR);
        Stats.onNativeCall();
    }

    @ApiStatus.Internal
    public Paint(long ptr, boolean managed) {
        super(ptr, _FinalizerHolder.PTR, managed);
    }

    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        try {
            return _nEquals(_ptr, Native.getPtr(other));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(other);
        }
    }

    public boolean isAntiAlias() {
        try {
            Stats.onNativeCall();
            return _nIsAntiAlias(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setAntiAlias(boolean value) {
        Stats.onNativeCall();
        _nSetAntiAlias(_ptr, value);
        return this;
    }

    public PaintMode getMode() {
        try {
            Stats.onNativeCall();
            return PaintMode.values()[_nGetMode(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setMode(PaintMode style) {
        Stats.onNativeCall();
        _nSetMode(_ptr, style.ordinal());
        return this;
    }

    public int getColor() {
        try {
            Stats.onNativeCall();
            return _nGetColor(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            Stats.onNativeCall();
            return _nGetColor4f(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            Stats.onNativeCall();
            _nSetColor4f(_ptr, color.getR(), color.getG(), color.getB(), color.getA(), Native.getPtr(colorSpace));
            return this;
        } finally {
            Reference.reachabilityFence(colorSpace);
        }
    }

    public float getStrokeWidth() {
        try {
            Stats.onNativeCall();
            return _nGetStrokeWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setStrokeWidth(float width) {
        Stats.onNativeCall();
        _nSetStrokeWidth(_ptr, width);
        return this;
    }

    public float getStrokeMiter() {
        try {
            Stats.onNativeCall();
            return _nGetStrokeMiter(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setStrokeMiter(float limit) {
        Stats.onNativeCall();
        _nSetStrokeMiter(_ptr, limit);
        return this;
    }

    public PaintStrokeCap getStrokeCap() {
        try {
            Stats.onNativeCall();
            return PaintStrokeCap.values()[_nGetStrokeCap(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setStrokeCap(PaintStrokeCap cap) {
        Stats.onNativeCall();
        _nSetStrokeCap(_ptr, cap.ordinal());
        return this;
    }

    public PaintStrokeJoin getStrokeJoin() {
        try {
            Stats.onNativeCall();
            return PaintStrokeJoin.values()[_nGetStrokeJoin(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            Stats.onNativeCall();
            if (cull == null)
                return new Path(_nGetFillPath(_ptr, Native.getPtr(src), resScale));
            else
                return new Path(_nGetFillPathCull(_ptr, Native.getPtr(src), cull._left, cull._top, cull._right, cull._bottom, resScale));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(src);
        }
    }

    /**
     * @return  {@link Shader} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refShader">https://fiddle.skia.org/c/@Paint_refShader</a>
     */
    public Shader getShader() {
        try {
            Stats.onNativeCall();
            long shaderPtr = _nGetShader(_ptr);
            return shaderPtr == 0 ? null : new Shader(shaderPtr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @param shader  how geometry is filled with color; if null, color is used instead
     *
     * @see <a href="https://fiddle.skia.org/c/@Color_Filter_Methods">https://fiddle.skia.org/c/@Color_Filter_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setShader">https://fiddle.skia.org/c/@Paint_setShader</a>
     */
    public Paint setShader(Shader shader) {
        try {
            Stats.onNativeCall();
            _nSetShader(_ptr, Native.getPtr(shader));
            return this;
        } finally {
            Reference.reachabilityFence(shader);
        }
    }

    /**
     * @return  {@link ColorFilter} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refColorFilter">https://fiddle.skia.org/c/@Paint_refColorFilter</a>
     */
    public ColorFilter getColorFilter() {
        try {
            Stats.onNativeCall();
            long colorFilterPtr = _nGetColorFilter(_ptr);
            return colorFilterPtr == 0 ? null : new ColorFilter(colorFilterPtr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @param colorFilter {@link ColorFilter} to apply to subsequent draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Blend_Mode_Methods">https://fiddle.skia.org/c/@Blend_Mode_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setColorFilter">https://fiddle.skia.org/c/@Paint_setColorFilter</a>
     */
    public Paint setColorFilter(ColorFilter colorFilter) {
        try {
            Stats.onNativeCall();
            _nSetColorFilter(_ptr, Native.getPtr(colorFilter));
            return this;
        } finally {
            Reference.reachabilityFence(colorFilter);
        }
    }

    public BlendMode getBlendMode() {
        try {
            Stats.onNativeCall();
            return BlendMode.values()[_nGetBlendMode(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            Stats.onNativeCall();
            long pathEffectPtr = _nGetPathEffect(_ptr);
            return pathEffectPtr == 0 ? null : new PathEffect(pathEffectPtr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @param p  replace {@link Path} with a modification when drawn
     *
     * @see <a href="https://fiddle.skia.org/c/@Mask_Filter_Methods">https://fiddle.skia.org/c/@Mask_Filter_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setPathEffect">https://fiddle.skia.org/c/@Paint_setPathEffect</a>
     */
    public Paint setPathEffect(PathEffect p) {
        try {
            Stats.onNativeCall();
            _nSetPathEffect(_ptr, Native.getPtr(p));
            return this;
        } finally {
            Reference.reachabilityFence(p);
        }
    }

    /**
     * @return  {@link MaskFilter} if previously set, null otherwise
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refMaskFilter">https://fiddle.skia.org/c/@Paint_refMaskFilter</a>
     */
    public MaskFilter getMaskFilter() {
        try {
            Stats.onNativeCall();
            long maskFilterPtr = _nGetMaskFilter(_ptr);
            return maskFilterPtr == 0 ? null : new MaskFilter(maskFilterPtr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @param maskFilter  modifies clipping mask generated from drawn geometry
     * @return            this
     *
     * @see  <a href="https://fiddle.skia.org/c/@Paint_setMaskFilter">https://fiddle.skia.org/c/@Paint_setMaskFilter</a>
     * @see  <a href="https://fiddle.skia.org/c/@Typeface_Methods">https://fiddle.skia.org/c/@Typeface_Methods</a>
     */
    public Paint setMaskFilter(MaskFilter maskFilter) {
        try {
            Stats.onNativeCall();
            _nSetMaskFilter(_ptr, Native.getPtr(maskFilter));
            return this;
        } finally {
            Reference.reachabilityFence(maskFilter);
        }
    }

    /**
     * @return  {@link ImageFilter} or null
     * @see     <a href="https://fiddle.skia.org/c/@Paint_refImageFilter">https://fiddle.skia.org/c/@Paint_refImageFilter</a>
     */
    public ImageFilter getImageFilter() {
        try {
            Stats.onNativeCall();
            long imageFilterPtr = _nGetImageFilter(_ptr);
            return imageFilterPtr == 0 ? null : new ImageFilter(imageFilterPtr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @param imageFilter  how SkImage is sampled when transformed
     *
     * @see <a href="https://fiddle.skia.org/c/@Draw_Looper_Methods">https://fiddle.skia.org/c/@Draw_Looper_Methods</a>
     * @see <a href="https://fiddle.skia.org/c/@Paint_setImageFilter">https://fiddle.skia.org/c/@Paint_setImageFilter</a>
     */
    public Paint setImageFilter(ImageFilter imageFilter) {
        try {
            Stats.onNativeCall();
            _nSetImageFilter(_ptr, Native.getPtr(imageFilter));
            return this;
        } finally {
            Reference.reachabilityFence(imageFilter);
        }
    }

    public FilterQuality getFilterQuality() {
        try {
            Stats.onNativeCall();
            return FilterQuality.values()[_nGetFilterQuality(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Paint setFilterQuality(FilterQuality filterQuality) {
        Stats.onNativeCall();
        _nSetFilterQuality(_ptr, filterQuality.ordinal());
        return this;
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

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

