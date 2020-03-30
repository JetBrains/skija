package skija;

import java.lang.ref.Cleaner;

public class Context {
    public static Context makeGL() {
        return new Context(nMakeGL());
    }

    public void flush() {
        nFlush(mNativeInstance);
    }

    public Context(long nativeInstance) {
        mNativeInstance = nativeInstance;
        mFinalizer = RefCnt.mAllocations.registerNativeAllocation(this, mNativeInstance);
    }

    long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    
    public long getNativeInstance() {
        return mNativeInstance;
    }
    
    public Context release() {
        mFinalizer.clean();
        mNativeInstance = 0;
        return null;
    }

    public String toString() {
        return "[GrContext 0x" + Long.toString(mNativeInstance, 16) + "]";
    }

    private static native long nMakeGL();
    private static native long nFlush(long nativeInstance);
}