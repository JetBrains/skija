package org.jetbrains.skija;

import lombok.Data;

@Data
public class FontExtents {
    public final float _ascender;
    public final float _descender;
    public final float _lineGap;

    public float getAscenderAbs() {
        return Math.abs(_ascender);
    }

    public float getLineHeight() {
        return -_ascender + _descender + _lineGap;
    }
}
