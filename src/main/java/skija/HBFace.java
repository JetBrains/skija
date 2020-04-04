package skija;

public class HBFace extends Managed {
    protected HBFace(long nativeInstance) { super(nativeInstance, kNativeFinalizer); }

    public static HBFace makeFromFile(String path, int index) {
        long ptr = nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create font path=\"" + path + "\" index=" + index);
        return new HBFace(ptr);
    }

    public HBBuffer shape(String text, float size, String options) {
        return new HBBuffer(nShape(mNativeInstance, text, size, options));
    }

    private static long kNativeFinalizer = nGetNativeFinalizer();
    private static native long nMakeFromFile(String path, int index);
    private static native long nGetNativeFinalizer();

    private static native long nShape(long nativeInstance, String text, float size, String options);
}