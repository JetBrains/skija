package org.jetbrains.skija.svg;

import lombok.*;
import org.jetbrains.annotations.*;

@AllArgsConstructor @Data @With
public class SVGLength {
    @ApiStatus.Internal public final float _value;
    @ApiStatus.Internal public final SVGLengthUnit _unit;

    @ApiStatus.Internal
    public SVGLength(float value, int unit) {
        this(value, SVGLengthUnit._values[unit]);
    }

    public SVGLength(float value) {
        this(value, SVGLengthUnit.NUMBER);
    }
}