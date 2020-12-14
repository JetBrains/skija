package org.jetbrains.skija.shaper;

import java.lang.ref.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public abstract class ManagedRunIterator<T> extends Managed implements Iterator<T> {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public final ManagedString _text;

    @ApiStatus.Internal
    public ManagedRunIterator(long ptr, ManagedString text, boolean manageText) {
        super(ptr, _FinalizerHolder.PTR);
        _text = manageText ? text : null;
    }

    @Override
    public void close() {
        super.close();
        if (_text != null)
            _text.close();
    }

    @ApiStatus.Internal
    public int _getEndOfCurrentRun() {
        try {
            return _nGetEndOfCurrentRun(_ptr, Native.getPtr(_text));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(_text);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return !_nIsAtEnd(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native void _nConsume(long ptr);
    @ApiStatus.Internal public static native int  _nGetEndOfCurrentRun(long ptr, long textPtr);
    @ApiStatus.Internal public static native boolean _nIsAtEnd(long ptr);
}