package org.jetbrains.skija.shaper;

import java.text.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class JavaTextBidiRunIterator implements Iterator<BidiRun> {
    @ApiStatus.Internal public final Bidi _bidi;
    @ApiStatus.Internal public final int _runsCount;
    @ApiStatus.Internal public int _run;

    public JavaTextBidiRunIterator(String text) {
        this(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
    }

    public JavaTextBidiRunIterator(String text, int flags) {
        _bidi = new Bidi(text, flags);
        _runsCount = _bidi.getRunCount();
        _run = -1;
    }

    @Override
    public BidiRun next() {
        _run++;
        return new BidiRun(_bidi.getRunLimit(_run), _bidi.getRunLevel(_run));
    }

    @Override
    public boolean hasNext() {
        return _run + 1 < _runsCount;
    }
}