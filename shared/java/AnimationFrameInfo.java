package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;

/**
 * Information about individual frames in a multi-framed image.
 */
@lombok.Data @AllArgsConstructor @With
public class AnimationFrameInfo {
    @ApiStatus.Internal
    public AnimationFrameInfo(int requiredFrame, int duration, boolean fullyReceived, int alphaTypeOrdinal, boolean hasAlphaWithinBounds, int disposalMethodOrdinal, int blendModeOrdinal, IRect frameRect) {
        this(requiredFrame, duration, fullyReceived, ColorAlphaType._values[alphaTypeOrdinal], hasAlphaWithinBounds, AnimationDisposalMode._values[disposalMethodOrdinal], BlendMode._values[blendModeOrdinal], frameRect);
    }

    /**
     * <p>The frame that this frame needs to be blended with, or
     * -1 if this frame is independent (so it can be
     * drawn over an uninitialized buffer).</p>
     *
     * <p>Note that this is the *earliest* frame that can be used
     * for blending. Any frame from [_requiredFrame, i) can be
     * used, unless its getDisposalMethod() is {@link AnimationDisposalMode#RESTORE_PREVIOUS}.</p>
     */
    @ApiStatus.Internal
    public int _requiredFrame;

    /**
     * Number of milliseconds to show this frame.
     */
    @ApiStatus.Internal
    public int _duration;

    /**
     * <p>Whether the end marker for this frame is contained in the stream.</p>
     *
     * <p>Note: this does not guarantee that an attempt to decode will be complete.
     * There could be an error in the stream.</p>
     */
    @ApiStatus.Internal
    public boolean _fullyReceived;

    /**
     * <p>This is conservative; it will still return non-opaque if e.g. a
     * color index-based frame has a color with alpha but does not use it.</p>
     */
    @ApiStatus.Internal
    public ColorAlphaType _alphaType;

    /**
     * <p>Whether the updated rectangle contains alpha.</p>
     *
     * <p>This is conservative; it will still be set to true if e.g. a color
     * index-based frame has a color with alpha but does not use it. In
     * addition, it may be set to true, even if the final frame, after
     * blending, is opaque.</p>
     */
    @ApiStatus.Internal
    public boolean _hasAlphaWithinBounds;

    /**
     * <p>How this frame should be modified before decoding the next one.</p>
     */
    @ApiStatus.Internal
    public AnimationDisposalMode _disposalMethod;

    /**
     * <p>How this frame should blend with the prior frame.</p>
     */
    @ApiStatus.Internal
    public BlendMode _blendMode;

    /**
     * <p>The rectangle updated by this frame.</p>
     *
     * <p>It may be empty, if the frame does not change the image. It will
     * always be contained by {@link Codec#getSize()}.
     */
    @ApiStatus.Internal
    public IRect _frameRect;
}