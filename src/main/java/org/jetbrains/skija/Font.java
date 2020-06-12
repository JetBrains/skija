package org.jetbrains.skija;

public class Font extends Managed {
    public final Typeface typeface;
    public Font(Typeface typeface, float size) {
        super(nInit(typeface._ptr, size), nativeFinalizer);
        Stats.onNativeCall();
        this.typeface = typeface;
    }

    public TextBlob shape(String str, float width) {
        return new TextBlob(nShape(_ptr, str, width));
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long typefacePtr, float size);
    private static native long nGetNativeFinalizer();
    private static native long nShape(long ptr, String str, float width);
}