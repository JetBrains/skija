package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum PathFillMode {
    /** Specifies that "inside" is computed by a non-zero sum of signed edge crossings. */
    WINDING,

    /** Specifies that "inside" is computed by an odd number of edge crossings. */
    EVEN_ODD,

    /** Same as {@link #WINDING}, but draws outside of the path, rather than inside. */
    INVERSE_WINDING,

    /** Same as {@link #EVEN_ODD}, but draws outside of the path, rather than inside. */
    INVERSE_EVEN_ODD;

    @ApiStatus.Internal public static final PathFillMode[] _values = values();

    /**
     * Returns if FillType describes area outside Path geometry. The inverse fill area
     * extends indefinitely.
     *
     * @return  true if FillType is {@link #INVERSE_WINDING} or {@link #INVERSE_EVEN_ODD}
     */
    public boolean isInverse() {
        return this == INVERSE_WINDING || this == INVERSE_EVEN_ODD;
    }

    /**
     * Returns the inverse fill type. The inverse of FillType describes the area
     * unmodified by the original FillType.
     *
     * @return  inverse FillType
     */
    public PathFillMode inverse() {
        switch (this) {
            case WINDING:
                return INVERSE_WINDING;
            case EVEN_ODD:
                return INVERSE_EVEN_ODD;
            case INVERSE_WINDING:
                return WINDING;
            case INVERSE_EVEN_ODD:
                return EVEN_ODD;
            default:
                throw new RuntimeException("Unreachable");
        }
    }
}
