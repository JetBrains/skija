package org.jetbrains.skija;

public class Context extends RefCnt {
    public static Context makeGL() {
        Stats.onNativeCall();
        return new Context(nMakeGL());
    }

    public void flush() { Stats.onNativeCall(); nFlush(_ptr); }

    protected Context(long nativeInstance) { super(nativeInstance); }

    private static native long nMakeGL();
    private static native long nFlush(long nativeInstance);
}