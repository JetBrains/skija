package org.jetbrains.skija;

import org.jetbrains.skija.impl.Internal;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class ColorSpace extends Managed {
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

    @Internal
    public ColorSpace(long ptr) {
        super(ptr, _finalizerPtr);
    }

    @Internal
    public ColorSpace(long ptr, boolean allowClose) {
        super(ptr, _finalizerPtr, allowClose);
    }
    
    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native long _nMakeSRGB();
    public static native long _nMakeDisplayP3();
    public static native long _nMakeSRGBLinear();
    public static native float[] _nConvert(long fromPtr, long toPtr, float r, float g, float b, float a);
}