package skija;

import java.lang.ref.Cleaner;

public class Context {
    public static Context makeGL() {
        return new Context(nMakeGL());
    }

    public Context(long nativeInstance) {
        mNativeInstance = nativeInstance;
        mFinalizer = RefCnt.mAllocations.registerNativeAllocation(this, mNativeInstance);
    }

    private long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    
    public long getNativeInstance() {
        return mNativeInstance;
    }
    
    public void release() {
        mFinalizer.clean();
        mNativeInstance = 0;
    }

    private static native long nMakeGL();
}