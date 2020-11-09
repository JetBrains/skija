package org.jetbrains.skija.impl;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import lombok.*;
import org.jetbrains.annotations.*;

public class Library {
    @ApiStatus.Internal
    public static volatile boolean _loaded = false;

    public static void staticLoad() {
        if (!_loaded && !"false".equals(System.getProperty("skija.staticLoad")))
            load();
    }

    public static synchronized void load() {
        if (_loaded) return;

        File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + System.nanoTime());
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("mac") || os.contains("darwin")) {
            File library = _extract("/", "libskija.dylib", tempDir);
            System.load(library.getAbsolutePath());
        } else if (os.contains("windows")) {
            _extract("/", "icudtl.dat", tempDir);
            File library = _extract("/", "skija.dll", tempDir);
            System.load(library.getAbsolutePath());
        } else if (os.contains("nux") || os.contains("nix")) {
            File library = _extract("/", "libskija.so", tempDir);
            System.load(library.getAbsolutePath());
        } else
            throw new RuntimeException("Unknown operation system");
        
        _loaded = true;
        _nAfterLoad();
    }

    // https://github.com/adamheinrich/native-utils/blob/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/java/cz/adamh/utils/NativeUtils.java
    @ApiStatus.Internal
    @SneakyThrows
    public static File _extract(String resourcePath, String fileName, File tempDir) {
        File file;
        URL url = Library.class.getResource(resourcePath + fileName);
        if (url == null)
            throw new IllegalArgumentException("Library file " + fileName + " not found in " + resourcePath);
        else if (url.getProtocol() == "file") {
            // System.out.println("Loading " + url);
            file = new File(url.toURI());
        } else
            try (InputStream is = url.openStream()) {
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                    tempDir.deleteOnExit();
                }
                file = new File(tempDir, fileName);
                file.deleteOnExit();
                Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // System.out.println("Loading " + url + " from " + file);
            }
        return file;
    }

    @ApiStatus.Internal public static native void _nAfterLoad();
}
