package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

/**
 *  <p>A compressed form of a rotation+scale matrix.</p>
 *
 *  <pre>[ fSCos     -fSSin    fTx ]
 *  [ fSSin      fSCos    fTy ]
 *  [     0          0      1 ]</pre>
 */
@Data
public class RSXform {
    @ApiStatus.Internal
    public final float _scos, _ssin, _tx, _ty;

    /**
     * Initialize a new xform based on the scale, rotation (in radians), final tx,ty location
     * and anchor-point ax,ay within the src quad.
     *
     * Note: the anchor point is not normalized (e.g. 0...1) but is in pixels of the src image.
     */
    public static RSXform makeFromRadians(float scale, float radians, float tx, float ty, float ax, float ay) {
        float s = (float) Math.sin(radians) * scale;
        float c = (float) Math.cos(radians) * scale;
        return new RSXform(c, s, tx + -c * ax + s * ay, ty + -s * ax - c * ay);
    }
}
