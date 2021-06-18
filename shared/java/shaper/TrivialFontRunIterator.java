package org.jetbrains.skija.shaper;

import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialFontRunIterator implements Iterator<FontRun> {
    @ApiStatus.Internal public final int     _length;
    @ApiStatus.Internal public final Font    _font;
    @ApiStatus.Internal public       boolean _atEnd;
    
    public TrivialFontRunIterator(String text, Font font) {
        _length = text.length();
        _font   = font;
        _atEnd  = _length == 0;
    }

    @Override
    public FontRun next() {
        _atEnd = true;
        return new FontRun(_length, _font);
    }

    @Override
    public boolean hasNext() {
        return !_atEnd;
    }
}