package org.jetbrains.skija;

public class TextStyle extends Managed {
    public TextStyle() { super(nInit(), nativeFinalizer); Native.onNativeCall(); }

    public TextStyle setColor(int color) {
        Native.onNativeCall();
        nSetColor(nativeInstance, color);
        return this;
    }

    public TextStyle setFontSize(float size) {
        Native.onNativeCall();
        nSetFontSize(nativeInstance, size);
        return this;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native void nSetColor(long ptr, int color);
    private static native void nSetFontSize(long ptr, float size);
}