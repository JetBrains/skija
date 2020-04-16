package org.jetbrains.skija;

public class Font implements AutoCloseable {
    public final Typeface typeface;
    public final SkFont skFont;
    public final HBFont hbFont;

    protected Font(Typeface typeface, SkFont skFont, HBFont hbFont) {
        this.typeface = typeface;
        this.skFont = skFont;
        this.hbFont = hbFont;
    }

    public Font(Typeface typeface, float size, FontFeature... features) {
        this(typeface,
             new SkFont(typeface.skTypeface, size),
             new HBFont(typeface.hbFace, size, features, typeface.variations));
    }

    public Font(Typeface typeface, float size) {
        this(typeface, size, FontFeature.EMPTY);
    }

    @Override
    public void close() {
        skFont.close();
        hbFont.close();
    }
}