package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Stats;

public class BackendRenderTarget extends Managed {
    public static BackendRenderTarget makeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeGL(width, height, sampleCnt, stencilBits, fbId, fbFormat));
    }

    @ApiStatus.Internal
    public BackendRenderTarget(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native long _nMakeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat);
}