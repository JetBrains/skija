package org.jetbrains.skija;

public class ColorFilter extends RefCnt {
    public MaskFilter compose(MaskFilter inner) {
        Stats.onNativeCall();
        return new MaskFilter(nCompose(_ptr, Native.getPtr(inner)));
    }

    public ColorFilter composed(ColorFilter inner) {
        Stats.onNativeCall();
        return new ColorFilter(nCompose(_ptr, Native.getPtr(inner)));
    }

    public static ColorFilter compose(ColorFilter outer, ColorFilter inner) {
        Stats.onNativeCall();
        return new ColorFilter(nCompose(Native.getPtr(outer), Native.getPtr(inner)));
    }

    public static ColorFilter blend(int color, BlendMode mode) {
        Stats.onNativeCall();
        return new ColorFilter(nBlend(color, mode.ordinal()));
    }

    public static ColorFilter matrix(float[] rowMajor) {
        assert rowMajor.length == 20 : "Expected 20 elements, got " + rowMajor.length;
        Stats.onNativeCall();
        return new ColorFilter(nMatrix(rowMajor));
    }

    public static ColorFilter hslaMatrix(float[] rowMajor) {
        assert rowMajor.length == 20 : "Expected 20 elements, got " + rowMajor.length;
        Stats.onNativeCall();
        return new ColorFilter(nHSLAMatrix(rowMajor));
    }

    private static class LinearToSRGBGammaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(nLinearToSRGBGamma(), false);
    }

    public static ColorFilter getLinearToSRGBGamma() { return LinearToSRGBGammaHolder.INSTANCE; }

    private static class SRGBToLinearGammaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(nSRGBToLinearGamma(), false);
    }

    public static ColorFilter getSRGBToLinearGamma() { return SRGBToLinearGammaHolder.INSTANCE; }

    public static ColorFilter lerp(float t, ColorFilter dst, ColorFilter src) {
        return new ColorFilter(nLerp(t, Native.getPtr(dst), Native.getPtr(src)));
    }

    public static ColorFilter lighting(int colorMul, int colorAdd) {
        return new ColorFilter(nLighting(colorMul, colorAdd));
    }

    public enum InvertStyle { NO, BRIGHTNESS, LIGHTNESS }

    public static ColorFilter highContrast(boolean grayscale, InvertStyle invertStyle, float contrast) {
        return new ColorFilter(nHighContrast(grayscale, invertStyle.ordinal(), contrast));
    }

    public static ColorFilter table(byte[] table) {
        assert table.length == 256 : "Expected 256 elements, got " + table.length;
        return new ColorFilter(nTable(table));
    }

    public static ColorFilter tableARGB(byte[] a, byte[] r, byte[] g, byte[] b) {
        assert a == null || a.length == 256 : "Expected 256 elements in a[], got " + a.length;
        assert r == null || r.length == 256 : "Expected 256 elements in r[], got " + r.length;
        assert g == null || g.length == 256 : "Expected 256 elements in g[], got " + g.length;
        assert b == null || b.length == 256 : "Expected 256 elements in b[], got " + b.length; 
        return new ColorFilter(nTableARGB(a, r, g, b));
    }

    public static ColorFilter overdraw(int[] colors) {
        assert colors.length == 6 : "Expected 6 elements, got " + colors.length;
        return new ColorFilter(nOverdraw(colors[0], colors[1], colors[2], colors[3], colors[4], colors[5]));
    }

    private static class LumaHolder {
        static { Stats.onNativeCall(); }
        public static final ColorFilter INSTANCE = new ColorFilter(nLuma(), false);
    }

    public static ColorFilter getLuma() { return LumaHolder.INSTANCE; }

    protected ColorFilter(long nativeInstance) { super(nativeInstance); }
    protected ColorFilter(long nativeInstance, boolean allowClose) { super(nativeInstance, allowClose); }
    private static native long nCompose(long outer, long inner);
    private static native long nBlend(int color, int blendMode);
    private static native long nMatrix(float[] rowMajor);
    private static native long nHSLAMatrix(float[] rowMajor);
    private static native long nLinearToSRGBGamma();
    private static native long nSRGBToLinearGamma();
    private static native long nLerp(float t, long dstPtr, long srcPtr);
    private static native long nLighting(int colorMul, int colorAdd);
    private static native long nHighContrast(boolean grayscale, int invertStyle, float contrast);
    private static native long nTable(byte[] table);
    private static native long nTableARGB(byte[] a, byte[] r, byte[] g, byte[] b);
    private static native long nOverdraw(int c0, int c1, int c2, int c3, int c4, int c5);
    private static native long nLuma();
}