package org.jetbrains.skija;

public class Canvas extends Native {
    public enum PointMode { POINTS, LINES, POLYGON }
    public enum ClipOp { DIFFERENCE, INTERSECT }
    public enum SrcRectConstraint { STRICT, FAST }

    protected Surface surface;

    Canvas(long nativeInstance, Surface surface) {
        super(nativeInstance);
        this.surface = surface;
    }

    public void drawPoint(float x, float y, Paint paint) {
        Native.onNativeCall();
        nDrawPoint(nativeInstance, x, y, paint.nativeInstance);
    }

    public void drawPoints(PointMode mode, float[] coords, Paint paint) {
        Native.onNativeCall();
        nDrawPoints(nativeInstance, mode.ordinal(), coords, paint.nativeInstance);
    }

    public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        Native.onNativeCall();
        nDrawLine(nativeInstance, x0, y0, x1, y1, paint.nativeInstance);
    }

    public void drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        Native.onNativeCall();
        nDrawArc(nativeInstance, left, top, width, height, startAngle, sweepAngle, includeCenter, paint.nativeInstance);
    }

    public void drawRect(Rect r, Paint paint) {
        Native.onNativeCall();
        nDrawRect(nativeInstance, r.left, r.top, r.right, r.bottom, paint.nativeInstance);
    }

    public void drawOval(Rect r, Paint paint) {
        Native.onNativeCall();
        nDrawOval(nativeInstance, r.left, r.top, r.right, r.bottom, paint.nativeInstance);
    }

    public void drawCircle(float x, float y, float radius, Paint paint) {
        Native.onNativeCall();
        nDrawOval(nativeInstance, x - radius, y - radius, x + radius, y + radius, paint.nativeInstance);
    }

    public void drawRoundedRect(RoundedRect r, Paint paint) {
        Native.onNativeCall();
        nDrawRoundedRect(nativeInstance, r.left, r.top, r.right, r.bottom, r.radii, paint.nativeInstance);
    }

    public void drawDoubleRoundedRect(RoundedRect outer, RoundedRect inner, Paint paint) {
        Native.onNativeCall();
        nDrawDoubleRoundedRect(nativeInstance, outer.left, outer.top, outer.right, outer.bottom, outer.radii, inner.left, inner.top, inner.right, inner.bottom, inner.radii, paint.nativeInstance);
    }

    public void drawPath(Path path, Paint paint) {
        Native.onNativeCall();
        nDrawPath(nativeInstance, path.nativeInstance, paint.nativeInstance);
    }

    public void drawImage(Image image, float left, float top) {
        drawImage(image, left, top, null);
    }

    public void drawImage(Image image, float left, float top, Paint paint) {
        Native.onNativeCall();
        nDrawImageIRect(nativeInstance, Native.pointer(image), 0, 0, image.getWidth(), image.getHeight(), left, top, image.getWidth(), image.getHeight(), Native.pointer(paint), SrcRectConstraint.STRICT.ordinal());
    }

    public void drawImageRect(Image image, Rect dst) {
        drawImageRect(image, dst, null);
    }
    
    public void drawImageRect(Image image, Rect dst, Paint paint) {
        Native.onNativeCall();
        nDrawImageIRect(nativeInstance, Native.pointer(image), 0, 0, image.getWidth(), image.getHeight(), dst.left, dst.top, dst.right, dst.bottom, Native.pointer(paint), SrcRectConstraint.STRICT.ordinal());
    }

    public void drawImageRect(Image image, Rect src, Rect dst, Paint paint) {
        drawImageRect(image, src, dst, paint, SrcRectConstraint.STRICT);
    }

    public void drawImageRect(Image image, Rect src, Rect dst, Paint paint, SrcRectConstraint constraint) {
        Native.onNativeCall();
        nDrawImageRect(nativeInstance, Native.pointer(image), src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, Native.pointer(paint), constraint.ordinal());
    }

    public void drawImageRect(Image image, IRect src, Rect dst) {
        drawImageRect(image, src, dst, null);
    }

    public void drawImageRect(Image image, IRect src, Rect dst, Paint paint) {
        drawImageRect(image, src, dst, paint, SrcRectConstraint.STRICT);
    }

    public void drawImageRect(Image image, IRect src, Rect dst, Paint paint, SrcRectConstraint constraint) {
        Native.onNativeCall();
        nDrawImageIRect(nativeInstance, Native.pointer(image), src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, Native.pointer(paint), constraint.ordinal());
    }

    public void drawRegion(Region r, Paint paint) {
        Native.onNativeCall();
        nDrawRegion(nativeInstance, r.nativeInstance, paint.nativeInstance);
    }

    public void drawString(String s, float x, float y, Font font, Paint paint) {
        Native.onNativeCall();
        nDrawString(nativeInstance, s, x, y, Native.pointer(font), Native.pointer(paint));
    }

    public void drawTextBlob(TextBlob blob, float x, float y, Font font, Paint paint) {
        Native.onNativeCall();
        nDrawTextBlob(nativeInstance, blob.nativeInstance, x, y, font.nativeInstance, paint.nativeInstance);
    }

    public void clear(int color) { Native.onNativeCall(); nClear(nativeInstance, color); }
    public void drawPaint(Paint paint) { Native.onNativeCall(); nDrawPaint(nativeInstance, paint.nativeInstance); }

    public void clipRect(Rect r, ClipOp op, boolean antiAlias) { Native.onNativeCall(); nClipRect(nativeInstance, r.left, r.top, r.right, r.bottom, op.ordinal(), antiAlias); }
    public void clipRect(Rect r, ClipOp op) { clipRect(r, op, false); }
    public void clipRect(Rect r, boolean antiAlias) { clipRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRect(Rect r) { clipRect(r, ClipOp.INTERSECT, false); }

    public void clipRoundedRect(RoundedRect r, ClipOp op, boolean antiAlias) { Native.onNativeCall(); nClipRoundedRect(nativeInstance, r.left, r.top, r.right, r.bottom, r.radii, op.ordinal(), antiAlias); }
    public void clipRoundedRect(RoundedRect r, ClipOp op) { clipRoundedRect(r, op, false); }
    public void clipRoundedRect(RoundedRect r, boolean antiAlias) { clipRoundedRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRoundedRect(RoundedRect r) { clipRoundedRect(r, ClipOp.INTERSECT, false); }

    public void clipPath(Path p, ClipOp op, boolean antiAlias) { Native.onNativeCall(); nClipPath(nativeInstance, p.nativeInstance, op.ordinal(), antiAlias); }
    public void clipPath(Path p, ClipOp op) { clipPath(p, op, false); }
    public void clipPath(Path p, boolean antiAlias) { clipPath(p, ClipOp.INTERSECT, antiAlias); }
    public void clipPath(Path p) { clipPath(p, ClipOp.INTERSECT, false); }

    public void clipRegion(Region r, ClipOp op) { Native.onNativeCall(); nClipRegion(nativeInstance, r.nativeInstance, op.ordinal()); }
    public void clipRegion(Region r) { clipRegion(r, ClipOp.INTERSECT); }

    public void translate(float dx, float dy) { Native.onNativeCall(); nConcat(nativeInstance, 1, 0, dx, 0, 1, dy, 0, 0, 1); }
    public void scale(float sx, float sy) { Native.onNativeCall(); nConcat(nativeInstance, sx, 0, 0, 0, sy, 0, 0, 0, 1); }
    public void rotate(float deg) { concat(Matrix.rotate(deg)); }
    public void concat(float[] matrix) {
        assert matrix.length == 9 : "Expected 9 elements in matrix, got " + matrix == null ? null : matrix.length;
        Native.onNativeCall();
        nConcat(nativeInstance, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8]);
    }

    public int save() { Native.onNativeCall(); return nSave(nativeInstance); }
    public int getSaveCount() { Native.onNativeCall(); return nGetSaveCount(nativeInstance); }
    public void restore() { Native.onNativeCall(); nRestore(nativeInstance); }
    public void restoreToCount(int saveCount) { Native.onNativeCall(); nRestoreToCount(nativeInstance, saveCount); }

    private static native void nDrawPoint(long nativeCanvas, float x, float y, long nativePaint);
    private static native void nDrawPoints(long nativeCanvas, int mode, float[] coords, long nativePaint);
    private static native void nDrawLine(long nativeCanvas, float x0, float y0, float x1, float y1, long nativePaint);
    private static native void nDrawArc(long nativeCanvas, float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, long nativePaint);
    private static native void nDrawRect(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawOval(long nativeCanvas, float left, float top, float right, float bottom, long nativePaint);
    private static native void nDrawRoundedRect(long nativeCanvas, float left, float top, float right, float bottom, float radii[], long nativePaint);
    private static native void nDrawDoubleRoundedRect(long nativeCanvas, float ol, float ot, float or, float ob, float oradii[], float il, float it, float ir, float ib, float iradii[], long nativePaint);
    private static native void nDrawPath(long nativeCanvas, long nativePath, long nativePaint);
    private static native void nDrawImageRect(long nativeCanvas, long nativeImage, float sl, float st, float sr, float sb, float dl, float dt, float dr, float db, long nativePaint, int 
constraint);
    private static native void nDrawImageIRect(long nativeCanvas, long nativeImage, int sl, int st, int sr, int sb, float dl, float dt, float dr, float db, long nativePaint, int 
constraint);
    private static native void nDrawRegion(long nativeCanvas, long nativeRegion, long nativePaint);
    private static native void nDrawString(long nativeCanvas, String string, float x, float y, long font, long paint);
    private static native void nDrawTextBlob(long nativeCanvas, long blob, float x, float y, long font, long paint);
    private static native void nClear(long nativeCanvas, int color);
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
