package skija;

public class Region extends Managed {
    public enum Op { DIFFERENCE, INTERSECT, UNION, XOR, REVERSE_DIFFERENCE, REPLACE }

    public Region() { super(nInit(), kNativeFinalizer); }

    public boolean set(Region r) { return nSet(mNativeInstance, r.mNativeInstance); }
    public boolean isEmpty() { return nIsEmpty(mNativeInstance); }
    public boolean isRect() { return nIsRect(mNativeInstance); }
    public boolean isComplex() { return nIsComplex(mNativeInstance); }
    public int[]   getBounds() { return nGetBounds(mNativeInstance); }
    public int     computeRegionComplexity() { return nComputeRegionComplexity(mNativeInstance); }
    public boolean getBoundaryPath(Path p) { return nGetBoundaryPath(mNativeInstance, p.mNativeInstance); }
    public boolean setEmpty() { return nSetEmpty(mNativeInstance); }
    public boolean setRect(int left, int top, int right, int bottom) { return nSetRect(mNativeInstance, left, top, right, bottom); }
    public boolean setRects(int[] rects) {
        assert rects.length > 0 && rects.length % 4 == 0 : "Expected array length divisible by 4, got " + rects.length;
        return nSetRects(mNativeInstance, rects);
    }
    public boolean setRegion(Region r) { return nSetRegion(mNativeInstance, r.mNativeInstance); }
    public boolean setPath(Path path, Region clip) { return nSetPath(mNativeInstance, path.mNativeInstance, clip.mNativeInstance); }
    public boolean intersects(int left, int top, int right, int bottom) { return nIntersectsIRect(mNativeInstance, left, top, right, bottom); }
    public boolean intersects(Region r) { return nIntersectsRegion(mNativeInstance, r.mNativeInstance); }
    public boolean contains(int x, int y) { return nContainsIPoint(mNativeInstance, x, y); }
    public boolean contains(int left, int top, int right, int bottom) { return nContainsIRect(mNativeInstance, left, top, right, bottom); }
    public boolean contains(Region r) { return nContainsRegion(mNativeInstance, r.mNativeInstance); }
    public boolean quickContains(int left, int top, int right, int bottom) { return nQuickContains(mNativeInstance, left, top, right, bottom); }
    public boolean quickReject(int left, int top, int right, int bottom) { return nQuickRejectIRect(mNativeInstance, left, top, right, bottom); }
    public boolean quickReject(Region r) { return nQuickRejectRegion(mNativeInstance, r.mNativeInstance); }
    public void    translate(int dx, int dy) { nTranslate(mNativeInstance, dx, dy); }
    public boolean op(int left, int top, int right, int bottom, Op op) { return nOpIRect(mNativeInstance, left, top, right, bottom, op.ordinal()); }
    public boolean op(Region r, Op op) { return nOpRegion(mNativeInstance, r.mNativeInstance, op.ordinal()); }
    public boolean op(int left, int top, int right, int bottom, Region r, Op op) { return nOpIRectRegion(mNativeInstance, left, top, right, bottom, r.mNativeInstance, op.ordinal()); }
    public boolean op(Region r, int left, int top, int right, int bottom, Op op) { return nOpRegionIRect(mNativeInstance, r.mNativeInstance, left, top, right, bottom, op.ordinal()); }
    public boolean op(Region a, Region b, Op op) { return nOpRegionRegion(mNativeInstance, a.mNativeInstance, b.mNativeInstance, op.ordinal()); }

    private static long kNativeFinalizer = nGetNativeFinalizer();
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