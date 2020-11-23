package org.jetbrains.skija.impl;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import lombok.*;
import org.jetbrains.annotations.*;

public class Library {
    private static boolean _loaded = false;
    private static boolean _loading = false;

    public static synchronized void staticLoad() {
         if (_loaded || _loading)
            return;

        _loading = true;
        try {
            Class clazz = Class.forName("org.jetbrains.skija.CustomLoader");
            Runnable customLoader = (Runnable)clazz.newInstance();
            customLoader.run();
            _loaded = true;
            return;
        } catch (ClassNotFoundException cnfe) {
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            _loading = false;
        }

        if (!_loaded)
            load();
    }

    public static synchronized void load() {
        if (_loaded) return;

        _loading = true;
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
        _loading = false;
        _nAfterLoad();
    }

    // https://github.com/adamheinrich/native-utils/blob/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/java/cz/adamh/utils/NativeUtils.java
    @ApiStatus.Internal
    public static File _extract(String resourcePath, String fileName, File tempDir) {
        // @SneakyThrows leak Lombok to the runtime.
        try {
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
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @ApiStatus.Internal public static native void _nAfterLoad();
}
