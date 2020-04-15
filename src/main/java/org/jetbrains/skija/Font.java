package org.jetbrains.skija;

public class Font {
    public final Typeface mTypeface;
    public final SkFont mSkFont;
    public final HBFont mHBFont;

    protected Font(Typeface typeface, SkFont skFont, HBFont hbFont) {
        mTypeface = typeface;
        mSkFont = skFont;
        mHBFont = hbFont;
    }

    public Font(Typeface typeface, float size, FontFeature[] features) {
        this(typeface,
             new SkFont(typeface.mSkTypeface, size),
             new HBFont(typeface.mHBFace, size, features, typeface.variations));
    }

    public Font(Typeface typeface, float size) {
        this(typeface, size, FontFeature.EMPTY);
    }

    public void release() {
        mSkFont.release();
        mHBFont.release();
    }
}