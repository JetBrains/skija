package org.jetbrains.skija;

import java.lang.ref.*;
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
        try {
            Stats.onNativeCall();
            return _nToString(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("-> this")
    public ManagedString insert(int offset, @NotNull String s) {
        Stats.onNativeCall();
        _nInsert(_ptr, offset, s);
        return this;
    }

    @NotNull @Contract("-> this")
    public ManagedString append(@NotNull String s) {
        Stats.onNativeCall();
        _nAppend(_ptr, s);
        return this;
    }

    @NotNull @Contract("-> this")
    public ManagedString remove(int from) {
        Stats.onNativeCall();
        _nRemoveSuffix(_ptr, from);
        return this;
    }

    @NotNull @Contract("-> this")
    public ManagedString remove(int from, int length) {
        Stats.onNativeCall();
        _nRemove(_ptr, from, length);
        return this;
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nMake(String s);
    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native String _nToString(long ptr);
    @ApiStatus.Internal public static native void _nInsert(long ptr, int offset, String s);
    @ApiStatus.Internal public static native void _nAppend(long ptr, String s);
    @ApiStatus.Internal public static native void _nRemoveSuffix(long ptr, int from);
    @ApiStatus.Internal public static native void _nRemove(long ptr, int from, int length);
}