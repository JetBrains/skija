package org.jetbrains.skija;

import org.jetbrains.annotations.*;

/**
 * Cap draws at the beginning and end of an open path contour.
 */
public enum PaintStrokeCap {
    /**
     * No stroke extension
     */
    BUTT,

    /**
     * adds circle
     */
    ROUND,

    /**
     * adds square
     */
    SQUARE;

    @ApiStatus.Internal public static final PaintStrokeCap[] _values = values();
}
