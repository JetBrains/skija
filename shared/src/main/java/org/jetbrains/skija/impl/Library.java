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

    public static void staticLoad() {
        if (!_loaded && !"false".equals(System.getProperty("skija.staticLoad")))
            load();
    }

    private static Class<?> getPlatformClass() throws ClassNotFoundException{
      return Class.forName("org.jetbrains.skija.platform.Library" + getPlatformName());
    }

    private static String getPlatformName() {
      String os = System.getProperty("os.name").toLowerCase();
      if (os.contains("mac") || os.contains("darwin")) {
        return "aarch64".equals(System.getProperty("os.arch")) ? "MacosArm64" : "MacosX64";
      }
      if (os.contains("windows")) {
        return "Windows";
      }
      if (os.contains("nux") || os.contains("nix")) {
        return "Linux";
      }
      throw new RuntimeException("Unknown operation system");
    }

    public static String readResource(String path) {

      try (InputStream is = getPlatformClass().getResourceAsStream(path)) {
        if (is != null) {
          byte[] bytes = is.readAllBytes();
          return new String(bytes).trim();
        }
      } catch (ClassNotFoundException e1) {
      } catch (IOException e2) {
      }
      
      URL url = Library.class.getResource(path);
      try (InputStream is = url != null ? url.openStream() : Library.class.getResourceAsStream(path)) {
        if (is == null) {
          return null;
        }
        byte[] bytes = is.readAllBytes();
        return new String(bytes).trim();
      } catch (IOException e) {
        return null;
      }
    }

    public static synchronized void load() {
        if (_loaded) return;

        String version = readResource("/skija.version");
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "skija_" + (version == null ? "" + System.nanoTime() : version));
        String os = System.getProperty("os.name").toLowerCase();
        
        if (os.contains("mac") || os.contains("darwin")) {
            String file = "aarch64".equals(System.getProperty("os.arch")) ? "libskija_arm64.dylib" : "libskija_x64.dylib";
            File library = _extract("/", file, tempDir);
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
        URL url = Library.class.getResource(resourcePath + fileName);
        if (url == null) {
            file = new File(fileName);
            if (!file.exists()) {
              return extractFromResource(resourcePath, fileName, tempDir);
            }
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
    
    @ApiStatus.Internal
    @SneakyThrows
    private static File extractFromResource(String resourcePath, String fileName, File tempDir) {
      try (InputStream is = getPlatformClass().getResourceAsStream(resourcePath + fileName)) {
        if (is == null) {
          throw new IllegalArgumentException("resource jar not found");
        }
        File file = new File(tempDir, fileName);
        if (!file.exists() && !tempDir.exists()) {
          tempDir.mkdirs();
        }
        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
        
      } catch (Exception e) {
        throw new IllegalArgumentException("Library file " + fileName + " not found in " + resourcePath, e);
      }
    }

    @ApiStatus.Internal public static native void _nAfterLoad();
}
