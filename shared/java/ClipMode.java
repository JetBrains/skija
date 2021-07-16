package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum ClipMode {
    DIFFERENCE,
    INTERSECT;

    @ApiStatus.Internal public static final ClipMode[] _values = values();
}
