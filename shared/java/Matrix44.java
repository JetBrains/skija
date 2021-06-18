package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

/**
 * <p>4x4 matrix used by SkCanvas and other parts of Skia.</p>
 *
 *  Skia assumes a right-handed coordinate system:
 *      +X goes to the right
 *      +Y goes down
 *      +Z goes into the screen (away from the viewer)
 */

@Data
public class Matrix44 {
    /**
     * Matrix elements are in row-major order.
     */
    @ApiStatus.Internal
    public final float[] _mat;

    /**
     *  The constructor parameters are in row-major order.
     */
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

    /**
     * <p>When converting from Matrix44 to Matrix33, the third row and
     * column is dropped.</p>
     * 
     * <pre><code>
     * [ a b _ c ]      [ a b c ]
     * [ d e _ f ]  -&gt;  [ d e f ]
     * [ _ _ _ _ ]      [ g h i ]
     * [ g h _ i ]                 
     * </code></pre>
     */
    public Matrix33 asMatrix33() {
        return new Matrix33(_mat[0],  _mat[1],  _mat[3],
                            _mat[4],  _mat[5],  _mat[7],
                            _mat[12], _mat[13], _mat[15]);
    }
}