package org.jetbrains.skija.paragraph;

public enum RectWidthMode {

    /** Provide tight bounding boxes that fit widths to the runs of each line independently. */
    TIGHT,

    /** Extends the width of the last rect of each line to match the position of the widest rect over all the lines. */
    MAX
}
