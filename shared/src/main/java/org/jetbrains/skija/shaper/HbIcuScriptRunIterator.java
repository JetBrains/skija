package org.jetbrains.skija.shaper;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class HbIcuScriptRunIterator extends ManagedRunIterator<ScriptRun> {
    static { Library.staticLoad(); }

    public HbIcuScriptRunIterator(ManagedString text, boolean manageText) {
        super(_nMake(Native.getPtr(text)), text, manageText);
        Stats.onNativeCall();
        Reference.reachabilityFence(text);
    }

    public HbIcuScriptRunIterator(String text) {
        this(new ManagedString(text), true);
    }

    @Override
    public ScriptRun next() {
        try {
            _nConsume(_ptr);
            return new ScriptRun(_getEndOfCurrentRun(), _nGetCurrentScriptTag(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr);
    @ApiStatus.Internal public static native int _nGetCurrentScriptTag(long ptr);
}