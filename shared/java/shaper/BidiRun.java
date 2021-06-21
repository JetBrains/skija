package org.jetbrains.skija.shaper;

import lombok.*;
import org.jetbrains.annotations.*;

@Data
public class BidiRun {
    @ApiStatus.Internal public final int _end;

    /** The unicode bidi embedding level (even ltr, odd rtl) */
    @ApiStatus.Internal public final int _level;
}
