package org.jetbrains.skija;

public class Paint extends Managed {
    public enum Style { FILL, STROKE, STROKE_AND_FILL }
    public enum Cap { BUTT, ROUND, SQUARE }
    public enum Join { MITER, ROUND, BEVEL }

    public Paint() { super(nInit(), nativeFinalizer); Stats.onNativeCall(); }

    public boolean isAntiAlias() { Stats.onNativeCall(); return nIsAntiAlias(_ptr); }
    public Paint setAntiAlias(boolean value) { Stats.onNativeCall(); nSetAntiAlias(_ptr, value); return this; }

    public int getColor() { Stats.onNativeCall(); return nGetColor(_ptr); }
    public Paint setColor(int color) { Stats.onNativeCall(); nSetColor(_ptr, color); return this; }

    public Style getStyle() { Stats.onNativeCall(); return Style.values()[nGetStyle(_ptr)]; }
    public Paint setStyle(Style style) { Stats.onNativeCall(); nSetStyle(_ptr, style.ordinal()); return this; }

    public float getStrokeWidth() { Stats.onNativeCall(); return nGetStrokeWidth(_ptr); }
    public Paint setStrokeWidth(float width) { Stats.onNativeCall(); nSetStrokeWidth(_ptr, width); return this; }

    public long getStrokeMiter() { Stats.onNativeCall(); return nGetStrokeMiter(_ptr); }
    public Paint setStrokeMiter(float limit) { Stats.onNativeCall(); nSetStrokeMiter(_ptr, limit); return this; }

    public Cap getStrokeCap() { Stats.onNativeCall(); return Cap.values()[nGetStrokeJoin(_ptr)]; }
    public Paint setStrokeCap(Cap cap) { Stats.onNativeCall(); nSetStrokeCap(_ptr, cap.ordinal()); return this; }

    public Join getStrokeJoin() { Stats.onNativeCall(); return Join.values()[nGetStrokeJoin(_ptr)]; }
    public Paint setStrokeJoin(Join join) { Stats.onNativeCall(); nSetStrokeJoin(_ptr, join.ordinal()); return this; }

    public Path getFillPath(Path src) { return getFillPath(src, null, 1); }
    
    public Path getFillPath(Path src, Rect cull, float resScale) {
        Stats.onNativeCall();
        if (cull == null)
            return new Path(nGetFillPath(_ptr, Native.getPtr(src), resScale));
        else
            return new Path(nGetFillPathCull(_ptr, Native.getPtr(src), cull.left, cull.top, cull.right, cull.bottom, resScale));
    }

    protected MaskFilter maskFilter;
    public MaskFilter getMaskFilter() { return maskFilter; }
    public Paint setMaskFilter(MaskFilter f) { this.maskFilter = f; Stats.onNativeCall(); nSetMaskFilter(_ptr, Native.getPtr(f)); return this; }

    protected ImageFilter imageFilter;
    public ImageFilter getImageFilter() { return imageFilter; }
    public Paint setImageFilter(ImageFilter f) { this.imageFilter = f; Stats.onNativeCall(); nSetImageFilter(_ptr, Native.getPtr(f)); return this; }

    protected BlendMode blendMode = BlendMode.SRC_OVER;
    public BlendMode getBlendMode() { return blendMode; }
    public Paint setBlendMode(BlendMode mode) { this.blendMode = mode; Stats.onNativeCall(); nSetBlendMode(_ptr, mode.ordinal()); return this; }

    protected PathEffect pathEffect;
    public PathEffect getPathEffect() { return pathEffect; }
    public Paint setPathEffect(PathEffect p) { this.pathEffect = p; Stats.onNativeCall(); nSetPathEffect(_ptr, Native.getPtr(p)); return this; }

    protected Shader shader;
    public Shader getShader() { return shader; }
    public Paint setShader(Shader shader) { this.shader = shader; Stats.onNativeCall(); nSetShader(_ptr, Native.getPtr(shader)); return this; }

    protected ColorFilter colorFilter;
    public ColorFilter getColorFilter() { return colorFilter; }
    public Paint setColorFilter(ColorFilter colorFilter) { this.colorFilter = colorFilter; Stats.onNativeCall(); nSetColorFilter(_ptr, Native.getPtr(colorFilter)); return this; }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native boolean nIsAntiAlias(long nativeInstance);
    private static native void nSetAntiAlias(long nativeInstance, boolean value);
    private static native int nGetColor(long nativeInstance);
    private static native void nSetColor(long nativeInstance, int argb);
    private static native int  nGetStyle(long nativeInstance);
    private static native void nSetStyle(long nativeInstance, int value);
    private static native long nGetStrokeWidth(long nativeInstance);
    private static native void nSetStrokeWidth(long nativeInstance, float value);
    private static native long nGetStrokeMiter(long nativeInstance);
    private static native void nSetStrokeMiter(long nativeInstance, float value);
    private static native int  nGetStrokeCap(long nativeInstance);
    private static native void nSetStrokeCap(long nativeInstance, int value);
    private static native int  nGetStrokeJoin(long nativeInstance);
    private static native void nSetStrokeJoin(long nativeInstance, int value);
    private static native long nGetFillPath(long nativeInstance, long path, float resScale);
    private static native long nGetFillPathCull(long nativeInstance, long path, float left, float top, float right, float bottom, float resScale);
    private static native void nSetMaskFilter(long nativeInstance, long ptr);
    private static native void nSetImageFilter(long nativeInstance, long ptr);
    private static native void nSetBlendMode(long nativeInstance, int mode);
    private static native void nSetPathEffect(long nativeInstance, long pathEffectPtr);
    private static native void nSetShader(long nativeInstance, long shaderPtr);
    private static native void nSetColorFilter(long nativeInstance, long colorFilterPtr);
}

