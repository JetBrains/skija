package org.jetbrains.skija.shaper;

import java.lang.ref.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

@lombok.Data
public class FontRun {
    @ApiStatus.Internal public final int _end;
    @ApiStatus.Internal public final Font _font;

    @ApiStatus.Internal 
    public long _getFontPtr() {
        try {
            return Native.getPtr(_font);
        } finally {
            Reference.reachabilityFence(_font);
        }
    }
}
