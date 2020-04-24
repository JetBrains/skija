package org.jetbrains.skija;

public class ColorSpace extends Managed {
    public static ColorSpace SRGB = new ColorSpace(nMakeSRGB());
    public static ColorSpace SRGB_LINEAR = new ColorSpace(nMakeSRGBLinear());

    public Color4f convert(ColorSpace to, Color4f color) {
        return new Color4f(nConvert(nativeInstance, Native.pointer(to == null ? SRGB : to), color.r, color.g, color.b, color.a));
    }

    protected ColorSpace(long nativeInstance) { super(nativeInstance, nativeFinalizer); }
    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native long nMakeSRGB();
    private static native long nMakeSRGBLinear();
    private static native float[] nConvert(long fromPtr, long toPtr, float r, float g, float b, float a);
}