package org.jetbrains.skija;

public class Paint extends Managed {
    public enum Style { FILL, STROKE, STROKE_AND_FILL }
    public enum Cap { BUTT, ROUND, SQUARE }
    public enum Join { MITER, ROUND, BEVEL }

    public Paint() { super(nInit(), nativeFinalizer); }

    public boolean isAntiAlias() { return nIsAntiAlias(nativeInstance); }
    public Paint setAntiAlias(boolean value) { nSetAntiAlias(nativeInstance, value); return this; }

    public long getColor() { return nGetColor(nativeInstance); }
    public Paint setColor(long color) { nSetColor(nativeInstance, color); return this; }

    public Style getStyle() { return Style.values()[nGetStyle(nativeInstance)]; }
    public Paint setStyle(Style style) { nSetStyle(nativeInstance, style.ordinal()); return this; }

    public float getStrokeWidth() { return nGetStrokeWidth(nativeInstance); }
    public Paint setStrokeWidth(float width) { nSetStrokeWidth(nativeInstance, width); return this; }

    public long getStrokeMiter() { return nGetStrokeMiter(nativeInstance); }
    public Paint setStrokeMiter(float limit) { nSetStrokeMiter(nativeInstance, limit); return this; }

    public Cap getStrokeCap() { return Cap.values()[nGetStrokeJoin(nativeInstance)]; }
    public Paint setStrokeCap(Cap cap) { nSetStrokeCap(nativeInstance, cap.ordinal()); return this; }

    public Join getStrokeJoin() { return Join.values()[nGetStrokeJoin(nativeInstance)]; }
    public Paint setStrokeJoin(Join join) { nSetStrokeJoin(nativeInstance, join.ordinal()); return this; }

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
}

