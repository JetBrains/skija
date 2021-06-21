package org.jetbrains.skija.impl;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import lombok.*;
import org.jetbrains.annotations.*;

public class Library {
    @ApiStatus.Internal
    public static volatile boolean _loaded = false;

    @ApiStatus.Internal
    public static Class<?> _nativeLibraryClass = null;

    @ApiStatus.Internal
    public static String _resourcePath = null;    

    static {
        try {
            switch (Platform.CURRENT) {
            case WINDOWS:
                _nativeLibraryClass = Class.forName("org.jetbrains.skija.windows.LibraryFinder");
                _resourcePath = "/org/jetbrains/skija/windows/";
                break;
            case LINUX:
                _nativeLibraryClass = Class.forName("org.jetbrains.skija.linux.LibraryFinder");
                _resourcePath = "/org/jetbrains/skija/linux/";
                break;
            case MACOS_X64:
                _nativeLibraryClass = Class.forName("org.jetbrains.skija.macos.x64.LibraryFinder");
                _resourcePath = "/org/jetbrains/skija/macos/x64/";
                break;
            case MACOS_ARM64:
                _nativeLibraryClass = Class.forName("org.jetbrains.skija.macos.arm64.LibraryFinder");
                _resourcePath = "/org/jetbrains/skija/macos/arm64/";
                break;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void staticLoad() {
        if (!_loaded && !"false".equals(System.getProperty("skija.staticLoad")))
            load();
    }

    public static String readResource(String path) {
        URL url = _nativeLibraryClass.getResource(path);
        if (url == null)
            return null;
        try (InputStream is = url.openStream()) {
            byte[] bytes = is.readAllBytes();
            return new String(bytes).trim();
        } catch (IOException e) {
            return null;
        }
    }

    public static synchronized void load() {
        if (_loaded) return;

        String version = readResource(_resourcePath + "skija.version");
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + (version == null ? "" + System.nanoTime() : version));
        File library;
        
        switch (Platform.CURRENT) {
        case WINDOWS:
            _extract(_resourcePath, "icudtl.dat", tempDir);
            library = _extract(_resourcePath, "skija.dll", tempDir);
            System.load(library.getAbsolutePath());
            break;
        case LINUX:
            library = _extract(_resourcePath, "libskija.so", tempDir);
            System.load(library.getAbsolutePath());
            break;
        case MACOS_X64:
            library = _extract(_resourcePath, "libskija_x64.dylib", tempDir);
            System.load(library.getAbsolutePath());
            break;
        case MACOS_ARM64:
            library = _extract(_resourcePath, "libskija_arm64.dylib", tempDir);
            System.load(library.getAbsolutePath());
            break;
        default:
            throw new RuntimeException("Unexpected Platform.CURRENT = " + Platform.CURRENT);
        }

        if (tempDir.exists() && version == null) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.walk(tempDir.toPath())
                         .map(Path::toFile)
                         .sorted(Comparator.reverseOrder())
                         .forEach((f) -> {
                            Log.debug("Deleting " + f);
                            f.delete();
                         });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }));
        }
        
        _loaded = true;
        _nAfterLoad();
    }

    @ApiStatus.Internal
    @SneakyThrows
    public static File _extract(String resourcePath, String fileName, File tempDir) {
        File file;
        URL url = _nativeLibraryClass.getResource(resourcePath + fileName);
        if (url == null) {
            file = new File(fileName);
            if (!file.exists())
                throw new IllegalArgumentException("Library file " + fileName + " not found in " + resourcePath);
        } else if ("file".equals(url.getProtocol())) {
            file = new File(url.toURI());
        } else {
            file = new File(tempDir, fileName);
            if (!file.exists()) {
                if (!tempDir.exists())
                    tempDir.mkdirs();
                try (InputStream is = url.openStream()) {
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
        Log.debug("Loading " + file);
        return file;
    }

    @ApiStatus.Internal public static native void _nAfterLoad();
}
