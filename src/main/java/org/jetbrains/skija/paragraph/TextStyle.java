package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Stats;

public class TextStyle extends Managed {
    public TextStyle() {
        super(_nMake(), _finalizerPtr);
        Stats.onNativeCall();
    }

    public TextStyle setColor(int color) {
        Stats.onNativeCall();
        _nSetColor(_ptr, color);
        return this;
    }

    public TextStyle setFontSize(float size) {
        Stats.onNativeCall();
        _nSetFontSize(_ptr, size);
        return this;
    }

    public TextStyle setFontFamilies(String[] families) {
        Stats.onNativeCall();
        _nSetFontFamilies(_ptr, families);
        return this;
    }

    public static final  long _finalizerPtr = _nGetFinalizer();
    public static native long _nMake();
    public static native long _nGetFinalizer();
    public static native void _nSetColor(long ptr, int color);
    public static native void _nSetFontSize(long ptr, float size);
    public static native void _nSetFontFamilies(long ptr, String[] families);
}