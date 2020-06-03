package org.jetbrains.skija;

public class TypefaceFontProvider extends FontManager { 
    public TypefaceFontProvider() {
        super(nInit());
        Native.onNativeCall();
    }

    public TypefaceFontProvider registerTypeface(SkTypeface typeface) {
        return registerTypeface(typeface, null);
    }

    public TypefaceFontProvider registerTypeface(SkTypeface typeface, String alias) {
        Native.onNativeCall();
        nRegisterTypeface(nativeInstance, Native.pointer(typeface), alias);
        return this;
    }

    private static native long nInit();
    private static native long nRegisterTypeface(long ptr, long typefacePtr, String alias);
}