package org.jetbrains.skija.skottie;

import org.jetbrains.annotations.*;

public enum LogLevel {
    WARNING,
    ERROR;

    @ApiStatus.Internal public static final LogLevel[] _values = values();
}