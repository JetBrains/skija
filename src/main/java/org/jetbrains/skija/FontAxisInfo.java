package org.jetbrains.skija;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FontAxisInfo {
    public static final int FLAG_HIDDEN = 1;

    public final int axisIndex;
    public final int tag;
    public final String name;
    public final int flags;
    public final float minValue;
    public final float defaultValue;
    public final float maxValue;

    public FontAxisInfo(int axisIndex, int tag, byte[] nameBytes, int flags, float minValue, float defaultValue, float maxValue) {
        this.axisIndex = axisIndex;
        this.tag = tag;
        this.name = new String(nameBytes, StandardCharsets.UTF_8);
        this.flags = flags;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
    }

    public boolean isHidden() { return (flags & FLAG_HIDDEN) != 0; }

    public String getTagName() { return FontFeature.untag(tag); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontAxisInfo that = (FontAxisInfo) o;
        return axisIndex == that.axisIndex &&
                tag == that.tag &&
                flags == that.flags &&
                Float.compare(that.minValue, minValue) == 0 &&
                Float.compare(that.defaultValue, defaultValue) == 0 &&
                Float.compare(that.maxValue, maxValue) == 0 &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(axisIndex, tag, name, flags, minValue, defaultValue, maxValue);
    }

    @Override
    public String toString() {
        return "FontAxisInfo{" +
                "axisIndex=" + axisIndex +
                ", tag=" + tag +
                ", name='" + name + '\'' +
                ", flags=" + flags +
                ", minValue=" + minValue +
                ", defaultValue=" + defaultValue +
                ", maxValue=" + maxValue +
                '}';
    }
}