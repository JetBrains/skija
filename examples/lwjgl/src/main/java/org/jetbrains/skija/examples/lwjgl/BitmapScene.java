package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;
import java.util.*;

public class BitmapScene implements Scene {
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
            if (canvas.readPixels(bitmap, (int) ((target.getLeft() - 10) * dpi), (int) ((target.getTop() - 10) * dpi)))
                canvas.writePixels(bitmap, (int) (target.getLeft() * dpi), (int) (target.getTop() * dpi));
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
                    var targetRelative = target.translate(-extended.getLeft(), -extended.getTop()).toRect();
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
            bitmap.allocPixels(ImageInfo.makeN32((int) (target.getWidth() * dpi), (int) (target.getHeight() * dpi), ColorAlphaType.PREMUL));
            bitmap.erase(0x80a8dadc);
            bitmap.erase(0x00FFFFFF, IRect.makeXYWH((int) ((target.getWidth() / 2 - 10) * dpi), (int) ((target.getHeight() / 2 - 10) * dpi), (int) (20 * dpi), (int) (20 * dpi)));
            Canvas canvas2 = new Canvas(bitmap);
            canvas2.scale(dpi, dpi);
            path.moveTo(0, target.getHeight() / 2)
                .lineTo(target.getWidth() / 2, target.getHeight() / 2 - target.getWidth() / 2)
                .lineTo(target.getWidth(), target.getHeight() / 2)
                .lineTo(target.getWidth() / 2, target.getHeight() / 2 + target.getWidth() / 2)
                .closePath();
            canvas2.drawPath(path, stroke);
            canvas.drawBitmapRect(bitmap, target.toRect());
        }

        try(var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            canvas.drawRect(target.toRect(), stroke);
        }
    }


    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        try (var fill = new Paint();
             var stroke = new Paint().setColor(0xFFE5E5E5).setMode(PaintMode.STROKE).setStrokeWidth(1);) {
            for (var tuple: shapes) {
                fill.setColor(tuple.getSecond());
                canvas.drawPath(tuple.getFirst(), fill);
            }

            int bw = 324;
            int bh = 200;

            int left = xpos - bw - 10;
            int top  = ypos - bh - 10;
            drawGray(canvas, IRect.makeXYWH(left, top, bw, bh), dpi);

            left = xpos + 10;
            drawBlur(canvas, IRect.makeXYWH(left, top, bw, bh), IRect.makeXYWH(0, 0, width, height), 20, dpi);

            left = xpos - bw - 10;
            top = ypos + 10;
            drawBitmapCanvas(canvas, IRect.makeXYWH(left, top, bw, bh), dpi);
        }
    }
}