package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class DirectContext extends RefCnt {
    static { Library.staticLoad(); }
    
    public static DirectContext makeGL() {
        Stats.onNativeCall();
        return new DirectContext(_nMakeGL());
    }

    public void flush() {
        Stats.onNativeCall();
        _nFlush(_ptr);
    }

    @ApiStatus.Internal
    public DirectContext(long ptr) {
        super(ptr);
    }

    public static native long _nMakeGL();
    public static native long _nFlush(long ptr);
}