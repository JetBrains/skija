package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class FontMgrRunIterator extends ManagedRunIterator<FontRun> {
    static { Library.staticLoad(); }

    public FontMgrRunIterator(ManagedString text, boolean manageText, Font font, FontMgr fontMgr) {
        super(_nMake(Native.getPtr(text), Native.getPtr(font), Native.getPtr(fontMgr)), text, manageText);
        Stats.onNativeCall();
    }

    public FontMgrRunIterator(String text, Font font, @Nullable FontMgr fontMgr) {
        this(new ManagedString(text), true, font, fontMgr);
    }

    @Override
    public FontRun next() {
        _nConsume(_ptr);
        return new FontRun(_getEndOfCurrentRun(), new Font(_nGetCurrentFont(_ptr)));
    }

    @ApiStatus.Internal public static native long _nMake(long textPtr, long fontPtr, long fontMgrPtr);
    @ApiStatus.Internal public static native long _nGetCurrentFont(long ptr);
}