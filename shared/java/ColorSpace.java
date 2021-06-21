package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class ColorSpace extends Managed {
    static { Library.staticLoad(); }
    
    public static class _SRGBHolder {
        static { Stats.onNativeCall(); }
        public static final ColorSpace INSTANCE = new ColorSpace(_nMakeSRGB(), false);
    }

    public static ColorSpace getSRGB() {
        return _SRGBHolder.INSTANCE;
    }

    public static class _SRGBLinearHolder {
        static { Stats.onNativeCall(); }
        public static final ColorSpace INSTANCE = new ColorSpace(_nMakeSRGBLinear(), false);
    }

    public static ColorSpace getSRGBLinear() {
        return _SRGBLinearHolder.INSTANCE;
    }

    public static class _DisplayP3Holder {
        static { Stats.onNativeCall(); }
        public static final ColorSpace INSTANCE = new ColorSpace(_nMakeDisplayP3(), false);
    }

    public static ColorSpace getDisplayP3() {
        return _DisplayP3Holder.INSTANCE;
    }

    public Color4f convert(ColorSpace to, Color4f color) {
        to = to == null ? getSRGB() : to;
        try {
            return new Color4f(_nConvert(_ptr, Native.getPtr(to), color.getR(), color.getG(), color.getB(), color.getA()));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(to);
        }
    }

    @ApiStatus.Internal
    public ColorSpace(long ptr) {
        super(ptr, _FinalizerHolder.PTR, true);
    }

    @ApiStatus.Internal
    public ColorSpace(long ptr, boolean managed) {
        super(ptr, _FinalizerHolder.PTR, managed);
    }

    /**
     * @return  true if the color space gamma is near enough to be approximated as sRGB
     */
    public boolean isGammaCloseToSRGB() {
        try {
            Stats.onNativeCall();
            return _nIsGammaCloseToSRGB(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  true if the color space gamma is linear
     */
    public boolean isGammaLinear() {
        try {
            Stats.onNativeCall();
            return _nIsGammaLinear(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns true if the color space is sRGB. Returns false otherwise.</p>
     *
     * <p>This allows a little bit of tolerance, given that we might see small numerical error
     * in some cases: converting ICC fixed point to float, converting white point to D50,
     * rounding decisions on transfer function and matrix.</p>
     *
     * <p>This does not consider a 2.2f exponential transfer function to be sRGB.  While these
     * functions are similar (and it is sometimes useful to consider them together), this
     * function checks for logical equality.</p>
     */
    public boolean isSRGB() {
        try {
            Stats.onNativeCall();
            return _nIsSRGB(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }
    
    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long _nGetFinalizer();
    public static native long _nMakeSRGB();
    public static native long _nMakeDisplayP3();
    public static native long _nMakeSRGBLinear();
    public static native float[] _nConvert(long fromPtr, long toPtr, float r, float g, float b, float a);
    public static native boolean _nIsGammaCloseToSRGB(long ptr);
    public static native boolean _nIsGammaLinear(long ptr);
    public static native boolean _nIsSRGB(long ptr);
}