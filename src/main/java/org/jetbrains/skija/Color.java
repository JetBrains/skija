package org.jetbrains.skija;

public class Color {
    // TODO premultiply
    // TODO alpha
    public static int makeLerp(int c1, int c2, float weight) {
        int r1 = (c1 & 0xFF0000) >> 16;
        int r2 = (c2 & 0xFF0000) >> 16;
        int r = (int) (r1 * weight + r2 * (1 - weight));

        int g1 = (c1 & 0x00FF00) >> 8;
        int g2 = (c2 & 0x00FF00) >> 8;
        int g = (int) (g1 * weight + g2 * (1 - weight));

        int b1 = c1 & 0x0000FF;
        int b2 = c2 & 0x0000FF;
        int b = (int) (b1 * weight + b2 * (1 - weight));

        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0xFFFFFF);
    }
}