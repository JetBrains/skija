package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class ParagraphStyle extends Managed {
    public ParagraphStyle() {
        super(_nMake(), _finalizerPtr);
        Stats.onNativeCall();
    }

    public ParagraphStyle setTextStyle(TextStyle style) {
        Stats.onNativeCall();
        _nSetTextStyle(_ptr, Native.getPtr(style));
        return this;
    }

    public static final  long _finalizerPtr = _nGetFinalizer();
    public static native long _nMake();
    public static native long _nGetFinalizer();
    public static native void _nSetTextStyle(long ptr, long textStylePtr);
}