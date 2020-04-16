package org.jetbrains.skija;

public class Context extends RefCounted {
    public static Context makeGL() {
        Native.onNativeCall(); 
        return new Context(nMakeGL());
    }

    public void flush() { Native.onNativeCall(); nFlush(nativeInstance); }

    protected Context(long nativeInstance) { super(nativeInstance); }

    private static native long nMakeGL();
    private static native long nFlush(long nativeInstance);
}