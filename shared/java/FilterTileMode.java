package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum FilterTileMode {
    /** Replicate the edge color if the shader draws outside of its original bounds. */
    CLAMP,

    /** Repeat the shader's image horizontally and vertically. */
    REPEAT,

    /** Repeat the shader's image horizontally and vertically, alternating mirror images so that adjacent images always seam. */
    MIRROR,

    /** Only draw within the original domain, return transparent-black everywhere else. */
    DECAL;

    @ApiStatus.Internal public static final FilterTileMode[] _values = values();
}