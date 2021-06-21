package org.jetbrains.skija;

import lombok.*;

@AllArgsConstructor
@lombok.Data
@With
public class Color4f {
    public final float _r;
    public final float _g;
    public final float _b;
    public final float _a;

    public Color4f(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    public Color4f(float[] rgba) {
        this(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public Color4f(int c) {
        this((c >> 16 & 0xFF) / 255f,
             (c >> 8 & 0xFF) / 255f,
             (c & 0xFF) / 255f,
             (c >> 24 & 0xFF) / 255f);
    }

    public int toColor() {
        return ((Math.round(_a * 255f)) << 24)
          | ((Math.round(_r * 255f)) << 16)
          | ((Math.round(_g * 255f)) << 8)
          | (Math.round(_b * 255f));
    }

    public float[] flatten() {
        return new float[] {_r, _g, _b, _a};
    }

    public static float[] flattenArray(Color4f[] colors) {
        float[] arr = new float[colors.length * 4];
        for (int i = 0; i < colors.length; ++i) {
            arr[i * 4]     = colors[i]._r;
            arr[i * 4 + 1] = colors[i]._g;
            arr[i * 4 + 2] = colors[i]._b;
            arr[i * 4 + 3] = colors[i]._a;
        }
        return arr;
    }

    // TODO premultiply alpha
    public Color4f makeLerp(Color4f other, float weight) {
        return new Color4f(_r + (other._r - _r) * weight,
                           _g + (other._g - _g) * weight,
                           _b + (other._b - _b) * weight,
                           _a + (other._a - _a) * weight);
    }
}