package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialFontRunIterator extends TrivialRunIterator implements FontRunIterator {
    @ApiStatus.Internal
    public final Font _font;

    public TrivialFontRunIterator(int length, Font font) {
        super(length);
        _font   = font;
    }

    @Override
    public Font getCurrentFont() {
        return _font;
    }
}