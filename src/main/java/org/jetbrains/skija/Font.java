package org.jetbrains.skija;

import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class Font extends Managed {
    public Font(Typeface typeface, float size) {
        super(_nMake(Native.getPtr(typeface), size), _finalizerPtr);
        Stats.onNativeCall();
    }

    public TextBlob shape(String str, float width) {
        return new TextBlob(_nShape(_ptr, str, width));
    }

    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nMake(long typefacePtr, float size);
    public static native long _nGetFinalizer();
    public static native long _nShape(long ptr, String str, float width);
}