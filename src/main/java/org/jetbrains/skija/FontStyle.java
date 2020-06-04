package org.jetbrains.skija;

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

    protected final int value;

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
        value = (weight & 0xFFFF) | ((width & 0xFF) << 16) | (slant.ordinal() << 24);
    }

    protected FontStyle(int value) {
        this.value = value;
    }

    public int getWeight() {
        return value & 0xFFFF;
    }

    public FontStyle withWeight(int weight) {
        return new FontStyle(weight, getWidth(), getSlant());
    }

    public int getWidth() {
        return (value >> 16) & 0xFF;
    }

    public FontStyle withWidth(int width) {
        return new FontStyle(getWeight(), width, getSlant());
    }

    public Slant getSlant() {
        return Slant.values()[(value >> 24) & 0xFF];
    }

    public FontStyle withSlant(Slant slant) {
        return new FontStyle(getWeight(), getWidth(), slant);
    }
}