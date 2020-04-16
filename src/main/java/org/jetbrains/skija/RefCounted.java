package org.jetbrains.skija;

public abstract class RefCounted extends Managed {
    protected RefCounted(long nativeInstance) {
        super(nativeInstance, nativeFinalizer);
    }

    protected static long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
}