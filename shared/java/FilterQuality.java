package org.jetbrains.skija;

public enum FilterQuality {
    /** fastest but lowest quality, typically nearest-neighbor */
    NONE,
    /** typically bilerp */
    LOW,
    /** typically bilerp + mipmaps for down-scaling */
    MEDIUM,
    /** slowest but highest quality, typically bicubic or bett */
    HIGH
}
