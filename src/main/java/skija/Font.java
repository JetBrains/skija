package skija;

public class Font extends Managed {
    private Typeface typeface;
    public Font(Typeface typeface, float size) {
        super(nInit(typeface.mNativeInstance, size), kNativeFinalizer);
        this.typeface = typeface;
    }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long typefacePtr, float size);
    private static native long nGetNativeFinalizer();
}