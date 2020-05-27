package org.jetbrains.skija;

public class ParagraphStyle extends Managed {
    public ParagraphStyle() { super(nInit(), nativeFinalizer); Native.onNativeCall(); }

    public ParagraphStyle setTextStyle(TextStyle style) {
        Native.onNativeCall();
        nSetTextStyle(nativeInstance, Native.pointer(style));
        return this;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native void nSetTextStyle(long ptr, long textStylePtr);
}