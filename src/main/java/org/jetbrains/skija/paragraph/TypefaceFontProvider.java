package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.FontMgr;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;
import org.jetbrains.skija.Typeface;

public class TypefaceFontProvider extends FontMgr {
    public TypefaceFontProvider() {
        super(_nMake());
        Stats.onNativeCall();
    }

    public TypefaceFontProvider registerTypeface(Typeface typeface) {
        return registerTypeface(typeface, null);
    }

    public TypefaceFontProvider registerTypeface(Typeface typeface, String alias) {
        Stats.onNativeCall();
        _nRegisterTypeface(_ptr, Native.getPtr(typeface), alias);
        return this;
    }

    public static native long _nMake();
    public static native long _nRegisterTypeface(long ptr, long typefacePtr, String alias);
}