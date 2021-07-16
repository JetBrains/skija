package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum RectHeightMode {

    /** Provide tight bounding boxes that fit heights per run. */
    TIGHT,

    /**
     * The height of the boxes will be the maximum height of all runs in the
     * line. All rects in the same line will be the same height.
     */
    MAX,

    /**
     * Extends the top and/or bottom edge of the bounds to fully cover any line
     * spacing. The top edge of each line should be the same as the bottom edge
     * of the line above. There should be no gaps in vertical coverage given any
     * ParagraphStyle line_height.
     *
     * The top and bottom of each rect will cover half of the
     * space above and half of the space below the line.
     */
    INCLUDE_LINE_SPACING_MIDDLE,

    /** The line spacing will be added to the top of the rect. */
    INCLUDE_LINE_SPACING_TOP,

    /** The line spacing will be added to the bottom of the rect. */
    INCLUDE_LINE_SPACING_BOTTOM,

    STRUT;

    @ApiStatus.Internal public static final RectHeightMode[] _values = values();
}
