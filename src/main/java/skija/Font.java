package skija;

public class Font {
    public final SkFont mSkFont;
    public final HBFont mHBFont;
    public final Typeface mTypeface;

    protected Font(SkFont skFont, HBFont hbFont) {
        mSkFont = skFont;
        mHBFont = hbFont;
        mTypeface = new Typeface(skFont.typeface, hbFont.mFace);
    }

    public Font(Typeface typeface, float size, FontFeature[] features, FontVariation[] variations) {
        this(new SkFont(typeface.mSkTypeface.makeClone(variations), size),
             new HBFont(typeface.mHBFace, size, features, variations));
    }

    public Font(Typeface typeface, float size, FontFeature[] features) {
        this(typeface, size, features, HBFont.NO_VARIATIONS);
    }

    public Font(Typeface typeface, float size, FontVariation[] variations) {
        this(typeface, size, HBFont.NO_FEATURES, variations);
    }

    public Font(Typeface typeface, float size) {
        this(typeface, size, HBFont.NO_FEATURES, HBFont.NO_VARIATIONS);
    }

    public void release() {
        mSkFont.release();
        mHBFont.release();
    }
}