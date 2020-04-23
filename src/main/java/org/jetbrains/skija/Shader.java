package org.jetbrains.skija;

public class Shader extends RefCounted {
    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors) {
        return makeLinearGradient(x0, y0, x1, y1, colors, null, TileMode.CLAMP);
    }
    
    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions) {
        return makeLinearGradient(x0, y0, x1, y1, colors, positions, TileMode.CLAMP);
    }

    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeLinearGradient(x0, y0, x1, y1, colors, positions, tileMode.ordinal()));
    }

    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, Color4f[] colors, ColorSpace cs, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeLinearGradientCS(x0, y0, x1, y1, Color4f.flattenArray(colors), Native.pointer(cs), positions, tileMode.ordinal()));
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors) {
        return makeRadialGradient(x, y, r, colors, null, TileMode.CLAMP);
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors, float[] positions) {
        return makeRadialGradient(x, y, r, colors, positions, TileMode.CLAMP);
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeRadialGradient(x, y, r, colors, positions, tileMode.ordinal()));
    }

    public static Shader makeRadialGradient(float x, float y, float r, Color4f[] colors, ColorSpace cs, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeRadialGradientCS(x, y, r, Color4f.flattenArray(colors), Native.pointer(cs), positions, tileMode.ordinal()));
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors) {
        return makeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, null, TileMode.CLAMP);
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions) {
        return makeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, TileMode.CLAMP);
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, tileMode.ordinal()));
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, Color4f[] colors, ColorSpace cs, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeTwoPointConicalGradientCS(x0, y0, r0, x1, y1, r1, Color4f.flattenArray(colors), Native.pointer(cs), positions, tileMode.ordinal()));
    }

    public static Shader makeSweepGradient(float x, float y, int[] colors) {
        return makeSweepGradient(x, y, 0, 360, colors, null, TileMode.CLAMP);
    }
    
    public static Shader makeSweepGradient(float x, float y, int[] colors, float[] positions) {
        return makeSweepGradient(x, y, 0, 360, colors, positions, TileMode.CLAMP);
    }

    public static Shader makeSweepGradient(float x, float y, int[] colors, float[] positions, TileMode tileMode) {
        return makeSweepGradient(x, y, 0, 360, colors, positions, tileMode);
    }

    public static Shader makeSweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeSweepGradient(x, y, startAngle, endAngle, colors, positions, tileMode.ordinal()));
    }

    public static Shader makeSweepGradient(float x, float y, float startAngle, float endAngle, Color4f[] colors, ColorSpace cs, float[] positions, TileMode tileMode) {
        Native.onNativeCall();
        return new Shader(nMakeSweepGradientCS(x, y, startAngle, endAngle, Color4f.flattenArray(colors), Native.pointer(cs), positions, tileMode.ordinal()));
    }

    public static Shader makeEmpty() { Native.onNativeCall(); return new Shader(nMakeEmpty()); }
    public static Shader makeColor(int color) { Native.onNativeCall(); return new Shader(nMakeColor(color)); }

    public static Shader makeColor(Color4f color, ColorSpace space) {
        Native.onNativeCall();
        return new Shader(nMakeColorCS(color.r, color.g, color.b, color.a, Native.pointer(space)));
    }

    public static Shader makeBlend(BlendMode mode, Shader dst, Shader src) {
        Native.onNativeCall();
        return new Shader(nMakeBlend(mode.ordinal(), Native.pointer(dst), Native.pointer(src)));
    }

    public static Shader makeLerp(float t, Shader dst, Shader src) {
        Native.onNativeCall();
        return new Shader(nMakeLerp(t, Native.pointer(dst), Native.pointer(src)));
    }

    protected Shader(long nativeInstance) { super(nativeInstance); }
    private static native long nMakeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, int tileMode);
    private static native long nMakeLinearGradientCS(float x0, float y0, float x1, float y1, float[] colors, long colorSpacePtr, float[] positions, int tileMode);
    private static native long nMakeRadialGradient(float x, float y, float r, int[] colors, float[] positions, int tileMode);
    private static native long nMakeRadialGradientCS(float x, float y, float r, float[] colors, long colorSpacePtr, float[] positions, int tileMode);
    private static native long nMakeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, int tileMode);
    private static native long nMakeTwoPointConicalGradientCS(float x0, float y0, float r0, float x1, float y1, float r1, float[] colors, long colorSpacePtr, float[] positions, int tileMode);
    private static native long nMakeSweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, int tileMode);
    private static native long nMakeSweepGradientCS(float x, float y, float startAngle, float endAngle, float[] colors, long colorSpacePtr, float[] positions, int tileMode);

    private static native long nMakeEmpty();
    private static native long nMakeColor(int color);
    private static native long nMakeColorCS(float r, float g, float b, float a, long colorSpacePtr);
    private static native long nMakeBlend(int blendMode, long dst, long src);
    private static native long nMakeLerp(float t, long dst, long src);
}