package skija;

public class Font {
    public final SkFont mSkFont;
    public final HBFont mHBFont;
    public final Typeface mTypeface;

    protected Font(Typeface typeface, SkFont skFont, HBFont hbFont) {
        mTypeface = typeface;
        mSkFont = skFont;
        mHBFont = hbFont;
    }

    public Font(Typeface typeface, float size, FontFeature[] features) {
        this(typeface, new SkFont(typeface.mSkTypeface, size), new HBFont(typeface.mHBFace, size, features));
    }

    public Font(Typeface typeface, float size) {
        this(typeface, size, HBFont.NO_FEATURES);
    }

    public FontExtents getHorizontalExtents() { return mHBFont.getHorizontalExtents(); }
    public FontExtents getVerticalExtents() { return mHBFont.getVerticalExtents(); }
    public TextBuffer shape(String text) { return mHBFont.shape(text); }
    public TextBuffer shape(String text, FontFeature[] features) { return mHBFont.shape(text, features); }

    public void release() {
        mSkFont.release();
        mHBFont.release();
    }
}