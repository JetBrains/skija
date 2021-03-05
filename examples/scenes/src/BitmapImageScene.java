package org.jetbrains.skija.examples.scenes;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import org.jetbrains.skija.*;

public class BitmapImageScene extends Scene {
    public final Image image;
    public int x, y;

    public BitmapImageScene() {
        try {
            image = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/IMG_7098.jpeg"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void advance(Canvas canvas, int width) {
        canvas.restore();
        x += 220;
        if (x + 220 >= width) {
            x = 20;
            y += 240;
        }   
        canvas.save();
        canvas.translate(x, y);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.save();
        canvas.translate(20, 20);
        x = 20;
        y = 20;
        
        // Image
        canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Bitmap + Image.readPixels
        var bitmap = new Bitmap();
        bitmap.allocPixels(ImageInfo.makeS32(400, 400, ColorAlphaType.OPAQUE));
        image.readPixels(bitmap);
        canvas.drawBitmapRect(bitmap, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image.readPixels", 0, 220, inter13, blackFill);
        advance(canvas, width);
        
        // Bitmap + Image.readPixels(50, 50)
        var partialBitmap = new Bitmap();
        partialBitmap.allocPixels(new ImageInfo(300, 300, ColorType.GRAY_8, ColorAlphaType.OPAQUE));
        image.readPixels(partialBitmap, 50, 50);
        canvas.drawBitmapRect(partialBitmap, Rect.makeXYWH(25, 25, 150, 150));
        canvas.drawString("Image.readPixels(50, 50)", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Bitmap.makeFromImage
        var bitmapFromImage = Bitmap.makeFromImage(image);
        canvas.drawBitmapRect(bitmapFromImage, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Bitmap.makeFromImage", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Image.makeFromBitmap
        var imageFromBitmap = Image.makeFromBitmap(bitmap);
        canvas.drawImageRect(imageFromBitmap, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image.makeFromBitmap", 0, 220, inter13, blackFill);
        advance(canvas, width);
        
        // Bitmap readPixels/installPixels
        var info = bitmapFromImage.getImageInfo();
        var threshold = 100 + phase() * 100;
        byte[] pixels = bitmapFromImage.readPixels();
        ByteBuffer buffer = ByteBuffer.wrap(pixels); // Assume RGBA_8888
        Function<Integer, Integer> luminocity = color -> Color.getR(color) + Color.getG(color) + Color.getB(color);
        Comparator<Integer> cmp = (a, b) -> Integer.compare(luminocity.apply(a), luminocity.apply(b));
        for (int x = 0; x < info.getWidth(); ++x) {
            // read pixels
            Integer column[] = new Integer[info.getHeight()];
            for (int y = 0; y < info.getHeight(); ++y)
                column[y] = buffer.getInt((y * info.getWidth() + x) * info.getBytesPerPixel());

            // sort pixels
            var lastIdx = 0;
            for (int y = 0; y < info.getHeight() - 1; ++y) {
                if (Math.abs(luminocity.apply(column[y]) - luminocity.apply(column[y + 1])) > threshold) {
                    Arrays.parallelSort(column, lastIdx, y, cmp);
                    lastIdx = y;
                }
            }
            Arrays.parallelSort(column, lastIdx, info.getHeight(), cmp);

            // write pixels
            for (int y = 0; y < info.getHeight(); ++y)
                buffer.putInt((y * info.getWidth() + x)  * info.getBytesPerPixel(), column[y]);
        }
        bitmapFromImage.installPixels(pixels);
        canvas.drawBitmapRect(bitmapFromImage, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Bitmap.readPixels/installPixels", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Image.makeRaster
        var imageFromPixels = Image.makeRaster(info, pixels, info.getMinRowBytes());
        canvas.drawImageRect(imageFromPixels, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image.makeRaster", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Image.makeRaster + Data
        var imageFromData = Image.makeRaster(info, Data.makeFromBytes(pixels), info.getMinRowBytes());
        canvas.drawImageRect(imageFromPixels, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image.makeRaster + Data", 0, 220, inter13, blackFill);
        advance(canvas, width);

        bitmap.close();
        partialBitmap.close();
        bitmapFromImage.close();
        imageFromBitmap.close();
        imageFromPixels.close();
        imageFromData.close();
    }
}