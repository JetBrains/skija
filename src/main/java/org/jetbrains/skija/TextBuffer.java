package org.jetbrains.skija;

public class TextBuffer extends Managed {
    protected TextBuffer(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    // xAdvance, yAdvance
    public float[] getAdvances() { return nGetAdvances(mNativeInstance); }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native float[] nGetAdvances(long nativeInstance);
}