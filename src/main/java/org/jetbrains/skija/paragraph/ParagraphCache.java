package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class ParagraphCache extends Native {
    static { Library.load(); }
    
    public void abandon() {
        _validate();
        Stats.onNativeCall();
        _nAbandon(_ptr);
    }

    public void reset() {
        _validate();
        Stats.onNativeCall();
        _nReset(_ptr);
    }

    public boolean updateParagraph(Paragraph paragraph) {
        _validate();
        Stats.onNativeCall();
        return _nUpdateParagraph(_ptr, Native.getPtr(paragraph));
    }

    public boolean findParagraph(Paragraph paragraph) {
        _validate();
        Stats.onNativeCall();
        return _nFindParagraph(_ptr, Native.getPtr(paragraph));
    }

    public void printStatistics() {
        _validate();
        Stats.onNativeCall();
        _nPrintStatistics(_ptr);
    }

    public void setEnabled(boolean value) {
        _validate();
        Stats.onNativeCall();
        _nSetEnabled(_ptr, value);
    }

    public int getCount() {
        _validate();
        Stats.onNativeCall();
        return _nGetCount(_ptr);
    }

    @ApiStatus.Internal
    public ParagraphCache(FontCollection owner, long ptr) {
        super(ptr);
        this._owner = owner;
    }

    @ApiStatus.Internal
    public final FontCollection _owner;

    @ApiStatus.Internal
    public void _validate() {
        if (_owner._ptr == 0)
            throw new IllegalStateException("ParagraphCache needs owning FontCollection to be alive");
    }

    @ApiStatus.Internal public static native void    _nAbandon(long ptr);
    @ApiStatus.Internal public static native void    _nReset(long ptr);
    @ApiStatus.Internal public static native boolean _nUpdateParagraph(long ptr, long paragraphPtr);
    @ApiStatus.Internal public static native boolean _nFindParagraph(long ptr, long paragraphPtr);
    @ApiStatus.Internal public static native void    _nPrintStatistics(long ptr);
    @ApiStatus.Internal public static native void    _nSetEnabled(long ptr, boolean value);
    @ApiStatus.Internal public static native int     _nGetCount(long ptr);
 }