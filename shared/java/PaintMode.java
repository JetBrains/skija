package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum PaintMode {
    FILL,
    STROKE,
    STROKE_AND_FILL;

    @ApiStatus.Internal public static final PaintMode[] _values = values();
}
