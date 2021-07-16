package org.jetbrains.skija.svg;

import org.jetbrains.annotations.*;

public enum SVGTag {
    CIRCLE,
    CLIP_PATH,
    DEFS,
    ELLIPSE,
    FE_BLEND,
    FE_COLOR_MATRIX,
    FE_COMPOSITE,
    FE_DIFFUSE_LIGHTING,
    FE_DISPLACEMENT_MAP,
    FE_DISTANT_LIGHT,
    FE_FLOOD,
    FE_GAUSSIAN_BLUR,
    FE_IMAGE,
    FE_MORPHOLOGY,
    FE_OFFSET,
    FE_POINT_LIGHT,
    FE_SPECULAR_LIGHTING,
    FE_SPOT_LIGHT,
    FE_TURBULENCE,
    FILTER,
    G,
    IMAGE,
    LINE,
    LINEAR_GRADIENT,
    MASK,
    PATH,
    PATTERN,
    POLYGON,
    POLYLINE,
    RADIAL_GRADIENT,
    RECT,
    STOP,
    SVG,
    TEXT,
    TEXT_LITERAL,
    TEXTPATH,
    TSPAN,
    USE;

    @ApiStatus.Internal public static final SVGTag[] _values = values();
}
