package org.jetbrains.skija;

public class Image extends RefCnt {
    private int width = -1;
    private int height = -1;

    public static Image fromEncoded(byte[] bytes) {
        return fromEncoded(bytes, null);
    }

    public static Image fromEncoded(byte[] bytes, IRect subset) {
        Stats.onNativeCall();
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
        Stats.onNativeCall();
        long res = nDimensions(_ptr);
        width = (int) (res & 0xFFFFFFFF);
        height = (int) (res >>> 32);
    }

    protected Image(long nativeInstance) { super(nativeInstance); }
    private static native long nFromEncoded(byte[] bytes, IRect subset);
    private static native long nDimensions(long nativeInstance);
}