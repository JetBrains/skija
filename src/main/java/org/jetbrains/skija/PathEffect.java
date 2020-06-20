package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class PathEffect extends RefCnt {
    public enum Style {
        /** translate the shape to each position */
        TRANSLATE,
        /** rotate the shape about its center */
        ROTATE,
        /** transform each point, and turn lines into curves */
        MORPH
    }

    public PathEffect makeSum(PathEffect second) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeSum(_ptr, Native.getPtr(second)));
    }
    
    public PathEffect makeCompose(PathEffect inner) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeCompose(_ptr, Native.getPtr(inner)));
    }
    
    public Rect computeFastBounds(Rect src) {
        Stats.onNativeCall();
        return _nComputeFastBounds(_ptr, src._left, src._top, src._right, src._bottom);
    }

    public static PathEffect makePath1D(Path path, float advance, float phase, Style style) {
        Stats.onNativeCall();
        return new PathEffect(_nMakePath1D(Native.getPtr(path), advance, phase, style.ordinal()));
    }

    public static PathEffect makePath2D(Matrix33 matrix, Path path) {
        Stats.onNativeCall();
        return new PathEffect(_nMakePath2D(matrix.getMat(), Native.getPtr(path)));
    }

    public static PathEffect makeLine2D(float width, Matrix33 matrix) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeLine2D(width, matrix.getMat()));
    }

    public static PathEffect makeCorner(float radius) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeCorner(radius));
    }

    public static PathEffect makeDash(float[] intervals, float phase) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeDash(intervals, phase));
    }

    public static PathEffect makeDiscrete(float segLength, float dev, int seed) {
        Stats.onNativeCall();
        return new PathEffect(_nMakeDiscrete(segLength, dev, seed));
    }

    @ApiStatus.Internal
    public PathEffect(long ptr) {
        super(ptr);
    }

    public static native long _nMakeSum(long firstPtr, long secondPtr);
    public static native long _nMakeCompose(long outerPtr, long innerPtr);
    public static native Rect _nComputeFastBounds(long ptr, float l, float t, float r, float b);
    public static native long _nMakePath1D(long pathPtr, float advance, float phase, int style);
    public static native long _nMakePath2D(float[] matrix, long pathPtr);
    public static native long _nMakeLine2D(float width, float[] matrix);
    public static native long _nMakeCorner(float radius);
    public static native long _nMakeDash(float[] intervals, float phase);
    public static native long _nMakeDiscrete(float segLength, float dev, int seed);
}