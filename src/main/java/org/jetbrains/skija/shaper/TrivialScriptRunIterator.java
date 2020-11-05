package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;

public class TrivialScriptRunIterator extends TrivialRunIterator implements ScriptRunIterator {
    @ApiStatus.Internal
    public final String _script;

    public TrivialScriptRunIterator(long length, String script) {
        super(length);
        _script = script;
    }

    @Override
    public String getCurrentScript() {
        return _script;
    }
}