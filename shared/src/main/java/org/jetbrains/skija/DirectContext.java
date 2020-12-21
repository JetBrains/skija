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

    public DirectContext flush() {
        Stats.onNativeCall();
        _nFlush(_ptr);
        return this;
    }

    public DirectContext resetAll() {
        Stats.onNativeCall();
        _nReset(_ptr, -1);
        return this;
    }

    public DirectContext resetGLAll() {
        Stats.onNativeCall();
        _nReset(_ptr, 0xffff);
        return this;
    }

    public DirectContext resetGL(GLBackendState... states) {
        Stats.onNativeCall();
        int flags = 0;
        for (var state: states)
            flags |= state._bit;
        _nReset(_ptr, flags);
        return this;
    }

    @ApiStatus.Internal
    public DirectContext(long ptr) {
        super(ptr);
    }

    public static native long _nMakeGL();
    public static native long _nFlush(long ptr);
    public static native void _nReset(long ptr, int flags);
}