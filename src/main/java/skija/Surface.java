package skija;

import java.lang.ref.Cleaner;

public class Surface {
    public static enum Origin {
        TOP_LEFT,
        BOTTOM_LEFT
    }

    public static enum ColorType {
        UNKNOWN,      //!< uninitialized
        ALPHA_8,      //!< pixel with alpha in 8-bit byte
        RGB_565,      //!< pixel with 5 bits red, 6 bits green, 5 bits blue, in 16-bit word
        ARGB_4444,    //!< pixel with 4 bits for alpha, red, green, blue; in 16-bit word
        RGBA_8888,    //!< pixel with 8 bits for red, green, blue, alpha; in 32-bit word
        RGB_888x,     //!< pixel with 8 bits each for red, green, blue; in 32-bit word
        BGRA_8888,    //!< pixel with 8 bits for blue, green, red, alpha; in 32-bit word
        RGBA_1010102, //!< 10 bits for red, green, blue; 2 bits for alpha; in 32-bit word
        RGB_101010x,  //!< pixel with 10 bits each for red, green, blue; in 32-bit word
        GRAY_8,       //!< pixel with grayscale level in 8-bit byte
        RGBA_F16_NORM, //!< pixel with half floats in [0,1] for red, green, blue, alpha; in 64-bit word
        RGBA_F16,     //!< pixel with half floats for red, green, blue, alpha; in 64-bit word
        RGBA_F32,     //!< pixel using C float for red, green, blue, alpha; in 128-bit word

        // The following 6 colortypes are just for reading from - not for rendering to
        R8G8_UNORM,   //<! pixel with a uint8_t for red and green

        A16_FLOAT,    //<! pixel with a half float for alpha
        R16G16_FLOAT, //<! pixel with a half float for red and green

        A16_UNORM,    //<! pixel with a little endian uint16_t for alpha
        R16G16_UNORM, //<! pixel with a little endian uint16_t for red and green
        R16G16B16A16_UNORM//<! pixel with a little endian uint16_t for red, green, blue, and alpha
    }

    public Context mContext;
    public BackendRenderTarget mRenderTarget;

    public static Surface makeFromBackendRenderTarget(Context context, BackendRenderTarget rt, Origin origin, ColorType colorType) {
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