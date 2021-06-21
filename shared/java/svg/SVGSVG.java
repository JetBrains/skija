package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class SVGSVG extends SVGContainer {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public SVGSVG(long ptr) {
        super(ptr);
    }

    @NotNull
    public SVGLength getX() {
        try {
            Stats.onNativeCall();
            return _nGetX(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public SVGLength getY() {
        try {
            Stats.onNativeCall();
            return _nGetY(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public SVGLength getWidth() {
        try {
            Stats.onNativeCall();
            return _nGetWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public SVGLength getHeight() {
        try {
            Stats.onNativeCall();
            return _nGetHeight(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public SVGPreserveAspectRatio getPreserveAspectRatio() {
        try {
            Stats.onNativeCall();
            return _nGetPreserveAspectRatio(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @Nullable
    public Rect getViewBox() {
        try {
            Stats.onNativeCall();
            return _nGetViewBox(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public Point getIntrinsicSize(@NotNull SVGLengthContext lc) {
        try {
            Stats.onNativeCall();
            return _nGetIntrinsicSize(_ptr, lc._width, lc._height, lc._dpi);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setX(@NotNull SVGLength length) {
        try {
            Stats.onNativeCall();
            _nSetX(_ptr, length._value, length._unit.ordinal());
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setY(@NotNull SVGLength length) {
        try {
            Stats.onNativeCall();
            _nSetY(_ptr, length._value, length._unit.ordinal());
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setWidth(@NotNull SVGLength length) {
        try {
            Stats.onNativeCall();
            _nSetWidth(_ptr, length._value, length._unit.ordinal());
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setHeight(@NotNull SVGLength length) {
        try {
            Stats.onNativeCall();
            _nSetHeight(_ptr, length._value, length._unit.ordinal());
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setPreserveAspectRatio(@NotNull SVGPreserveAspectRatio ratio) {
        try {
            Stats.onNativeCall();
            _nSetPreserveAspectRatio(_ptr, ratio._align._value, ratio._scale.ordinal());
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("_ -> this")
    public SVGSVG setViewBox(@NotNull Rect viewBox) {
        try {
            Stats.onNativeCall();
            _nSetViewBox(_ptr, viewBox._left, viewBox._top, viewBox._right, viewBox._bottom);
            return this;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native SVGLength _nGetX(long ptr);
    @ApiStatus.Internal public static native SVGLength _nGetY(long ptr);
    @ApiStatus.Internal public static native SVGLength _nGetWidth(long ptr);
    @ApiStatus.Internal public static native SVGLength _nGetHeight(long ptr);
    @ApiStatus.Internal public static native SVGPreserveAspectRatio _nGetPreserveAspectRatio(long ptr);
    @ApiStatus.Internal public static native Rect _nGetViewBox(long ptr);
    @ApiStatus.Internal public static native Point _nGetIntrinsicSize(long ptr, float width, float height, float dpi);
    @ApiStatus.Internal public static native void _nSetX(long ptr, float value, int unit);
    @ApiStatus.Internal public static native void _nSetY(long ptr, float value, int unit);
    @ApiStatus.Internal public static native void _nSetWidth(long ptr, float value, int unit);
    @ApiStatus.Internal public static native void _nSetHeight(long ptr, float value, int unit);
    @ApiStatus.Internal public static native void _nSetPreserveAspectRatio(long ptr, int align, int scale);
    @ApiStatus.Internal public static native void _nSetViewBox(long ptr, float l, float t, float r, float b);
}