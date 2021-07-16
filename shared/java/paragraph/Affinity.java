package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum Affinity {
    UPSTREAM,
    DOWNSTREAM;

    @ApiStatus.Internal public static final Affinity[] _values = values();
}
