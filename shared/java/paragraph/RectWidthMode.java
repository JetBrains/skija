package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum RectWidthMode {

    /** Provide tight bounding boxes that fit widths to the runs of each line independently. */
    TIGHT,

    /** Extends the width of the last rect of each line to match the position of the widest rect over all the lines. */
    MAX;

    @ApiStatus.Internal public static final RectWidthMode[] _values = values();
}
