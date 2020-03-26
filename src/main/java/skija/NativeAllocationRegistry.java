package skija;

import java.lang.ref.Cleaner;

public class NativeAllocationRegistry {
    static {
        JNI.loadLibrary("/", "skija");
    }

    private long freeFunction;
    private Cleaner cleaner = Cleaner.create();
    
    NativeAllocationRegistry(long freeFunction) {
        this.freeFunction = freeFunction;
    }
    
    public Cleaner.Cleanable registerNativeAllocation(Object referent, long nativePtr) {
        CleanerThunk thunk;
        return cleaner.register(referent, new CleanerThunk(nativePtr));
    }
    
    private class CleanerThunk implements Runnable {
        private long nativePtr;
        
        public CleanerThunk(long nativePtr) {
            this.nativePtr = nativePtr;
        }
        
        public void run() {
            applyFreeFunction(freeFunction, nativePtr);
        }
    }
    
    public static native void applyFreeFunction(long freeFunction, long nativePtr);
}
