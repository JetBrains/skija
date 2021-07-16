package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Region extends Managed {
    static { Library.staticLoad(); }
    
    public enum Op {
        DIFFERENCE,
        INTERSECT,
        UNION,
        XOR,
        REVERSE_DIFFERENCE,
        REPLACE;

        @ApiStatus.Internal public static final Op[] _values = values();
    }

    public Region() {
        super(_nMake(), _FinalizerHolder.PTR);
        Stats.onNativeCall();
    }

    public boolean set(Region r) {
        try {
            Stats.onNativeCall();
            return _nSet(_ptr, Native.getPtr(r));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean isEmpty() {
        try {
            Stats.onNativeCall();
            return _nIsEmpty(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean isRect() {
        try {
            Stats.onNativeCall();
            return _nIsRect(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean isComplex() {
        try {
            Stats.onNativeCall();
            return _nIsComplex(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public IRect getBounds() {
        try {
            Stats.onNativeCall();
            return _nGetBounds(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public int computeRegionComplexity() {
        try {
            Stats.onNativeCall();
            return _nComputeRegionComplexity(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean getBoundaryPath(Path p) {
        try {
            Stats.onNativeCall();
            return _nGetBoundaryPath(_ptr, Native.getPtr(p));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(p);
        }
    }

    public boolean setEmpty() {
        try {
            Stats.onNativeCall();
            return _nSetEmpty(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean setRect(IRect rect) {
        try {
            Stats.onNativeCall();
            return _nSetRect(_ptr, rect._left, rect._top, rect._right, rect._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean setRects(IRect[] rects) {
        try {
            int[] arr = new int[rects.length * 4];
            for (int i = 0; i < rects.length; ++i) {
                arr[i * 4]     = rects[i]._left;
                arr[i * 4 + 1] = rects[i]._top;
                arr[i * 4 + 2] = rects[i]._right;
                arr[i * 4 + 3] = rects[i]._bottom;
            }
            Stats.onNativeCall();
            return _nSetRects(_ptr, arr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean setRegion(Region r) {
        try {
            Stats.onNativeCall();
            return _nSetRegion(_ptr, Native.getPtr(r));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean setPath(Path path, Region clip) {
        try {
            Stats.onNativeCall();
            return _nSetPath(_ptr, Native.getPtr(path), Native.getPtr(clip));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(path);
            Reference.reachabilityFence(clip);
        }
    }

    public boolean intersects(IRect rect) {
        try {
            Stats.onNativeCall();
            return _nIntersectsIRect(_ptr, rect._left, rect._top, rect._right, rect._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean intersects(Region r) {
        try {
            Stats.onNativeCall();
            return _nIntersectsRegion(_ptr, Native.getPtr(r));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean contains(int x, int y) {
        try {
            Stats.onNativeCall();
            return _nContainsIPoint(_ptr, x, y);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean contains(IRect rect) {
        try {
            Stats.onNativeCall();
            return _nContainsIRect(_ptr, rect._left, rect._top, rect._right, rect._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean contains(Region r) {
        try {
            Stats.onNativeCall();
            return _nContainsRegion(_ptr, Native.getPtr(r));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean quickContains(IRect rect) {
        try {
            Stats.onNativeCall();
            return _nQuickContains(_ptr, rect._left, rect._top, rect._right, rect._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean quickReject(IRect rect) {
        try {
            Stats.onNativeCall();
            return _nQuickRejectIRect(_ptr, rect._left, rect._top, rect._right, rect._bottom);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean quickReject(Region r) {
        try {
            Stats.onNativeCall();
            return _nQuickRejectRegion(_ptr, Native.getPtr(r));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public void translate(int dx, int dy) {
        try {
            Stats.onNativeCall();
            _nTranslate(_ptr, dx, dy);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean op(IRect rect, Op op) {
        try {
            Stats.onNativeCall();
            return _nOpIRect(_ptr, rect._left, rect._top, rect._right, rect._bottom, op.ordinal());
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public boolean op(Region r, Op op) {
        try {
            Stats.onNativeCall();
            return _nOpRegion(_ptr, Native.getPtr(r), op.ordinal());
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean op(IRect rect, Region r, Op op) {
        try {
            Stats.onNativeCall();
            return _nOpIRectRegion(_ptr, rect._left, rect._top, rect._right, rect._bottom, Native.getPtr(r), op.ordinal());
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean op(Region r, IRect rect, Op op) {
        try {
            Stats.onNativeCall();
            return _nOpRegionIRect(_ptr, Native.getPtr(r), rect._left, rect._top, rect._right, rect._bottom, op.ordinal());
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(r);
        }
    }

    public boolean op(Region a, Region b, Op op) {
        try {
            Stats.onNativeCall();
            return _nOpRegionRegion(_ptr, Native.getPtr(a), Native.getPtr(b), op.ordinal());
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(a);
            Reference.reachabilityFence(b);
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long    _nMake();
    public static native long    _nGetFinalizer();
    public static native boolean _nSet(long ptr, long regoinPtr);
    public static native boolean _nIsEmpty(long ptr);
    public static native boolean _nIsRect(long ptr);
    public static native boolean _nIsComplex(long ptr);
    public static native IRect   _nGetBounds(long ptr);
    public static native int     _nComputeRegionComplexity(long ptr);
    public static native boolean _nGetBoundaryPath(long ptr, long pathPtr);
    public static native boolean _nSetEmpty(long ptr);
    public static native boolean _nSetRect(long ptr, int left, int top, int right, int bottom);
    public static native boolean _nSetRects(long ptr, int[] rects);
    public static native boolean _nSetRegion(long ptr, long regionPtr);
    public static native boolean _nSetPath(long ptr, long pathPtr, long regionPtr);
    public static native boolean _nIntersectsIRect(long ptr, int left, int top, int right, int bottom);
    public static native boolean _nIntersectsRegion(long ptr, long regionPtr);
    public static native boolean _nContainsIPoint(long ptr, int x, int y);
    public static native boolean _nContainsIRect(long ptr, int left, int top, int right, int bottom);
    public static native boolean _nContainsRegion(long ptr, long regionPtr);
    public static native boolean _nQuickContains(long ptr, int left, int top, int right, int bottom);
    public static native boolean _nQuickRejectIRect(long ptr, int left, int top, int right, int bottom);
    public static native boolean _nQuickRejectRegion(long ptr, long regionPtr);
    public static native void    _nTranslate(long ptr, int dx, int dy);
    public static native boolean _nOpIRect(long ptr, int left, int top, int right, int bottom, int op);
    public static native boolean _nOpRegion(long ptr, long regionPtr, int op);
    public static native boolean _nOpIRectRegion(long ptr, int left, int top, int right, int bottom, long regionPtr, int op);
    public static native boolean _nOpRegionIRect(long ptr, long regionPtr, int left, int top, int right, int bottom, int op);
    public static native boolean _nOpRegionRegion(long ptr, long regionPtrA, long regionPtrB, int op);
}