package skija;

public class HBFont extends Managed {
    protected HBFace mFace;

    public HBFont(HBFace face, float size, String options) {
        super(nInit(face.mNativeInstance, size, options), kNativeFinalizer);
        mFace = face;
    }

    public HBExtents getHorizontalExtents() {
        float[] res = nGetHorizontalExtents(mNativeInstance);
        return new HBExtents(res[0], res[1], res[2]);
    }

    public HBExtents getVerticalExtents() {
        float[] res = nGetVerticalExtents(mNativeInstance);
        return new HBExtents(res[0], res[1], res[2]);
    }

    public HBBuffer shape(String text) {
        return new HBBuffer(nShape(mNativeInstance, text));
    }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long facePtr, float size, String options);
    private static native long nGetNativeFinalizer();

    private static native float[] nGetHorizontalExtents(long nativeInstance);
    private static native float[] nGetVerticalExtents(long nativeInstance);
    private static native long nShape(long nativeInstance, String text);
}