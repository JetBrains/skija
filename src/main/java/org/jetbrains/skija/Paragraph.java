package org.jetbrains.skija;

public class Paragraph extends Managed {
    public float getMaxWidth() {
        Native.onNativeCall();
        return nGetMaxWidth(nativeInstance);
    }

    public float getHeight() {
        Native.onNativeCall();
        return nGetHeight(nativeInstance);
    }

    public float getMinIntrinsicWidth() {
        Native.onNativeCall();
        return nGetMinIntrinsicWidth(nativeInstance);
    }

    public float getMaxIntrinsicWidth() {
        Native.onNativeCall();
        return nGetMaxIntrinsicWidth(nativeInstance);
    }

    public Paragraph layout(float width) {
        Native.onNativeCall();
        nLayout(nativeInstance, width);
        return this;
    }

    public Paragraph paint(Canvas canvas, float x, float y) {
        Native.onNativeCall();
        nPaint(nativeInstance, Native.pointer(canvas), x, y);
        return this;
    }

    protected Paragraph(long ptr) { super(ptr, nativeFinalizer); Native.onNativeCall(); }
    private static final  long  nativeFinalizer = nGetNativeFinalizer();
    private static native long  nGetNativeFinalizer();
    private static native float nGetMaxWidth(long ptr);
    private static native float nGetHeight(long ptr);
    private static native float nGetMinIntrinsicWidth(long ptr);
    private static native float nGetMaxIntrinsicWidth(long ptr);
    private static native void  nLayout(long ptr, float width);
    private static native long  nPaint(long ptr, long canvasPtr, float x, float y);
}