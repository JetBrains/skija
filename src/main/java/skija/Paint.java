package skija;

import java.lang.ref.Cleaner;

public class Paint {
    public Paint() {
        mNativeInstance = nInit();
        mAllocations.registerNativeAllocation(this, mNativeInstance);
    }
    
    private long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    private static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());

    public long getNativeInstance() {
        return mNativeInstance;
    }
    
    public void release() {
        mFinalizer.clean();
        mNativeInstance = 0;
    }

    public long getColor() { return nGetColor(mNativeInstance); }
    public void setColor(long color) { nSetColor(mNativeInstance, color); }
    
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native long nGetColor(long nativeInstance);
    private static native void nSetColor(long nativeInstance, long argb);
}

