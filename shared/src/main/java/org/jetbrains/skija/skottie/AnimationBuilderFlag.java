package org.jetbrains.skija.skottie;

import org.jetbrains.annotations.*;

public enum AnimationBuilderFlag {
    DEFER_IMAGE_LOADING(0x01),
    PREFER_EMBEDDED_FONTS(0x02);

    @ApiStatus.Internal public final int _flag;

    AnimationBuilderFlag(int flag) {
        this._flag = flag;
    }
}