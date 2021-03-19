package org.jetbrains.skija.examples.scenes;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;

public class CodecScene extends Scene {
    Paint stroke = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    List<Pair<String, Bitmap>> formats = new ArrayList<>();
    List<Pair<String, Codec>> orientations = new ArrayList<>();

    static class Animation {
        Codec codec;
        Bitmap bitmap;
        int prevFrame = -1;
        int[] durations;
        long totalDuration;
    }

    List<Pair<String, Animation>> animations = new ArrayList<>();

    float x, y;
    float rowH = 100;
    float columnW = 100;

    public CodecScene() {
        for (var file: new String[] {"bmp.bmp", "gif.gif", "favicon.ico", "dotpeek.ico", "jpeg.jpg", "png.png", "webp_lossy.webp", "webp_loseless.webp"}) {
            try (var codec = Codec.makeFromData(Data.makeFromFileName(file("images/codecs/" + file)))) {
                formats.add(new Pair(file + "\n" + codec.getEncodedImageFormat(), codec.readPixels().setImmutable()));
            } catch (Exception e) {
                formats.add(new Pair(file + "\n" + e.getMessage(), null));
            }
        }

        for (var file: new String[] {"orient_tl.jpg", "orient_tr.jpg", "orient_br.jpg", "orient_bl.jpg", "orient_lt.jpg", "orient_lb.jpg", "orient_rb.jpg", "orient_rt.jpg",}) {
            orientations.add(new Pair(file, Codec.makeFromData(Data.makeFromFileName(file("images/codecs/" + file)))));
        }
 
        for (var file: new String[] {"animated.gif", "animated.webp"}) {
            var animation = new Animation();
            animation.codec = Codec.makeFromData(Data.makeFromFileName(file("images/codecs/" + file)));
            animation.bitmap = new Bitmap();
            animation.bitmap.allocPixels(animation.codec.getImageInfo());
            animation.durations = Arrays.stream(animation.codec.getFramesInfo()).mapToInt(AnimationFrameInfo::getDuration).toArray();
            animation.totalDuration = Arrays.stream(animation.durations).sum();
            animations.add(new Pair(file, animation));
        }
    }

    public void drawOne(Canvas canvas, int width, String s, Runnable draw) {
        if (x + columnW >= width) {
            x = 20;
            y += rowH + 60;
        }
        canvas.save();
        canvas.translate(x, y);
        draw.run();

        var lines = s.lines().toArray();
        for (int i = 0; i < lines.length; ++i)
            canvas.drawString((String) lines[i], 0, rowH + 20 + i * 20, inter13, blackFill);
        canvas.restore();
        x += columnW + 20;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        x = 20;
        y = 20;

        for (var pair: formats) {
            var label = pair.getFirst();
            var bitmap = pair.getSecond();
            if (bitmap == null) {
                drawOne(canvas, width, label, () -> {
                    canvas.drawRect(Rect.makeXYWH(0, 0, columnW, rowH), stroke);
                    canvas.drawLine(0, 0, columnW, rowH, stroke);
                    canvas.drawLine(0, rowH, columnW, 0, stroke);
                });
            } else {
                drawOne(canvas, width, label, () -> {
                    try (var image = Image.makeFromBitmap(bitmap);) {
                        canvas.drawImageRect(image, Rect.makeXYWH(0, 0, columnW, rowH));
                    }
                });
            }
        }

        x = 20;
        y += rowH + 60;
        for (var pair: orientations) {
            var label = pair.getFirst();
            var codec = pair.getSecond();
            var origin = codec.getEncodedOrigin();
            try (var bitmap = codec.readPixels().setImmutable()) {
                int bitmapWidth = origin.swapsWidthHeight() ? codec.getHeight() : codec.getWidth();
                int bitmapHeight = origin.swapsWidthHeight() ? codec.getWidth() : codec.getHeight();
                drawOne(canvas, width, label + "\n" + codec.getEncodedImageFormat(), () -> {
                    canvas.save();
                    canvas.concat(origin.toMatrix(bitmapWidth, bitmapHeight));
                    try (var image = Image.makeFromBitmap(bitmap);) {
                        canvas.drawImageRect(image, Rect.makeXYWH(0, 0, codec.getWidth(), codec.getHeight()));
                    }
                    canvas.restore();
                    canvas.drawRect(Rect.makeXYWH(0, 0, bitmapWidth, bitmapHeight), stroke);
                });
            }
        }

        x = 20;
        y += rowH + 60;

        for (var pair: animations) {
            var label = pair.getFirst();
            var animation = pair.getSecond();
            var codec = animation.codec;

            int duration = 0;
            int frame = 0;
            long now = System.currentTimeMillis() % animation.totalDuration;
            for (; frame < animation.durations.length; ++frame) {
                duration += animation.durations[frame];
                if (duration >= now)
                    break;
            }
            int finalFrame = frame;

            drawOne(canvas, width, label + "\n" + codec.getEncodedImageFormat(), () -> {
                try (var bitmap = new Bitmap()) {
                    bitmap.allocPixels(codec.getImageInfo());
                    codec.readPixels(bitmap, finalFrame);
                    try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                        canvas.drawImageRect(image, Rect.makeXYWH(0, 0, columnW, rowH));
                    }
                }
            });

            drawOne(canvas, width, label + "\n" + codec.getEncodedImageFormat() + " + priorFrame", () -> {
                codec.readPixels(animation.bitmap, finalFrame, animation.prevFrame);
                try (var image = Image.makeFromBitmap(animation.bitmap);) {
                    canvas.drawImageRect(image, Rect.makeXYWH(0, 0, columnW, rowH));
                }
            });
        }
    }
}