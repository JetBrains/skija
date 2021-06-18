package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;

@lombok.Data @AllArgsConstructor
public class FilterMipmap implements SamplingMode {
    @ApiStatus.Internal public final FilterMode _filterMode;
    @ApiStatus.Internal public final MipmapMode _mipmapMode;

    public FilterMipmap(FilterMode filterMode) {
        this(filterMode, MipmapMode.NONE);
    }

    @ApiStatus.Internal @Override
    public long _pack() {
        return 0x7FFFFFFFFFFFFFFFL & (((long) _filterMode.ordinal() << 32) | (long) _mipmapMode.ordinal());
    }
}
