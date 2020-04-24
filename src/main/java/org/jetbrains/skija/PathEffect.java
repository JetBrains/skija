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

    public static PathEffect make1D(Path path, float advance, float phase, Style style) {
        Native.onNativeCall();
        return new PathEffect(nMake1D(Native.pointer(path), advance, phase, style.ordinal()));
    }

    protected PathEffect(long nativeInstance) { super(nativeInstance); }
    private static native long nSum(long firstPtr, long secondPtr);
    private static native long nCompose(long outerPtr, long innerPtr);
    private static native Rect nComputeFastBounds(long ptr, float l, float t, float r, float b);
    private static native long nMake1D(long pathPtr, float advance, float phase, int style);
}