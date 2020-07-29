package org.jetbrains.skija;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import org.jetbrains.annotations.*;

public class Library {
    @ApiStatus.Internal
    public static boolean _loaded = false;

    public static void load() {
        if (_loaded) return;

        String os = System.getProperty("os.name").toLowerCase();
        String fileName;

        if (os.contains("mac") || os.contains("darwin"))
            fileName = "libskija.dylib";
        else if (os.contains("windows"))
            fileName = "skija.dll";
        else if (os.contains("nux") || os.contains("nix"))
            fileName = "libskija.so";
        else
            throw new RuntimeException("Unknown operation system");

        _load("/", fileName);
        _loaded = true;
    }

    // https://github.com/adamheinrich/native-utils/blob/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/java/cz/adamh/utils/NativeUtils.java
    @ApiStatus.Internal
    public static void _load(String resourcePath, String fileName) {
        try {
            File file;
            URL url = Library.class.getResource(resourcePath + fileName);
            if (url == null)
                throw new IllegalArgumentException("Library " + fileName + " is not found in " + resourcePath);
            else if (url.getProtocol() == "file")
                try {
                    // System.out.println("Loading " + url);
                    file = new File(url.toURI());
                } catch (URISyntaxException e){
                    throw new RuntimeException(e);
                }
            else
                try (InputStream is = url.openStream()) {
                    File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + System.nanoTime());
                    tempDir.mkdirs();
                    tempDir.deleteOnExit();
                    file = new File(tempDir, fileName);
                    file.deleteOnExit();
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    // System.out.println("Loading " + url + " from " + file);
                }

            System.load(file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
