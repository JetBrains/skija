package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

/**
 *  Data holds an immutable data buffer.
 */
public class Data extends Managed {
    public long getSize() {
        Stats.onNativeCall();
        return _nSize(_ptr);
    }

    public byte[] getBytes() {
        return getBytes(0, getSize());
    }

    public byte[] getBytes(long offset, long length) {
        Stats.onNativeCall();
        return _nBytes(_ptr, offset, length);
    }

    /**
     *  Returns true if these two objects have the same length and contents,
     *  effectively returning 0 == memcmp(...)
     */
    @Override
    public boolean nativeEquals(Native other) {
        Stats.onNativeCall();
        return _nEquals(_ptr, Native.getPtr(other));
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
        Stats.onNativeCall();
        return new Data(_nMakeSubset(_ptr, offset, length));
    }

    public Data makeCopy() {
        Stats.onNativeCall();
        return new Data(_nMakeSubset(_ptr, 0, getSize()));
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
        super(ptr, _finalizerPtr);
        Stats.onNativeCall();
    }

    public static final  long    _finalizerPtr = _nGetFinalizer();
    public static native long    _nGetFinalizer();
    public static native long    _nSize(long ptr);
    public static native byte[]  _nBytes(long ptr, long offset, long length);
    public static native boolean _nEquals(long ptr, long otherPtr);
    public static native long    _nMakeFromBytes(byte[] bytes, long offset, long length);
    public static native long    _nMakeFromFileName(String path);
    public static native long    _nMakeSubset(long ptr, long offset, long length);
    public static native long    _nMakeEmpty();
}