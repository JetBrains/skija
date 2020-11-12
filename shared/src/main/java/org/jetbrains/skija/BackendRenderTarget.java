package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class BackendRenderTarget extends Managed {
    static { Library.staticLoad(); }
    
    public static BackendRenderTarget makeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeGL(width, height, sampleCnt, stencilBits, fbId, fbFormat));
    }

    @ApiStatus.Internal
    public BackendRenderTarget(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long _nGetFinalizer();
    public static native long _nMakeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat);
}