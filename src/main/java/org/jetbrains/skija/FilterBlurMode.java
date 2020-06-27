package org.jetbrains.skija;

public enum FilterBlurMode {
    /** fuzzy inside and outside */
    NORMAL,
    /** solid inside, fuzzy outside */
    SOLID,
    /** nothing inside, fuzzy outside */
    OUTER,
    /** fuzzy inside, nothing outside */
    INNER
}
