package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.FontManager;
import org.jetbrains.skija.Native;
import org.jetbrains.skija.Stats;
import org.jetbrains.skija.Typeface;

public class TypefaceFontProvider extends FontManager {
    public TypefaceFontProvider() {
        super(nInit());
        Stats.onNativeCall();
    }

    public TypefaceFontProvider registerTypeface(Typeface typeface) {
        return registerTypeface(typeface, null);
    }

    public TypefaceFontProvider registerTypeface(Typeface typeface, String alias) {
        Stats.onNativeCall();
        nRegisterTypeface(_ptr, Native.getPtr(typeface), alias);
        return this;
    }

    private static native long nInit();
    private static native long nRegisterTypeface(long ptr, long typefacePtr, String alias);
}