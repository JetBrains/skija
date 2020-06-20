package org.jetbrains.skija;

import lombok.Getter;
import lombok.With;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class Shader extends RefCnt {

    public static final class GradientOptions {
        public static final int _INTERPOLATE_PREMUL = 1;
        public static GradientOptions DEFAULT = new GradientOptions(TileMode.CLAMP, GradientOptions._INTERPOLATE_PREMUL, null);

        @Getter @With
        public final TileMode _tileMode;
        public final int      _flags;
        @Getter @With
        public final Matrix33 _localMatrix;

        @ApiStatus.Internal
        public GradientOptions(TileMode t, int f, Matrix33 m) {
            _tileMode = t;
            _flags = f;
            _localMatrix = m;
        }

        public boolean isPremul() {
            return (_flags & _INTERPOLATE_PREMUL) != 0;
        }

        public GradientOptions withPremul(boolean premul) {
            return new GradientOptions(_tileMode, premul ? _flags | _INTERPOLATE_PREMUL : _flags & ~_INTERPOLATE_PREMUL, _localMatrix);
        }
    }

    public Shader makeWithColorFilter(ColorFilter f) {
        return new Shader(_nMakeWithColorFilter(_ptr, Native.getPtr(f)));
    }

    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors) {
        return makeLinearGradient(x0, y0, x1, y1, colors, null, GradientOptions.DEFAULT);
    }
    
    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions) {
        return makeLinearGradient(x0, y0, x1, y1, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeLinearGradient(x0, y0, x1, y1, colors, positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeLinearGradient(float x0, float y0, float x1, float y1, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeLinearGradientCS(x0, y0, x1, y1, Color4f.flattenArray(colors), Native.getPtr(cs), positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors) {
        return makeRadialGradient(x, y, r, colors, null, GradientOptions.DEFAULT);
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors, float[] positions) {
        return makeRadialGradient(x, y, r, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader makeRadialGradient(float x, float y, float r, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeRadialGradient(x, y, r, colors, positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeRadialGradient(float x, float y, float r, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeRadialGradientCS(x, y, r, Color4f.flattenArray(colors), Native.getPtr(cs), positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors) {
        return makeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, null, GradientOptions.DEFAULT);
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions) {
        return makeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeTwoPointConicalGradientCS(x0, y0, r0, x1, y1, r1, Color4f.flattenArray(colors), Native.getPtr(cs), positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeSweepGradient(float x, float y, int[] colors) {
        return makeSweepGradient(x, y, 0, 360, colors, null, GradientOptions.DEFAULT);
    }
    
    public static Shader makeSweepGradient(float x, float y, int[] colors, float[] positions) {
        return makeSweepGradient(x, y, 0, 360, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader makeSweepGradient(float x, float y, int[] colors, float[] positions, GradientOptions opts) {
        return makeSweepGradient(x, y, 0, 360, colors, positions, opts);
    }

    public static Shader makeSweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeSweepGradient(x, y, startAngle, endAngle, colors, positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeSweepGradient(float x, float y, float startAngle, float endAngle, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Stats.onNativeCall();
        return new Shader(_nMakeSweepGradientCS(x, y, startAngle, endAngle, Color4f.flattenArray(colors), Native.getPtr(cs), positions, opts._tileMode.ordinal(), opts._flags, opts._localMatrix == null ? null : opts._localMatrix.getMat()));
    }

    public static Shader makeEmpty() {
        Stats.onNativeCall();
        return new Shader(_nMakeEmpty());
    }

    public static Shader makeColor(int color) {
        Stats.onNativeCall();
        return new Shader(_nMakeColor(color));
    }

    public static Shader makeColor(Color4f color, ColorSpace space) {
        Stats.onNativeCall();
        return new Shader(_nMakeColorCS(color.getR(), color.getG(), color.getB(), color.getA(), Native.getPtr(space)));
    }

    public static Shader makeBlend(BlendMode mode, Shader dst, Shader src) {
        Stats.onNativeCall();
        return new Shader(_nMakeBlend(mode.ordinal(), Native.getPtr(dst), Native.getPtr(src)));
    }

    public static Shader makeLerp(float t, Shader dst, Shader src) {
        Stats.onNativeCall();
        return new Shader(_nMakeLerp(t, Native.getPtr(dst), Native.getPtr(src)));
    }

    @ApiStatus.Internal
    public Shader(long ptr) {
        super(ptr);
    }

    public static native long _nMakeWithColorFilter(long ptr, long colorFilterPtr);
    public static native long _nMakeLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeLinearGradientCS(float x0, float y0, float x1, float y1, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeRadialGradient(float x, float y, float r, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeRadialGradientCS(float x, float y, float r, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeTwoPointConicalGradientCS(float x0, float y0, float r0, float x1, float y1, float r1, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeSweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeSweepGradientCS(float x, float y, float startAngle, float endAngle, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    public static native long _nMakeEmpty();
    public static native long _nMakeColor(int color);
    public static native long _nMakeColorCS(float r, float g, float b, float a, long colorSpacePtr);
    public static native long _nMakeBlend(int blendMode, long dst, long src);
    public static native long _nMakeLerp(float t, long dst, long src);
}