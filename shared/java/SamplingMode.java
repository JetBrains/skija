package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;

/**
 * @see org.jetbrains.skija.FilterMipmap
 * @see org.jetbrains.skija.CubicResampler
 */
public interface SamplingMode {
    public static final SamplingMode DEFAULT = new FilterMipmap(FilterMode.NEAREST, MipmapMode.NONE);
    public static final SamplingMode LINEAR = new FilterMipmap(FilterMode.LINEAR, MipmapMode.NONE);
    public static final SamplingMode MITCHELL = new CubicResampler(0.33333334f, 0.33333334f);
    public static final SamplingMode CATMULL_ROM = new CubicResampler(0, 0.5f);

    @ApiStatus.Internal long _pack();
}