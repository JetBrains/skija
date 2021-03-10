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
        byte[] pixels = bitmapFromImage.readPixels();
        pixelSorting(canvas, ByteBuffer.wrap(pixels), info);
        bitmapFromImage.installPixels(pixels);
        canvas.drawBitmapRect(bitmapFromImage, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Bitmap.readPixels/installPixels", 0, 220, inter13, blackFill);
        advance(canvas, width);

        // Bitmap peekPixels
        var bitmapFromImage2 = Bitmap.makeFromImage(image);
        pixelSorting(canvas, bitmapFromImage2.peekPixels(), info);
        canvas.drawBitmapRect(bitmapFromImage2, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Bitmap.peekPixels", 0, 220, inter13, blackFill);
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

        // Image.peekPixels
        imageFromBitmap.peekPixels().get(pixels);
        var bitmapFromPeekPixels = new Bitmap();
        bitmapFromPeekPixels.installPixels(imageFromBitmap.getImageInfo(), pixels, imageFromBitmap.getWidth() * imageFromBitmap.getBytesPerPixel());
        canvas.drawBitmapRect(bitmapFromPeekPixels, Rect.makeXYWH(0, 0, 200, 200));
        canvas.drawString("Image.peekPixels " + imageFromBitmap.getColorType() + " -> Bitmap", 0, 220, inter13, blackFill);
        advance(canvas, width);

        bitmap.close();
        partialBitmap.close();
        bitmapFromImage.close();
        bitmapFromImage2.close();
        imageFromBitmap.close();
        imageFromPixels.close();
        imageFromData.close();
        bitmapFromPeekPixels.close();
    }

    public static float luminocity(ColorType colorType, int color) {
        return Color.getR(color) + Color.getG(color) + Color.getB(color);
        // return colorType.getR(color) + colorType.getG(color) + colorType.getB(color);
    }

    public void pixelSorting(Canvas canvas, ByteBuffer pixels, ImageInfo info) {
        var threshold = 100 + phase() * 100;
        // var threshold = 0.4f + phase() * 0.4f;
        var colorType = info.getColorType();
        Comparator<Integer> cmp = (a, b) -> Float.compare(luminocity(colorType, a), luminocity(colorType, b));
        for (int x = 0; x < info.getWidth(); ++x) {
            // read pixels
            Integer column[] = new Integer[info.getHeight()];
            for (int y = 0; y < info.getHeight(); ++y)
                column[y] = pixels.getInt((y * info.getWidth() + x) * info.getBytesPerPixel());  // Assume RGBA_8888

            // sort pixels
            var lastIdx = 0;
            for (int y = 0; y < info.getHeight() - 1; ++y) {
                if (Math.abs(luminocity(colorType, column[y]) - luminocity(colorType, column[y + 1])) > threshold) {
                    Arrays.parallelSort(column, lastIdx, y, cmp);
                    lastIdx = y;
                }
            }
            Arrays.parallelSort(column, lastIdx, info.getHeight(), cmp);

            // write pixels
            for (int y = 0; y < info.getHeight(); ++y)
                pixels.putInt((y * info.getWidth() + x)  * info.getBytesPerPixel(), column[y]);
        }
    }
}