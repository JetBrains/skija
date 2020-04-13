package org.jetbrains.skija;

public class Typeface {
    public final SkTypeface mSkTypeface;
    public final HBFace mHBFace;

    protected Typeface(SkTypeface skTypeface, HBFace hbFace) {
        mSkTypeface = skTypeface;
        mHBFace = hbFace;
    }

    public void release() {
        mSkTypeface.release();
        mHBFace.release();
    }

    public static Typeface makeFromFile(String path) {
        return makeFromFile(path, 0);
    }

    public static Typeface makeFromFile(String path, int index) {
        long[] ptrs = nMakeFromFile(path, index);
        if (ptrs == null)
            throw new RuntimeException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(new SkTypeface(ptrs[0]), new HBFace(ptrs[1]));
    }

    private static native long[] nMakeFromFile(String path, int index);
}