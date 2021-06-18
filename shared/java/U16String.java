package org.jetbrains.skija;

import java.lang.ref.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

/**
 * Java mirror of std::vector&lt;jchar&gt; (UTF-16)
 */
public class U16String extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public U16String(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    public U16String(String s) {
        this(_nMake(s));
        Stats.onNativeCall();
    }

    @Override
    public String toString() {
        try {
            Stats.onNativeCall();
            return _nToString(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nMake(String s);
    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native String _nToString(long ptr);
}