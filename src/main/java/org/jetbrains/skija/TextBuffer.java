package org.jetbrains.skija;

public class TextBuffer extends Managed {
    private float advances[];
    protected TextBuffer(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    // xAdvance, yAdvance
    public float[] getAdvances() {
        if (advances == null)
            advances = nGetAdvances(mNativeInstance);
        return advances;
    }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native float[] nGetAdvances(long nativeInstance);
}