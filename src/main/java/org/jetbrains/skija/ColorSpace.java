package org.jetbrains.skija;

public class ColorSpace extends Managed {
    private static class SRGBHolder {
        public static final ColorSpace INSTANCE = new ColorSpace(nMakeSRGB());
    }

    public static ColorSpace getSRGB() { return SRGBHolder.INSTANCE; }

    private static class SRGBLinearHolder {
        public static final ColorSpace INSTANCE = new ColorSpace(nMakeSRGBLinear());
    }

    public static ColorSpace getSRGBLinear() { return SRGBLinearHolder.INSTANCE; }

    private static class DisplayP3Holder {
        public static final ColorSpace INSTANCE = new ColorSpace(nMakeDisplayP3());
    }

    public static ColorSpace getDisplayP3() { return DisplayP3Holder.INSTANCE; }


    public Color4f convert(ColorSpace to, Color4f color) {
        return new Color4f(nConvert(nativeInstance, Native.pointer(to == null ? getSRGB() : to), color.r, color.g, color.b, color.a));
    }

    protected ColorSpace(long nativeInstance) { super(nativeInstance, nativeFinalizer); }
    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nGetNativeFinalizer();
    private static native long nMakeSRGB();
    private static native long nMakeDisplayP3();
    private static native long nMakeSRGBLinear();
    private static native float[] nConvert(long fromPtr, long toPtr, float r, float g, float b, float a);
}