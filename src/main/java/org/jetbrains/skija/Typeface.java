package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class Typeface extends RefCnt {
    public static Typeface makeFromFile(String path) {
        return makeFromFile(path, 0);
    }

    public static Typeface makeFromFile(String path, int index) {
        Stats.onNativeCall();
        long ptr = _nMakeFromFile(path, index);
        if (ptr == 0)
            throw new RuntimeException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(ptr);
    }

    public Typeface makeClone(FontVariation[] variations) {
        if (variations.length == 0)
            return this;
        Stats.onNativeCall();
        return new Typeface(_nMakeClone(_ptr, variations));
    }

    @ApiStatus.Internal
    public Typeface(long ptr) {
        super(ptr);
    }

    public static native long _nMakeFromFile(String path, int index);
    public static native long _nMakeClone(long ptr, FontVariation[] variations);
}