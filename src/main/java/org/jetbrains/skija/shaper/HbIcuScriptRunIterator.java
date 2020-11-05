package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class HbIcuScriptRunIterator extends ManagedRunIterator implements ScriptRunIterator {
    static { Library.load(); }

    @ApiStatus.Internal
    public HbIcuScriptRunIterator(ManagedString text) {
        super(_nMake(Native.getPtr(text)), text);
        Stats.onNativeCall();
    }

    public HbIcuScriptRunIterator(String text) {
        this(new ManagedString(text));
    }

    @Override
    public String getCurrentScript() {
        return FourByteTag.toString(_getCurrentScriptTag());
    }

    @Override
    public int _getCurrentScriptTag() {
        Stats.onNativeCall();
        return _nGetCurrentScriptTag(_ptr);
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr);
    @ApiStatus.Internal public static native int _nGetCurrentScriptTag(long ptr);
}