package org.jetbrains.skija;

import java.util.Objects;

public class Color4f {
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color4f(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1f;
    }

    public Color4f(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color4f(int c) {
        this.a = (c >> 24 & 0xFF) / 256f;
        this.r = (c >> 16 & 0xFF) / 256f;
        this.g = (c >> 8 & 0xFF) / 256f;
        this.b = (c & 0xFF) / 256f;
    }

    public static float[] flattenArray(Color4f[] colors) {
        float[] arr = new float[colors.length * 4];
        for (int i = 0; i < colors.length; ++i) {
            arr[i * 4]     = colors[i].r;
            arr[i * 4 + 1] = colors[i].g;
            arr[i * 4 + 2] = colors[i].b;
            arr[i * 4 + 3] = colors[i].a;
        }
        return arr;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color4f color4f = (Color4f) o;
        return Float.compare(color4f.r, r) == 0 &&
                Float.compare(color4f.g, g) == 0 &&
                Float.compare(color4f.b, b) == 0 &&
                Float.compare(color4f.a, a) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString() {
        return "Color4f{r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + '}';
    }
}