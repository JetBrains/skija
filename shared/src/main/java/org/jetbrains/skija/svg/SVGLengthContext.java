package org.jetbrains.skija.svg;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

@AllArgsConstructor @lombok.Data @With
public class SVGLengthContext {
    @ApiStatus.Internal public final float _width;
    @ApiStatus.Internal public final float _height;
    @ApiStatus.Internal public final float _dpi;

    public SVGLengthContext(float width, float height) {
        this(width, height, 90);
    }

    public SVGLengthContext(@NotNull Point size) {
        this(size._x, size._y, 90);
    }

    public float resolve(@NotNull SVGLength length, @NotNull SVGLengthType type) {
        switch (length._unit) {
            case NUMBER:
                return length._value;
            case PX:
                return length._value;
            case PERCENTAGE:
                switch (type) {
                    case HORIZONTAL:
                        return length._value * _width / 100f;
                    case VERTICAL:
                        return length._value * _height / 100f;
                    case OTHER:
                        // https://www.w3.org/TR/SVG11/coords.html#Units_viewport_percentage
                        return (float) (length._value * Math.hypot(_width, _height) / Math.sqrt(2.0) / 100.0);
                }
            case CM:
                return length._value * _dpi / 2.54f;
            case MM:
                return length._value * _dpi / 25.4f;
            case IN:
                return length._value * _dpi;
            case PT:
                return length._value * _dpi / 72.272f;
            case PC:
                return length._value * _dpi * 12f / 72.272f;
            default:
                throw new IllegalArgumentException("Unknown SVGLengthUnit: " + length._unit);
        }
    }

    @NotNull
    public Rect resolveRect(@NotNull SVGLength x, @NotNull SVGLength y, @NotNull SVGLength width, @NotNull SVGLength height) {
        return Rect.makeXYWH(resolve(x, SVGLengthType.HORIZONTAL),
                             resolve(y, SVGLengthType.VERTICAL),
                             resolve(width, SVGLengthType.HORIZONTAL),
                             resolve(height, SVGLengthType.VERTICAL));
    }
}