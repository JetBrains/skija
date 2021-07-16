package org.jetbrains.skija.impl;

import org.jetbrains.annotations.*;

public enum Platform {
    WINDOWS,
    LINUX,
    MACOS_X64,
    MACOS_ARM64;

    @ApiStatus.Internal public static final Platform[] _values = values();

    public static final Platform CURRENT;

    static {
        String os = System.getProperty("os.name").toLowerCase();        
        if (os.contains("mac") || os.contains("darwin")) {
            if ("aarch64".equals(System.getProperty("os.arch")))
                CURRENT = MACOS_ARM64;
            else
                CURRENT = MACOS_X64;
        } else if (os.contains("windows"))
            CURRENT = WINDOWS;
        else if (os.contains("nux") || os.contains("nix"))
            CURRENT = LINUX;
        else
            throw new RuntimeException("Unsupported platform: " + os);
    }
}