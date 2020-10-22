package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class ColorSpace extends Managed {
    static { Library.load(); }
    
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
        return new Color4f(_nConvert(_ptr, Native.getPtr(to == null ? getSRGB() : to), color.getR(), color.getG(), color.getB(), color.getA()));
    }

    @ApiStatus.Internal
    public ColorSpace(long ptr) {
        super(ptr, _finalizerPtr, true);
    }

    @ApiStatus.Internal
    public ColorSpace(long ptr, boolean managed) {
        super(ptr, _finalizerPtr, managed);
    }

    /**
     * @return  true if the color space gamma is near enough to be approximated as sRGB
     */
    public boolean isGammaCloseToSRGB() {
        Stats.onNativeCall();
        return _nIsGammaCloseToSRGB(_ptr);
    }

    /**
     * @return  true if the color space gamma is linear
     */
    public boolean isGammaLinear() {
        Stats.onNativeCall();
        return _nIsGammaLinear(_ptr);
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
        Stats.onNativeCall();
        return _nIsSRGB(_ptr);
    }
    
    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native long _nMakeSRGB();
    public static native long _nMakeDisplayP3();
    public static native long _nMakeSRGBLinear();
    public static native float[] _nConvert(long fromPtr, long toPtr, float r, float g, float b, float a);
    public static native boolean _nIsGammaCloseToSRGB(long ptr);
    public static native boolean _nIsGammaLinear(long ptr);
    public static native boolean _nIsSRGB(long ptr);
}