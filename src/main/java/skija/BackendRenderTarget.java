package skija;

import java.lang.ref.Cleaner;

public class BackendRenderTarget {
    public static BackendRenderTarget newGL(int width, int height, int sampleCnt, int stencilBits, long fbId, long fbFormat) {
        return new BackendRenderTarget(nNewGL(width, height, sampleCnt, stencilBits, fbId, fbFormat));
    }

    BackendRenderTarget(long nativeInstance) {
        mNativeInstance = nativeInstance;
        mFinalizer = mAllocations.registerNativeAllocation(this, mNativeInstance);
    }
    
    long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    private static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());

    public long getNativeInstance() {
        return mNativeInstance;
    }

    public String toString() {
        return "[GrBackendRenderTarget 0x" + Long.toString(mNativeInstance, 16) + "]";
    }
    
    public BackendRenderTarget release() {
        mFinalizer.clean();
        mNativeInstance = 0;
        return null;
    }
    
    private static native long nGetNativeFinalizer();
    private static native long nNewGL(int width, int height, int sampleCnt, int stencilBits, long fbId, long fbFormat);
}