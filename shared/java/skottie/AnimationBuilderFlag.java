package org.jetbrains.skija.skottie;

import org.jetbrains.annotations.*;

public enum AnimationBuilderFlag {
    /**
     * Normally, all static image frames are resolved at
     * load time via ImageAsset::getFrame(0).  With this flag,
     * frames are only resolved when needed, at seek() time.
     */
    DEFER_IMAGE_LOADING(0x01),

    /**
     * Attempt to use the embedded fonts (glyph paths,
     * normally used as fallback) over native Skia typefaces.
     */
    PREFER_EMBEDDED_FONTS(0x02);

    @ApiStatus.Internal public static final AnimationBuilderFlag[] _values = values();

    @ApiStatus.Internal public final int _flag;

    AnimationBuilderFlag(int flag) {
        this._flag = flag;
    }
}