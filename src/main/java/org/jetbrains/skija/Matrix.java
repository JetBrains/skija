package org.jetbrains.skija;

public class Matrix {
    public static float[] makeTranslate(float dx, float dy) { return new float[] {1, 0, dx, 0, 1, dy, 0, 0, 1}; }
    public static float[] makeScale(float s) { return makeScale(s, s); }
    public static float[] makeScale(float sx, float sy) { return new float[] {sx, 0, 0, 0, sy, 0, 0, 0, 1}; }

    public static float[] makeScale(float[] matrix, float sx, float sy) {
        return new float[] {
            matrix[0] * sx, matrix[1] * sy, matrix[2],
            matrix[3] * sx, matrix[4] * sy, matrix[5],
            matrix[6] * sx, matrix[7] * sy, matrix[8]
        };
    }

    public static float[] makeRotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        return new float[] {(float) cos, (float) -sin, 0, (float) sin, (float) cos, 0, 0, 0, 1};
    }
}