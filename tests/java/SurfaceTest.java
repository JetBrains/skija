package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import org.jetbrains.skija.Bitmap;
import org.jetbrains.skija.ContentChangeMode;
import org.jetbrains.skija.IRect;
import org.jetbrains.skija.ImageInfo;
import org.jetbrains.skija.Surface;
import org.jetbrains.skija.test.runner.*;

public class SurfaceTest implements Executable {
    @Override
    public void execute() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> Surface.makeRasterN32Premul(0, 0));
        try (var surface = Surface.makeRasterN32Premul(100, 200)) {
            assertEquals(100, surface.getWidth());
            assertEquals(200, surface.getHeight());

            var readPixelsBitmap = new Bitmap();
            readPixelsBitmap.setImageInfo(ImageInfo.makeN32Premul(100, 200));
            readPixelsBitmap.allocPixels();
            assertEquals(true, surface.readPixels(readPixelsBitmap, 0, 0));

            int id = surface.getGenerationId();
            assertEquals(id, surface.getGenerationId());
            var writePixelsBitmap = new Bitmap();
            writePixelsBitmap.setImageInfo(ImageInfo.makeN32Premul(100, 200));
            writePixelsBitmap.allocPixels();
            surface.writePixels(writePixelsBitmap, 0, 0);
            assertNotEquals(id, surface.getGenerationId());

            assertEquals(true, surface.isUnique());

            var imageInfo = surface.getImageInfo();
            assertEquals(100, imageInfo.getWidth());
            assertEquals(200, imageInfo.getHeight());

            var newSurface = surface.makeSurface(50, 100);
            assertEquals(50, newSurface.getWidth());
            assertEquals(100, newSurface.getHeight());

            var newSurface2 = surface.makeSurface(ImageInfo.makeN32Premul(200, 400));
            assertEquals(200, newSurface2.getWidth());
            assertEquals(400, newSurface2.getHeight());

            var image = surface.makeImageSnapshot(new IRect(0, 0, 20, 30));
            assertEquals(20, image.getWidth());
            assertEquals(30, image.getHeight());

            int id2 = surface.getGenerationId();
            assertEquals(id2, surface.getGenerationId());
            surface.notifyContentWillChange(ContentChangeMode.DISCARD);
            assertNotEquals(id2, surface.getGenerationId());

            var context = surface.getRecordingContext();
            assertEquals(context, null);
        }
    }
}