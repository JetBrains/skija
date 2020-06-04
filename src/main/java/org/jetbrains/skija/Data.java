package org.jetbrains.skija;

/**
 *  Data holds an immutable data buffer.
 */
public class Data extends Managed {

    public long size() {
        Native.onNativeCall();
        return nSize(nativeInstance);
    }

    public byte[] bytes() {
        return bytes(0, size());
    }

    public byte[] bytes(long offset, long length) {
        Native.onNativeCall();
        return nBytes(nativeInstance, offset, length);
    }

    /**
     *  Returns true if these two objects have the same length and contents,
     *  effectively returning 0 == memcmp(...)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data od = (Data) o;
        if (nativeInstance == od.nativeInstance) return true;
        Native.onNativeCall();
        return nEquals(nativeInstance, od.nativeInstance);
    }

    public static Data makeFromBytes(byte[] bytes) {
        return makeFromBytes(bytes, 0, bytes.length);
    }

    public static Data makeFromBytes(byte[] bytes, long offset, long length) {
        Native.onNativeCall();
        return new Data(nMakeFromBytes(bytes, offset, length));
    }

    /**
     *  Create a new dataref the file with the specified path.
     *  If the file cannot be opened, this returns null.
     */
    public static Data makeFromFileName(String path) {
        Native.onNativeCall();
        return new Data(nMakeFromFileName(path));
    }

    /**
     *  Create a new dataref using a subset of the data in the specified
     *  src dataref.
     */
    public Data makeSubset(long offset, long length) {
        Native.onNativeCall();
        return new Data(nMakeSubset(nativeInstance, offset, length));
    }

    public Data makeCopy() {
        Native.onNativeCall();
        return new Data(nMakeSubset(nativeInstance, 0, size()));
    }

    /**
     *  Returns a new empty dataref (or a reference to a shared empty dataref).
     *  New or shared, the caller must see that {@link close()} is eventually called.
     */
    public static Data makeEmpty() {
        Native.onNativeCall();
        return new Data(nMakeEmpty());
    }

    protected Data(long ptr) {
        super(ptr, nativeFinalizer);
        Native.onNativeCall();
    }

    private static final  long    nativeFinalizer = nGetNativeFinalizer();
    private static native long    nGetNativeFinalizer();
    private static native long    nSize(long ptr);
    private static native byte[]  nBytes(long ptr, long offset, long length);
    private static native boolean nEquals(long ptr, long otherPtr);
    private static native long    nMakeFromBytes(byte[] bytes, long offset, long length);
    private static native long    nMakeFromFileName(String path);
    private static native long    nMakeSubset(long ptr, long offset, long length);
    private static native long    nMakeEmpty();
}