package org.jetbrains.skija.svg;

import lombok.*;
import org.jetbrains.annotations.*;

@AllArgsConstructor @Data @With
public class SVGPreserveAspectRatio {
    @ApiStatus.Internal public final SVGPreserveAspectRatioAlign _align;
    @ApiStatus.Internal public final SVGPreserveAspectRatioScale _scale;

    @ApiStatus.Internal
    public SVGPreserveAspectRatio(int align, int scale) {
        this(SVGPreserveAspectRatioAlign.valueOf(align), SVGPreserveAspectRatioScale._values[scale]);
    }

    public SVGPreserveAspectRatio() {
        this(SVGPreserveAspectRatioAlign.XMID_YMID, SVGPreserveAspectRatioScale.MEET);
    }

    public SVGPreserveAspectRatio(SVGPreserveAspectRatioAlign align) {
        this(align, SVGPreserveAspectRatioScale.MEET);
    }

    public SVGPreserveAspectRatio(SVGPreserveAspectRatioScale scale) {
        this(SVGPreserveAspectRatioAlign.XMID_YMID, scale);
    }
}