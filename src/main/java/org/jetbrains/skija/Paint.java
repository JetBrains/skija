package org.jetbrains.skija;

public class Paint extends Managed {
    public enum Style { FILL, STROKE, STROKE_AND_FILL }
    public enum Cap { BUTT, ROUND, SQUARE }
    public enum Join { MITER, ROUND, BEVEL }

    public Paint() { super(nInit(), nativeFinalizer); Native.onNativeCall(); }

    public boolean isAntiAlias() { Native.onNativeCall(); return nIsAntiAlias(nativeInstance); }
    public Paint setAntiAlias(boolean value) { Native.onNativeCall(); nSetAntiAlias(nativeInstance, value); return this; }

    public long getColor() { Native.onNativeCall(); return nGetColor(nativeInstance); }
    public Paint setColor(long color) { Native.onNativeCall(); nSetColor(nativeInstance, color); return this; }

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

    protected ImageFilter imageFilter;
    public ImageFilter getImageFilter() { return imageFilter; }
    public Paint setImageFilter(ImageFilter f) { this.imageFilter = f; Native.onNativeCall(); nSetImageFilter(nativeInstance, f == null ? 0 : f.nativeInstance); return this; }

    protected BlendMode blendMode = BlendMode.SRC_OVER;
    public BlendMode getBlendMode() { return blendMode; }
    public Paint setBlendMode(BlendMode mode) { this.blendMode = mode; Native.onNativeCall(); nSetBlendMode(nativeInstance, mode.ordinal()); return this; }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native boolean nIsAntiAlias(long nativeInstance);
    private static native void nSetAntiAlias(long nativeInstance, boolean value);
    private static native long nGetColor(long nativeInstance);
    private static native void nSetColor(long nativeInstance, long argb);
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
    private static native void nSetImageFilter(long nativeInstance, long ptr);
    private static native void nSetBlendMode(long nativeInstance, int mode);
}

