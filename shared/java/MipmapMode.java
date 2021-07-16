package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum MipmapMode {
    /**
     * ignore mipmap levels, sample from the "base"
     */
    NONE,

    /**
     * sample from the nearest level
     */
    NEAREST,   

    /**
     * interpolate between the two nearest levels
     */
    LINEAR;

    @ApiStatus.Internal public static final MipmapMode[] _values = values();
}
