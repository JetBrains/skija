package org.jetbrains.skija;

import org.jetbrains.annotations.*;

/**
 * Whether edge pixels draw opaque or with partial transparency.
 */
public enum FontEdging {
    /**
     * no transparent pixels on glyph edges
     */
    ALIAS,

    /**
     * may have transparent pixels on glyph edges
     */
    ANTI_ALIAS,

    /**
     * glyph positioned in pixel using transparency
     */
    SUBPIXEL_ANTI_ALIAS;

    @ApiStatus.Internal public static final FontEdging[] _values = values();
}