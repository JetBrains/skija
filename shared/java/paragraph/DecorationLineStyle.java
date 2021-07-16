package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum DecorationLineStyle {
    SOLID,
    DOUBLE,
    DOTTED,
    DASHED,
    WAVY;

    @ApiStatus.Internal public static final DecorationLineStyle[] _values = values();
}
