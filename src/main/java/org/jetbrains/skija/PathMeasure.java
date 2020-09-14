package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class PathMeasure extends Managed {
    @ApiStatus.Internal
    public PathMeasure(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public PathMeasure() {
        this(_nMake());
        Stats.onNativeCall();
    }

    /**
     * Initialize the pathmeasure with the specified path. The parts of the path that are needed
     * are copied, so the client is free to modify/delete the path after this call.
     */
    public PathMeasure(Path path, boolean forceClosed) {
        this(path, forceClosed, 1f);
    }

    /**
     * <p>Initialize the pathmeasure with the specified path. The parts of the path that are needed
     * are copied, so the client is free to modify/delete the path after this call.</p>
     *
     * <p>resScale controls the precision of the measure. values > 1 increase the
     * precision (and possible slow down the computation).</p>
     */
    public PathMeasure(Path path, boolean forceClosed, float resScale) {
        this(_nMakePath(Native.getPtr(path), forceClosed, resScale));
        Stats.onNativeCall();
    }

    /**
     * Reset the pathmeasure with the specified path. The parts of the path that are needed
     * are copied, so the client is free to modify/delete the path after this call.
     */
    public PathMeasure setPath(@Nullable Path path, boolean forceClosed) {
        Stats.onNativeCall();
        _nSetPath(_ptr, Native.getPtr(path), forceClosed);
        return this;
    }

    /** 
     * Return the total length of the current contour, or 0 if no path
     * is associated (e.g. resetPath(null))
     */
    public float getLength() {
        Stats.onNativeCall();
        return _nGetLength(_ptr);
    }

    /**
     * Pins distance to 0 <= distance <= getLength(), and then computes
     * the corresponding position.
     * 
     * @return  null if there is no path, or a zero-length path was specified.
     */
    @Nullable
    public Point getPosition(float distance) {
        Stats.onNativeCall();
        return _nGetPosition(_ptr, distance);   
    }

    /**
     * Pins distance to 0 <= distance <= getLength(), and then computes
     * the corresponding tangent.
     * 
     * @return  null if there is no path, or a zero-length path was specified.
     */
    @Nullable
    public Point getTangent(float distance) {
        Stats.onNativeCall();
        return _nGetTangent(_ptr, distance);   
    }

    /**
     * Pins distance to 0 <= distance <= getLength(), and then computes
     * the corresponding matrix (by calling getPosition/getTangent).
     * 
     * @return  null if there is no path, or a zero-length path was specified.
     */
    @Nullable
    public Matrix33 getMatrix(float distance, boolean getPosition, boolean getTangent) {
        Stats.onNativeCall();
        float[] mat = _nGetMatrix(_ptr, distance, getPosition, getTangent);
        return mat == null ? null : new Matrix33(mat);
    }

    /**
     * Given a start and stop distance, return in dst the intervening segment(s).
     * If the segment is zero-length, return false, else return true.
     * startD and stopD are pinned to legal values (0..getLength()). If startD > stopD
     * then return false (and leave dst untouched).
     * Begin the segment with a moveTo if startWithMoveTo is true
    */
    @Nullable
    public boolean getSegment(float startD, float endD, @NotNull Path dst, boolean startWithMoveTo) {
        Stats.onNativeCall();
        return _nGetSegment(_ptr, startD, endD, Native.getPtr(dst), startWithMoveTo);
    }

    /**
     * @return  true if the current contour is closed.
     */
    public boolean isClosed() {
        Stats.onNativeCall();
        return _nIsClosed(_ptr);
    }

    /** 
     * Move to the next contour in the path. Return true if one exists, or false if
     * we're done with the path.
     */
    public boolean nextContour() {
        Stats.onNativeCall();
        return _nNextContour(_ptr);
    }

    @ApiStatus.Internal public static final  long    _finalizerPtr = _nGetFinalizer();
    @ApiStatus.Internal public static native long    _nGetFinalizer();
    @ApiStatus.Internal public static native long    _nMake();
    @ApiStatus.Internal public static native long    _nMakePath(long pathPtr, boolean forceClosed, float resScale);
    @ApiStatus.Internal public static native void    _nSetPath(long ptr, long pathPtr, boolean forceClosed);
    @ApiStatus.Internal public static native float   _nGetLength(long ptr);
    @ApiStatus.Internal public static native Point   _nGetPosition(long ptr, float distance);
    @ApiStatus.Internal public static native Point   _nGetTangent(long ptr, float distance);
    @ApiStatus.Internal public static native float[] _nGetMatrix(long ptr, float distance, boolean getPosition, boolean getTangent);
    @ApiStatus.Internal public static native boolean _nGetSegment(long ptr, float startD, float endD, long dstPtr, boolean startWithMoveTo);
    @ApiStatus.Internal public static native boolean _nIsClosed(long ptr);
    @ApiStatus.Internal public static native boolean _nNextContour(long ptr);
}
