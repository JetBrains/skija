package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum FilterMode {
    /**
     * single sample point (nearest neighbor)
     */
    NEAREST,

    /**
     * interporate between 2x2 sample points (bilinear interpolation)
     */
    LINEAR;

    @ApiStatus.Internal public static final FilterMode[] _values = values();
}
