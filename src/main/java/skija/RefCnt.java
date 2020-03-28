package skija;

public class RefCnt {
    protected static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());
    private static native long nGetNativeFinalizer();
}