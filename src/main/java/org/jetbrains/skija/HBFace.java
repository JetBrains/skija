package org.jetbrains.skija;

public class HBFace extends Managed {
    protected HBFace(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    public static HBFace makeFromFile(String path, int index) {
        long ptr = nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create font path=\"" + path + "\" index=" + index);
        return new HBFace(ptr);
    }

    public FontAxisInfo[] getAxes() { return nGetAxes(mNativeInstance); }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nMakeFromFile(String path, int index);
    private static native long nGetNativeFinalizer();
    private static native FontAxisInfo[] nGetAxes(long ptr);
}