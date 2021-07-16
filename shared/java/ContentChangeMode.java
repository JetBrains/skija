package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum ContentChangeMode {
    /** Discards surface on change. */
    DISCARD,

    /** Preserves surface on change. */
    RETAIN;

    @ApiStatus.Internal public static final ContentChangeMode[] _values = values();
}