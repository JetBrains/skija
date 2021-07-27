package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class BackendRenderTarget extends Managed {
    static { Library.staticLoad(); }
    
    @NotNull @Contract("_, _, _, _, _, _ -> new")
    public static BackendRenderTarget makeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeGL(width, height, sampleCnt, stencilBits, fbId, fbFormat));
    }

    @NotNull @Contract("_, _, _ -> new")
    public static BackendRenderTarget makeMetal(int width, int height, long texturePtr) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeMetal(width, height, texturePtr));
    }

    /**
     * <p>Creates Direct3D backend render target object from D3D12 texture resource.</p>
     * <p>For more information refer to skia GrBackendRenderTarget class.</p>
     * 
     * @param width         width of the render target in pixels                                 
     * @param height        height of the render target in pixels
     * @param texturePtr    pointer to ID3D12Resource texture resource object; must be not zero
     * @param format        pixel data DXGI_FORMAT fromat of the texturePtr resource
     * @param sampleCnt     samples count for texture resource
     * @param levelCnt      sampling quality level for texture resource
     */ 
    @NotNull @Contract("_, _, _, _, _, _ -> new")
    public static BackendRenderTarget makeDirect3D(int width, int height, long texturePtr, int format, int sampleCnt, int levelCnt) {
        Stats.onNativeCall();
        return new BackendRenderTarget(_nMakeDirect3D(width, height, texturePtr, format, sampleCnt, levelCnt));
    }

    @ApiStatus.Internal
    public BackendRenderTarget(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMakeGL(int width, int height, int sampleCnt, int stencilBits, int fbId, int fbFormat);
    @ApiStatus.Internal public static native long _nMakeMetal(int width, int height, long texturePtr);
    @ApiStatus.Internal public static native long _nMakeDirect3D(int width, int height, long texturePtr, int format, int sampleCnt, int levelCnt);

}