package org.jetbrains.skija;

import org.jetbrains.skija.impl.Internal;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class Context extends RefCnt {
    public static Context makeGL() {
        Stats.onNativeCall();
        return new Context(_nMakeGL());
    }

    public void flush() {
        Stats.onNativeCall();
        _nFlush(_ptr);
    }

    @Internal
    public Context(long ptr) {
        super(ptr);
    }

    public static native long _nMakeGL();
    public static native long _nFlush(long ptr);
}