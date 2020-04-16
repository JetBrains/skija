package org.jetbrains.skija;

public class Context extends RefCounted {
    public static Context makeGL() {
        return new Context(nMakeGL());
    }

    public void flush() { nFlush(nativeInstance); }

    protected Context(long nativeInstance) { super(nativeInstance); }

    private static native long nMakeGL();
    private static native long nFlush(long nativeInstance);
}