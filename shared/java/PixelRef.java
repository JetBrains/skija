package org.jetbrains.skija;

import java.lang.ref.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class PixelRef extends RefCnt {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public PixelRef(long ptr) {
        super(ptr);
    }

    public int getWidth() {
        try {
            Stats.onNativeCall();
            return _nGetWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int getHeight() {
        try {
            Stats.onNativeCall();
            return _nGetHeight(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public long getRowBytes() {
        try {
            Stats.onNativeCall();
            return _nGetRowBytes(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /** 
     * Returns a non-zero, unique value corresponding to the pixels in this
     * pixelref. Each time the pixels are changed (and notifyPixelsChanged is
     * called), a different generation ID will be returned.
     */
    public int getGenerationId() {
        try {
            Stats.onNativeCall();
            return _nGetGenerationId(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Call this if you have changed the contents of the pixels. This will in-
     * turn cause a different generation ID value to be returned from
     * getGenerationID().
     */
    public PixelRef notifyPixelsChanged() {
        Stats.onNativeCall();
        _nNotifyPixelsChanged(_ptr);
        return this;
    }

    /** 
     * Returns true if this pixelref is marked as immutable, meaning that the
     * contents of its pixels will not change for the lifetime of the pixelref.
     */
    public boolean isImmutable() {
        try {
            Stats.onNativeCall();
            return _nIsImmutable(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /** 
     * Marks this pixelref is immutable, meaning that the contents of its
     * pixels will not change for the lifetime of the pixelref. This state can
     * be set on a pixelref, but it cannot be cleared once it is set.
     */
    public PixelRef setImmutable() {
        Stats.onNativeCall();
        _nSetImmutable(_ptr);
        return this;
    }

    @ApiStatus.Internal public static native int  _nGetWidth(long ptr);
    @ApiStatus.Internal public static native int  _nGetHeight(long ptr);
    @ApiStatus.Internal public static native long _nGetRowBytes(long ptr);
    @ApiStatus.Internal public static native int  _nGetGenerationId(long ptr);
    @ApiStatus.Internal public static native void _nNotifyPixelsChanged(long ptr);
    @ApiStatus.Internal public static native boolean _nIsImmutable(long ptr);
    @ApiStatus.Internal public static native void _nSetImmutable(long ptr);
}