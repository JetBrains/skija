package org.jetbrains.skija;

public class Color {
    // TODO premultiply, alpha
    public static int makeLerp(int c1, int c2, float weight) {
        int r = (int) (getR(c1) * weight + getR(c2) * (1 - weight));
        int g = (int) (getG(c1) * weight + getG(c2) * (1 - weight));
        int b = (int) (getB(c1) * weight + getB(c2) * (1 - weight));
        return makeRGB(r, g, b);
    }

    public static int makeARGB(int a, int r, int g, int b) {
        assert 0 <= a && a <= 255 : "Alpha is out of 0..255 range: " + a;
        assert 0 <= r && r <= 255 : "Red is out of 0..255 range: " + r;
        assert 0 <= g && g <= 255 : "Green is out of 0..255 range: " + g;
        assert 0 <= b && b <= 255 : "Blue is out of 0..255 range: " + b;
        return ((a & 0xFF) << 24)
            | ((r & 0xFF) << 16)
            | ((g & 0xFF) << 8)
            | (b & 0xFF);
    }

    public static int makeRGB(int r, int g, int b) {
        return makeARGB(255, r, g, b);
    }

    public static int getA(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int getR(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int getG(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int getB(int color) {
        return color & 0xFF;
    }    

    public static int withA(int color, int a) {
        assert 0 <= a && a <= 255 : "Alpha is out of 0..255 range: " + a;
        return ((a & 0xFF) << 24) | (color & 0x00FFFFFF);
    }

    public static int withR(int color, int r) {
        assert 0 <= r && r <= 255 : "Red is out of 0..255 range: " + r;
        return ((r & 0xFF) << 16) | (color & 0xFF00FFFF);
    }

    public static int withG(int color, int g) {
        assert 0 <= g && g <= 255 : "Green is out of 0..255 range: " + g;
        return ((g & 0xFF) << 8) | (color & 0xFFFF00FF);
    }

    public static int withB(int color, int b) {
        assert 0 <= b && b <= 255 : "Blue is out of 0..255 range: " + b;
        return (b & 0xFF) | (color & 0xFFFFFF00);
    }
}