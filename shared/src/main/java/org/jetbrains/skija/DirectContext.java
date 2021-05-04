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
        for (var state: states)
            flags |= state._bit;
        _nReset(_ptr, flags);
        return this;
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
    @ApiStatus.Internal public static native long _nFlush(long ptr);
    @ApiStatus.Internal public static native void _nReset(long ptr, int flags);
    @ApiStatus.Internal public static native void _nAbandon(long ptr);
}