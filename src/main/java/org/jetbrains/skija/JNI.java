package org.jetbrains.skija;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class JNI {
    // https://github.com/adamheinrich/native-utils/blob/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/java/cz/adamh/utils/NativeUtils.java
    public static void loadLibrary(String resourcePath, String name) {
        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + System.nanoTime());
            tempDir.mkdirs();
            tempDir.deleteOnExit();

            String fileName = "lib" + name + "." + libraryExtension();
            File temp = new File(tempDir, fileName);
            temp.deleteOnExit();

            try (InputStream is = JNI.class.getResourceAsStream(resourcePath + fileName)) {
                if (is != null)
                    Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                else
                    throw new IllegalArgumentException(
                        "Library " + fileName + " is not found in " + resourcePath
                    );
            }

            System.load(temp.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String libraryExtension() {
        String os = System.getProperty("os.name");
        if (os == null) throw new RuntimeException("Unknown operation system");
        String lowerCaseOs = os.toLowerCase();
        if (lowerCaseOs.contains("mac") || lowerCaseOs.contains("darwin")) return "dylib";
        if (lowerCaseOs.contains("windows")) return "dll";
        if (lowerCaseOs.contains("nux")) return "so";

        throw new RuntimeException("Unknown operation system: " + os);
    }

}