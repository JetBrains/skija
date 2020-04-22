package org.jetbrains.skija;

public class ImageFilter extends RefCounted {
    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color) {
        return dropShadow(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input) {
        return dropShadow(dx, dy, sigmaX, sigmaY, color, input, null);
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input, IRect crop) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadow(dx, dy, sigmaX, sigmaY, color, input == null ? 0 : input.nativeInstance, crop));
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color) {
        return dropShadowOnly(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input) {
        return dropShadowOnly(dx, dy, sigmaX, sigmaY, color, input, null);
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input, IRect crop) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadowOnly(dx, dy, sigmaX, sigmaY, color, input == null ? 0 : input.nativeInstance, crop));
    }

    public static ImageFilter blur(float sigmaX, float sigmaY, TileMode mode) {
        return blur(sigmaX, sigmaY, mode, null, null);
    }

    public static ImageFilter blur(float sigmaX, float sigmaY, TileMode mode, ImageFilter input) {
        return blur(sigmaX, sigmaY, mode, input, null);
    }

    public static ImageFilter blur(float sigmaX, float sigmaY, TileMode mode, ImageFilter input, IRect crop) {
        Native.onNativeCall(); 
        return new ImageFilter(nBlur(sigmaX, sigmaY, mode.ordinal(), input == null ? 0 : input.nativeInstance, crop));
    }

    protected ImageFilter(long nativeInstance) { super(nativeInstance); }
    private static native long nDropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, long input, IRect crop);
    private static native long nDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, long input, IRect crop);
    private static native long nBlur(float sigmaX, float sigmaY, int tileMode, long input, IRect crop);
}