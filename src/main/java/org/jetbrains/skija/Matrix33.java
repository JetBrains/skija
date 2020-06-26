package org.jetbrains.skija;

import lombok.Data;

@Data
public class Matrix33 {
    public final float[] _mat;

    public Matrix33(float... mat) {
        assert mat.length == 9 : "Expected 9 elements, got " + mat == null ? null : mat.length;
        _mat = mat;
    }

    public static Matrix33 makeTranslate(float dx, float dy) {
        return new Matrix33(new float[] {1, 0, dx, 0, 1, dy, 0, 0, 1});
    }

    public static Matrix33 makeScale(float s) {
        return makeScale(s, s);
    }

    public static Matrix33 makeScale(float sx, float sy) {
        return new Matrix33(new float[] {sx, 0, 0, 0, sy, 0, 0, 0, 1});
    }

    public Matrix33 makePreScale(float sx, float sy) {
        return new Matrix33(new float[] {
            _mat[0] * sx, _mat[1] * sy, _mat[2],
            _mat[3] * sx, _mat[4] * sy, _mat[5],
            _mat[6] * sx, _mat[7] * sy, _mat[8]
        });
    }

    public Matrix33 makeConcat(Matrix33 m) {
        return new Matrix33(new float[] {
            _mat[0] * m._mat[0] + _mat[1] * m._mat[3] + _mat[2] * m._mat[6],
            _mat[0] * m._mat[1] + _mat[1] * m._mat[4] + _mat[2] * m._mat[7],
            _mat[0] * m._mat[2] + _mat[1] * m._mat[5] + _mat[2] * m._mat[8],
            _mat[3] * m._mat[0] + _mat[4] * m._mat[3] + _mat[5] * m._mat[6],
            _mat[3] * m._mat[1] + _mat[4] * m._mat[4] + _mat[5] * m._mat[7],
            _mat[3] * m._mat[2] + _mat[4] * m._mat[5] + _mat[5] * m._mat[8],
            _mat[6] * m._mat[0] + _mat[7] * m._mat[3] + _mat[8] * m._mat[6],
            _mat[6] * m._mat[1] + _mat[7] * m._mat[4] + _mat[8] * m._mat[7],
            _mat[6] * m._mat[2] + _mat[7] * m._mat[5] + _mat[8] * m._mat[8],
        });
    }

    public static Matrix33 makeRotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        return new Matrix33(new float[] {(float) cos, (float) -sin, 0, (float) sin, (float) cos, 0, 0, 0, 1});
    }

    public static Matrix33 makeSkew(float sx, float sy) {
        return new Matrix33(new float[]{
                1, sx, 0,
                sy, 1, 0,
                0, 0, 1
        });
    }
}