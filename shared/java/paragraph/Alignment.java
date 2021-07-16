package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum Alignment {
    LEFT,
    RIGHT,
    CENTER,
    JUSTIFY,
    START,
    END;

    @ApiStatus.Internal public static final Alignment[] _values = values();
}
