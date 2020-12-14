package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class FontStyleSet extends RefCnt {
    static { Library.staticLoad(); }
    
    public static FontStyleSet makeEmpty() {
        Stats.onNativeCall();
        return new FontStyleSet(_nMakeEmpty());
    }

    public int count() {
        try {
            Stats.onNativeCall();
            return _nCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public FontStyle getStyle(int index) {
        try {
            Stats.onNativeCall();
            return new FontStyle(_nGetStyle(_ptr, index));
        } finally {
            Reference.reachabilityFence(this);
        }
    }    

    public String getStyleName(int index) {
        try {
            Stats.onNativeCall();
            return _nGetStyleName(_ptr, index);
        } finally {
            Reference.reachabilityFence(this);
        }
    }        

    public Typeface getTypeface(int index) {
        try {
            Stats.onNativeCall();
            long ptr = _nGetTypeface(_ptr, index);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }    

    public Typeface matchStyle(FontStyle style) {
        try {
            Stats.onNativeCall();
            long ptr = _nMatchStyle(_ptr, style._value);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }    

    @ApiStatus.Internal
    public FontStyleSet(long ptr) {
        super(ptr);
    }

    public static native long _nMakeEmpty();
    public static native int _nCount(long ptr);
    public static native int _nGetStyle(long ptr, int index);
    public static native String _nGetStyleName(long ptr, int index);
    public static native long _nGetTypeface(long ptr, int index);
    public static native long _nMatchStyle(long ptr, int style);
}