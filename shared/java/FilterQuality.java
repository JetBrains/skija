package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum FilterQuality {
    /** fastest but lowest quality, typically nearest-neighbor */
    NONE,
    /** typically bilerp */
    LOW,
    /** typically bilerp + mipmaps for down-scaling */
    MEDIUM,
    /** slowest but highest quality, typically bicubic or bett */
    HIGH;

    @ApiStatus.Internal public static final FilterQuality[] _values = values();
}
