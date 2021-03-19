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
        try (var bitmap = new Bitmap();) {
            bitmap.allocPixels(ImageInfo.makeS32(400, 400, ColorAlphaType.OPAQUE));
            image.readPixels(bitmap);
            try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200));
            }
            canvas.drawString("Image.readPixels", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }
        
        // Bitmap + Image.readPixels(50, 50)
        try (var bitmap = new Bitmap();) {
            bitmap.allocPixels(new ImageInfo(300, 300, ColorType.GRAY_8, ColorAlphaType.OPAQUE));
            image.readPixels(bitmap, 50, 50);
            try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                canvas.drawImageRect(image, Rect.makeXYWH(25, 25, 150, 150));
            }
            canvas.drawString("Image.readPixels(50, 50)", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }

        byte[] pixels;
        ImageInfo info;

        // Bitmap readPixels/installPixels
        try (var bitmap = Bitmap.makeFromImage(image);) {
            info = bitmap.getImageInfo();
            pixels = bitmap.readPixels();
            pixelSorting(canvas, ByteBuffer.wrap(pixels), info);
            bitmap.installPixels(pixels);
            try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200));
            }
            canvas.drawString("Bitmap.readPixels/installPixels", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }

        // Bitmap peekPixels
        try (var bitmap = Bitmap.makeFromImage(image);) {
            pixelSorting(canvas, bitmap.peekPixels(), bitmap.getImageInfo());
            try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200));
            }
            canvas.drawString("Bitmap.peekPixels", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }

        // Image.makeRaster
        try (var imageFromPixels = Image.makeRaster(info, pixels, info.getMinRowBytes());) {
            canvas.drawImageRect(imageFromPixels, Rect.makeXYWH(0, 0, 200, 200));
            canvas.drawString("Image.makeRaster", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }

        // Image.makeRaster + Data
        try (var imageFromData = Image.makeRaster(info, Data.makeFromBytes(pixels), info.getMinRowBytes());) {
            canvas.drawImageRect(imageFromData, Rect.makeXYWH(0, 0, 200, 200));
            canvas.drawString("Image.makeRaster + Data", 0, 220, inter13, blackFill);
            advance(canvas, width);
        }
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