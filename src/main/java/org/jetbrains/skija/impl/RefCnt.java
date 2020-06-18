package org.jetbrains.skija.impl;

public abstract class RefCnt extends Managed {
    protected RefCnt(long ptr) {
        super(ptr, _finalizerPtrPtr);
    }

    protected RefCnt(long ptr, boolean allowClose) {
        super(ptr, _finalizerPtrPtr, allowClose);
    }

    public int getRefCount() {
        Stats.onNativeCall();
        return _nGetRefCount(_ptr);
    }

    @Override
    public String toString() {
        String s = super.toString();
        return s.substring(0, s.length() - 1) + ", refCount=" + getRefCount() + ")";
    }

    public static long _finalizerPtrPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native int  _nGetRefCount(long ptr);
}