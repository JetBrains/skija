package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public interface ScriptRunIterator extends RunIterator {
    /** Should be iso15924 codes. */
    String getCurrentScript();

    @ApiStatus.Internal
    default int _getCurrentScriptTag() {
        return FourByteTag.fromString(getCurrentScript());
    }
}
