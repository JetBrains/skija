package org.jetbrains.skija;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Library {
    public static boolean _loaded = false;

    // https://github.com/adamheinrich/native-utils/blob/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/java/cz/adamh/utils/NativeUtils.java
    public static void load(String resourcePath, String name) {
        if (_loaded) return;
        try {
            File file;
            String fileName = _getPrefix() + name + "." + _getExtension();

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
            _loaded = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String _getPrefix() {
        String os = System.getProperty("os.name");
        System.err.println(os);
        String lowerCaseOs = os.toLowerCase();
        if (lowerCaseOs.contains("windows")) return "";
        return "lib";
    }

    public static String _getExtension() {
        String os = System.getProperty("os.name");
        if (os == null) throw new RuntimeException("Unknown operation system");
        String lowerCaseOs = os.toLowerCase();
        if (lowerCaseOs.contains("mac") || lowerCaseOs.contains("darwin")) return "dylib";
        if (lowerCaseOs.contains("windows")) return "dll";
        if (lowerCaseOs.contains("nux")) return "so";

        throw new RuntimeException("Unknown operation system: " + os);
    }

}
