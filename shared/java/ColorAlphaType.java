package org.jetbrains.skija;

import org.jetbrains.annotations.*;

/**
 * <p>Describes how to interpret the alpha component of a pixel. A pixel may
 * be opaque, or alpha, describing multiple levels of transparency.</p>
 * 
 * <p>In simple blending, alpha weights the draw color and the destination
 * color to create a new color. If alpha describes a weight from zero to one:</p>
 * 
 * <pre><code>new color = draw color * alpha + destination color * (1 - alpha)</code></pre>
 * 
 * <p>In practice alpha is encoded in two or more bits, where 1.0 equals all bits set.</p>
 * 
 * <p>RGB may have alpha included in each component value; the stored
 * value is the original RGB multiplied by alpha. Premultiplied color
 * components improve performance.</p>
 */
public enum ColorAlphaType {
    /**
     * uninitialized
     */
    UNKNOWN,  

    /**
     * pixel is opaque
     */
    OPAQUE,   

    /**
     * pixel components are premultiplied by alpha
     */
    PREMUL,   

    /**
     * pixel components are independent of alpha
     */
    UNPREMUL;

    @ApiStatus.Internal public static final ColorAlphaType[] _values = values();
}