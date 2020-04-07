package skija;

public class FontExtents {
    public final float ascender;
    public final float descender;
    public final float lineGap;

    public FontExtents(float ascender, float descender, float lineGap) {
        this.ascender = ascender;
        this.descender = descender;
        this.lineGap = lineGap;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || FontExtents.class != object.getClass()) return false;
        FontExtents that = (FontExtents) object;
        return java.lang.Float.compare(that.ascender, ascender) == 0 &&
                java.lang.Float.compare(that.descender, descender) == 0 &&
                java.lang.Float.compare(that.lineGap, lineGap) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(ascender, descender, lineGap);
    }

    @Override
    public java.lang.String toString() {
        return "FontExtents{ascender=" + ascender + ", descender=" + descender + ", lineGap=" + lineGap + '}';
    }
}
