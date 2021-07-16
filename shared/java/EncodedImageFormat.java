package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum EncodedImageFormat {
    BMP,
    GIF,
    ICO,
    JPEG,
    PNG,
    WBMP,
    WEBP,
    PKM,
    KTX,
    ASTC,
    DNG,
    HEIF;

    @ApiStatus.Internal public static final EncodedImageFormat[] _values = values();
}