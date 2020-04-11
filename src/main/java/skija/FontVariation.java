package skija;

import java.util.Objects;

public class FontVariation {
    public final int tag;
    public final float value;

    public FontVariation(int tag, float value) {
        this.tag = tag;
        this.value = value;
    }

    public FontVariation(String feature, float value) {
        this(FontFeature.tag(feature), value);
    }

    public String getTagName() { return FontFeature.untag(tag); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontVariation that = (FontVariation) o;
        return tag == that.tag &&
                Float.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, value);
    }
    
    public String toString() {
        return getTagName() + "=" + value;
    }
}