package org.jetbrains.skija;

import java.util.Arrays;

public class ImageFilter extends RefCnt {
    public enum FilterQuality {
        /** fastest but lowest quality, typically nearest-neighbor */
        NONE,
        /** typically bilerp */
        LOW,
        /** typically bilerp + mipmaps for down-scaling */
        MEDIUM,
        /** slowest but highest quality, typically bicubic or bett */
        HIGH
    }

    public enum ColorChannel { R, G, B, A }

    public static ImageFilter alphaThreshold(Region r, float innerMin, float outerMax, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nAlphaThreshold(Native.getPtr(r), innerMin, outerMax, Native.getPtr(input), crop));
    }

    public static ImageFilter arithmetic(float k1, float k2, float k3, float k4, boolean enforcePMColor, ImageFilter bg, ImageFilter fg, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nArithmetic(k1, k2, k3, k4, enforcePMColor, Native.getPtr(bg), Native.getPtr(fg), crop));
    }

    public static ImageFilter blur(float sigmaX, float sigmaY, TileMode mode) {
        return blur(sigmaX, sigmaY, mode, null, null);
    }

    public static ImageFilter blur(float sigmaX, float sigmaY, TileMode mode, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nBlur(sigmaX, sigmaY, mode.ordinal(), Native.getPtr(input), crop));
    }

    public static ImageFilter colorFilter(ColorFilter f, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nColorFilter(Native.getPtr(f), Native.getPtr(input), crop));
    }

    public static ImageFilter compose(ImageFilter outer, ImageFilter inner) {
        Stats.onNativeCall();
        return new ImageFilter(nCompose(Native.getPtr(outer), Native.getPtr(inner)));
    }

    public static ImageFilter displacementMap(ColorChannel x, ColorChannel y, float scale, ImageFilter displacement, ImageFilter color, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDisplacementMap(x.ordinal(), y.ordinal(), scale, Native.getPtr(displacement), Native.getPtr(color), crop));
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, int color) {
        return dropShadow(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter dropShadow(float dx, float dy, float sigmaX, float sigmaY, int color, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDropShadow(dx, dy, sigmaX, sigmaY, color, Native.getPtr(input), crop));
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color) {
        return dropShadowOnly(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter dropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDropShadowOnly(dx, dy, sigmaX, sigmaY, color, Native.getPtr(input), crop));
    }

    public static ImageFilter image(Image image) {
        Rect r = Rect.makeWH(image.getWidth(), image.getHeight());
        return ImageFilter.image(image, r, r, FilterQuality.HIGH);
    }

    public static ImageFilter image(Image image, Rect src, Rect dst, FilterQuality q) {
        Stats.onNativeCall();
        return new ImageFilter(nImage(Native.getPtr(image), src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, q.ordinal()));
    }

    public static ImageFilter magnifier(Rect r, float inset, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nMagnifier(r.left, r.top, r.right, r.bottom, inset, Native.getPtr(input), crop));
    }

    public static ImageFilter matrixConvolution(int kernelW, int kernelH, float[] kernel, float gain, float bias, int offsetX, int offsetY, TileMode tileMode, boolean convolveAlpha, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nMatrixConvolution(kernelW, kernelH, kernel, gain, bias, offsetX, offsetY, tileMode.ordinal(), convolveAlpha, Native.getPtr(input), crop));
    }

    public static ImageFilter matrixTransform(float[] matrix, FilterQuality q, ImageFilter input) {
        Stats.onNativeCall();
        return new ImageFilter(nMatrixTransform(matrix, q.ordinal(), Native.getPtr(input)));
    }

    public static ImageFilter merge(ImageFilter[] filters, IRect crop) {
        Stats.onNativeCall();
        long[] filterPtrs = new long[filters.length];
        Arrays.setAll(filterPtrs, i -> Native.getPtr(filters[i]));
        return new ImageFilter(nMerge(filterPtrs, crop));
    }

    public static ImageFilter offset(float dx, float dy, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nOffset(dx, dy, Native.getPtr(input), crop));
    }

    public static ImageFilter paint(Paint paint, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nPaint(Native.getPtr(paint), crop));
    }

    // public static ImageFilter picture(Picture picture, Rect target) {
    //     Native.onNativeCall();
    //     return new ImageFilter(nPicture(Native.pointer(picture), target.left, target.top, target.right, target.bottom));
    // }

    public static ImageFilter tile(Rect src, Rect dst, ImageFilter input) {
        Stats.onNativeCall();
        return new ImageFilter(nTile(src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, Native.getPtr(input)));
    }

    public static ImageFilter xfermode(BlendMode blendMode, ImageFilter bg, ImageFilter fg, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nXfermode(blendMode.ordinal(), Native.getPtr(bg), Native.getPtr(fg), crop));
    }

    public static ImageFilter dilate(float rx, float ry, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDilate(rx, ry, Native.getPtr(input), crop));
    }

    public static ImageFilter erode(float rx, float ry, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nErode(rx, ry, Native.getPtr(input), crop));
    }

    public static ImageFilter distantLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDistantLitDiffuse(x, y, z, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter pointLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nPointLitDiffuse(x, y, z, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter spotLitDiffuse(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nSpotLitDiffuse(x0, y0, z0, x1, y1, z1, falloffExponent, cutoffAngle, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter distantLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nDistantLitSpecular(x, y, z, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    public static ImageFilter pointLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nPointLitSpecular(x, y, z, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    public static ImageFilter spotLitSpecular(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(nSpotLitSpecular(x0, y0, z0, x1, y1, z1, falloffExponent, cutoffAngle, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    protected ImageFilter(long nativeInstance) { super(nativeInstance); }
    private static native long nAlphaThreshold(long regionPtr, float innerMin, float outerMax, long input, IRect crop);
    private static native long nArithmetic(float k1, float k2, float k3, float k4, boolean enforcePMColor, long bg, long fg, IRect crop);
    private static native long nBlur(float sigmaX, float sigmaY, int tileMode, long input, IRect crop);
    private static native long nColorFilter(long colorFilterPtr, long input, IRect crop);
    private static native long nCompose(long outer, long inner);
    private static native long nDisplacementMap(int xChan, int yChan, float scale, long displacement, long color, IRect crop);
    private static native long nDropShadow(float dx, float dy, float sigmaX, float sigmaY, int color, long input, IRect crop);
    private static native long nDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color, long input, IRect crop);
    private static native long nImage(long image, float l0, float t0, float r0, float b0, float l1, float t1, float r1, float b1, int filterQuality);
    private static native long nMagnifier(float l, float t, float r, float b, float inset, long input, IRect crop);
    private static native long nMatrixConvolution(int kernelW, int kernelH, float[] kernel, float gain, float bias, int offsetX, int offsetY, int tileMode, boolean convolveAlpha, long input, IRect crop);
    private static native long nMatrixTransform(float[] matrix, int filterQuality, long input);
    private static native long nMerge(long[] filters, IRect crop);
    private static native long nOffset(float dx, float dy, long input, IRect crop);
    private static native long nPaint(long paint, IRect crop);
    private static native long nPicture(long picture, float l, float t, float r, float b);
    private static native long nTile(float l0, float t0, float r0, float b0, float l1, float t1, float r1, float b1, long input);
    private static native long nXfermode(int blendMode, long bg, long fg, IRect crop);
    private static native long nDilate(float rx, float ry, long input, IRect crop);
    private static native long nErode(float rx, float ry, long input, IRect crop);
    private static native long nDistantLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    private static native long nPointLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    private static native long nSpotLitDiffuse(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    private static native long nDistantLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
    private static native long nPointLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
    private static native long nSpotLitSpecular(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
}