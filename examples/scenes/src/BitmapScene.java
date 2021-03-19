package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;
import java.util.*;

public class BitmapScene extends Scene {
    private List<Pair<Path, Integer>> shapes = new ArrayList<>(100);

    public BitmapScene() {
        var random = new Random(0);
        for (int i = 0; i < 100; ++i) {
            var path = new Path();

            switch (random.nextInt(4)) {
                case 0:
                    path.addRect(Rect.makeXYWH(-0.5f, -0.5f, 1, 1));
                    break;
                case 1:
                    path.addCircle(0, 0, 0.5f);
                    break;
                case 2:
                    path.moveTo(0, -0.5f).lineTo(0.5f, 0.36f).lineTo(-0.5f, 0.36f).closePath();
                    break;
                case 3:
                    path.addRRect(RRect.makeXYWH(-0.6f, -0.4f, 1.2f, 0.8f, 0.4f));
                    break;
            }

            path.transform(Matrix33.makeRotate(random.nextInt(360)));
            path.transform(Matrix33.makeScale(10 + random.nextInt(250)));
            path.transform(Matrix33.makeTranslate(random.nextInt(1920), random.nextInt(1080)));

            int color = 0xFF000000 | random.nextInt(0xFFFFFF);

            shapes.add(new Pair<>(path, color));
        }
    }

    private void drawGray(Canvas canvas, IRect target, float dpi) {        
        try (Bitmap bitmap = new Bitmap();) {
            bitmap.allocPixels(new ImageInfo((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorType.GRAY_8, ColorAlphaType.OPAQUE));
            if (canvas.readPixels(bitmap, (int) ((target.getLeft() - 10) * dpi), (int) ((target.getTop() - 10) * dpi))) {
                try (Image image = Image.makeFromBitmap(bitmap.setImmutable())) {
                    canvas.drawImageRect(image, target.toRect());
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    private void drawBlur(Canvas canvas, IRect target, IRect screen, int radius, float dpi) {
        target = target.intersect(screen);
        if (target == null) return;

        try (Bitmap bitmap = new Bitmap();) {
            int radiusDPI = (int) (radius * dpi);
            IRect extended = IRect.makeLTRB(target.getLeft() - radiusDPI, target.getTop() - radiusDPI, target.getRight() + radiusDPI, target.getBottom() + radiusDPI).intersect(screen);
            bitmap.allocPixels(ImageInfo.makeS32((int) (extended.getWidth() * dpi), (int) (extended.getHeight() * dpi), ColorAlphaType.OPAQUE));
            if (canvas.readPixels(bitmap, (int) (extended.getLeft() * dpi), (int) (extended.getTop() * dpi))) {
                try (var shader = bitmap.makeShader(Matrix33.makeScale(1 / dpi));
                     var blur   = ImageFilter.makeBlur(radius, radius, FilterTileMode.CLAMP);
                     var fill   = new Paint().setShader(shader).setImageFilter(blur))
                {
                    canvas.save();
                    canvas.translate(extended.getLeft(), extended.getTop());
                    var targetRelative = target.offset(-extended.getLeft(), -extended.getTop()).toRect();
                    canvas.clipRect(targetRelative);
                    canvas.drawRect(Rect.makeXYWH(0, 0, extended.getWidth(), extended.getHeight()), fill);
                    canvas.restore();
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    private void drawBitmapCanvas(Canvas canvas, IRect target, float dpi) {
        try (var bitmap = new Bitmap();
             var path = new Path();
             var stroke = new Paint().setColor(0xFFe76f51).setMode(PaintMode.STROKE).setStrokeWidth(10);) {
            bitmap.allocPixels(ImageInfo.makeS32((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorAlphaType.PREMUL));
            
            bitmap.erase(0x80a8dadc);
            int color = bitmap.getColor(10, 10);
            assert 0x80a7d9db == color : "Expected 0x" + Integer.toString(0x80a7d9db, 16) + ", got 0x" + Integer.toString(color, 16);
            float alpha = bitmap.getAlphaf(10, 10);
            assert Math.abs(0.5f - alpha) < 0.01 : "Expected 0.5f, got " + alpha;

            bitmap.erase(0x00FFFFFF, IRect.makeXYWH((int) ((target.getWidth() / 2 - 10) * dpi), (int) ((target.getHeight() / 2 - 10) * dpi), (int) (20 * dpi), (int) (20 * dpi)));
            color = bitmap.getColor((int) (target.getWidth() / 2 * dpi), (int) (target.getHeight() / 2 * dpi));
            assert 0x00000000 == color : "Expected 0x" + Integer.toString(0x00000000, 16) + ", got 0x" + Integer.toString(color, 16);
            alpha = bitmap.getAlphaf((int) (target.getWidth() / 2 * dpi), (int) (target.getHeight() / 2 * dpi));
            assert Math.abs(0f - alpha) < 0.01 : "Expected 0f, got " + alpha;

            Canvas canvas2 = new Canvas(bitmap);
            canvas2.scale(dpi, dpi);
            path.moveTo(0, target.getHeight() / 2)
                .lineTo(target.getWidth() / 2, target.getHeight() / 2 - target.getWidth() / 2)
                .lineTo(target.getWidth(), target.getHeight() / 2)
                .lineTo(target.getWidth() / 2, target.getHeight() / 2 + target.getWidth() / 2)
                .closePath();
            canvas2.drawPath(path, stroke);

            try (Image image = Image.makeFromBitmap(bitmap.setImmutable())) {
                canvas.drawImageRect(image, target.toRect());
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    public void drawPixels(Canvas canvas, IRect target, IRect screen, float dpi) {
        target = target.intersect(screen);
        if (target == null) return;

        IRect inner = IRect.makeXYWH(target.getLeft() + 10, target.getTop() + 10, target.getWidth() - 20, target.getHeight() - 20);
        if (inner == null) return;

        try (var bitmap = new Bitmap();) {
            var srcInfo = ImageInfo.makeS32((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorAlphaType.OPAQUE);
            bitmap.allocPixelsFlags(srcInfo, true);
            if (canvas.readPixels(bitmap, (int) (target.getLeft() * dpi), (int) (target.getTop() * dpi))) {
                var dstInfo = new ImageInfo((int) (inner.getWidth() * dpi), (int) (inner.getHeight() * dpi), ColorType.ARGB_4444, ColorAlphaType.UNPREMUL);
                long rowBytes = dstInfo.getMinRowBytes();
                byte[] pixels = bitmap.readPixels(dstInfo, rowBytes, (int) (10 * dpi), (int) (10 * dpi));
                int bpp = dstInfo.getBytesPerPixel();

                for (int i = 0; i < pixels.length / bpp / 2; ++i) {
                    for (int j = 0; j < bpp; ++j) {
                        int i1 = i * bpp + j;
                        int i2 = (pixels.length / bpp - i - 1) * bpp + j;
                        byte b = pixels[i1];
                        pixels[i1] = pixels[i2];
                        pixels[i2] = b;
                    }
                }

                bitmap.installPixels(dstInfo, pixels, rowBytes);
                bitmap.notifyPixelsChanged();
                try (var image = Image.makeFromBitmap(bitmap.setImmutable());) {
                    canvas.drawImageRect(image, inner.toRect());
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    public void drawSubset(Canvas canvas, IRect target, IRect screen, float dpi) {
        try (var src = new Bitmap();
             var dst = new Bitmap();)
        {
            var srcInfo = ImageInfo.makeS32((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorAlphaType.OPAQUE);
            src.allocPixels(srcInfo);
            if (canvas.readPixels(src, (int) (target.getLeft() * dpi), (int) (target.getTop() * dpi))) {
                src.extractSubset(dst, Rect.makeXYWH(target.getWidth() / 4 * dpi, target.getHeight() / 4 * dpi, target.getWidth() / 2 * dpi, target.getHeight() / 2 * dpi).toIRect());
                try (var image = Image.makeFromBitmap(dst.setImmutable());) {
                    canvas.drawImageRect(image, target.toRect());
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    public void drawPixelRef(Canvas canvas, IRect target, IRect screen, float dpi) {
        try (var src = new Bitmap();
             var dst = new Bitmap();)
        {
            var info = ImageInfo.makeS32((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorAlphaType.OPAQUE);
            src.allocPixels(info);
            if (canvas.readPixels(src, (int) (target.getLeft() * dpi), (int) (target.getTop() * dpi))) {
                var pixelRef = src.getPixelRef();
                dst.setImageInfo(info.withWidthHeight((int) (target.getWidth() / 2 * dpi), (int) (target.getHeight() / 2 * dpi)));
                dst.setPixelRef(pixelRef, (int) (target.getWidth() / 4 * dpi), (int) (target.getHeight() / 4 * dpi));
                try (var image = Image.makeFromBitmap(dst.setImmutable());) {
                    canvas.drawImageRect(image, target.toRect());
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }


    public void drawAlpha(Canvas canvas, IRect target, IRect screen, float dpi) {
        try (var src = new Bitmap();
             var dst = new Bitmap();)
        {
            var srcInfo = ImageInfo.makeS32((int) (target.getWidth() * dpi), (int) (target.getHeight() / 2 * dpi), ColorAlphaType.UNPREMUL);
            src.allocPixels(srcInfo);
            int len = (int) (target.getWidth() * dpi);
            for (int x = 0; x < len; ++x) {
                int alpha = (int) (255f * x / len);
                int color = (alpha << 24) | (alpha << 16) | (0 << 8) | (255 - alpha);
                src.erase(color, IRect.makeXYWH(x, 0, 1, (int) (target.getHeight() / 2 * dpi)));
            }
            try (var image = Image.makeFromBitmap(src.setImmutable());) {
                canvas.drawImageRect(image, Rect.makeXYWH(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight() / 2));
            }

            if (src.extractAlpha(dst)) {
                try (var image = Image.makeFromBitmap(dst.setImmutable());) {
                    canvas.drawImageRect(image, Rect.makeXYWH(target.getLeft(), target.getTop() + target.getHeight() / 2, target.getWidth(), target.getHeight() / 2));
                }
            }
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        try (var fill = new Paint();
             var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            for (var tuple: shapes) {
                fill.setColor(tuple.getSecond());
                canvas.drawPath(tuple.getFirst(), fill);
            }

            IRect screen = IRect.makeXYWH(0, 0, width, height);
            int bw = 200;
            int bh = 200;

            int left = xpos - bw - 20 - bw / 2 - 10;
            int top  = ypos - bh - 20 - bh / 2 - 10;
            drawGray(canvas, IRect.makeXYWH(left, top, bw, bh), dpi);

            left = xpos - bw / 2 - 10;
            drawBlur(canvas, IRect.makeXYWH(left, top, bw, bh), screen, 20, dpi);

            left = xpos + 10 + bw / 2;
            drawBitmapCanvas(canvas, IRect.makeXYWH(left, top, bw, bh), dpi);

            left = xpos - bw - 20 - bw / 2 - 10;
            top  = ypos - bh / 2 - 10;
            drawPixels(canvas, IRect.makeXYWH(left, top, bw, bh), screen, dpi);

            left = xpos - bw / 2 - 10;
            drawSubset(canvas, IRect.makeXYWH(left, top, bw, bh), screen, dpi);

            left = xpos + bw / 2 + 10;
            drawPixelRef(canvas, IRect.makeXYWH(left, top, bw, bh), screen, dpi);

            left = xpos - bw - 20 - bw / 2 - 10;
            top  = ypos + bh / 2 + 10;
            drawAlpha(canvas, IRect.makeXYWH(left, top, bw, bh), screen, dpi);
        }
    }
}