package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.RefCnt;
import org.jetbrains.skija.impl.Stats;

public class Image extends RefCnt {
    public int _width = -1;
    public int _height = -1;

    public static Image makeFromEncoded(byte[] bytes) {
        Stats.onNativeCall();
        return new Image(_nMakeFromEncoded(bytes));
    }

    public int getWidth() {
        if (_width == -1) _getDimensions();
        return _width;
    }

    public int getHeight() {
        if (_height == -1) _getDimensions();
        return _height;
    }

    public Data encodeToData() {
        Stats.onNativeCall();
        return new Data(_nEncodeToData(_ptr));
    }

    public void _getDimensions() {
        Stats.onNativeCall();
        long res = _nGetDimensions(_ptr);
        _width = (int) (res & 0xFFFFFFFF);
        _height = (int) (res >>> 32);
    }

    @ApiStatus.Internal
    public Image(long ptr) {
        super(ptr);
    }

    public static native long _nMakeFromEncoded(byte[] bytes);
    public static native long _nGetDimensions(long ptr);
    public static native long _nEncodeToData(long ptr);
}