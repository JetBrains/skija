package org.jetbrains.skija.shaper;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class FontMgrRunIterator extends ManagedRunIterator<FontRun> {
    static { Library.staticLoad(); }

    public FontMgrRunIterator(ManagedString text, boolean manageText, Font font, FontMgr fontMgr) {
        super(_nMake(Native.getPtr(text), Native.getPtr(font), Native.getPtr(fontMgr)), text, manageText);
        Stats.onNativeCall();
        Reference.reachabilityFence(text);
        Reference.reachabilityFence(font);
        Reference.reachabilityFence(fontMgr);
    }

    public FontMgrRunIterator(String text, Font font, @Nullable FontMgr fontMgr) {
        this(new ManagedString(text), true, font, fontMgr);
    }

    @Override
    public FontRun next() {
        try {
            _nConsume(_ptr);
            return new FontRun(_getEndOfCurrentRun(), new Font(_nGetCurrentFont(_ptr)));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr, long fontPtr, long fontMgrPtr);
    @ApiStatus.Internal public static native long _nGetCurrentFont(long ptr);
}