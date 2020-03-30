package skija;

import java.lang.ref.Cleaner;

public class Paint {
    public Paint() {
        mNativeInstance = nInit();
        mFinalizer = mAllocations.registerNativeAllocation(this, mNativeInstance);
    }
    
    long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    private static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());

    public long getNativeInstance() {
        return mNativeInstance;
    }

    public String toString() {
        return "[SkPaint 0x" + Long.toString(mNativeInstance, 16) + "]";
    }
    
    public Paint release() {
        mFinalizer.clean();
        mNativeInstance = 0;
        return null;
    }

    public long getColor() { return nGetColor(mNativeInstance); }
    public void setColor(long color) { nSetColor(mNativeInstance, color); }
    
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native long nGetColor(long nativeInstance);
    private static native void nSetColor(long nativeInstance, long argb);
}

