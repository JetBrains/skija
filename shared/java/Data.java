package org.jetbrains.skija;

import java.nio.ByteBuffer;
import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

/**
 *  Data holds an immutable data buffer.
 */
public class Data extends Managed {
    static { Library.staticLoad(); }

    public long getSize() {
        try {
            Stats.onNativeCall();
            return _nSize(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public byte[] getBytes() {
        return getBytes(0, getSize());
    }

    public byte[] getBytes(long offset, long length) {
        try {
            Stats.onNativeCall();
            return _nBytes(_ptr, offset, length);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     *  Returns true if these two objects have the same length and contents,
     *  effectively returning 0 == memcmp(...)
     */
    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        try {
            Stats.onNativeCall();
            return _nEquals(_ptr, Native.getPtr(other));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(other);
        }
    }

    public static Data makeFromBytes(byte[] bytes) {
        return makeFromBytes(bytes, 0, bytes.length);
    }

    public static Data makeFromBytes(byte[] bytes, long offset, long length) {
        Stats.onNativeCall();
        return new Data(_nMakeFromBytes(bytes, offset, length));
    }

    /**
     *  Create a new dataref the file with the specified path.
     *  If the file cannot be opened, this returns null.
     */
    public static Data makeFromFileName(String path) {
        Stats.onNativeCall();
        return new Data(_nMakeFromFileName(path));
    }

    /**
     *  Create a new dataref using a subset of the data in the specified
     *  src dataref.
     */
    public Data makeSubset(long offset, long length) {
        try {
            Stats.onNativeCall();
            return new Data(_nMakeSubset(_ptr, offset, length));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Data makeCopy() {
        try {
            Stats.onNativeCall();
            return new Data(_nMakeSubset(_ptr, 0, getSize()));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public ByteBuffer toByteBuffer() {
        try {
            Stats.onNativeCall();
            return _nToByteBuffer(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     *  Returns a new empty dataref (or a reference to a shared empty dataref).
     *  New or shared, the caller must see that {@link #close()} is eventually called.
     */
    public static Data makeEmpty() {
        Stats.onNativeCall();
        return new Data(_nMakeEmpty());
    }

    @ApiStatus.Internal
    public Data(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long    _nGetFinalizer();
    public static native long    _nSize(long ptr);
    public static native byte[]  _nBytes(long ptr, long offset, long length);
    public static native boolean _nEquals(long ptr, long otherPtr);
    public static native ByteBuffer _nToByteBuffer(long ptr);
    public static native long    _nMakeFromBytes(byte[] bytes, long offset, long length);
    public static native long    _nMakeFromFileName(String path);
    public static native long    _nMakeSubset(long ptr, long offset, long length);
    public static native long    _nMakeEmpty();
}