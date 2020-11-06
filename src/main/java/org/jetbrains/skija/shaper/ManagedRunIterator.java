package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public abstract class ManagedRunIterator extends Managed implements RunIterator {
    static { Library.load(); }

    @ApiStatus.Internal
    public final ManagedString _text;

    @ApiStatus.Internal
    public ManagedRunIterator(long ptr, ManagedString text) {
        super(ptr, _FinalizerHolder.PTR);
        _text = text;
    }

    @Override
    public void consume() {
        Stats.onNativeCall();
        _nConsume(_ptr);
    }

    @Override
    public int getEndOfCurrentRun() {
        Stats.onNativeCall();
        return _nGetEndOfCurrentRun(_ptr);
    }

    @Override
    public boolean isAtEnd() {
        Stats.onNativeCall();
        return _nIsAtEnd(_ptr);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        static { Stats.onNativeCall(); }
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native void _nConsume(long ptr);
    @ApiStatus.Internal public static native int _nGetEndOfCurrentRun(long ptr);
    @ApiStatus.Internal public static native boolean _nIsAtEnd(long ptr);
}