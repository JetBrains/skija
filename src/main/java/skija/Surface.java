package skija;

import java.lang.ref.Cleaner;

public class Surface {
    public Context mContext;
    public BackendRenderTarget mRenderTarget;

    public static Surface makeFromBackendRenderTarget(Context context, BackendRenderTarget rt, SurfaceOrigin origin, ColorType colorType) {
        long nativeInstance = nMakeFromBackendRenderTarget(context.mNativeInstance, rt.mNativeInstance, origin.ordinal(), colorType.ordinal());
        return new Surface(nativeInstance, context, rt);
    }

    public Canvas getCanvas() {
        return new Canvas(nGetCanvas(mNativeInstance), this);
    }

    Surface(long nativeInstance, Context context, BackendRenderTarget rt) {
        mNativeInstance = nativeInstance;
        mFinalizer = RefCnt.mAllocations.registerNativeAllocation(this, mNativeInstance);
        mContext = context;
        mRenderTarget = rt;
    }

    private long mNativeInstance;
    private Cleaner.Cleanable mFinalizer;
    
    public long getNativeInstance() {
        return mNativeInstance;
    }

    public String toString() {
        return "[GrSurface 0x" + Long.toString(mNativeInstance, 16) + "]";
    }
    
    public Surface release() {
        mFinalizer.clean();
        mNativeInstance = 0;
        mContext = null;
        mRenderTarget = null;
        return null;
    }

    private static native long nMakeFromBackendRenderTarget(long pContext, long pBackendRenderTarget, int surfaceOrigin, int colorType);
    private static native long nGetCanvas(long nativeInstance);
}