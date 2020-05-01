package org.jetbrains.skija;

public class Paint extends Managed {
    public enum Style { FILL, STROKE, STROKE_AND_FILL }
    public enum Cap { BUTT, ROUND, SQUARE }
    public enum Join { MITER, ROUND, BEVEL }

    public Paint() { super(nInit(), nativeFinalizer); Native.onNativeCall(); }

    public boolean isAntiAlias() { Native.onNativeCall(); return nIsAntiAlias(nativeInstance); }
    public Paint setAntiAlias(boolean value) { Native.onNativeCall(); nSetAntiAlias(nativeInstance, value); return this; }

    public int getColor() { Native.onNativeCall(); return nGetColor(nativeInstance); }
    public Paint setColor(int color) { Native.onNativeCall(); nSetColor(nativeInstance, color); return this; }

    public Style getStyle() { Native.onNativeCall(); return Style.values()[nGetStyle(nativeInstance)]; }
    public Paint setStyle(Style style) { Native.onNativeCall(); nSetStyle(nativeInstance, style.ordinal()); return this; }

    public float getStrokeWidth() { Native.onNativeCall(); return nGetStrokeWidth(nativeInstance); }
    public Paint setStrokeWidth(float width) { Native.onNativeCall(); nSetStrokeWidth(nativeInstance, width); return this; }

    public long getStrokeMiter() { Native.onNativeCall(); return nGetStrokeMiter(nativeInstance); }
    public Paint setStrokeMiter(float limit) { Native.onNativeCall(); nSetStrokeMiter(nativeInstance, limit); return this; }

    public Cap getStrokeCap() { Native.onNativeCall(); return Cap.values()[nGetStrokeJoin(nativeInstance)]; }
    public Paint setStrokeCap(Cap cap) { Native.onNativeCall(); nSetStrokeCap(nativeInstance, cap.ordinal()); return this; }

    public Join getStrokeJoin() { Native.onNativeCall(); return Join.values()[nGetStrokeJoin(nativeInstance)]; }
    public Paint setStrokeJoin(Join join) { Native.onNativeCall(); nSetStrokeJoin(nativeInstance, join.ordinal()); return this; }

    public Path getFillPath(Path src) { return getFillPath(src, null, 1); }
    
    public Path getFillPath(Path src, Rect cull, float resScale) {
        Native.onNativeCall();
        if (cull == null)
            return new Path(nGetFillPath(nativeInstance, Native.pointer(src), resScale));
        else
            return new Path(nGetFillPathCull(nativeInstance, Native.pointer(src), cull.left, cull.top, cull.right, cull.bottom, resScale));
    }

    protected ImageFilter imageFilter;
    public ImageFilter getImageFilter() { return imageFilter; }
    public Paint setImageFilter(ImageFilter f) { this.imageFilter = f; Native.onNativeCall(); nSetImageFilter(nativeInstance, Native.pointer(f)); return this; }

    protected BlendMode blendMode = BlendMode.SRC_OVER;
    public BlendMode getBlendMode() { return blendMode; }
    public Paint setBlendMode(BlendMode mode) { this.blendMode = mode; Native.onNativeCall(); nSetBlendMode(nativeInstance, mode.ordinal()); return this; }

    protected PathEffect pathEffect;
    public PathEffect getPathEffect() { return pathEffect; }
    public Paint setPathEffect(PathEffect p) { this.pathEffect = p; Native.onNativeCall(); nSetPathEffect(nativeInstance, Native.pointer(p)); return this; }

    protected Shader shader;
    public Shader getShader() { return shader; }
    public Paint setShader(Shader shader) { this.shader = shader; Native.onNativeCall(); nSetShader(nativeInstance, Native.pointer(shader)); return this; }

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
    private static native void nSetImageFilter(long nativeInstance, long ptr);
    private static native void nSetBlendMode(long nativeInstance, int mode);
    private static native void nSetPathEffect(long nativeInstance, long pathEffectPtr);
    private static native void nSetShader(long nativeInstance, long shaderPtr);
}

