package org.jetbrains.skija.svg;

import org.jetbrains.annotations.*;

public enum SVGPreserveAspectRatioAlign {
    // These values are chosen such that bits [0,1] encode X alignment, and
    // bits [2,3] encode Y alignment.
    XMIN_YMIN(0x00),
    XMID_YMIN(0x01),
    XMAX_YMIN(0x02),
    XMIN_YMID(0x04),
    XMID_YMID(0x05),
    XMAX_YMID(0x06),
    XMIN_YMAX(0x08),
    XMID_YMAX(0x09),
    XMAX_YMAX(0x0a),
    NONE(0x10);

    @ApiStatus.Internal public static final SVGPreserveAspectRatioAlign[] _values = values();

    @ApiStatus.Internal public final int _value;

    @ApiStatus.Internal 
    SVGPreserveAspectRatioAlign(int value){
        this._value = value;
    }

    @ApiStatus.Internal 
    public static SVGPreserveAspectRatioAlign valueOf(int value) {
        switch (value) {
            case 0x00: return XMIN_YMIN;
            case 0x01: return XMID_YMIN;
            case 0x02: return XMAX_YMIN;
            case 0x04: return XMIN_YMID;
            case 0x05: return XMID_YMID;
            case 0x06: return XMAX_YMID;
            case 0x08: return XMIN_YMAX;
            case 0x09: return XMID_YMAX;
            case 0x0a: return XMAX_YMAX;
            case 0x10: return NONE;
            default: throw new IllegalArgumentException("Unknown SVGPreserveAspectRatioAlign value: " + value);
        }
    }
}