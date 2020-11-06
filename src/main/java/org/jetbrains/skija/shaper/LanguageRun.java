package org.jetbrains.skija.shaper;

import lombok.*;
import org.jetbrains.annotations.*;

@Data
public class LanguageRun {
    @ApiStatus.Internal public final int _end;

    /** Should be BCP-47, c locale names may also work. */
    @ApiStatus.Internal public final String _language;
}
