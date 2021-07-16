package org.jetbrains.skija.svg;

import org.jetbrains.annotations.*;

public enum SVGLengthUnit {
    UNKNOWN,
    NUMBER,
    PERCENTAGE,
    EMS,
    EXS,
    PX,
    CM,
    MM,
    IN,
    PT,
    PC;

    @ApiStatus.Internal public static final SVGLengthUnit[] _values = values();
}
