package org.jetbrains.skija;

public class TextBlob extends Managed {
    protected TextBlob(long nativeInstance) {
        super(nativeInstance, nativeFinalizer);
    }

    public Rect getBounds() {
        float[] bounds = nBounds(_ptr);
        return Rect.makeLTRB(bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nMake();
    private static native long nGetNativeFinalizer();
    private static native float[] nBounds(long ptr);
}