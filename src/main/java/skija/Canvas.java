package skija;

import java.lang.ref.Cleaner;

public class Canvas {
    public Canvas() {
        mNativeInstance = nInit();
        mFinalizer = mAllocations.registerNativeAllocation(this, mNativeInstance);
    }
    
    public void drawRect(float left, float top, float right, float bottom, Paint paint) {
        nDrawRect(mNativeInstance, left, top, right, bottom, paint.getNativeInstance());
    }

    private long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    private static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());
    
    public long getNativeCanvasWrapper() {
        return mNativeInstance;
    }
    
    public void release() {
        mFinalizer.clean();
        mNativeInstance = 0;
    }

    private static native long nInit();
    private static native long nGetNativeFinalizer();
    
    private static native void nDrawRect(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
}
