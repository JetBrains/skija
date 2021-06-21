package org.jetbrains.skija;

import lombok.Data;

@Data
public class ColorMatrix {
    public final float[] _mat;

    public ColorMatrix(float... mat) {
        assert mat.length == 20 : "Expected 20 elements, got " + mat == null ? null : mat.length;
        _mat = mat;
    }
}