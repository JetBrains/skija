package org.jetbrains.skija.shaper;

import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class IcuBidiRunIterator extends ManagedRunIterator<BidiRun> {
    static { Library.load(); }

    public IcuBidiRunIterator(ManagedString text, boolean manageText, int bidiLevel) {
        super(_nMake(Native.getPtr(text), bidiLevel), text, manageText);
        Stats.onNativeCall();
    }

    public IcuBidiRunIterator(String text, int bidiLevel) {
        this(new ManagedString(text), true, bidiLevel);
    }

    @Override
    public BidiRun next() {
        _nConsume(_ptr);
        return new BidiRun(_getEndOfCurrentRun(), _nGetCurrentLevel(_ptr));
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr, int bidiLevel);
    @ApiStatus.Internal public static native int _nGetCurrentLevel(long ptr);
}