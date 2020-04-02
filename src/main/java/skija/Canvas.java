package skija;

public class Canvas extends Native {
    public static enum PointMode { POINTS, LINES, POLYGON }
    public static enum ClipOp { DIFFERENCE, INTERSECT }

    Surface mSurface;

    Canvas(long nativeInstance, Surface surface) {
        super(nativeInstance);
        mSurface = surface;
    }
    
    public void drawPoint(float x, float y, Paint paint) {
        nDrawPoint(mNativeInstance, x, y, paint.mNativeInstance);
    }

    public void drawPoints(PointMode mode, float[] coords, Paint paint) {
        nDrawPoints(mNativeInstance, mode.ordinal(), coords, paint.mNativeInstance);
    }

    public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        nDrawLine(mNativeInstance, x0, y0, x1, y1, paint.mNativeInstance);
    }

    public void drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        nDrawArc(mNativeInstance, left, top, width, height, startAngle, sweepAngle, includeCenter, paint.mNativeInstance);
    }

    public void drawRect(Rect r, Paint paint) {
        nDrawRect(mNativeInstance, r.left, r.top, r.right, r.bottom, paint.mNativeInstance);
    }

    public void drawOval(Rect r, Paint paint) {
        nDrawOval(mNativeInstance, r.left, r.top, r.right, r.bottom, paint.mNativeInstance);
    }

    public void drawCircle(float x, float y, float radius, Paint paint) {
        nDrawOval(mNativeInstance, x - radius, y - radius, x + radius, y + radius, paint.mNativeInstance);
    }

    public void drawRoundedRect(RoundedRect r, Paint paint) {
        nDrawRoundedRect(mNativeInstance, r.left, r.top, r.right, r.bottom, r.radii, paint.mNativeInstance);
    }

    public void drawDoubleRoundedRect(RoundedRect outer, RoundedRect inner, Paint paint) {
        nDrawDoubleRoundedRect(mNativeInstance, outer.left, outer.top, outer.right, outer.bottom, outer.radii, inner.left, inner.top, inner.right, inner.bottom, inner.radii, paint.mNativeInstance);
    }

    public void drawPath(Path path, Paint paint) {
        nDrawPath(mNativeInstance, path.mNativeInstance, paint.mNativeInstance);
    }
    
    public void clear(long color) { nClear(mNativeInstance, color); }
    public void drawPaint(Paint paint) { nDrawPaint(mNativeInstance, paint.mNativeInstance); }

    public void clipRect(Rect r, ClipOp op, boolean antiAlias) { nClipRect(mNativeInstance, r.left, r.top, r.right, r.bottom, op.ordinal(), antiAlias); }
    public void clipRect(Rect r, ClipOp op) { clipRect(r, op, false); }
    public void clipRect(Rect r, boolean antiAlias) { clipRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRect(Rect r) { clipRect(r, ClipOp.INTERSECT, false); }

    public void clipRoundedRect(RoundedRect r, ClipOp op, boolean antiAlias) { nClipRoundedRect(mNativeInstance, r.left, r.top, r.right, r.bottom, r.radii, op.ordinal(), antiAlias); }
    public void clipRoundedRect(RoundedRect r, ClipOp op) { clipRoundedRect(r, op, false); }
    public void clipRoundedRect(RoundedRect r, boolean antiAlias) { clipRoundedRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRoundedRect(RoundedRect r) { clipRoundedRect(r, ClipOp.INTERSECT, false); }

    public void clipPath(Path p, ClipOp op, boolean antiAlias) { nClipPath(mNativeInstance, p.mNativeInstance, op.ordinal(), antiAlias); }
    public void clipPath(Path p, ClipOp op) { clipPath(p, op, false); }
    public void clipPath(Path p, boolean antiAlias) { clipPath(p, ClipOp.INTERSECT, antiAlias); }
    public void clipPath(Path p) { clipPath(p, ClipOp.INTERSECT, false); }

    public void translate(float dx, float dy) { nConcat(mNativeInstance, 1, 0, dx, 0, 1, dy, 0, 0, 1); }
    public void scale(float sx, float sy) { nConcat(mNativeInstance, sx, 0, 0, 0, sy, 0, 0, 0, 1); }
    public void rotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        nConcat(mNativeInstance, (float) cos, (float) -sin, 0, (float) sin, (float) cos, 0, 0, 0, 1);
    }

    public int save() { return nSave(mNativeInstance); }
    public int getSaveCount() { return nGetSaveCount(mNativeInstance); }
    public void restore() { nRestore(mNativeInstance); }
    public void restoreToCount(int saveCount) { nRestoreToCount(mNativeInstance, saveCount); }

    private static native void nDrawPoint(long nativeCanvas, float x, float y, long nativePaint);
    private static native void nDrawPoints(long nativeCanvas, int mode, float[] coords, long nativePaint);
    private static native void nDrawLine(long nativeCanvas, float x0, float y0, float x1, float y1, long nativePaint);
    private static native void nDrawArc(long nativeCanvas, float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, long nativePaint);
    private static native void nDrawRect(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawOval(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawRoundedRect(long nativeCanvas, float left, float top, float right, float bottom, float radii[], long nativePaint);
    private static native void nDrawDoubleRoundedRect(long nativeCanvas, float ol, float ot, float or, float ob, float oradii[], float il, float it, float ir, float ib, float iradii[], long nativePaint);
    private static native void nDrawPath(long nativeCanvas, long nativePath, long nativePaint);
    private static native void nClear(long nativeCanvas, long color);
    private static native void nDrawPaint(long nativeCanvas, long nativePaint);
    private static native void nClipRect(long nativeCanvas, float left, float top, float right, float bottom, int op, boolean antiAlias);
    private static native void nClipRoundedRect(long nativeCanvas, float left, float top, float right, float bottom, float[] radii, int op, boolean antiAlias);
    private static native void nClipPath(long nativeCanvas, long nativePath, int op, boolean antiAlias);

    private static native void nConcat(long nativeCanvas,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2);
    private static native int nSave(long nativeCanvas);
    private static native int nGetSaveCount(long nativeCanvas);
    private static native void nRestore(long nativeCanvas);
    private static native void nRestoreToCount(long nativeCanvas, int saveCount);
}
