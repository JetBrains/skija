package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class HbIcuScriptRunIterator extends ManagedRunIterator<ScriptRun> {
    static { Library.staticLoad(); }

    public HbIcuScriptRunIterator(ManagedString text, boolean manageText) {
        super(_nMake(Native.getPtr(text)), text, manageText);
        Stats.onNativeCall();
    }

    public HbIcuScriptRunIterator(String text) {
        this(new ManagedString(text), true);
    }

    @Override
    public ScriptRun next() {
        _nConsume(_ptr);
        return new ScriptRun(_getEndOfCurrentRun(), _nGetCurrentScriptTag(_ptr));
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr);
    @ApiStatus.Internal public static native int _nGetCurrentScriptTag(long ptr);
}