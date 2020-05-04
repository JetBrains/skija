package org.jetbrains.skija;

public class MaskFilter extends RefCounted {
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

    public enum CoverageMode {
        /** A ∪ B    A+B-A*B */
        UNION,             
        /** A ∩ B    A*B */
        INTERSECT,         
        /** A - B    A*(1-B) */
        DIFFERENCE,        
        /** B - A    B*(1-A) */
        REVERSE_DIFFERENCE, 
        /** A ⊕ B    A+B-2*A*B */
        XOR
    }

    public static MaskFilter blur(BlurStyle style, float sigma) {
        return blur(style, sigma, true);
    }

    public static MaskFilter blur(BlurStyle style, float sigma, boolean respectCTM) {
        Native.onNativeCall();
        return new MaskFilter(nBlur(style.ordinal(), sigma, respectCTM));
    }

    public MaskFilter compose(MaskFilter inner) {
        Native.onNativeCall();
        return new MaskFilter(nCompose(nativeInstance, Native.pointer(inner)));
    }

    public MaskFilter combine(MaskFilter inner, CoverageMode mode) {
        Native.onNativeCall();
        return new MaskFilter(nCombine(nativeInstance, Native.pointer(inner), mode.ordinal()));
    }

    public static MaskFilter shader(Shader s) {
        Native.onNativeCall();
        return new MaskFilter(nShader(Native.pointer(s)));
    }

    public static MaskFilter table(byte[] table) {
        Native.onNativeCall();
        return new MaskFilter(nTable(table));
    }

    public static MaskFilter gamma(float gamma) {
        Native.onNativeCall();
        return new MaskFilter(nGamma(gamma));
    }

    public static MaskFilter clip(int min, int max) {
        Native.onNativeCall();
        return new MaskFilter(nClip((byte) min, (byte) max));
    }

    // public MaskFilter emboss(float blurSigma, float lightDirectionX, float lightDirectionY, float lightDirectionZ, int lightPad, int lightAmbient, int lightSpecular) {
    //     Native.onNativeCall();
    //     return new MaskFilter(nEmboss(blurSigma, lightDirectionX, lightDirectionY, lightDirectionZ, lightPad, lightAmbient, lightSpecular));
    // }

    protected MaskFilter(long nativeInstance) { super(nativeInstance); }
    private static native long nBlur(int style, float sigma, boolean respectCTM);
    private static native long nCompose(long outer, long inner);
    private static native long nCombine(long filterA, long filterB, int coverageMode);
    private static native long nShader(long shaderPtr);
    // private static native long nEmboss(float sigma, float x, float y, float z, int pad, int ambient, int specular);
    private static native long nTable(byte[] table);
    private static native long nGamma(float gamma);
    private static native long nClip(byte min, byte max);
}