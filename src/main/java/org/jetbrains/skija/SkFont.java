package org.jetbrains.skija;

public class SkFont extends Managed {
    public final SkTypeface typeface;
    public SkFont(SkTypeface typeface, float size) {
        super(nInit(typeface.nativeInstance, size), nativeFinalizer);
        Native.onNativeCall(); 
        this.typeface = typeface;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long typefacePtr, float size);
    private static native long nGetNativeFinalizer();
}