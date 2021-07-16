package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum FilterBlurMode {
    /** fuzzy inside and outside */
    NORMAL,
    /** solid inside, fuzzy outside */
    SOLID,
    /** nothing inside, fuzzy outside */
    OUTER,
    /** fuzzy inside, nothing outside */
    INNER;

    @ApiStatus.Internal public static final FilterBlurMode[] _values = values();
}
