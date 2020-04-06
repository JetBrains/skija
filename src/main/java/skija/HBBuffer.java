package skija;

public class HBBuffer extends Managed {
    protected HBBuffer(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    // xAdvance, yAdvance
    public float[] getAdvances() { return nGetAdvances(mNativeInstance); }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native float[] nGetAdvances(long nativeInstance);
}