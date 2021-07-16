package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum BlendMode {
    /** Replaces destination with zero: fully transparent. */
    CLEAR,
    
    /** Replaces destination. */
    SRC,
    
    /** Preserves destination. */
    DST,
    
    /** Source over destination. */
    SRC_OVER,
    
    /** Destination over source. */
    DST_OVER,
    
    /** Source trimmed inside destination. */
    SRC_IN,
    
    /** Destination trimmed by source. */
    DST_IN,
    
    /** Source trimmed outside destination. */
    SRC_OUT,
    
    /** Destination trimmed outside source. */
    DST_OUT,
    
    /** Source inside destination blended with destination. */
    SRC_ATOP,
    
    /** Destination inside source blended with source. */
    DST_ATOP,
    
    /** Each of source and destination trimmed outside the other. */
    XOR,
    
    /** Sum of colors. */
    PLUS,
    
    /** Product of premultiplied colors; darkens destination. */
    MODULATE,
    
    /** Multiply inverse of pixels, inverting result; brightens destination. */
    SCREEN,
    
    /** Multiply or screen, depending on destination. */
    OVERLAY,
    
    /** Darker of source and destination. */
    DARKEN,
    
    /** Lighter of source and destination. */
    LIGHTEN,
    
    /** Brighten destination to reflect source. */
    COLOR_DODGE,
    
    /** Darken destination to reflect source. */
    COLOR_BURN,
    
    /** Multiply or screen, depending on source. */
    HARD_LIGHT,
    
    /** Lighten or darken, depending on source. */
    SOFT_LIGHT,
    
    /** Subtract darker from lighter with higher contrast. */
    DIFFERENCE,
    
    /** Subtract darker from lighter with lower contrast. */
    EXCLUSION,
    
    /** Multiply source with destination, darkening image. */
    MULTIPLY,
    
    /** Hue of source with saturation and luminosity of destination. */
    HUE,
    
    /** Saturation of source with hue and luminosity of destination. */
    SATURATION,
    
    /** Hue and saturation of source with luminosity of destination. */
    COLOR,
    
    /** Luminosity of source with hue and saturation of destination. */
    LUMINOSITY;

    @ApiStatus.Internal public static final BlendMode[] _values = values();
}