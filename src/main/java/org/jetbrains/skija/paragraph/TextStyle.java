package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.Managed;
import org.jetbrains.skija.Stats;

public class TextStyle extends Managed {
    public TextStyle() { super(nInit(), nativeFinalizer); Stats.onNativeCall(); }

    public TextStyle setColor(int color) {
        Stats.onNativeCall();
        nSetColor(_ptr, color);
        return this;
    }

    public TextStyle setFontSize(float size) {
        Stats.onNativeCall();
        nSetFontSize(_ptr, size);
        return this;
    }

    public TextStyle setFontFamilies(String[] families) {
        Stats.onNativeCall();
        nSetFontFamilies(_ptr, families);
        return this;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native void nSetColor(long ptr, int color);
    private static native void nSetFontSize(long ptr, float size);
    private static native void nSetFontFamilies(long ptr, String[] families);
}