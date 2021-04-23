package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

/** 
  * <p>Matrix holds a 3x3 matrix for transforming coordinates. This allows mapping
  * Point and vectors with translation, scaling, skewing, rotation, and
  * perspective.</p>
  * 
  * <p>Matrix includes a hidden variable that classifies the type of matrix to
  * improve performance. Matrix is not thread safe unless getType() is called first.</p>
  * 
  * @see <a href="https://fiddle.skia.org/c/@Matrix_063">https://fiddle.skia.org/c/@Matrix_063</a>
  */
@Data
public class Matrix33 {
    /**
     * <p>Matrix33 elements are in row-major order.</p>
     * 
     * <pre><code>
     * | scaleX   skewX  transX |
     * |  skewY  scaleY  transY |
     * | persp0  persp1  persp2 |
     * </code></pre>
     */
    @ApiStatus.Internal
    public final float[] _mat;

    public Matrix33(float... mat) {
        assert mat.length == 9 : "Expected 9 elements, got " + mat == null ? null : mat.length;
        _mat = mat;
    }

    /**
     * An identity Matrix33:
     *
     * <pre><code>
     * | 1 0 0 |
     * | 0 1 0 |
     * | 0 0 1 |
     * </code></pre>
     */
    @NotNull
    public static final Matrix33 IDENTITY = makeTranslate(0, 0);

    /**
     * <p>Creates a Matrix33 to translate by (dx, dy). Returned matrix is:</p>
     *
     * <pre><code>
     * | 1 0 dx |
     * | 0 1 dy |
     * | 0 0  1 |
     * </code></pre>
     *
     * @param dx  horizontal translation
     * @param dy  vertical translation
     * @return    Matrix33 with translation
    */
    @NotNull @Contract("_, _ -> new")
    public static Matrix33 makeTranslate(float dx, float dy) {
        return new Matrix33(new float[] {1, 0, dx, 0, 1, dy, 0, 0, 1});
    }

    /**
     * <p>Creates a Matrix33 to scale by s. Returned matrix is:</p>
     *
     * <pre><code>
     * | s 0 0 |
     * | 0 s 0 |
     * | 0 0 1 |
     * </code></pre>
     *
     * @param s  scale factor
     * @return   Matrix33 with scale
     */
    @NotNull
    public static Matrix33 makeScale(float s) {
        return makeScale(s, s);
    }

    /**
     * <p>Creates a Matrix33 to scale by (sx, sy). Returned matrix is:</p>
     *
     * <pre> <code>
     * | sx  0  0 |
     * |  0 sy  0 |
     * |  0  0  1 |
     * </code></pre>
     *
     * @param sx horizontal scale factor
     * @param sy vertical scale factor
     * @return   Matrix33 with scale
     */
    @NotNull
    public static Matrix33 makeScale(float sx, float sy) {
        return new Matrix33(new float[] {sx, 0, 0, 0, sy, 0, 0, 0, 1});
    }

    @NotNull
    public Matrix33 makePreScale(float sx, float sy) {
        return new Matrix33(new float[] {
            _mat[0] * sx, _mat[1] * sy, _mat[2],
            _mat[3] * sx, _mat[4] * sy, _mat[5],
            _mat[6] * sx, _mat[7] * sy, _mat[8]
        });
    }

    /**
     * <p>Creates Matrix33 by multiplying this by other. This can be thought of mapping by other before applying Matrix.</p>
     * 
     * <p>Given:</p>
     * 
     * <pre><code>
     *        | A B C |          | J K L |
     * this = | D E F |, other = | M N O |
     *        | G H I |          | P Q R |
     * </code></pre>
     * 
     * <p>Returns:</p>
     *
     * <pre><code> 
     *                | A B C |   | J K L |   | AJ+BM+CP AK+BN+CQ AL+BO+CR |
     * this * other = | D E F | * | M N O | = | DJ+EM+FP DK+EN+FQ DL+EO+FR |
     *                | G H I |   | P Q R |   | GJ+HM+IP GK+HN+IQ GL+HO+IR |
     * </code></pre>
     * 
     * @param other  Matrix on right side of multiply expression
     * @return       this multiplied by other
     */
    @NotNull
    public Matrix33 makeConcat(Matrix33 other) {
        return new Matrix33(new float[] {
            _mat[0] * other._mat[0] + _mat[1] * other._mat[3] + _mat[2] * other._mat[6],
            _mat[0] * other._mat[1] + _mat[1] * other._mat[4] + _mat[2] * other._mat[7],
            _mat[0] * other._mat[2] + _mat[1] * other._mat[5] + _mat[2] * other._mat[8],
            _mat[3] * other._mat[0] + _mat[4] * other._mat[3] + _mat[5] * other._mat[6],
            _mat[3] * other._mat[1] + _mat[4] * other._mat[4] + _mat[5] * other._mat[7],
            _mat[3] * other._mat[2] + _mat[4] * other._mat[5] + _mat[5] * other._mat[8],
            _mat[6] * other._mat[0] + _mat[7] * other._mat[3] + _mat[8] * other._mat[6],
            _mat[6] * other._mat[1] + _mat[7] * other._mat[4] + _mat[8] * other._mat[7],
            _mat[6] * other._mat[2] + _mat[7] * other._mat[5] + _mat[8] * other._mat[8],
        });
    }

    /**
     * Creates a Matrix33 to rotate by |deg| about a pivot point at (0, 0).
     *
     * @param deg  rotation angle in degrees (positive rotates clockwise)
     * @return     Matrix33 with rotation
     */
    @NotNull
    public static Matrix33 makeRotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        return new Matrix33(new float[] {
            (float) cos, (float) -sin, 0,
            (float) sin, (float) cos, 0,
            0, 0, 1
        });
    }

    /**
     * Creates a Matrix33 to rotate by |deg| about a pivot point at pivot.
     *
     * @param deg    rotation angle in degrees (positive rotates clockwise)
     * @param pivot  pivot point
     * @return       Matrix33 with rotation
     */
    @NotNull
    public static Matrix33 makeRotate(float deg, Point pivot) {
        return makeRotate(deg, pivot._x, pivot._y);
    }

    /**
     * Creates a Matrix33 to rotate by |deg| about a pivot point at (pivotx, pivoty).
     *
     * @param deg     rotation angle in degrees (positive rotates clockwise)
     * @param pivotx  x-coord of pivot
     * @param pivoty  y-coord of pivot
     * @return        Matrix33 with rotation
     */
    @NotNull
    public static Matrix33 makeRotate(float deg, float pivotx, float pivoty) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        return new Matrix33(new float[] {
            (float) cos, (float) -sin, (float) (pivotx - pivotx * cos + pivoty * sin),
            (float) sin, (float) cos, (float) (pivoty - pivoty * cos - pivotx * sin),
            0, 0, 1
        });
    }

    /**
     * <p>Creates a Matrix33 to skew by (sx, sy). Returned matrix is:</p>
     *
     * <pre> <code>
     * | 1  sx  0 |
     * | sy  1  0 |
     * |  0  0  1 |
     * </code></pre>
     *
     * @param sx horizontal skew factor
     * @param sy vertical skew factor
     * @return   Matrix33 with skew
     */
   @NotNull
   public static Matrix33 makeSkew(float sx, float sy) {
        return new Matrix33(new float[]{
                1, sx, 0,
                sy, 1, 0,
                0, 0, 1
        });
    }

    /**
     * <p>When converting from Matrix33 to Matrix44, the third row and
     * column remain as identity:</p>
     * 
     * <pre><code>
     * [ a b c ]      [ a b 0 c ]      
     * [ d e f ]  -&gt;  [ d e 0 f ]
     * [ g h i ]      [ 0 0 1 0 ]      
     *                [ g h 0 i ]        
     * </code></pre>
     */
    @NotNull
    public Matrix44 asMatrix44() {
        return new Matrix44(_mat[0], _mat[1], 0, _mat[2],
                            _mat[3], _mat[4], 0, _mat[5],
                                  0,       0, 1,       0,
                            _mat[6], _mat[7], 0, _mat[8]);
    }
}