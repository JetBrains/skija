package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class DirectContext extends RefCnt {
    static { Library.staticLoad(); }
    
    public static DirectContext makeGL() {
        Stats.onNativeCall();
        return new DirectContext(_nMakeGL());
    }

    public void flush() {
        try {
            Stats.onNativeCall();
            _nFlush(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public DirectContext(long ptr) {
        super(ptr);
    }

    public static native long _nMakeGL();
    public static native long _nFlush(long ptr);
}