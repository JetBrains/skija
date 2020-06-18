package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import org.jetbrains.skija.impl.Internal;

@EqualsAndHashCode
public class FontStyle {
    public static final int WEIGHT_INVISIBLE   = 0;
    public static final int WEIGHT_THIN        = 100;
    public static final int WEIGHT_EXTRA_LIGHT = 200;
    public static final int WEIGHT_LIGHT       = 300;
    public static final int WEIGHT_NORMAL      = 400;
    public static final int WEIGHT_MEDIUM      = 500;
    public static final int WEIGHT_SEMI_BOLD   = 600;
    public static final int WEIGHT_BOLD        = 700;
    public static final int WEIGHT_EXTRA_BOLD  = 800;
    public static final int WEIGHT_BLACK       = 900;
    public static final int WEIGHT_EXTRA_BLACK = 1000;

    public static final int WIDTH_ULTRA_CONDENSED = 1;
    public static final int WIDTH_EXTRA_CONDENSED = 2;
    public static final int WIDTH_CONDENSED       = 3;
    public static final int WIDTH_SEMI_CONDENSED  = 4;
    public static final int WIDTH_NORMAL          = 5;
    public static final int WIDTH_SEMI_EXPANDED   = 6;
    public static final int WIDTH_EXPANDED        = 7;
    public static final int WIDTH_EXTRA_EXPANDED  = 8;
    public static final int WIDTH_ULTRA_EXPANDED  = 9;

    public final int _value;

    public enum Slant {
        UPRIGHT,
        ITALIC,
        OBLIQUE
    }

    public static final FontStyle NORMAL = new FontStyle(WEIGHT_NORMAL, WIDTH_NORMAL, Slant.UPRIGHT);
    public static final FontStyle BOLD = new FontStyle(WEIGHT_BOLD, WIDTH_NORMAL, Slant.UPRIGHT);
    public static final FontStyle ITALIC = new FontStyle(WEIGHT_NORMAL, WIDTH_NORMAL, Slant.ITALIC);
    public static final FontStyle BOLD_ITALIC = new FontStyle(WEIGHT_BOLD, WIDTH_NORMAL, Slant.ITALIC);

    public FontStyle(int weight, int width, Slant slant) {
        _value = (weight & 0xFFFF) | ((width & 0xFF) << 16) | (slant.ordinal() << 24);
    }

    @Internal
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

    public Slant getSlant() {
        return Slant.values()[(_value >> 24) & 0xFF];
    }

    public FontStyle withSlant(Slant slant) {
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