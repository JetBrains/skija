package org.jetbrains.skija;

public abstract class RefCounted extends Managed {

    protected RefCounted(long nativeInstance) {
        super(nativeInstance, kNativeFinalizer);
    }

    protected static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
}