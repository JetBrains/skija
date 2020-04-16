package org.jetbrains.skija;

public class SkTypeface extends RefCounted {
    public static SkTypeface makeFromFile(String path, int index) {
        Native.onNativeCall(); 
        long ptr = nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create SkTypeface from path=\"" + path + "\" index=" + index);
        return new SkTypeface(ptr);
    }

    public SkTypeface makeClone(FontVariation[] variations) {
        if (variations.length == 0)
            return this;
        Native.onNativeCall(); 
        return new SkTypeface(nMakeClone(nativeInstance, variations));
    }

    protected SkTypeface(long nativeInstance) { super(nativeInstance); }
    private static native long nMakeFromFile(String path, int index);
    private static native long nMakeClone(long ptr, FontVariation[] variations);
}