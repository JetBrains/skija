package org.jetbrains.skija;

import java.lang.ref.Cleaner;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Managed extends Native implements AutoCloseable {
    protected Cleaner.Cleanable finalizer;
    public static boolean stats = false;

    protected Managed(long nativePtr, long nativeFinalizer) {
        super(nativePtr);
        String name = getClass().getSimpleName();
        if (stats)
            allocated.merge(name, 1, Integer::sum);
        finalizer = cleaner.register(this, new CleanerThunk(name, nativePtr, nativeFinalizer));
    }

    @Override
    public void close() {
        finalizer.clean();
        finalizer = null;
        nativeInstance = 0;
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
            if (stats)
                allocated.merge(name, -1, Integer::sum);
            Native.onNativeCall(); 
            applyNativeFinalizer(nativePtr, nativeFinalizer);
        }
    }

    public static native void applyNativeFinalizer(long nativePtr, long nativeFinalizer);
}