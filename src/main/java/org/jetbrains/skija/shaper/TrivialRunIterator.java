package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialRunIterator implements RunIterator {
    @ApiStatus.Internal
    public boolean _atEnd;

    @ApiStatus.Internal
    public final long _length;

    public TrivialRunIterator(long length) {
        _atEnd = false;
        _length = length;
    }

    @Override
    public void consume() {
        _atEnd = true;
    }

    @Override
    public long getEndOfCurrentRun() {
        return _atEnd ? _length : 0;
    }

    @Override
    public boolean isAtEnd() {
        return _atEnd;
    }
}