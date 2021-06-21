package org.jetbrains.skija;

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
    LINEAR
}
