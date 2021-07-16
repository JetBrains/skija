package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum GLBackendState {
    RENDER_TARGET     (1 << 0),
    /**
     * Also includes samplers bound to texture units.
     */
    TEXTURE_BINDING   (1 << 1),
    /**
     * View state stands for scissor and viewport
     */
    VIEW              (1 << 2),
    BLEND             (1 << 3),
    MSAA_ENABLE       (1 << 4),
    VERTEX            (1 << 5),
    STENCIL           (1 << 6),
    PIXEL_STORE       (1 << 7),
    PROGRAM           (1 << 8),
    FIXED_FUNCTION    (1 << 9),
    MISC              (1 << 10),
    PATH_RENDERING    (1 << 11);

    @ApiStatus.Internal public static final GLBackendState[] _values = values();

    @ApiStatus.Internal public final int _bit;

    GLBackendState(int bit) {
        this._bit = bit;
    }
}