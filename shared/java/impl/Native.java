package org.jetbrains.skija.impl;

import java.lang.ref.*;
import org.jetbrains.annotations.*;

public abstract class Native {
    public long _ptr;

    public static long getPtr(Native n) { return n == null ? 0 : n._ptr; }

    public Native(long ptr) {
        if (ptr == 0)
            throw new RuntimeException("Can't wrap nullptr");
        this._ptr = ptr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(_ptr=0x" + Long.toString(_ptr, 16) + ")";
    }

    @Override
    public boolean equals(Object other) {
        try {
            if (this == other)
                return true;
            if (null == other)
                return false;
            if (!getClass().isInstance(other))
                return false;
            Native nOther = (Native) other;
            if (_ptr == nOther._ptr)
                return true;
            return _nativeEquals(nOther);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(other);
        }
    }

    @ApiStatus.Internal
    public boolean _nativeEquals(Native other) {
        return false;
    }

    // FIXME two different pointers might point to equal objects
    @Override
    public int hashCode() {
        return Long.hashCode(_ptr);
    }
}