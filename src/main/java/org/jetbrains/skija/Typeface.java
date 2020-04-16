package org.jetbrains.skija;

import java.util.Arrays;

public class Typeface implements AutoCloseable {
    public final SkTypeface skTypeface;
    public final HBFace hbFace;
    public final FontVariation[] variations;

    protected Typeface(SkTypeface skTypeface, HBFace hbFace, FontVariation[] variations) {
        this.skTypeface = skTypeface;
        this.hbFace = hbFace;
        this.variations = variations;
    }

    @Override
    public void close() {
        skTypeface.close();
        hbFace.close();
    }

    public static Typeface makeFromFile(String path) {
        return makeFromFile(path, 0);
    }

    public static Typeface makeFromFile(String path, int index) {
        Native.onNativeCall(); 
        long[] ptrs = nMakeFromFile(path, index);
        if (ptrs == null)
            throw new RuntimeException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(new SkTypeface(ptrs[0]), new HBFace(ptrs[1]), FontVariation.EMPTY);
    }

    public Typeface with(FontVariation... variations) {
        FontVariation[] newVariations = Arrays.copyOf(this.variations, this.variations.length + variations.length);
        System.arraycopy(variations, 0, newVariations, this.variations.length, variations.length);
        return new Typeface(skTypeface.makeClone(variations), hbFace, newVariations);
    }

    private static native long[] nMakeFromFile(String path, int index);
}