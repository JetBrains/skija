package org.jetbrains.skija;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
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
}