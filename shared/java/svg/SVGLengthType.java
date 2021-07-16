package org.jetbrains.skija.svg;

import org.jetbrains.annotations.*;

public enum SVGLengthType {
    HORIZONTAL,
    VERTICAL,
    OTHER;

    @ApiStatus.Internal public static final SVGLengthType[] _values = values();
}
