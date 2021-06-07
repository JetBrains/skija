package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class SVGDOM extends RefCnt {
    static { Library.staticLoad(); }

    public SVGDOM(@NotNull Data data) {
        this(_nMakeFromData(Native.getPtr(data)));
        Stats.onNativeCall();
        Reference.reachabilityFence(data);
    }

    @Nullable
    public SVGSVG getRoot() {
        try {
            Stats.onNativeCall();
            long ptr = _nGetRoot(_ptr);
            return ptr == 0 ? null : new SVGSVG(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Deprecated. Use getRoot().intrinsicSize() instead
     */
    @NotNull @Deprecated
    public Point getContainerSize() {
        try {
            return _nGetContainerSize(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("-> this")
    public SVGDOM setContainerSize(float width, float height) {
        Stats.onNativeCall();
        _nSetContainerSize(_ptr, width, height);
        return this;
    }

    @NotNull @Contract("-> this")
    public SVGDOM setContainerSize(Point size) {
        Stats.onNativeCall();
        _nSetContainerSize(_ptr, size._x, size._y);
        return this;
    }

    // sk_sp<SkSVGNode>* findNodeById(const char* id);

    @NotNull @Contract("-> this")
    public SVGDOM render(@NotNull Canvas canvas) {
        try {
            Stats.onNativeCall();
            _nRender(_ptr, Native.getPtr(canvas));
            return this;
        } finally {
            Reference.reachabilityFence(canvas);
        }
    }

    @ApiStatus.Internal
    public SVGDOM(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal public static native long  _nMakeFromData(long dataPtr);
    @ApiStatus.Internal public static native long  _nGetRoot(long ptr);
    @ApiStatus.Internal public static native Point _nGetContainerSize(long ptr);
    @ApiStatus.Internal public static native void  _nSetContainerSize(long ptr, float width, float height);
    @ApiStatus.Internal public static native void  _nRender(long ptr, long canvasPtr);
}