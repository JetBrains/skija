package org.jetbrains.skija.impl;

import java.nio.ByteBuffer;

public class BufferUtil {
    public static ByteBuffer getByteBufferFromPointer(long ptr, int size) {
        ByteBuffer result = _nGetByteBufferFromPointer(ptr, size);
        if (result == null)
            throw new IllegalArgumentException("JNI direct buffer access not support by current JVM!");
        return result;
    }

    public static long getPointerFromByteBuffer(ByteBuffer buffer) {
        long result = _nGetPointerFromByteBuffer(buffer);
        if (result == 0)
            throw new IllegalArgumentException("The given buffer " + buffer + "is not a direct buffer or current JVM doesn't support JNI direct buffer access!");
        return result;
    }

    public static native ByteBuffer _nGetByteBufferFromPointer(long ptr, int size);
    public static native long _nGetPointerFromByteBuffer(ByteBuffer buffer);
}