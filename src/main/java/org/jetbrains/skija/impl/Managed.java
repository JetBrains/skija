package org.jetbrains.skija.impl;

import java.lang.ref.Cleaner;

public abstract class Managed extends Native implements AutoCloseable {
    public final boolean _allowClose;
    public Cleaner.Cleanable _cleanable;

    public Managed(long ptr, long finalizer) {
        this(ptr, finalizer, true);
    }

    public Managed(long ptr, long finalizer, boolean allowClose) {
        this(ptr, finalizer, allowClose, true);
    }

    public Managed(long ptr, long finalizer, boolean allowClose, boolean isManaged) {
        super(ptr);
        if (isManaged) {
            String className = getClass().getSimpleName();
            Stats.onAllocated(className);
            this._cleanable = _cleaner.register(this, new CleanerThunk(className, ptr, finalizer));
            this._allowClose = allowClose;
        } else {
            this._cleanable = null;
            this._allowClose = false;
        }
    }

    @Override
    public void close() {
        if (_allowClose) {
            _cleanable.clean();
            _cleanable = null;
            _ptr = 0;
        } else
            throw new RuntimeException("close() is not allowed on " + this);
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