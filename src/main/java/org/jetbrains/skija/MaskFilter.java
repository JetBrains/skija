package org.jetbrains.skija;

public class MaskFilter extends RefCnt {
    public enum BlurStyle {
        /** fuzzy inside and outside */
        NORMAL,
        /** solid inside, fuzzy outside */
        SOLID,
        /** nothing inside, fuzzy outside */
        OUTER,
        /** fuzzy inside, nothing outside */
        INNER
    }

    public static MaskFilter blur(BlurStyle style, float sigma) {
        return blur(style, sigma, true);
    }

    public static MaskFilter blur(BlurStyle style, float sigma, boolean respectCTM) {
        Stats.onNativeCall();
        return new MaskFilter(nBlur(style.ordinal(), sigma, respectCTM));
    }

    public static MaskFilter shader(Shader s) {
        Stats.onNativeCall();
        return new MaskFilter(nShader(Native.getPtr(s)));
    }

    public static MaskFilter table(byte[] table) {
        Stats.onNativeCall();
        return new MaskFilter(nTable(table));
    }

    public static MaskFilter gamma(float gamma) {
        Stats.onNativeCall();
        return new MaskFilter(nGamma(gamma));
    }

    public static MaskFilter clip(int min, int max) {
        Stats.onNativeCall();
        return new MaskFilter(nClip((byte) min, (byte) max));
    }

    // public MaskFilter emboss(float blurSigma, float lightDirectionX, float lightDirectionY, float lightDirectionZ, int lightPad, int lightAmbient, int lightSpecular) {
    //     Native.onNativeCall();
    //     return new MaskFilter(nEmboss(blurSigma, lightDirectionX, lightDirectionY, lightDirectionZ, lightPad, lightAmbient, lightSpecular));
    // }

    protected MaskFilter(long nativeInstance) { super(nativeInstance); }
    private static native long nBlur(int style, float sigma, boolean respectCTM);
    private static native long nShader(long shaderPtr);
    // private static native long nEmboss(float sigma, float x, float y, float z, int pad, int ambient, int specular);
    private static native long nTable(byte[] table);
    private static native long nGamma(float gamma);
    private static native long nClip(byte min, byte max);
}