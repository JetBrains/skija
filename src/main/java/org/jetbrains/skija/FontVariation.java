package org.jetbrains.skija;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class FontVariation {
    public static final FontVariation[] EMPTY = new FontVariation[0];

    public final int _tag;
    @Getter
    public final float _value;

    public FontVariation(String feature, float value) {
        this(FontFeature.tag(feature), value);
    }

    public String getTag() {
        return FontFeature.untag(_tag);
    }

    public String toString() {
        return getTag() + "=" + _value;
    }
}