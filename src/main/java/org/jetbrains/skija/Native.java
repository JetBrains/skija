package org.jetbrains.skija;

public abstract class Native {
    protected long nativeInstance;

    protected Native(long nativeInstance) {
        if (nativeInstance == 0)
            throw new RuntimeException("nativeInstance returned nullptr");
        this.nativeInstance = nativeInstance;
    }

    public long getNativeInstance() {
        return nativeInstance;
    }

    @Override
    public boolean equals(Object object) {
        return this == object || (object != null && getClass() == object.getClass() && nativeInstance == ((Managed) object).nativeInstance);
    }

    @Override
    public int hashCode() { return Long.hashCode(nativeInstance); }

    @Override
    public String toString() { return "[" + getClass().getSimpleName() + " 0x" + Long.toString(nativeInstance, 16) + "]"; }
}