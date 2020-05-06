package org.jetbrains.skija;

public abstract class RefCounted extends Managed {
    protected RefCounted(long nativeInstance) {
        super(nativeInstance, nativeFinalizer);
    }

    protected RefCounted(long nativeInstance, boolean allowClose) {
        super(nativeInstance, nativeFinalizer, allowClose);
    }

    protected static long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
}