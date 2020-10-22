package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class StrutStyle extends Managed {
    static { Library.load(); }
    
    @ApiStatus.Internal
    public StrutStyle(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public StrutStyle() {
        this(_nMake());
        Stats.onNativeCall();
    }

    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        Stats.onNativeCall();
        return _nEquals(_ptr, Native.getPtr(other));
    }

    public String[] getFontFamilies() {
        Stats.onNativeCall();
        return _nGetFontFamilies(_ptr);
    }

    public StrutStyle setFontFamilies(String[] families) {
        Stats.onNativeCall();
        _nSetFontFamilies(_ptr, families);
        return this;
    }

    public FontStyle getFontStyle() {
        Stats.onNativeCall();
        return new FontStyle(_nGetFontStyle(_ptr));
    }

    public StrutStyle setFontStyle(FontStyle style) {
        Stats.onNativeCall();
        _nSetFontStyle(_ptr, style._value);
        return this;
    }

    public float getFontSize() {
        Stats.onNativeCall();
        return _nGetFontSize(_ptr);
    }

    public StrutStyle setFontSize(float value) {
        Stats.onNativeCall();
        _nSetFontSize(_ptr, value);
        return this;
    }

    public float getHeight() {
        Stats.onNativeCall();
        return _nGetHeight(_ptr);
    }

    public StrutStyle setHeight(float value) {
        Stats.onNativeCall();
        _nSetHeight(_ptr, value);
        return this;
    }

    public float getLeading() {
        Stats.onNativeCall();
        return _nGetLeading(_ptr);
    }

    public StrutStyle setLeading(float value) {
        Stats.onNativeCall();
        _nSetLeading(_ptr, value);
        return this;
    }

    public boolean isEnabled() {
        Stats.onNativeCall();
        return _nIsEnabled(_ptr);
    }

    public StrutStyle setEnabled(boolean value) {
        Stats.onNativeCall();
        _nSetEnabled(_ptr, value);
        return this;
    }

    public boolean isHeightForced() {
        Stats.onNativeCall();
        return _nIsHeightForced(_ptr);
    }

    public StrutStyle setHeightForced(boolean value) {
        Stats.onNativeCall();
        _nSetHeightForced(_ptr, value);
        return this;
    }

    public boolean isHeightOverridden() {
        Stats.onNativeCall();
        return _nIsHeightOverridden(_ptr);
    }

    public StrutStyle setHeightOverridden(boolean value) {
        Stats.onNativeCall();
        _nSetHeightOverridden(_ptr, value);
        return this;
    }

    @ApiStatus.Internal public static final  long     _finalizerPtr = _nGetFinalizer();
    @ApiStatus.Internal public static native long     _nGetFinalizer();
    @ApiStatus.Internal public static native long     _nMake();
    @ApiStatus.Internal public static native boolean  _nEquals(long ptr, long otherPtr);
    @ApiStatus.Internal public static native String[] _nGetFontFamilies(long ptr);
    @ApiStatus.Internal public static native void     _nSetFontFamilies(long ptr, String[] families);
    @ApiStatus.Internal public static native int      _nGetFontStyle(long ptr);
    @ApiStatus.Internal public static native void     _nSetFontStyle(long ptr, int value);
    @ApiStatus.Internal public static native float    _nGetFontSize(long ptr);
    @ApiStatus.Internal public static native void     _nSetFontSize(long ptr, float value);
    @ApiStatus.Internal public static native float    _nGetHeight(long ptr);
    @ApiStatus.Internal public static native void     _nSetHeight(long ptr, float value);
    @ApiStatus.Internal public static native float    _nGetLeading(long ptr);
    @ApiStatus.Internal public static native void     _nSetLeading(long ptr, float value);
    @ApiStatus.Internal public static native boolean  _nIsEnabled(long ptr);
    @ApiStatus.Internal public static native void     _nSetEnabled(long ptr, boolean value);
    @ApiStatus.Internal public static native boolean  _nIsHeightForced(long ptr);
    @ApiStatus.Internal public static native void     _nSetHeightForced(long ptr, boolean value);
    @ApiStatus.Internal public static native boolean  _nIsHeightOverridden(long ptr);
    @ApiStatus.Internal public static native void     _nSetHeightOverridden(long ptr, boolean value);
}