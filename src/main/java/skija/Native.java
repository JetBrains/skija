package skija;

public abstract class Native {
    protected long mNativeInstance;

    protected Native(long nativeInstance) {
        if (nativeInstance == 0)
            throw new RuntimeException("Native returned nullptr");
        mNativeInstance = nativeInstance;
    }

    public long getNativeInstance() {
        return mNativeInstance;
    }

    @Override
    public boolean equals(Object object) {
        return this == object || (object != null && getClass() == object.getClass() && mNativeInstance == ((Managed) object).mNativeInstance);
    }

    @Override
    public int hashCode() { return Long.hashCode(mNativeInstance); }

    @Override
    public String toString() { return "[" + getClass().getSimpleName() + " 0x" + Long.toString(mNativeInstance, 16) + "]"; }
}