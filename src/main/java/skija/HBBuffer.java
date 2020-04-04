package skija;

public class HBBuffer extends Managed {
    protected HBBuffer(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    public int[] getBounds() { return nGetBounds(mNativeInstance); }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();

    private static native int[] nGetBounds(long nativeInstance);
}