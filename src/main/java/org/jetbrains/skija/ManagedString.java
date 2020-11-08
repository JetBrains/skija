package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class ManagedString extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public ManagedString(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    public ManagedString(String s) {
        this(_nMake(s));
        Stats.onNativeCall();
    }

    @Override
    public String toString() {
        Stats.onNativeCall();
        return _nToString(_ptr);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        static { Stats.onNativeCall(); }
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nMake(String s);
    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native String _nToString(long ptr);
}