package skija;

import java.util.Arrays;

public class HBFont extends Managed {
    protected final HBFace mFace;
    protected final FontFeature[] mFeatures;
    protected final int[] mFeaturesData;

    public static final FontFeature[] NO_FEATURES = new FontFeature[0];

    public HBFont(HBFace face, float size) {
        this(face, size, NO_FEATURES);
    }

    public HBFont(HBFace face, float size, FontFeature[] features) {
        super(nInit(face.mNativeInstance, size), kNativeFinalizer);
        mFace = face;
        mFeatures = features;
        mFeaturesData = new int[mFeatures.length * 4];
        for (int i = 0; i < mFeatures.length; ++i) {
            mFeaturesData[i * 4] = mFeatures[i].tag;
            mFeaturesData[i * 4 + 1] = mFeatures[i].value;
            mFeaturesData[i * 4 + 2] = mFeatures[i].start;
            mFeaturesData[i * 4 + 3] = mFeatures[i].end;
        }
    }

    public FontExtents getHorizontalExtents() {
        float[] res = nGetHorizontalExtents(mNativeInstance);
        return new FontExtents(res[0], res[1], res[2]);
    }

    public FontExtents getVerticalExtents() {
        float[] res = nGetVerticalExtents(mNativeInstance);
        return new FontExtents(res[0], res[1], res[2]);
    }

    public TextBuffer shape(String text) {
        return new TextBuffer(nShape(mNativeInstance, text, mFeaturesData));
    }

    public TextBuffer shape(String text, FontFeature[] features) {
        int[] data = mFeaturesData;
        if (features.length > 0) {
            data = Arrays.copyOf(mFeaturesData, mFeaturesData.length + features.length * 4);
            int baseIdx = mFeaturesData.length;
            for (int i = 0; i < features.length; ++i) {
                data[baseIdx + i * 4] = features[i].tag;
                data[baseIdx + i * 4 + 1] = features[i].value;
                data[baseIdx + i * 4 + 2] = features[i].start;
                data[baseIdx + i * 4 + 3] = features[i].end;
            }
        }
        return new TextBuffer(nShape(mNativeInstance, text, data));
    }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long facePtr, float size);
    private static native long nGetNativeFinalizer();

    private static native float[] nGetHorizontalExtents(long nativeInstance);
    private static native float[] nGetVerticalExtents(long nativeInstance);
    private static native long nShape(long nativeInstance, String text, int[] features);
}