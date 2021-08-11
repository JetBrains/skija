package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

/**
 * <p>This specifies how the next frame is based on this frame.</p>
 *
 * <p>Names are based on the GIF 89a spec.</p>
 */
public enum AnimationDisposalMode {
    @ApiStatus.Internal
    _UNUSED,

    /**
     * <p>The next frame should be drawn on top of this one.</p>
     *
     * <p>In a GIF, a value of 0 (not specified) is also treated as KEEP.</p>
     */
    KEEP,

    /**
     * <p>Similar to KEEP, except the area inside this frame's rectangle
     * should be cleared to the BackGround color (transparent) before
     * drawing the next frame.</p>
     */
    RESTORE_BG_COLOR,

    /**
     * <p>The next frame should be drawn on top of the previous frame - i.e.
     * disregarding this one.</p>
     *
     * <p>In a GIF, a value of 4 is also treated as RestorePrevious.</p>
     */
    RESTORE_PREVIOUS;

    @ApiStatus.Internal public static final AnimationDisposalMode[] _values = values();
}