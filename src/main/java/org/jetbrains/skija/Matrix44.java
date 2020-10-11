package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

@Data
public class Matrix44 {
    @ApiStatus.Internal
    public final float[] _mat;

    public Matrix44(float... mat) {
        assert mat.length == 16 : "Expected 16 elements, got " + mat == null ? null : mat.length;
        _mat = mat;
    }

    public static final Matrix44 IDENTITY = new Matrix44(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    );
}