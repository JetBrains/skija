package org.jetbrains.skija.shaper;

import java.text.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class JavaTextBidiRunIterator implements BidiRunIterator {
    @ApiStatus.Internal
    public final Bidi _bidi;

    @ApiStatus.Internal
    public int _run;

    public JavaTextBidiRunIterator(String text) {
        this(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
    }

    public JavaTextBidiRunIterator(String text, int flags) {
        _bidi = new Bidi(text, flags);
        _run = 0;
    }

    @Override
    public void consume() {
        _run++;
    }

    @Override
    public long getEndOfCurrentRun() {
        return _bidi.getRunLimit(_run);
    }

    @Override
    public boolean isAtEnd() {
        return _run >= _bidi.getRunCount();
    }

    @Override
    public int getCurrentLevel() {
        return _bidi.getRunLevel(_run);
    }
}