package org.jetbrains.skija.shaper;

import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialBidiRunIterator implements Iterator<BidiRun> {
    @ApiStatus.Internal public final int _length;
    @ApiStatus.Internal public final int _level;
    @ApiStatus.Internal public boolean _atEnd;
    
    public TrivialBidiRunIterator(String text, int level) {
        _length = text.length();
        _level  = level;
        _atEnd  = _length == 0;
    }

    @Override
    public BidiRun next() {
        _atEnd = true;
        return new BidiRun(_length, _level);
    }

    @Override
    public boolean hasNext() {
        return !_atEnd;
    }
}