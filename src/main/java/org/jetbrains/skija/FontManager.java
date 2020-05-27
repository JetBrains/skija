package org.jetbrains.skija;

public class FontManager extends RefCounted {
    private static class DefaultHolder {
        static { Native.onNativeCall(); }
        public static final FontManager INSTANCE = new FontManager(nDefault(), false);
    }

    public static FontManager getDefault() { return DefaultHolder.INSTANCE; }

    protected FontManager(long nativeInstance) { super(nativeInstance); }
    protected FontManager(long nativeInstance, boolean allowClose) { super(nativeInstance, allowClose); }
    private static native long nDefault();
}