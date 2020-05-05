package org.jetbrains.skija;

public class Image extends RefCounted {
    private int width = -1;
    private int height = -1;

    public static Image fromEncoded(byte[] bytes) {
        return fromEncoded(bytes, null);
    }

    public static Image fromEncoded(byte[] bytes, IRect subset) {
        Native.onNativeCall();
        return new Image(nFromEncoded(bytes, subset));
    }

    public int getWidth() {
        if (width == -1) dimensions();
        return width;
    }

    public int getHeight() {
        if (height == -1) dimensions();
        return height;
    }

    protected void dimensions() {
        Native.onNativeCall();
        long res = nDimensions(nativeInstance);
        width = (int) (res & 0xFFFFFFFF);
        height = (int) (res >>> 32);
    }

    protected Image(long nativeInstance) { super(nativeInstance); }
    private static native long nFromEncoded(byte[] bytes, IRect subset);
    private static native long nDimensions(long nativeInstance);
}