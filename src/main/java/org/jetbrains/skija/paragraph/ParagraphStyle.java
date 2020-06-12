package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.Managed;
import org.jetbrains.skija.Native;
import org.jetbrains.skija.Stats;

public class ParagraphStyle extends Managed {
    public ParagraphStyle() { super(nInit(), nativeFinalizer); Stats.onNativeCall(); }

    public ParagraphStyle setTextStyle(TextStyle style) {
        Stats.onNativeCall();
        nSetTextStyle(_ptr, Native.getPtr(style));
        return this;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native void nSetTextStyle(long ptr, long textStylePtr);
}