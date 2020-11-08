package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class ParagraphStyle extends Managed {
    static { Library.staticLoad(); }
    
    public ParagraphStyle() {
        super(_nMake(), _finalizerPtr);
        Stats.onNativeCall();
    }

    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        Stats.onNativeCall();
        return _nEquals(_ptr, Native.getPtr(other));
    }

    public StrutStyle getStrutStyle() {
        Stats.onNativeCall();
        return new StrutStyle(_nGetStrutStyle(_ptr));
    }

    public ParagraphStyle setStrutStyle(StrutStyle s) {
        Stats.onNativeCall();
        _nSetStrutStyle(_ptr, Native.getPtr(s));
        return this;
    }

    public TextStyle getTextStyle() {
        Stats.onNativeCall();
        return new TextStyle(_nGetTextStyle(_ptr));
    }

    public ParagraphStyle setTextStyle(TextStyle style) {
        Stats.onNativeCall();
        _nSetTextStyle(_ptr, Native.getPtr(style));
        return this;
    }

    public Direction getDirection() {
        Stats.onNativeCall();
        return Direction.values()[_nGetDirection(_ptr)];
    }

    public ParagraphStyle setDirection(Direction style) {
        Stats.onNativeCall();
        _nSetDirection(_ptr, style.ordinal());
        return this;
    }

    public Alignment getAlignment() {
        Stats.onNativeCall();
        return Alignment.values()[_nGetAlignment(_ptr)];
    }

    public ParagraphStyle setAlignment(Alignment alignment) {
        Stats.onNativeCall();
        _nSetAlignment(_ptr, alignment.ordinal());
        return this;
    }

    public long getMaxLinesCount() {
        Stats.onNativeCall();
        return _nGetMaxLinesCount(_ptr);
    }

    public ParagraphStyle setMaxLinesCount(long count) {
        Stats.onNativeCall();
        _nSetMaxLinesCount(_ptr, count);
        return this;
    }

    public String getEllipsis() {
        Stats.onNativeCall();
        return _nGetEllipsis(_ptr);
    }

    public ParagraphStyle setEllipsis(String ellipsis) {
        Stats.onNativeCall();
        _nSetEllipsis(_ptr, ellipsis);
        return this;
    }

    public float getHeight() {
        Stats.onNativeCall();
        return _nGetHeight(_ptr);
    }

    public ParagraphStyle setHeight(float height) {
        Stats.onNativeCall();
        _nSetHeight(_ptr, height);
        return this;
    }

    public HeightMode getHeightMode() {
        Stats.onNativeCall();
        return HeightMode.values()[_nGetHeightMode(_ptr)];
    }

    public ParagraphStyle setHeightMode(HeightMode behavior) {
        Stats.onNativeCall();
        _nSetHeightMode(_ptr, behavior.ordinal());
        return this;
    }

    public Alignment getEffectiveAlignment() {
        Stats.onNativeCall();
        return Alignment.values()[_nGetEffectiveAlignment(_ptr)];
    }

    public boolean isHintingEnabled() {
        Stats.onNativeCall();
        return _nIsHintingEnabled(_ptr);
    }

    public ParagraphStyle disableHinting() {
        Stats.onNativeCall();
        _nDisableHinting(_ptr);
        return this;
    }

    @ApiStatus.Internal public static final  long    _finalizerPtr = _nGetFinalizer();
    @ApiStatus.Internal public static native long    _nGetFinalizer();
    @ApiStatus.Internal public static native long    _nMake();
    @ApiStatus.Internal public static native boolean _nEquals(long ptr, long otherPtr);
    @ApiStatus.Internal public static native long    _nGetStrutStyle(long ptr);
    @ApiStatus.Internal public static native void    _nSetStrutStyle(long ptr, long stylePtr);
    @ApiStatus.Internal public static native long    _nGetTextStyle(long ptr);
    @ApiStatus.Internal public static native void    _nSetTextStyle(long ptr, long textStylePtr);
    @ApiStatus.Internal public static native int     _nGetDirection(long ptr);
    @ApiStatus.Internal public static native void    _nSetDirection(long ptr, int direction);
    @ApiStatus.Internal public static native int     _nGetAlignment(long ptr);
    @ApiStatus.Internal public static native void    _nSetAlignment(long ptr, int align);
    @ApiStatus.Internal public static native long    _nGetMaxLinesCount(long ptr);
    @ApiStatus.Internal public static native void    _nSetMaxLinesCount(long ptr, long maxLines);
    @ApiStatus.Internal public static native String  _nGetEllipsis(long ptr);
    @ApiStatus.Internal public static native void    _nSetEllipsis(long ptr, String ellipsis);
    @ApiStatus.Internal public static native float   _nGetHeight(long ptr);
    @ApiStatus.Internal public static native void    _nSetHeight(long ptr, float height);
    @ApiStatus.Internal public static native int     _nGetHeightMode(long ptr);
    @ApiStatus.Internal public static native void    _nSetHeightMode(long ptr, int v);
    @ApiStatus.Internal public static native int     _nGetEffectiveAlignment(long ptr);
    @ApiStatus.Internal public static native boolean _nIsHintingEnabled(long ptr);
    @ApiStatus.Internal public static native void    _nDisableHinting(long ptr);
}