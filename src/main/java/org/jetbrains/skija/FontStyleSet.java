package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class FontStyleSet extends RefCnt {
    static { Library.load(); }
    
    public static FontStyleSet makeEmpty() {
        Stats.onNativeCall();
        return new FontStyleSet(_nMakeEmpty());
    }

    public int count() {
        Stats.onNativeCall();
        return _nCount(_ptr);
    }

    public FontStyle getStyle(int index) {
        Stats.onNativeCall();
        return new FontStyle(_nGetStyle(_ptr, index));
    }    

    public String getStyleName(int index) {
        Stats.onNativeCall();
        return _nGetStyleName(_ptr, index);
    }        

    public Typeface getTypeface(int index) {
        Stats.onNativeCall();
        long ptr = _nGetTypeface(_ptr, index);
        return ptr == 0 ? null : new Typeface(ptr);
    }    

    public Typeface matchStyle(FontStyle style) {
        Stats.onNativeCall();
        long ptr = _nMatchStyle(_ptr, style._value);
        return ptr == 0 ? null : new Typeface(ptr);
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