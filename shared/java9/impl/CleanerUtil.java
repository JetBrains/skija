package org.jetbrains.skija.impl;

import java.lang.ref.Cleaner;
import java.util.Objects;

public class CleanerUtil {

    public interface Cleanable {
        void clean();
    }

    private static final Cleaner cleaner = Cleaner.create();

    public static Cleanable register(Object obj, Runnable action) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(action);

        return cleaner.register(obj, action)::clean;
    }
}
