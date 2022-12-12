package org.jetbrains.skija.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;

public class CleanerUtil {

    public interface Cleanable {
        void clean();
    }

    private static final MethodHandle createCleanerHandle;
    private static final MethodHandle cleanHandle;

    static {
        try {
            Class<?> cleanerClass = Class.forName("sun.misc.Cleaner");
            createCleanerHandle = MethodHandles.publicLookup().findStatic(cleanerClass, "create", MethodType.methodType(cleanerClass, Object.class, Runnable.class));
            cleanHandle = MethodHandles.publicLookup().findVirtual(cleanerClass, "clean", MethodType.methodType(void.class));
        } catch (Throwable e) {
            throw new NoClassDefFoundError("sun.misc.Cleaner");
        }
    }

    public static Cleanable register(Object obj, Runnable action) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(action);
        try {
            Object cleaner = createCleanerHandle.invoke(obj, action);
            return () -> {
                try {
                    cleanHandle.invokeWithArguments(cleaner);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Throwable e) {
            throw new RuntimeException("Unreachable", e);
        }
    }
}
