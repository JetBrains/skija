package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class ColorFilter extends RefCnt {
    static { Library.staticLoad(); }
    
    public static ColorFilter makeComposed(ColorFilter outer, ColorFilter inner) {
        try {
            Stats.onNativeCall();
            return new ColorFilter(_nMakeComposed(Native.getPtr(outer), Native.getPtr(inner)));
        } finally {
            Reference.reachabilityFence(outer);
            Reference.reachabilityFence(inner);
        }
    }

    public static ColorFilter makeBlend(int color, BlendMode mode) {
        Stats.onNativeCall();
        return new ColorFilter(_nMakeBlend(color, mode.ordinal()));
    }

    public static ColorFilter makeMatrix(ColorMatrix matrix) {
        Stats.onNativeCall();
        return new ColorFilter(_nMakeMatrix(matrix.getMat()));
    }

    public static ColorFilter makeHSLAMatrix(ColorMatrix matrix) {
        Stats.onNativeCall();
        return new ColorFilter(_nMakeHSLAMatrix(matrix.getMat()));
    }

    public static class _LinearToSRGBGammaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(_nGetLinearToSRGBGamma(), false);
    }

    public static ColorFilter getLinearToSRGBGamma() {
        return _LinearToSRGBGammaHolder.INSTANCE;
    }

    public static class _SRGBToLinearGammaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(_nGetSRGBToLinearGamma(), false);
    }

    public static ColorFilter getSRGBToLinearGamma() {
        return _SRGBToLinearGammaHolder.INSTANCE;
    }

    public static ColorFilter makeLerp(ColorFilter dst, ColorFilter src, float t) {
        try {
            return new ColorFilter(_nMakeLerp(t, Native.getPtr(dst), Native.getPtr(src)));
        } finally {
            Reference.reachabilityFence(dst);
            Reference.reachabilityFence(src);
        }
    }

    public static ColorFilter makeLighting(int colorMul, int colorAdd) {
        return new ColorFilter(_nMakeLighting(colorMul, colorAdd));
    }

    public static ColorFilter makeHighContrast(boolean grayscale, InversionMode mode, float contrast) {
        return new ColorFilter(_nMakeHighContrast(grayscale, mode.ordinal(), contrast));
    }

    public static ColorFilter makeTable(byte[] table) {
        assert table.length == 256 : "Expected 256 elements, got " + table.length;
        return new ColorFilter(_nMakeTable(table));
    }

    public static ColorFilter makeTableARGB(byte[] a, byte[] r, byte[] g, byte[] b) {
        assert a == null || a.length == 256 : "Expected 256 elements in a[], got " + a.length;
        assert r == null || r.length == 256 : "Expected 256 elements in r[], got " + r.length;
        assert g == null || g.length == 256 : "Expected 256 elements in g[], got " + g.length;
        assert b == null || b.length == 256 : "Expected 256 elements in b[], got " + b.length; 
        return new ColorFilter(_nMakeTableARGB(a, r, g, b));
    }

    public static ColorFilter makeOverdraw(int[] colors) {
        assert colors.length == 6 : "Expected 6 elements, got " + colors.length;
        return new ColorFilter(_nMakeOverdraw(colors[0], colors[1], colors[2], colors[3], colors[4], colors[5]));
    }

    public static class _LumaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(_nGetLuma(), false);
    }

    public static ColorFilter getLuma() {
        return _LumaHolder.INSTANCE;
    }

    @ApiStatus.Internal
    public ColorFilter(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal
    public ColorFilter(long ptr, boolean allowClose) {
        super(ptr, allowClose);
    }
    
    public static native long _nMakeComposed(long outer, long inner);
    public static native long _nMakeBlend(int color, int blendMode);
    public static native long _nMakeMatrix(float[] rowMajor);
    public static native long _nMakeHSLAMatrix(float[] rowMajor);
    public static native long _nGetLinearToSRGBGamma();
    public static native long _nGetSRGBToLinearGamma();
    public static native long _nMakeLerp(float t, long dstPtr, long srcPtr);
    public static native long _nMakeLighting(int colorMul, int colorAdd);
    public static native long _nMakeHighContrast(boolean grayscale, int inversionMode, float contrast);
    public static native long _nMakeTable(byte[] table);
    public static native long _nMakeTableARGB(byte[] a, byte[] r, byte[] g, byte[] b);
    public static native long _nMakeOverdraw(int c0, int c1, int c2, int c3, int c4, int c5);
    public static native long _nGetLuma();
}