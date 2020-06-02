package org.jetbrains.skija;

public class Shader extends RefCounted {
    public static final class GradientOptions {
        public static final int INTERPOLATE_PREMUL = 1;
        public static GradientOptions DEFAULT = new GradientOptions(TileMode.CLAMP, GradientOptions.INTERPOLATE_PREMUL, null);
        
        public TileMode tileMode;
        public int flags;
        public float[] localMatrix;

        private GradientOptions(TileMode t, int f, float[] m) {
            tileMode = t;
            flags = f;
            localMatrix = m;
        }

        public GradientOptions with(TileMode t) {
            return new GradientOptions(t, flags, localMatrix);
        }

        public GradientOptions premul() {
            return new GradientOptions(tileMode, flags | INTERPOLATE_PREMUL, localMatrix);
        }

        public GradientOptions unpremul() {
            return new GradientOptions(tileMode, flags & ~INTERPOLATE_PREMUL, localMatrix);
        }

        public GradientOptions with(float[] m) {
            return new GradientOptions(tileMode, flags, m);
        }
    }

    public Shader withColorFilter(ColorFilter f) {
        return new Shader(nWithColorFilter(nativeInstance, Native.pointer(f)));
    }

    public static Shader linearGradient(float x0, float y0, float x1, float y1, int[] colors) {
        return linearGradient(x0, y0, x1, y1, colors, null, GradientOptions.DEFAULT);
    }
    
    public static Shader linearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions) {
        return linearGradient(x0, y0, x1, y1, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader linearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nLinearGradient(x0, y0, x1, y1, colors, positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader linearGradient(float x0, float y0, float x1, float y1, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nLinearGradientCS(x0, y0, x1, y1, Color4f.flattenArray(colors), Native.pointer(cs), positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader radialGradient(float x, float y, float r, int[] colors) {
        return radialGradient(x, y, r, colors, null, GradientOptions.DEFAULT);
    }

    public static Shader radialGradient(float x, float y, float r, int[] colors, float[] positions) {
        return radialGradient(x, y, r, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader radialGradient(float x, float y, float r, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nRadialGradient(x, y, r, colors, positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader radialGradient(float x, float y, float r, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nRadialGradientCS(x, y, r, Color4f.flattenArray(colors), Native.pointer(cs), positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader twoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors) {
        return twoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, null, GradientOptions.DEFAULT);
    }

    public static Shader twoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions) {
        return twoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader twoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nTwoPointConicalGradient(x0, y0, r0, x1, y1, r1, colors, positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader twoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nTwoPointConicalGradientCS(x0, y0, r0, x1, y1, r1, Color4f.flattenArray(colors), Native.pointer(cs), positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader sweepGradient(float x, float y, int[] colors) {
        return sweepGradient(x, y, 0, 360, colors, null, GradientOptions.DEFAULT);
    }
    
    public static Shader sweepGradient(float x, float y, int[] colors, float[] positions) {
        return sweepGradient(x, y, 0, 360, colors, positions, GradientOptions.DEFAULT);
    }

    public static Shader sweepGradient(float x, float y, int[] colors, float[] positions, GradientOptions opts) {
        return sweepGradient(x, y, 0, 360, colors, positions, opts);
    }

    public static Shader sweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nSweepGradient(x, y, startAngle, endAngle, colors, positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader sweepGradient(float x, float y, float startAngle, float endAngle, Color4f[] colors, ColorSpace cs, float[] positions, GradientOptions opts) {
        assert positions == null || colors.length == positions.length : "colors.length " + colors.length + "!= positions.length " + positions.length;
        Native.onNativeCall();
        return new Shader(nSweepGradientCS(x, y, startAngle, endAngle, Color4f.flattenArray(colors), Native.pointer(cs), positions, opts.tileMode.ordinal(), opts.flags, opts.localMatrix));
    }

    public static Shader empty() { Native.onNativeCall(); return new Shader(nEmpty()); }
    public static Shader color(int color) { Native.onNativeCall(); return new Shader(nColor(color)); }

    public static Shader color(Color4f color, ColorSpace space) {
        Native.onNativeCall();
        return new Shader(nColorCS(color.r, color.g, color.b, color.a, Native.pointer(space)));
    }

    public static Shader blend(BlendMode mode, Shader dst, Shader src) {
        Native.onNativeCall();
        return new Shader(nBlend(mode.ordinal(), Native.pointer(dst), Native.pointer(src)));
    }

    public static Shader lerp(float t, Shader dst, Shader src) {
        Native.onNativeCall();
        return new Shader(nLerp(t, Native.pointer(dst), Native.pointer(src)));
    }

    protected Shader(long nativeInstance) { super(nativeInstance); }
    private static native long nWithColorFilter(long nativeInstance, long colorFilterPtr);

    private static native long nLinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nLinearGradientCS(float x0, float y0, float x1, float y1, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nRadialGradient(float x, float y, float r, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nRadialGradientCS(float x, float y, float r, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nTwoPointConicalGradient(float x0, float y0, float r0, float x1, float y1, float r1, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nTwoPointConicalGradientCS(float x0, float y0, float r0, float x1, float y1, float r1, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nSweepGradient(float x, float y, float startAngle, float endAngle, int[] colors, float[] positions, int tileMode, int flags, float[] matrix);
    private static native long nSweepGradientCS(float x, float y, float startAngle, float endAngle, float[] colors, long colorSpacePtr, float[] positions, int tileMode, int flags, float[] matrix);

    private static native long nEmpty();
    private static native long nColor(int color);
    private static native long nColorCS(float r, float g, float b, float a, long colorSpacePtr);
    private static native long nBlend(int blendMode, long dst, long src);
    private static native long nLerp(float t, long dst, long src);
}