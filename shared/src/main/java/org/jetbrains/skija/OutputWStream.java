package org.jetbrains.skija;

import java.io.*;
import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class OutputWStream extends WStream {
    static { Library.staticLoad(); }

    @ApiStatus.Internal 
    public final OutputStream _out;

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public OutputWStream(OutputStream out) {
        super(_nMake(out), _FinalizerHolder.PTR);
        Stats.onNativeCall();
        _out = out;
    }
    
    @ApiStatus.Internal public static native long  _nGetFinalizer();
    @ApiStatus.Internal public static native long  _nMake(OutputStream out);
}
