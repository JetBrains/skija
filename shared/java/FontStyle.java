package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.*;

@EqualsAndHashCode
public class FontStyle {
    public final int _value;

    public static final FontStyle NORMAL = new FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.UPRIGHT);
    public static final FontStyle BOLD = new FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.UPRIGHT);
    public static final FontStyle ITALIC = new FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.ITALIC);
    public static final FontStyle BOLD_ITALIC = new FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.ITALIC);

    public FontStyle(int weight, int width, FontSlant slant) {
        _value = (weight & 0xFFFF) | ((width & 0xFF) << 16) | (slant.ordinal() << 24);
    }

    @ApiStatus.Internal
    public FontStyle(int value) {
        this._value = value;
    }

    public int getWeight() {
        return _value & 0xFFFF;
    }

    public FontStyle withWeight(int weight) {
        return new FontStyle(weight, getWidth(), getSlant());
    }

    public int getWidth() {
        return (_value >> 16) & 0xFF;
    }

    public FontStyle withWidth(int width) {
        return new FontStyle(getWeight(), width, getSlant());
    }

    public FontSlant getSlant() {
        return FontSlant._values[(_value >> 24) & 0xFF];
    }

    public FontStyle withSlant(FontSlant slant) {
        return new FontStyle(getWeight(), getWidth(), slant);
    }

    @Override
    public String toString() {
        return "FontStyle(" +
                "weight=" + getWeight() +
                ", width=" + getWidth() +
                ", slant='" + getSlant() + ')';
    }
}