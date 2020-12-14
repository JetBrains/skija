package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class MaskFilter extends RefCnt {
    static { Library.staticLoad(); }
    
    public static MaskFilter makeBlur(FilterBlurMode mode, float sigma) {
        return makeBlur(mode, sigma, true);
    }

    public static MaskFilter makeBlur(FilterBlurMode mode, float sigma, boolean respectCTM) {
        Stats.onNativeCall();
        return new MaskFilter(_nMakeBlur(mode.ordinal(), sigma, respectCTM));
    }

    public static MaskFilter makeShader(Shader s) {
        try {
            Stats.onNativeCall();
            return new MaskFilter(_nMakeShader(Native.getPtr(s)));
        } finally {
            Reference.reachabilityFence(s);
        }
    }

    public static MaskFilter makeTable(byte[] table) {
        Stats.onNativeCall();
        return new MaskFilter(_nMakeTable(table));
    }

    public static MaskFilter makeGamma(float gamma) {
        Stats.onNativeCall();
        return new MaskFilter(_nMakeGamma(gamma));
    }

    public static MaskFilter makeClip(int min, int max) {
        Stats.onNativeCall();
        return new MaskFilter(_nMakeClip((byte) min, (byte) max));
    }

    // public MaskFilter makeEmboss(float blurSigma, float lightDirectionX, float lightDirectionY, float lightDirectionZ, int lightPad, int lightAmbient, int lightSpecular) {
    //     Native.onNativeCall();
    //     return new MaskFilter(_nMakeEmboss(blurSigma, lightDirectionX, lightDirectionY, lightDirectionZ, lightPad, lightAmbient, lightSpecular));
    // }

    @ApiStatus.Internal
    public MaskFilter(long ptr) {
        super(ptr);
    }
    
    public static native long _nMakeBlur(int mode, float sigma, boolean respectCTM);
    public static native long _nMakeShader(long shaderPtr);
    // public static native long _nMakeEmboss(float sigma, float x, float y, float z, int pad, int ambient, int specular);
    public static native long _nMakeTable(byte[] table);
    public static native long _nMakeGamma(float gamma);
    public static native long _nMakeClip(byte min, byte max);
}