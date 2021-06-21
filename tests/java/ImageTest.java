package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import java.io.*;
import java.nio.file.*;

import org.jetbrains.skija.EncodedImageFormat;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.Surface;
import org.jetbrains.skija.Path;
import org.jetbrains.skija.test.runner.*;

public class ImageTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var surface = Surface.makeRasterN32Premul(100, 100);
             var paint = new Paint().setColor(0xFFFF0000);
             var path = new Path().moveTo(20, 80).lineTo(50, 20).lineTo(80, 80).closePath();)
        {
            var canvas = surface.getCanvas();
            canvas.drawPath(path, paint);
            try (var image = surface.makeImageSnapshot()) {
                new File("target/tests/ImageTest/").mkdirs();
                Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_default.png"), image.encodeToData().getBytes());
                Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_jpeg_default.jpeg"), image.encodeToData(EncodedImageFormat.JPEG).getBytes());
                Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_jpeg_50.jpeg"), image.encodeToData(EncodedImageFormat.JPEG, 50).getBytes());
                Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_webp_default.webp"), image.encodeToData(EncodedImageFormat.WEBP).getBytes());
                Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_webp_50.webp"), image.encodeToData(EncodedImageFormat.WEBP, 50).getBytes());
                // Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_heif_default.heif"), image.encodeToData(EncodedImageFormat.HEIF).getBytes());
                // Files.write(java.nio.file.Path.of("target/tests/ImageTest/polygon_heif_50.heif"), image.encodeToData(EncodedImageFormat.HEIF, 50).getBytes());
            }
        }
    }
}