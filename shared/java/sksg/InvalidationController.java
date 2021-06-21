package org.jetbrains.skija.sksg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

/**
 * <p>Receiver for invalidation events.</p>
 * <p>Tracks dirty regions for repaint.</p>
 */
public class InvalidationController extends Managed {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public InvalidationController(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public InvalidationController() {
        this(_nMake());
    }

    @NotNull @Contract("-> this")
    public InvalidationController invalidate(float left, float top, float right, float bottom, @Nullable Matrix33 matrix) {
        Stats.onNativeCall();
        _nInvalidate(_ptr, left, top, right, bottom, matrix == null ? Matrix33.IDENTITY._mat : matrix._mat);
        return this;
    }

    @NotNull
    public Rect getBounds() {
        try {
            Stats.onNativeCall();
            return _nGetBounds(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("-> this")
    public InvalidationController reset() {
        Stats.onNativeCall();
        _nReset(_ptr);
        return this;
    }

    public static native long _nGetFinalizer();
    public static native long _nMake();
    public static native void _nInvalidate(long ptr, float left, float top, float right, float bottom, float[] matrix);
    public static native Rect _nGetBounds(long ptr);
    public static native void _nReset(long ptr);
}