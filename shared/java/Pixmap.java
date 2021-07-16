package org.jetbrains.skija;

import java.lang.ref.*;
import java.nio.ByteBuffer;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.IRect;
import org.jetbrains.skija.ImageInfo;
import org.jetbrains.skija.SamplingMode;
import org.jetbrains.skija.impl.*;

public class Pixmap extends Managed {
    @ApiStatus.Internal
    public Pixmap(long ptr, boolean managed) {
        super(ptr, _FinalizerHolder.PTR, managed);
    }

    public Pixmap() {
        this(_nMakeNull(), true);
        Stats.onNativeCall();
    }

    public static Pixmap make(ImageInfo info, ByteBuffer buffer, int rowBytes) {
        return make(info, BufferUtil.getPointerFromByteBuffer(buffer), rowBytes);
    }

    public static Pixmap make(ImageInfo info, long addr, int rowBytes) {
        try {
            long ptr = _nMake(
                info._width, info._height,
                info._colorInfo._colorType.ordinal(),
                info._colorInfo._alphaType.ordinal(),
                Native.getPtr(info._colorInfo._colorSpace), addr, rowBytes);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Pixmap.");
            return new Pixmap(ptr, true);
        } finally {
            Reference.reachabilityFence(info._colorInfo._colorSpace);
        }
    }

    public void reset() {
        Stats.onNativeCall();
        _nReset(_ptr);
        Reference.reachabilityFence(this);
    }

    public void reset(ImageInfo info, long addr, int rowBytes) {
        Stats.onNativeCall();
        _nResetWithInfo(_ptr,
            info._width, info._height,
            info._colorInfo._colorType.ordinal(),
            info._colorInfo._alphaType.ordinal(),
            Native.getPtr(info._colorInfo._colorSpace), addr, rowBytes);
        Reference.reachabilityFence(this);
        Reference.reachabilityFence(info._colorInfo._colorSpace);
    }

    public void reset(ImageInfo info, ByteBuffer buffer, int rowBytes) {
        reset(info, BufferUtil.getPointerFromByteBuffer(buffer), rowBytes);
    }

    public void setColorSpace(ColorSpace colorSpace) {
        Stats.onNativeCall();
        _nSetColorSpace(_ptr, Native.getPtr(colorSpace));
        Reference.reachabilityFence(this);
        Reference.reachabilityFence(colorSpace);
    }

    public boolean extractSubset(long subsetPtr, IRect area) {
        try {
            Stats.onNativeCall();
            return _nExtractSubset(_ptr, subsetPtr, area._left, area._top, area._right, area._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean extractSubset(ByteBuffer buffer, IRect area) {
        return extractSubset(BufferUtil.getPointerFromByteBuffer(buffer), area);
    }

    public ImageInfo getInfo() {
        Stats.onNativeCall();
        try {
            return _nGetInfo(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int getRowBytes() {
        Stats.onNativeCall();
        try {
            return _nGetRowBytes(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public long getAddr() {
        Stats.onNativeCall();
        try {
            return _nGetAddr(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int getRowBytesAsPixels() {
        Stats.onNativeCall();
        try {
            return _nGetRowBytesAsPixels(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int computeByteSize() {
        Stats.onNativeCall();
        try {
            return _nComputeByteSize(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean computeIsOpaque() {
        Stats.onNativeCall();
        try {
            return _nComputeIsOpaque(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int getColor(int x, int y) {
        Stats.onNativeCall();
        try {
            return _nGetColor(_ptr, x, y);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public float getAlphaF(int x, int y) {
        Stats.onNativeCall();
        try {
            return _nGetAlphaF(_ptr, x, y);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public long getAddr(int x, int y) {
        Stats.onNativeCall();
        try {
            return _nGetAddrAt(_ptr, x, y);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean readPixels(ImageInfo info, long addr, int rowBytes) {
        Stats.onNativeCall();
        try {
            return _nReadPixels(_ptr,
                info._width, info._height,
                info._colorInfo._colorType.ordinal(),
                info._colorInfo._alphaType.ordinal(),
                Native.getPtr(info._colorInfo._colorSpace), addr, rowBytes);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(info._colorInfo._colorSpace);
        }
    }

    public boolean readPixels(ImageInfo info, long addr, int rowBytes, int srcX, int srcY) {
        Stats.onNativeCall();
        try {
            return _nReadPixelsFromPoint(_ptr,
                info._width, info._height,
                info._colorInfo._colorType.ordinal(),
                info._colorInfo._alphaType.ordinal(),
                Native.getPtr(info._colorInfo._colorSpace), addr, rowBytes,
                srcX, srcY);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(info._colorInfo._colorSpace);
        }
    }

    public boolean readPixels(Pixmap pixmap) {
        Stats.onNativeCall();
        try {
            return _nReadPixelsToPixmap(_ptr, Native.getPtr(pixmap));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(pixmap);
        }
    }

    public boolean readPixels(Pixmap pixmap, int srcX, int srcY) {
        Stats.onNativeCall();
        try {
            return _nReadPixelsToPixmapFromPoint(_ptr, Native.getPtr(pixmap), srcX, srcY);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(pixmap);
        }
    }

    public boolean scalePixels(Pixmap dstPixmap, SamplingMode samplingMode) {
        Stats.onNativeCall();
        try {
            return _nScalePixels(_ptr, Native.getPtr(dstPixmap), samplingMode._pack());
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(dstPixmap);
        }
    }

    public boolean erase(int color) {
        Stats.onNativeCall();
        try {
            return _nErase(_ptr, color);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean erase(int color, IRect subset) {
        Stats.onNativeCall();
        try {
            return _nEraseSubset(_ptr, color, subset._left, subset._top, subset._right, subset._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public ByteBuffer getBuffer() {
        return BufferUtil.getByteBufferFromPointer(getAddr(), computeByteSize());
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long _nGetFinalizer();
    public static native long _nMakeNull();
    public static native long _nMake(int width, int height, int colorType, int alphaType, long colorSpacePtr, long pixelsPtr, int rowBytes);

    public static native void _nReset(long ptr);
    public static native void _nResetWithInfo(long ptr, int width, int height, int colorType, int alphaType, long colorSpacePtr, long pixelsPtr, int rowBytes);
    public static native void _nSetColorSpace(long ptr, long colorSpacePtr);
    public static native boolean _nExtractSubset(long ptr, long subsetPtr, int l, int t, int r, int b);
    public static native ImageInfo _nGetInfo(long ptr);
    public static native int _nGetRowBytes(long ptr);
    public static native long _nGetAddr(long ptr);
    // TODO methods flattening ImageInfo not included yet - use GetInfo() instead.
    public static native int _nGetRowBytesAsPixels(long ptr);
    // TODO shiftPerPixel
    public static native int _nComputeByteSize(long ptr);
    public static native boolean _nComputeIsOpaque(long ptr);
    public static native int _nGetColor(long ptr, int x, int y);
    public static native float _nGetAlphaF(long ptr, int x, int y);
    public static native long _nGetAddrAt(long ptr, int x, int y);
    // methods related to C++ types(addr8/16/32/64, writable_addr8/16/32/64) not included - not needed
    public static native boolean _nReadPixels(long ptr, int width, int height, int colorType, int alphaType, long colorSpacePtr, long dstPixelsPtr, int dstRowBytes);
    public static native boolean _nReadPixelsFromPoint(long ptr, int width, int height, int colorType, int alphaType, long colorSpacePtr, long dstPixelsPtr, int dstRowBytes, int srcX, int srcY);
    public static native boolean _nReadPixelsToPixmap(long ptr, long dstPixmapPtr);
    public static native boolean _nReadPixelsToPixmapFromPoint(long ptr, long dstPixmapPtr, int srcX, int srcY);
    public static native boolean _nScalePixels(long ptr, long dstPixmapPtr, long samplingOptions);
    public static native boolean _nErase(long ptr, int color);
    public static native boolean _nEraseSubset(long ptr, int color, int l, int t, int r, int b);
    // TODO float erase methods not included
}