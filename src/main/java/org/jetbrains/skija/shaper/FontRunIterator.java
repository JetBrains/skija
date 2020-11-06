package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public interface FontRunIterator extends RunIterator {
    Font getCurrentFont();

    @ApiStatus.Internal
    default long _getCurrentFontPtr() {
        return Native.getPtr(getCurrentFont());
    }
}

