package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

@Data
public class IRange {
    @ApiStatus.Internal
    public final int _start;
    
    @ApiStatus.Internal
    public final int _end;

    @ApiStatus.Internal
    public static IRange _makeFromLong(long l) {
        return new IRange((int) (l >>> 32), (int) (l & 0xFFFFFFFF));
    }
}