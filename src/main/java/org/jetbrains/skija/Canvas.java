package org.jetbrains.skija;

public class Canvas extends Native {
    public enum PointMode { POINTS, LINES, POLYGON }
    public enum ClipOp { DIFFERENCE, INTERSECT }

    protected Surface surface;

    Canvas(long nativeInstance, Surface surface) {
        super(nativeInstance);
        this.surface = surface;
    }

    public void drawPoint(float x, float y, Paint paint) {
        nDrawPoint(nativeInstance, x, y, paint.nativeInstance);
    }

    public void drawPoints(PointMode mode, float[] coords, Paint paint) {
        nDrawPoints(nativeInstance, mode.ordinal(), coords, paint.nativeInstance);
    }

    public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        nDrawLine(nativeInstance, x0, y0, x1, y1, paint.nativeInstance);
    }

    public void drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        nDrawArc(nativeInstance, left, top, width, height, startAngle, sweepAngle, includeCenter, paint.nativeInstance);
    }

    public void drawRect(Rect r, Paint paint) {
        nDrawRect(nativeInstance, r.left, r.top, r.right, r.bottom, paint.nativeInstance);
    }

    public void drawOval(Rect r, Paint paint) {
        nDrawOval(nativeInstance, r.left, r.top, r.right, r.bottom, paint.nativeInstance);
    }

    public void drawCircle(float x, float y, float radius, Paint paint) {
        nDrawOval(nativeInstance, x - radius, y - radius, x + radius, y + radius, paint.nativeInstance);
    }

    public void drawRoundedRect(RoundedRect r, Paint paint) {
        nDrawRoundedRect(nativeInstance, r.left, r.top, r.right, r.bottom, r.radii, paint.nativeInstance);
    }

    public void drawDoubleRoundedRect(RoundedRect outer, RoundedRect inner, Paint paint) {
        nDrawDoubleRoundedRect(nativeInstance, outer.left, outer.top, outer.right, outer.bottom, outer.radii, inner.left, inner.top, inner.right, inner.bottom, inner.radii, paint.nativeInstance);
    }

    public void drawPath(Path path, Paint paint) {
        nDrawPath(nativeInstance, path.nativeInstance, paint.nativeInstance);
    }

    public void drawRegion(Region r, Paint paint) { nDrawRegion(nativeInstance, r.nativeInstance, paint.nativeInstance); }

    public void drawTextBuffer(TextBuffer buffer, float x, float y, SkFont font, Paint paint) {
        nDrawTextBuffer(nativeInstance, buffer.nativeInstance, x, y, font.nativeInstance, paint.nativeInstance);
    }

    public void clear(long color) { nClear(nativeInstance, color); }
    public void drawPaint(Paint paint) { nDrawPaint(nativeInstance, paint.nativeInstance); }

    public void clipRect(Rect r, ClipOp op, boolean antiAlias) { nClipRect(nativeInstance, r.left, r.top, r.right, r.bottom, op.ordinal(), antiAlias); }
    public void clipRect(Rect r, ClipOp op) { clipRect(r, op, false); }
    public void clipRect(Rect r, boolean antiAlias) { clipRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRect(Rect r) { clipRect(r, ClipOp.INTERSECT, false); }

    public void clipRoundedRect(RoundedRect r, ClipOp op, boolean antiAlias) { nClipRoundedRect(nativeInstance, r.left, r.top, r.right, r.bottom, r.radii, op.ordinal(), antiAlias); }
    public void clipRoundedRect(RoundedRect r, ClipOp op) { clipRoundedRect(r, op, false); }
    public void clipRoundedRect(RoundedRect r, boolean antiAlias) { clipRoundedRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRoundedRect(RoundedRect r) { clipRoundedRect(r, ClipOp.INTERSECT, false); }

    public void clipPath(Path p, ClipOp op, boolean antiAlias) { nClipPath(nativeInstance, p.nativeInstance, op.ordinal(), antiAlias); }
    public void clipPath(Path p, ClipOp op) { clipPath(p, op, false); }
    public void clipPath(Path p, boolean antiAlias) { clipPath(p, ClipOp.INTERSECT, antiAlias); }
    public void clipPath(Path p) { clipPath(p, ClipOp.INTERSECT, false); }

    public void clipRegion(Region r, ClipOp op) { nClipRegion(nativeInstance, r.nativeInstance, op.ordinal()); }
    public void clipRegion(Region r) { clipRegion(r, ClipOp.INTERSECT); }

    public void translate(float dx, float dy) { nConcat(nativeInstance, 1, 0, dx, 0, 1, dy, 0, 0, 1); }
    public void scale(float sx, float sy) { nConcat(nativeInstance, sx, 0, 0, 0, sy, 0, 0, 0, 1); }
    public void rotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        nConcat(nativeInstance, (float) cos, (float) -sin, 0, (float) sin, (float) cos, 0, 0, 0, 1);
    }

    public int save() { return nSave(nativeInstance); }
    public int getSaveCount() { return nGetSaveCount(nativeInstance); }
    public void restore() { nRestore(nativeInstance); }
    public void restoreToCount(int saveCount) { nRestoreToCount(nativeInstance, saveCount); }

    private static native void nDrawPoint(long nativeCanvas, float x, float y, long nativePaint);
    private static native void nDrawPoints(long nativeCanvas, int mode, float[] coords, long nativePaint);
    private static native void nDrawLine(long nativeCanvas, float x0, float y0, float x1, float y1, long nativePaint);
    private static native void nDrawArc(long nativeCanvas, float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, long nativePaint);
    private static native void nDrawRect(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawOval(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawRoundedRect(long nativeCanvas, float left, float top, float right, float bottom, float radii[], long nativePaint);
    private static native void nDrawDoubleRoundedRect(long nativeCanvas, float ol, float ot, float or, float ob, float oradii[], float il, float it, float ir, float ib, float iradii[], long nativePaint);
    private static native void nDrawPath(long nativeCanvas, long nativePath, long nativePaint);
    private static native void nDrawRegion(long nativeCanvas, long nativeRegion, long nativePaint);
    private static native void nDrawTextBuffer(long nativeCanvas, long buffer, float x, float y, long font, long paint);
    private static native void nClear(long nativeCanvas, long color);
    private static native void nDrawPaint(long nativeCanvas, long nativePaint);
    private static native void nClipRect(long nativeCanvas, float left, float top, float right, float bottom, int op, boolean antiAlias);
    private static native void nClipRoundedRect(long nativeCanvas, float left, float top, float right, float bottom, float[] radii, int op, boolean antiAlias);
    private static native void nClipPath(long nativeCanvas, long nativePath, int op, boolean antiAlias);
    private static native void nClipRegion(long nativeCanvas, long nativeRegion, int op);

    private static native void nConcat(long nativeCanvas,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2);
    private static native int nSave(long nativeCanvas);
    private static native int nGetSaveCount(long nativeCanvas);
    private static native void nRestore(long nativeCanvas);
    private static native void nRestoreToCount(long nativeCanvas, int saveCount);
}
