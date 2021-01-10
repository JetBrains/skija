package org.jetbrains.skija.skottie;

import org.jetbrains.annotations.*;

public enum RenderFlag {
    SKIP_TOP_LEVEL_ISOLATION(0x01),
    DISABLE_TOP_LEVEL_CLIPPING(0x02);

    @ApiStatus.Internal public final int _flag;

    RenderFlag(int flag) {
        this._flag = flag;
    }
}