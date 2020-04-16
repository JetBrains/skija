package org.jetbrains.skija;

import java.lang.ref.Cleaner;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Managed extends Native implements AutoCloseable {
    protected Cleaner.Cleanable mFinalizer;
    public static boolean allocationStats = false;

    protected Managed(long nativeInstance, long nativeFinalizer) {
        super(nativeInstance);
        String name = getClass().getSimpleName();
        if (allocationStats)
            allocated.merge(name, 1, Integer::sum);
        mFinalizer = cleaner.register(this, new CleanerThunk(name, nativeInstance, nativeFinalizer));
    }

    public void release() {
        mFinalizer.clean();
        mFinalizer = null;
        mNativeInstance = 0;
    }

    @Override
    public void close() {
        release();
    }

    public static Map<String, Integer> allocated = new ConcurrentHashMap<>();
    private static Cleaner cleaner = Cleaner.create();

    private static class CleanerThunk implements Runnable {
        private String name;
        private long nativePtr;
        private long nativeFinalizer;

        public CleanerThunk(String name, long nativePtr, long nativeFinalizer) {
            this.name = name;
            this.nativePtr = nativePtr;
            this.nativeFinalizer = nativeFinalizer;
        }

        public void run() {
            if (allocationStats)
                allocated.merge(name, -1, Integer::sum);
            applyNativeFinalizer(nativePtr, nativeFinalizer);
        }
    }

    public static native void applyNativeFinalizer(long nativePtr, long nativeFinalizer);
}