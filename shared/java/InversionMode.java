package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum InversionMode {
    NO,
    BRIGHTNESS,
    LIGHTNESS;

    @ApiStatus.Internal public static final InversionMode[] _values = values();
}
