package org.jetbrains.skija;

public class Region extends Managed {
    public enum Op { DIFFERENCE, INTERSECT, UNION, XOR, REVERSE_DIFFERENCE, REPLACE }

    public Region() { super(nInit(), nativeFinalizer); Stats.onNativeCall(); }

    public boolean set(Region r) { Stats.onNativeCall(); return nSet(_ptr, r._ptr); }
    public boolean isEmpty() { Stats.onNativeCall(); return nIsEmpty(_ptr); }
    public boolean isRect() { Stats.onNativeCall(); return nIsRect(_ptr); }
    public boolean isComplex() { Stats.onNativeCall(); return nIsComplex(_ptr); }
    public int[]   getBounds() { Stats.onNativeCall(); return nGetBounds(_ptr); }
    public int     computeRegionComplexity() { Stats.onNativeCall(); return nComputeRegionComplexity(_ptr); }
    public boolean getBoundaryPath(Path p) { Stats.onNativeCall(); return nGetBoundaryPath(_ptr, p._ptr); }
    public boolean setEmpty() { Stats.onNativeCall(); return nSetEmpty(_ptr); }
    public boolean setRect(int left, int top, int right, int bottom) { Stats.onNativeCall(); return nSetRect(_ptr, left, top, right, bottom); }
    public boolean setRects(int[] rects) {
        assert rects.length > 0 && rects.length % 4 == 0 : "Expected array length divisible by 4, got " + rects.length;
        Stats.onNativeCall();
        return nSetRects(_ptr, rects);
    }
    public boolean setRegion(Region r) { Stats.onNativeCall(); return nSetRegion(_ptr, r._ptr); }
    public boolean setPath(Path path, Region clip) { Stats.onNativeCall(); return nSetPath(_ptr, path._ptr, clip._ptr); }
    public boolean intersects(int left, int top, int right, int bottom) { Stats.onNativeCall(); return nIntersectsIRect(_ptr, left, top, right, bottom); }
    public boolean intersects(Region r) { Stats.onNativeCall(); return nIntersectsRegion(_ptr, r._ptr); }
    public boolean contains(int x, int y) { Stats.onNativeCall(); return nContainsIPoint(_ptr, x, y); }
    public boolean contains(int left, int top, int right, int bottom) { Stats.onNativeCall(); return nContainsIRect(_ptr, left, top, right, bottom); }
    public boolean contains(Region r) { Stats.onNativeCall(); return nContainsRegion(_ptr, r._ptr); }
    public boolean quickContains(int left, int top, int right, int bottom) { Stats.onNativeCall(); return nQuickContains(_ptr, left, top, right, bottom); }
    public boolean quickReject(int left, int top, int right, int bottom) { Stats.onNativeCall(); return nQuickRejectIRect(_ptr, left, top, right, bottom); }
    public boolean quickReject(Region r) { Stats.onNativeCall(); return nQuickRejectRegion(_ptr, r._ptr); }
    public void    translate(int dx, int dy) { Stats.onNativeCall(); nTranslate(_ptr, dx, dy); }
    public boolean op(int left, int top, int right, int bottom, Op op) { Stats.onNativeCall(); return nOpIRect(_ptr, left, top, right, bottom, op.ordinal()); }
    public boolean op(Region r, Op op) { Stats.onNativeCall(); return nOpRegion(_ptr, r._ptr, op.ordinal()); }
    public boolean op(int left, int top, int right, int bottom, Region r, Op op) { Stats.onNativeCall(); return nOpIRectRegion(_ptr, left, top, right, bottom, r._ptr, op.ordinal()); }
    public boolean op(Region r, int left, int top, int right, int bottom, Op op) { Stats.onNativeCall(); return nOpRegionIRect(_ptr, r._ptr, left, top, right, bottom, op.ordinal()); }
    public boolean op(Region a, Region b, Op op) { Stats.onNativeCall(); return nOpRegionRegion(_ptr, a._ptr, b._ptr, op.ordinal()); }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();

    private static native boolean nSet(long ptr, long regoinPtr);
    private static native boolean nIsEmpty(long ptr);
    private static native boolean nIsRect(long ptr);
    private static native boolean nIsComplex(long ptr);
    private static native int[]   nGetBounds(long ptr);
    private static native int     nComputeRegionComplexity(long ptr);
    private static native boolean nGetBoundaryPath(long ptr, long pathPtr);
    private static native boolean nSetEmpty(long ptr);
    private static native boolean nSetRect(long ptr, int left, int top, int right, int bottom);
    private static native boolean nSetRects(long ptr, int[] rects);
    private static native boolean nSetRegion(long ptr, long regionPtr);
    private static native boolean nSetPath(long ptr, long pathPtr, long regionPtr);

    private static native boolean nIntersectsIRect(long ptr, int left, int top, int right, int bottom);
    private static native boolean nIntersectsRegion(long ptr, long regionPtr);
    private static native boolean nContainsIPoint(long ptr, int x, int y);
    private static native boolean nContainsIRect(long ptr, int left, int top, int right, int bottom);
    private static native boolean nContainsRegion(long ptr, long regionPtr);
    private static native boolean nQuickContains(long ptr, int left, int top, int right, int bottom);
    private static native boolean nQuickRejectIRect(long ptr, int left, int top, int right, int bottom);
    private static native boolean nQuickRejectRegion(long ptr, long regionPtr);
    private static native void    nTranslate(long ptr, int dx, int dy);
    private static native boolean nOpIRect(long ptr, int left, int top, int right, int bottom, int op);
    private static native boolean nOpRegion(long ptr, long regionPtr, int op);
    private static native boolean nOpIRectRegion(long ptr, int left, int top, int right, int bottom, long regionPtr, int op);
    private static native boolean nOpRegionIRect(long ptr, long regionPtr, int left, int top, int right, int bottom, int op);
    private static native boolean nOpRegionRegion(long ptr, long regionPtrA, long regionPtrB, int op);
}