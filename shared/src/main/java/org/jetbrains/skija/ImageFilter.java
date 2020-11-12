package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

import java.util.Arrays;

public class ImageFilter extends RefCnt {
    static { Library.staticLoad(); }
    
    public static ImageFilter makeAlphaThreshold(Region r, float innerMin, float outerMax, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeAlphaThreshold(Native.getPtr(r), innerMin, outerMax, Native.getPtr(input), crop));
    }

    public static ImageFilter makeArithmetic(float k1, float k2, float k3, float k4, boolean enforcePMColor, ImageFilter bg, ImageFilter fg, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeArithmetic(k1, k2, k3, k4, enforcePMColor, Native.getPtr(bg), Native.getPtr(fg), crop));
    }

    public static ImageFilter makeBlur(float sigmaX, float sigmaY, FilterTileMode mode) {
        return makeBlur(sigmaX, sigmaY, mode, null, null);
    }

    public static ImageFilter makeBlur(float sigmaX, float sigmaY, FilterTileMode mode, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeBlur(sigmaX, sigmaY, mode.ordinal(), Native.getPtr(input), crop));
    }

    public static ImageFilter makeColorFilter(ColorFilter f, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeColorFilter(Native.getPtr(f), Native.getPtr(input), crop));
    }

    public static ImageFilter makeCompose(ImageFilter outer, ImageFilter inner) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeCompose(Native.getPtr(outer), Native.getPtr(inner)));
    }

    public static ImageFilter makeDisplacementMap(ColorChannel x, ColorChannel y, float scale, ImageFilter displacement, ImageFilter color, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDisplacementMap(x.ordinal(), y.ordinal(), scale, Native.getPtr(displacement), Native.getPtr(color), crop));
    }

    public static ImageFilter makeDropShadow(float dx, float dy, float sigmaX, float sigmaY, int color) {
        return makeDropShadow(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter makeDropShadow(float dx, float dy, float sigmaX, float sigmaY, int color, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDropShadow(dx, dy, sigmaX, sigmaY, color, Native.getPtr(input), crop));
    }

    public static ImageFilter makeDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color) {
        return makeDropShadowOnly(dx, dy, sigmaX, sigmaY, color, null, null);
    }

    public static ImageFilter makeDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDropShadowOnly(dx, dy, sigmaX, sigmaY, color, Native.getPtr(input), crop));
    }

    public static ImageFilter makeImage(Image image) {
        Rect r = Rect.makeWH(image.getWidth(), image.getHeight());
        return makeImage(image, r, r, FilterQuality.HIGH);
    }

    public static ImageFilter makeImage(Image image, Rect src, Rect dst, FilterQuality q) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeImage(Native.getPtr(image), src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, q.ordinal()));
    }

    public static ImageFilter makeMagnifier(Rect r, float inset, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeMagnifier(r._left, r._top, r._right, r._bottom, inset, Native.getPtr(input), crop));
    }

    public static ImageFilter makeMatrixConvolution(int kernelW, int kernelH, float[] kernel, float gain, float bias, int offsetX, int offsetY, FilterTileMode tileMode, boolean convolveAlpha, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeMatrixConvolution(kernelW, kernelH, kernel, gain, bias, offsetX, offsetY, tileMode.ordinal(), convolveAlpha, Native.getPtr(input), crop));
    }

    public static ImageFilter makeMatrixTransform(Matrix33 matrix, FilterQuality q, ImageFilter input) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeMatrixTransform(matrix.getMat(), q.ordinal(), Native.getPtr(input)));
    }

    public static ImageFilter makeMerge(ImageFilter[] filters, IRect crop) {
        Stats.onNativeCall();
        long[] filterPtrs = new long[filters.length];
        Arrays.setAll(filterPtrs, i -> Native.getPtr(filters[i]));
        return new ImageFilter(_nMakeMerge(filterPtrs, crop));
    }

    public static ImageFilter makeOffset(float dx, float dy, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeOffset(dx, dy, Native.getPtr(input), crop));
    }

    public static ImageFilter makePaint(Paint paint, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakePaint(Native.getPtr(paint), crop));
    }

    // public static ImageFilter makePicture(Picture picture, Rect target) {
    //     Native.onNativeCall();
    //     return new ImageFilter(_nMakePicture(Native.pointer(picture), target.left, target.top, target.right, target.bottom));
    // }

    public static ImageFilter makeTile(Rect src, Rect dst, ImageFilter input) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeTile(src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(input)));
    }

    public static ImageFilter makeXfermode(BlendMode blendMode, ImageFilter bg, ImageFilter fg, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeXfermode(blendMode.ordinal(), Native.getPtr(bg), Native.getPtr(fg), crop));
    }

    public static ImageFilter makeDilate(float rx, float ry, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDilate(rx, ry, Native.getPtr(input), crop));
    }

    public static ImageFilter makeErode(float rx, float ry, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeErode(rx, ry, Native.getPtr(input), crop));
    }

    public static ImageFilter makeDistantLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDistantLitDiffuse(x, y, z, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter makePointLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakePointLitDiffuse(x, y, z, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter makeSpotLitDiffuse(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float kd, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeSpotLitDiffuse(x0, y0, z0, x1, y1, z1, falloffExponent, cutoffAngle, lightColor, surfaceScale, kd, Native.getPtr(input), crop));
    }

    public static ImageFilter makeDistantLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeDistantLitSpecular(x, y, z, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    public static ImageFilter makePointLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakePointLitSpecular(x, y, z, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    public static ImageFilter makeSpotLitSpecular(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float ks, float shininess, ImageFilter input, IRect crop) {
        Stats.onNativeCall();
        return new ImageFilter(_nMakeSpotLitSpecular(x0, y0, z0, x1, y1, z1, falloffExponent, cutoffAngle, lightColor, surfaceScale, ks, shininess, Native.getPtr(input), crop));
    }

    @ApiStatus.Internal
    public ImageFilter(long ptr) {
        super(ptr);
    }
    
    public static native long _nMakeAlphaThreshold(long regionPtr, float innerMin, float outerMax, long input, IRect crop);
    public static native long _nMakeArithmetic(float k1, float k2, float k3, float k4, boolean enforcePMColor, long bg, long fg, IRect crop);
    public static native long _nMakeBlur(float sigmaX, float sigmaY, int tileMode, long input, IRect crop);
    public static native long _nMakeColorFilter(long colorFilterPtr, long input, IRect crop);
    public static native long _nMakeCompose(long outer, long inner);
    public static native long _nMakeDisplacementMap(int xChan, int yChan, float scale, long displacement, long color, IRect crop);
    public static native long _nMakeDropShadow(float dx, float dy, float sigmaX, float sigmaY, int color, long input, IRect crop);
    public static native long _nMakeDropShadowOnly(float dx, float dy, float sigmaX, float sigmaY, int color, long input, IRect crop);
    public static native long _nMakeImage(long image, float l0, float t0, float r0, float b0, float l1, float t1, float r1, float b1, int filterQuality);
    public static native long _nMakeMagnifier(float l, float t, float r, float b, float inset, long input, IRect crop);
    public static native long _nMakeMatrixConvolution(int kernelW, int kernelH, float[] kernel, float gain, float bias, int offsetX, int offsetY, int tileMode, boolean convolveAlpha, long input, IRect crop);
    public static native long _nMakeMatrixTransform(float[] matrix, int filterQuality, long input);
    public static native long _nMakeMerge(long[] filters, IRect crop);
    public static native long _nMakeOffset(float dx, float dy, long input, IRect crop);
    public static native long _nMakePaint(long paint, IRect crop);
    public static native long _nMakePicture(long picture, float l, float t, float r, float b);
    public static native long _nMakeTile(float l0, float t0, float r0, float b0, float l1, float t1, float r1, float b1, long input);
    public static native long _nMakeXfermode(int blendMode, long bg, long fg, IRect crop);
    public static native long _nMakeDilate(float rx, float ry, long input, IRect crop);
    public static native long _nMakeErode(float rx, float ry, long input, IRect crop);
    public static native long _nMakeDistantLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    public static native long _nMakePointLitDiffuse(float x, float y, float z, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    public static native long _nMakeSpotLitDiffuse(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float kd, long input, IRect crop);
    public static native long _nMakeDistantLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
    public static native long _nMakePointLitSpecular(float x, float y, float z, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
    public static native long _nMakeSpotLitSpecular(float x0, float y0, float z0, float x1, float y1, float z1, float falloffExponent, float cutoffAngle, int lightColor, float surfaceScale, float ks, float shininess, long input, IRect crop);
}