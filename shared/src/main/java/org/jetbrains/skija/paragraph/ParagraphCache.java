package org.jetbrains.skija.paragraph;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class ParagraphCache extends Native {
    static { Library.staticLoad(); }
    
    public void abandon() {
        try {
            _validate();
            Stats.onNativeCall();
            _nAbandon(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public void reset() {
        try {
            _validate();
            Stats.onNativeCall();
            _nReset(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean updateParagraph(Paragraph paragraph) {
        try {
            _validate();
            Stats.onNativeCall();
            return _nUpdateParagraph(_ptr, Native.getPtr(paragraph));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(paragraph);
        }
    }

    public boolean findParagraph(Paragraph paragraph) {
        try {
            _validate();
            Stats.onNativeCall();
            return _nFindParagraph(_ptr, Native.getPtr(paragraph));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(paragraph);
        }
    }

    public void printStatistics() {
        try {
            _validate();
            Stats.onNativeCall();
            _nPrintStatistics(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public void setEnabled(boolean value) {
        try {
            _validate();
            Stats.onNativeCall();
            _nSetEnabled(_ptr, value);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int getCount() {
        try {
            _validate();
            Stats.onNativeCall();
            return _nGetCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
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
        try {
            if (Native.getPtr(_owner) == 0)
                throw new IllegalStateException("ParagraphCache needs owning FontCollection to be alive");
        } finally {
            Reference.reachabilityFence(_owner);
        }
    }

    @ApiStatus.Internal public static native void    _nAbandon(long ptr);
    @ApiStatus.Internal public static native void    _nReset(long ptr);
    @ApiStatus.Internal public static native boolean _nUpdateParagraph(long ptr, long paragraphPtr);
    @ApiStatus.Internal public static native boolean _nFindParagraph(long ptr, long paragraphPtr);
    @ApiStatus.Internal public static native void    _nPrintStatistics(long ptr);
    @ApiStatus.Internal public static native void    _nSetEnabled(long ptr, boolean value);
    @ApiStatus.Internal public static native int     _nGetCount(long ptr);
 }