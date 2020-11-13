package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Surface extends RefCnt {
    static { Library.staticLoad(); }
    
    public static Surface makeFromBackendRenderTarget(DirectContext context, BackendRenderTarget rt, SurfaceOrigin origin, SurfaceColorFormat colorFormat, ColorSpace colorSpace) {
        Stats.onNativeCall();
        long ptr = _nMakeFromBackendRenderTarget(Native.getPtr(context), Native.getPtr(rt), origin.ordinal(), colorFormat.ordinal(), Native.getPtr(colorSpace));
        return new Surface(ptr);
    }

    public static Surface makeRasterN32Premul(int width, int height) {
        Stats.onNativeCall();
        long ptr = _nMakeRasterN32Premul(width, height);
        return new Surface(ptr);
    }

    public Canvas getCanvas() {
        Stats.onNativeCall();
        return new Canvas(_nGetCanvas(_ptr), false);
    }

    public Image makeImageSnapshot() {
        Stats.onNativeCall();
        return new Image(_nMakeImageSnapshot(_ptr));
    }

    @ApiStatus.Internal
    public Surface(long ptr) {
        super(ptr);
    }

    public static native long _nMakeFromBackendRenderTarget(long pContext, long pBackendRenderTarget, int surfaceOrigin, int colorType, long colorSpacePtr);
    public static native long _nMakeRasterN32Premul(int width, int height);
    public static native long _nMakeImageSnapshot(long ptr);
    public static native long _nGetCanvas(long ptr);
}