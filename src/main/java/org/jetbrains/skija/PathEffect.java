package org.jetbrains.skija;

public class PathEffect extends RefCounted {
    public enum Style {
        /** translate the shape to each position */
        TRANSLATE,
        /** rotate the shape about its center */
        ROTATE,
        /** transform each point, and turn lines into curves */
        MORPH
    }

    public PathEffect sum(PathEffect second) {
        Native.onNativeCall();
        return new PathEffect(nSum(nativeInstance, Native.pointer(second)));
    }
    
    public PathEffect compose(PathEffect inner) {
        Native.onNativeCall();
        return new PathEffect(nCompose(nativeInstance, Native.pointer(inner)));
    }
    
    public Rect computeFastBounds(Rect src) {
        Native.onNativeCall();
        return nComputeFastBounds(nativeInstance, src.left, src.top, src.right, src.bottom);
    }

    public static PathEffect path1D(Path path, float advance, float phase, Style style) {
        Native.onNativeCall();
        return new PathEffect(nPath1D(Native.pointer(path), advance, phase, style.ordinal()));
    }

    public static PathEffect path2D(float[] matrix, Path path) {
        Native.onNativeCall();
        return new PathEffect(nPath2D(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8],
            Native.pointer(path)));
    }

    public static PathEffect line2D(float width, float[] matrix) {
        Native.onNativeCall();
        return new PathEffect(nLine2D(width, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8]));
    }

    public static PathEffect corner(float radius) {
        Native.onNativeCall();
        return new PathEffect(nCorner(radius));
    }

    public static PathEffect dash(float[] intervals, float phase) {
        Native.onNativeCall();
        return new PathEffect(nDash(intervals, phase));
    }

    public static PathEffect discrete(float segLength, float dev, int seed) {
        Native.onNativeCall();
        return new PathEffect(nDiscrete(segLength, dev, seed));
    }

    protected PathEffect(long nativeInstance) { super(nativeInstance); }
    private static native long nSum(long firstPtr, long secondPtr);
    private static native long nCompose(long outerPtr, long innerPtr);
    private static native Rect nComputeFastBounds(long ptr, float l, float t, float r, float b);
    private static native long nPath1D(long pathPtr, float advance, float phase, int style);
    private static native long nPath2D(
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2,
        long pathPtr);
    private static native long nLine2D(float width,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2);
    private static native long nCorner(float radius);
    private static native long nDash(float[] intervals, float phase);
    private static native long nDiscrete(float segLength, float dev, int seed);
}