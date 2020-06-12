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
        Stats.onNativeCall();
        nDrawPoint(_ptr, x, y, paint._ptr);
    }

    public void drawPoints(PointMode mode, float[] coords, Paint paint) {
        Stats.onNativeCall();
        nDrawPoints(_ptr, mode.ordinal(), coords, paint._ptr);
    }

    public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        Stats.onNativeCall();
        nDrawLine(_ptr, x0, y0, x1, y1, paint._ptr);
    }

    public void drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        Stats.onNativeCall();
        nDrawArc(_ptr, left, top, width, height, startAngle, sweepAngle, includeCenter, paint._ptr);
    }

    public void drawRect(Rect r, Paint paint) {
        Stats.onNativeCall();
        nDrawRect(_ptr, r.left, r.top, r.right, r.bottom, paint._ptr);
    }

    public void drawOval(Rect r, Paint paint) {
        Stats.onNativeCall();
        nDrawOval(_ptr, r.left, r.top, r.right, r.bottom, paint._ptr);
    }

    public void drawCircle(float x, float y, float radius, Paint paint) {
        Stats.onNativeCall();
        nDrawOval(_ptr, x - radius, y - radius, x + radius, y + radius, paint._ptr);
    }

    public void drawRoundedRect(RoundedRect r, Paint paint) {
        Stats.onNativeCall();
        nDrawRoundedRect(_ptr, r.left, r.top, r.right, r.bottom, r.radii, paint._ptr);
    }

    public void drawDoubleRoundedRect(RoundedRect outer, RoundedRect inner, Paint paint) {
        Stats.onNativeCall();
        nDrawDoubleRoundedRect(_ptr, outer.left, outer.top, outer.right, outer.bottom, outer.radii, inner.left, inner.top, inner.right, inner.bottom, inner.radii, paint._ptr);
    }

    public void drawPath(Path path, Paint paint) {
        Stats.onNativeCall();
        nDrawPath(_ptr, path._ptr, paint._ptr);
    }

    public void drawImage(Image image, float left, float top) {
        drawImage(image, left, top, null);
    }

    public void drawImage(Image image, float left, float top, Paint paint) {
        Stats.onNativeCall();
        nDrawImageIRect(_ptr, Native.getPtr(image), 0, 0, image.getWidth(), image.getHeight(), left, top, image.getWidth(), image.getHeight(), Native.getPtr(paint), SrcRectConstraint.STRICT.ordinal());
    }

    public void drawImageRect(Image image, Rect dst) {
        drawImageRect(image, dst, null);
    }
    
    public void drawImageRect(Image image, Rect dst, Paint paint) {
        Stats.onNativeCall();
        nDrawImageIRect(_ptr, Native.getPtr(image), 0, 0, image.getWidth(), image.getHeight(), dst.left, dst.top, dst.right, dst.bottom, Native.getPtr(paint), SrcRectConstraint.STRICT.ordinal());
    }

    public void drawImageRect(Image image, Rect src, Rect dst, Paint paint) {
        drawImageRect(image, src, dst, paint, SrcRectConstraint.STRICT);
    }

    public void drawImageRect(Image image, Rect src, Rect dst, Paint paint, SrcRectConstraint constraint) {
        Stats.onNativeCall();
        nDrawImageRect(_ptr, Native.getPtr(image), src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, Native.getPtr(paint), constraint.ordinal());
    }

    public void drawImageRect(Image image, IRect src, Rect dst) {
        drawImageRect(image, src, dst, null);
    }

    public void drawImageRect(Image image, IRect src, Rect dst, Paint paint) {
        drawImageRect(image, src, dst, paint, SrcRectConstraint.STRICT);
    }

    public void drawImageRect(Image image, IRect src, Rect dst, Paint paint, SrcRectConstraint constraint) {
        Stats.onNativeCall();
        nDrawImageIRect(_ptr, Native.getPtr(image), src.left, src.top, src.right, src.bottom, dst.left, dst.top, dst.right, dst.bottom, Native.getPtr(paint), constraint.ordinal());
    }

    public void drawRegion(Region r, Paint paint) {
        Stats.onNativeCall();
        nDrawRegion(_ptr, r._ptr, paint._ptr);
    }

    public void drawString(String s, float x, float y, Font font, Paint paint) {
        Stats.onNativeCall();
        nDrawString(_ptr, s, x, y, Native.getPtr(font), Native.getPtr(paint));
    }

    public void drawTextBlob(TextBlob blob, float x, float y, Font font, Paint paint) {
        Stats.onNativeCall();
        nDrawTextBlob(_ptr, blob._ptr, x, y, font._ptr, paint._ptr);
    }

    public void clear(int color) { Stats.onNativeCall(); nClear(_ptr, color); }
    public void drawPaint(Paint paint) { Stats.onNativeCall(); nDrawPaint(_ptr, paint._ptr); }

    public void clipRect(Rect r, ClipOp op, boolean antiAlias) { Stats.onNativeCall(); nClipRect(_ptr, r.left, r.top, r.right, r.bottom, op.ordinal(), antiAlias); }
    public void clipRect(Rect r, ClipOp op) { clipRect(r, op, false); }
    public void clipRect(Rect r, boolean antiAlias) { clipRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRect(Rect r) { clipRect(r, ClipOp.INTERSECT, false); }

    public void clipRoundedRect(RoundedRect r, ClipOp op, boolean antiAlias) { Stats.onNativeCall(); nClipRoundedRect(_ptr, r.left, r.top, r.right, r.bottom, r.radii, op.ordinal(), antiAlias); }
    public void clipRoundedRect(RoundedRect r, ClipOp op) { clipRoundedRect(r, op, false); }
    public void clipRoundedRect(RoundedRect r, boolean antiAlias) { clipRoundedRect(r, ClipOp.INTERSECT, antiAlias); }
    public void clipRoundedRect(RoundedRect r) { clipRoundedRect(r, ClipOp.INTERSECT, false); }

    public void clipPath(Path p, ClipOp op, boolean antiAlias) { Stats.onNativeCall(); nClipPath(_ptr, p._ptr, op.ordinal(), antiAlias); }
    public void clipPath(Path p, ClipOp op) { clipPath(p, op, false); }
    public void clipPath(Path p, boolean antiAlias) { clipPath(p, ClipOp.INTERSECT, antiAlias); }
    public void clipPath(Path p) { clipPath(p, ClipOp.INTERSECT, false); }

    public void clipRegion(Region r, ClipOp op) { Stats.onNativeCall(); nClipRegion(_ptr, r._ptr, op.ordinal()); }
    public void clipRegion(Region r) { clipRegion(r, ClipOp.INTERSECT); }

    public void translate(float dx, float dy) { Stats.onNativeCall(); nConcat(_ptr, 1, 0, dx, 0, 1, dy, 0, 0, 1); }
    public void scale(float sx, float sy) { Stats.onNativeCall(); nConcat(_ptr, sx, 0, 0, 0, sy, 0, 0, 0, 1); }
    public void rotate(float deg) { concat(Matrix.rotate(deg)); }
    public void concat(float[] matrix) {
        assert matrix.length == 9 : "Expected 9 elements in matrix, got " + matrix == null ? null : matrix.length;
        Stats.onNativeCall();
        nConcat(_ptr, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8]);
    }

    public int save() { Stats.onNativeCall(); return nSave(_ptr); }
    public int getSaveCount() { Stats.onNativeCall(); return nGetSaveCount(_ptr); }
    public void restore() { Stats.onNativeCall(); nRestore(_ptr); }
    public void restoreToCount(int saveCount) { Stats.onNativeCall(); nRestoreToCount(_ptr, saveCount); }

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
