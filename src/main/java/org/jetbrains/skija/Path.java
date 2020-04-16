package org.jetbrains.skija;

public class Path extends Managed {
    public enum FillType { WINDING, EVEN_ODD, INVERSE_WINDING, INVERSE_EVEN_ODD }
    public enum Direction { CLOCKWISE, COUNTER_CLOCKWISE }
    public enum ArcSize { SMALL, LARGE }

    public Path() { super(nInit(), nativeFinalizer); Native.onNativeCall(); }

    public FillType getFillType() { Native.onNativeCall(); return FillType.values()[nGetFillType(nativeInstance)]; }
    public Path setFillType(FillType fillType) { Native.onNativeCall(); nSetFillType(nativeInstance, fillType.ordinal()); return this; }
    public Rect getBounds() { Native.onNativeCall(); float[] b = nGetBounds(nativeInstance); return Rect.makeLTRB(b[0], b[1], b[2], b[3]); }
    public Rect computeTightBounds() { Native.onNativeCall(); float[] b = nComputeTightBounds(nativeInstance); return Rect.makeLTRB(b[0], b[1], b[2], b[3]); }
    public Path reset() { Native.onNativeCall(); nReset(nativeInstance); return this; }
    public Path moveTo(float x, float y) { Native.onNativeCall(); nMoveTo(nativeInstance, x, y); return this; }
    public Path rMoveTo(float dx, float dy) { Native.onNativeCall(); nRMoveTo(nativeInstance, dx, dy); return this; }
    public Path lineTo(float x, float y) { Native.onNativeCall(); nLineTo(nativeInstance, x, y); return this; }
    public Path rLineTo(float dx, float dy) { Native.onNativeCall(); nRLineTo(nativeInstance, dx, dy); return this;}
    public Path closePath() { Native.onNativeCall(); nClosePath(nativeInstance); return this; }
    public Path quadTo(float x1, float y1, float x2, float y2) { Native.onNativeCall(); nQuadTo(nativeInstance, x1, y1, x2, y2); return this; }
    public Path rQuadTo(float dx1, float dy1, float dx2, float dy2) { Native.onNativeCall(); nRQuadTo(nativeInstance, dx1, dy1, dx2, dy2); return this; }
    public Path conicTo(float x1, float y1, float x2, float y2, float w) { Native.onNativeCall(); nConicTo(nativeInstance, x1, y1, x2, y2, w); return this; }
    public Path rConicTo(float dx1, float dy1, float dx2, float dy2, float w) { Native.onNativeCall(); nRConicTo(nativeInstance, dx1, dy1, dx2, dy2, w); return this; }
    public Path cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) { Native.onNativeCall(); nCubicTo(nativeInstance, x1, y1, x2, y2, x3, y3); return this; }
    public Path rCubicTo(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) { Native.onNativeCall(); nRCubicTo(nativeInstance, dx1, dy1, dx2, dy2, dx3, dy3); return this; }
    public Path arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) {
        Native.onNativeCall(); 
        nArcTo(nativeInstance, left, top, right, bottom, startAngle, sweepAngle, forceMoveTo);
        return this;
    }
    public Path tangentArcTo(float x1, float y1, float x2, float y2, float radius) { Native.onNativeCall(); nTangentArcTo(nativeInstance, x1, y1, x2, y2, radius); return this; }
    public Path ellipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float x, float y) {
        Native.onNativeCall(); 
        nEllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), x, y);
        return this;
    }

    public Path rEllipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float dx, float dy) {
        Native.onNativeCall(); 
        nREllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), dx, dy);
        return this;
    }

    public Path addPoly(float[] coords, boolean close) { Native.onNativeCall(); nAddPoly(nativeInstance, coords, close); return this; }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit();
    private static native long nGetNativeFinalizer();
    private static native int nGetFillType(long nativeInstance);
    private static native void nSetFillType(long nativeInstance, int fillType);
    private static native float[] nGetBounds(long nativeInstance);
    private static native float[] nComputeTightBounds(long nativeInstance);
    private static native void nReset(long nativeInstance);
    private static native void nMoveTo(long nativeInstance, float x, float y);
    private static native void nRMoveTo(long nativeInstance, float dx, float dy);
    private static native void nLineTo(long nativeInstance, float x, float y);
    private static native void nRLineTo(long nativeInstance, float dx, float dy);
    private static native void nQuadTo(long nativeInstance, float x1, float y1, float x2, float y2);
    private static native void nRQuadTo(long nativeInstance, float dx1, float dy1, float dx2, float dy2);
    private static native void nConicTo(long nativeInstance, float x1, float y1, float x2, float y2, float w);
    private static native void nRConicTo(long nativeInstance, float dx1, float dy1, float dx2, float dy2, float w);
    private static native void nCubicTo(long nativeInstance, float x1, float y1, float x2, float y2, float x3, float y3);
    private static native void nRCubicTo(long nativeInstance, float dx1, float dy1, float dx2, float dy2, float dx3, float dy3);
    private static native void nArcTo(long nativeInstance, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo);
    private static native void nTangentArcTo(long nativeInstance, float x1, float y1, float x2, float y2, float radius);
    private static native void nEllipticalArcTo(long nativeInstance, float rx, float ry, float xAxisRotate, int size, int direction, float x, float y);
    private static native void nREllipticalArcTo(long nativeInstance, float rx, float ry, float xAxisRotate, int size, int direction, float dx, float dy);
    private static native void nAddPoly(long nativeInstance, float[] coords, boolean close);
    private static native void nClosePath(long nativeInstance);
}