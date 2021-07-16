package org.jetbrains.skija;

import org.jetbrains.annotations.*;

/**
 * Level of glyph outline adjustment
 */
public enum FontHinting {
    /**
     * glyph outlines unchanged
     */
    NONE,

    /**
     * minimal modification to improve constrast
     */
    SLIGHT,

    /**
     * glyph outlines modified to improve constrast
     */
    NORMAL,

    /**
     * modifies glyph outlines for maximum constrast
     */
    FULL;

    @ApiStatus.Internal public static final FontHinting[] _values = values();
}