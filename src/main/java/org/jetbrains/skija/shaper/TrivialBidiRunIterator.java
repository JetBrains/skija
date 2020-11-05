package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialBidiRunIterator extends TrivialRunIterator implements BidiRunIterator {
    @ApiStatus.Internal
    public final int _level;

    public TrivialBidiRunIterator(long length, int level) {
        super(length);
        _level = level;
    }

    @Override
    public int getCurrentLevel() {
        return _level;
    }
}