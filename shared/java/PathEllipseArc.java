package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum PathEllipseArc {
    /** Smaller of arc pair. */
    SMALLER,

    /** Larger of arc pair. */
    LARGER;

    @ApiStatus.Internal public static final PathEllipseArc[] _values = values();
}
