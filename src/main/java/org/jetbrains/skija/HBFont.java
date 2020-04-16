package org.jetbrains.skija;

import java.util.Arrays;

public class HBFont extends Managed {
    public final HBFace face;
    public final FontFeature[] features;
    public final FontVariation[] variations;
    protected final int[] featuresData;
    
    public HBFont(HBFace face, float size) {
        this(face, size, FontFeature.EMPTY, FontVariation.EMPTY);
    }

    public HBFont(HBFace face, float size, FontFeature... features) {
        this(face, size, features, FontVariation.EMPTY);
    }

    public HBFont(HBFace face, float size, FontVariation... variations) {
        this(face, size, FontFeature.EMPTY, variations);
    }

    public HBFont(HBFace face, float size, FontFeature[] features, FontVariation[] variations) {
        super(nInit(face.nativeInstance, size, variations), nativeFinalizer);
        this.face = face;
        this.features = features;
        featuresData = new int[this.features.length * 4];
        for (int i = 0; i < this.features.length; ++i) {
            featuresData[i * 4] = this.features[i].tag;
            featuresData[i * 4 + 1] = this.features[i].value;
            featuresData[i * 4 + 2] = this.features[i].start;
            featuresData[i * 4 + 3] = this.features[i].end;
        }
        this.variations = variations;
    }

    public FontExtents getHorizontalExtents() {
        float[] res = nGetHorizontalExtents(nativeInstance);
        return new FontExtents(res[0], res[1], res[2]);
    }

    public FontExtents getVerticalExtents() {
        float[] res = nGetVerticalExtents(nativeInstance);
        return new FontExtents(res[0], res[1], res[2]);
    }

    public TextBuffer shape(String text) {
        return new TextBuffer(nShape(nativeInstance, text, featuresData));
    }

    public TextBuffer shape(String text, FontFeature[] features) {
        int[] data = featuresData;
        if (features.length > 0) {
            data = Arrays.copyOf(featuresData, featuresData.length + features.length * 4);
            int baseIdx = featuresData.length;
            for (int i = 0; i < features.length; ++i) {
                data[baseIdx + i * 4] = features[i].tag;
                data[baseIdx + i * 4 + 1] = features[i].value;
                data[baseIdx + i * 4 + 2] = features[i].start;
                data[baseIdx + i * 4 + 3] = features[i].end;
            }
        }
        return new TextBuffer(nShape(nativeInstance, text, data));
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long facePtr, float size, FontVariation[] variations);
    private static native long nGetNativeFinalizer();

    private static native float[] nGetHorizontalExtents(long nativeInstance);
    private static native float[] nGetVerticalExtents(long nativeInstance);
    private static native long nShape(long nativeInstance, String text, int[] features);
}