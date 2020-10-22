package org.jetbrains.skija.impl;

import org.jetbrains.annotations.*;
import java.lang.ref.Cleaner;

public abstract class Managed extends Native implements AutoCloseable {
    @ApiStatus.Internal
    public Cleaner.Cleanable _cleanable;

    public Managed(long ptr, long finalizer) {
        this(ptr, finalizer, true);
    }

    public Managed(long ptr, long finalizer, boolean managed) {
        super(ptr);
        if (managed) {
            assert ptr != 0 : "Managed ptr is 0";
            assert finalizer != 0 : "Managed finalizer is 0";
            String className = getClass().getSimpleName();
            Stats.onAllocated(className);
            this._cleanable = _cleaner.register(this, new CleanerThunk(className, ptr, finalizer));
        }
    }

    @Override
    public void close() {
        if (0 == _ptr)
            throw new RuntimeException("Object already closed: " + this);
        else if (null == _cleanable)
            throw new RuntimeException("Object is not managed in JVM, can't close(): " + this);
        else {
            _cleanable.clean();
            _cleanable = null;
            _ptr = 0;
        }
    }

    public static Cleaner _cleaner = Cleaner.create();

    public static class CleanerThunk implements Runnable {
        public String _className;
        public long _ptr;
        public long _finalizerPtr;

        public CleanerThunk(String className, long ptr, long finalizer) {
            this._className = className;
            this._ptr = ptr;
            this._finalizerPtr = finalizer;
        }

        public void run() {
            Stats.onDeallocated(_className);
            Stats.onNativeCall();
            _nInvokeFinalizer(_finalizerPtr, _ptr);
        }
    }

    public static native void _nInvokeFinalizer(long finalizer, long ptr);
}