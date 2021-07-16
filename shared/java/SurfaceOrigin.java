package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum SurfaceOrigin {
    TOP_LEFT,
    BOTTOM_LEFT;

    @ApiStatus.Internal public static final SurfaceOrigin[] _values = values();
}
