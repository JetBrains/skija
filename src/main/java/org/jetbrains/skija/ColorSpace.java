package org.jetbrains.skija;

public class ColorSpace extends Managed {
    public static ColorSpace SRGB = new ColorSpace(nMakeSRGB());
    public static ColorSpace SRGB_LINEAR = new ColorSpace(nMakeSRGBLinear());

    protected ColorSpace(long nativeInstance) { super(nativeInstance, nativeFinalizer); }
    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native long nMakeSRGB();
    private static native long nMakeSRGBLinear();
}