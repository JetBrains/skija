package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum PathDirection {
    /** Clockwise direction for adding closed contours. */
    CLOCKWISE,

    /** Counter-clockwise direction for adding closed contours. */
    COUNTER_CLOCKWISE;

    @ApiStatus.Internal public static final PathDirection[] _values = values();
}
