package org.jetbrains.skija;

public class ImageFilter extends RefCounted {
    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadow(dx, dy, sigmaX, sigmaY, color, 0));
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadow(dx, dy, sigmaX, sigmaY, color, input.nativeInstance));
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input, IRect crop) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadowCrop(dx, dy, sigmaX, sigmaY, color, input.nativeInstance, crop.left, crop.top, crop.right, crop.bottom));
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadowOnly(dx, dy, sigmaX, sigmaY, color, 0));
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadowOnly(dx, dy, sigmaX, sigmaY, color, input.nativeInstance));
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, ImageFilter input, IRect crop) {
        Native.onNativeCall(); 
        return new ImageFilter(nDropShadowOnlyCrop(dx, dy, sigmaX, sigmaY, color, input.nativeInstance, crop.left, crop.top, crop.right, crop.bottom));
    }

    protected ImageFilter(long nativeInstance) { super(nativeInstance); }
    private static native long nDropShadow(float dx, float dy, float sigmaX, float sigmaY, long color, long input);
    private static native long nDropShadowCrop(float dx, float dy, float sigmaX, float sigmaY, long color, long input, int cropL, int cropT, int cropR, int cropB);
    private static native long nDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, long color, long input);
    private static native long nDropShadowOnlyCrop(float dx, float dy, float sigmaX, float sigmaY, long color, long input, int cropL, int cropT, int cropR, int cropB);
}