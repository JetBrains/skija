package org.jetbrains.skija;

public class Typeface extends RefCounted {
    public static Typeface makeFromFile(String path) {
        return makeFromFile(path, 0);
    }

    public static Typeface makeFromFile(String path, int index) {
        Native.onNativeCall(); 
        long ptr = nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(ptr);
    }

    public Typeface makeClone(FontVariation[] variations) {
        if (variations.length == 0)
            return this;
        Native.onNativeCall(); 
        return new Typeface(nMakeClone(nativeInstance, variations));
    }

    public Typeface(long nativeInstance) { super(nativeInstance); }
    private static native long nMakeFromFile(String path, int index);
    private static native long nMakeClone(long ptr, FontVariation[] variations);
}