package skija;

import java.lang.ref.Cleaner;

public abstract class Managed extends Native {
    protected Cleaner.Cleanable mFinalizer;

    protected Managed(long nativeInstance, long nativeFinalizer) {
        super(nativeInstance);
        mFinalizer = cleaner.register(this, new CleanerThunk(nativeInstance, nativeFinalizer));
    }

    public void release() {
        mFinalizer.clean();
        mFinalizer = null;
        mNativeInstance = 0;
    }

    private static Cleaner cleaner = Cleaner.create();
        
    private class CleanerThunk implements Runnable {
        private long nativePtr;
        private long nativeFinalizer;
        
        public CleanerThunk(long nativePtr, long nativeFinalizer) {
            this.nativePtr = nativePtr;
            this.nativeFinalizer = nativeFinalizer;
        }
        
        public void run() {
            applyNativeFinalizer(nativePtr, nativeFinalizer);
        }
    }
    
    public static native void applyNativeFinalizer(long nativePtr, long nativeFinalizer);
}