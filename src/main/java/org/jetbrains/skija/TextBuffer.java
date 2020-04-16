package org.jetbrains.skija;

public class TextBuffer extends Managed {
    private float[] advances;
    protected TextBuffer(long nativeInstance) { super(nativeInstance, nativeFinalizer); }

    // xAdvance, yAdvance
    public float[] getAdvances() {
        if (advances == null)
            advances = nGetAdvances(nativeInstance);
        return advances;
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native float[] nGetAdvances(long nativeInstance);
}