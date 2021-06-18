package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import org.jetbrains.skija.Bitmap;
import org.jetbrains.skija.ColorAlphaType;
import org.jetbrains.skija.ImageInfo;
import org.jetbrains.skija.test.runner.*;

public class BitmapTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var bitmap = new Bitmap()) {
            int id1 = bitmap.getGenerationId();
            assertEquals(true, bitmap.isNull());
            assertEquals(true, bitmap.isEmpty());

            bitmap.allocPixels(ImageInfo.makeS32(7, 3, ColorAlphaType.OPAQUE));
            assertNotEquals(id1, bitmap.getGenerationId());

            assertEquals(false, bitmap.isNull());
            assertEquals(false, bitmap.isEmpty());
            
            assertEquals(7L * 4, bitmap.getRowBytes());
            assertEquals(4, bitmap.getBytesPerPixel());
            assertEquals(7, bitmap.getRowBytesAsPixels());

            bitmap.allocPixels(ImageInfo.makeS32(7, 3, ColorAlphaType.OPAQUE), 32);
            assertEquals(32L, bitmap.getRowBytes());
            assertEquals(4, bitmap.getBytesPerPixel());
            assertEquals(8, bitmap.getRowBytesAsPixels());

            bitmap.setImageInfo(ImageInfo.makeS32(7, 3, ColorAlphaType.OPAQUE));
            assertEquals(true, bitmap.isNull());
            assertEquals(false, bitmap.isEmpty());
            assertEquals(false, bitmap.isReadyToDraw());
            bitmap.allocPixels();
            assertEquals(false, bitmap.isNull());
            assertEquals(true, bitmap.isReadyToDraw());


            bitmap.getGenerationId();

        }
    }
}