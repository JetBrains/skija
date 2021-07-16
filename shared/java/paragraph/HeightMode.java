package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum HeightMode {
    ALL,
    DISABLE_FIRST_ASCENT,
    DISABLE_LAST_DESCENT,
    DISABLE_ALL;

    @ApiStatus.Internal public static final HeightMode[] _values = values();
}
