package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class DirectContext extends RefCnt {
    static { Library.staticLoad(); }

    @NotNull @Contract("-> new")    
    public static DirectContext makeGL() {
        Stats.onNativeCall();
        return new DirectContext(_nMakeGL());
    }

    @NotNull @Contract("-> this")
    public static DirectContext makeMetal(long devicePtr, long queuePtr) {
        Stats.onNativeCall();
        return new DirectContext(_nMakeMetal(devicePtr, queuePtr));
    }

    /**
     * <p>Creates Direct3D direct rendering context from D3D12 native objects.</p>
     * <p>For more information refer to skia GrDirectContext class.</p>
     * 
     * @param adapterPtr    pointer to IDXGIAdapter1 object; must be not zero
     * @param devicePtr     pointer to ID3D12Device objetc, which is created with 
     *                      provided adapter in adapterPtr; must be not zero 
     * @param queuePtr      Pointer to ID3D12CommandQueue object, which
     *                      is created with provided device in devicePtr with
     *                      type D3D12_COMMAND_LIST_TYPE_DIRECT; must be not zero 
     */ 
    @NotNull @Contract("-> this")
    public static DirectContext makeDirect3D(long adapterPtr, long devicePtr, long queuePtr) {
        Stats.onNativeCall();
        return new DirectContext(_nMakeDirect3D(adapterPtr, devicePtr, queuePtr));
    }

    @NotNull @Contract("-> this")
    public DirectContext flush() {
        Stats.onNativeCall();
        _nFlush(_ptr);
        return this;
    }

    @NotNull @Contract("-> this")
    public DirectContext resetAll() {
        Stats.onNativeCall();
        _nReset(_ptr, -1);
        return this;
    }

    @NotNull @Contract("-> this")
    public DirectContext resetGLAll() {
        Stats.onNativeCall();
        _nReset(_ptr, 0xffff);
        return this;
    }

    @NotNull @Contract("_ -> this")
    public DirectContext resetGL(GLBackendState... states) {
        Stats.onNativeCall();
        int flags = 0;
        for (GLBackendState state: states)
            flags |= state._bit;
        _nReset(_ptr, flags);
        return this;
    }

    /**
     * <p>Submit outstanding work to the gpu from all previously un-submitted flushes.</p>
     * <p>If the syncCpu flag is true this function will return once the gpu has finished with all submitted work.</p>
     * <p>For more information refer to skia GrDirectContext::submit(bool syncCpu) method.</p>
     * 
     * @param syncCpu flag to sync cpu and gpu work submission
     */ 
    public void submit(boolean syncCpu) {
        Stats.onNativeCall();
        _nSubmit(_ptr, syncCpu);
    }

    /**
     * <p>Abandons all GPU resources and assumes the underlying backend 3D API context is no longer
     * usable. Call this if you have lost the associated GPU context, and thus internal texture,
     * buffer, etc. references/IDs are now invalid. Calling this ensures that the destructors of the
     * context and any of its created resource objects will not make backend 3D API calls. Content
     * rendered but not previously flushed may be lost. After this function is called all subsequent
     * calls on the context will fail or be no-ops.</p>
     *
     * <p>The typical use case for this function is that the underlying 3D context was lost and further
     * API calls may crash.</p>
     *
     * <p>For Vulkan, even if the device becomes lost, the VkQueue, VkDevice, or VkInstance used to
     * create the context must be kept alive even after abandoning the context. Those objects must
     * live for the lifetime of the context object itself. The reason for this is so that
     * we can continue to delete any outstanding GrBackendTextures/RenderTargets which must be
     * cleaned up even in a device lost state.</p>
     */
    public void abandon() {
        try {
            Stats.onNativeCall();
            _nAbandon(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
   }

    @ApiStatus.Internal
    public DirectContext(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal public static native long _nMakeGL();
    @ApiStatus.Internal public static native long _nMakeMetal(long devicePtr, long queuePtr);
    @ApiStatus.Internal public static native long _nMakeDirect3D(long adapterPtr, long devicePtr, long queuePtr);
    @ApiStatus.Internal public static native long _nFlush(long ptr);
    @ApiStatus.Internal public static native long _nSubmit(long ptr, boolean syncCpu);
    @ApiStatus.Internal public static native void _nReset(long ptr, int flags);
    @ApiStatus.Internal public static native void _nAbandon(long ptr);
}