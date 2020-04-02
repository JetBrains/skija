package skija;

public class Paint extends Managed {
    public static enum Style {
        FILL,
        STROKE,
        STROKE_AND_FILL;

        // Style values = Style.values()
    }

    public static enum Cap {
        BUTT,
        ROUND,
        SQUARE
    }

    public static enum Join {
        MITER,
        ROUND,
        BEVEL
    }

    public Paint() { super(nInit(), kNativeFinalizer); }

    public boolean isAntiAlias() { return nIsAntiAlias(mNativeInstance); }
    public Paint setAntiAlias(boolean value) { nSetAntiAlias(mNativeInstance, value); return this; }

    public long getColor() { return nGetColor(mNativeInstance); }
    public Paint setColor(long color) { nSetColor(mNativeInstance, color); return this; }

    public Style getStyle() { return Style.values()[nGetStyle(mNativeInstance)]; }
    public Paint setStyle(Style style) { nSetStyle(mNativeInstance, style.ordinal()); return this; }

    public float getStrokeWidth() { return nGetStrokeWidth(mNativeInstance); }
    public Paint setStrokeWidth(float width) { nSetStrokeWidth(mNativeInstance, width); return this; }

    public long getStrokeMiter() { return nGetStrokeMiter(mNativeInstance); }
    public Paint setStrokeMiter(float limit) { nSetStrokeMiter(mNativeInstance, limit); return this; }

    public Cap getStrokeCap() { return Cap.values()[nGetStrokeJoin(mNativeInstance)]; }
    public Paint setStrokeCap(Cap cap) { nSetStrokeCap(mNativeInstance, cap.ordinal()); return this; }

    public Join getStrokeJoin() { return Join.values()[nGetStrokeJoin(mNativeInstance)]; }
    public Paint setStrokeJoin(Join join) { nSetStrokeJoin(mNativeInstance, join.ordinal()); return this; }
    
    private static long kNativeFinalizer = nGetNativeFinalizer();
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

