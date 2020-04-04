package skija;

public class Typeface extends RefCounted {
    public static Typeface makeFromFile(String path, int index) {
        long ptr = nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(ptr);
    }

    protected Typeface(long nativeInstance) { super(nativeInstance); }
    private static native long nMakeFromFile(String path, int index);
}