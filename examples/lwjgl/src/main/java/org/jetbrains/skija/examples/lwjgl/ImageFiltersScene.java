package org.jetbrains.skija.examples.lwjgl;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.IntStream;
import org.jetbrains.skija.*;

public class ImageFiltersScene implements Scene {
    protected final Image image;

    public ImageFiltersScene() {
        try {
            image = Image.fromEncoded(Files.readAllBytes(java.nio.file.Path.of("images", "circus.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        drawShadowsBlurs(canvas);
        drawImageFilters(canvas, width, dpi); 
        drawLights(canvas);
    }

    private void drawShadowsBlurs(Canvas canvas) {
        canvas.save();
        try (Paint fill = new Paint().setColor(0xFF8E86C9);
             Path path = new Path())
        {
            path.setFillType(Path.FillType.EVEN_ODD);
            path.lineTo(0, 60).lineTo(60, 60).lineTo(60, 0).closePath();
            path.moveTo(10, 5).lineTo(55, 10).lineTo(50, 55).lineTo(5, 50).closePath();

            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.dropShadow(0, 0, 10, 10, 0xFF000000),
                ImageFilter.dropShadow(2, 2, 0, 0, 0xFF000000),
                ImageFilter.dropShadow(0, 0, 10, 10, 0xFFF42372),
                ImageFilter.dropShadowOnly(0, 0, 2, 2, 0xFFCC3333),
                ImageFilter.dropShadow(0, 0, 2, 2, 0xFFCC3333, null, IRect.makeXYWH(30, 30, 30, 30)),
                ImageFilter.dropShadow(2, 2, 2, 2, 0xFF3333CC, ImageFilter.dropShadow(-2, -2, 2, 2, 0xFFCC3333), null),
                ImageFilter.blur(2, 2, TileMode.CLAMP),
                ImageFilter.blur(2, 2, TileMode.REPEAT),
                ImageFilter.blur(2, 2, TileMode.MIRROR),
                ImageFilter.blur(2, 2, TileMode.DECAL),
            };

            for (var filter: filters) {
                fill.setImageFilter(filter);
                canvas.drawPath(path, fill);
                canvas.translate(70, 0);
                filter.close();
            }
        }
        canvas.restore();
        canvas.translate(0, 70);
    }

    private void drawImageFilters(Canvas canvas, float width, float dpi) {
        canvas.save();
        try (Paint fill = new Paint().setColor(0xFFFF9F1B);
             Path path = new Path())
        {
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(10, 10).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);

            IRect bb = IRect.makeXYWH(0, 0, 60, 60);
            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.offset(0, 0, null, bb),
                ImageFilter.magnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 5f, null, bb),
                ImageFilter.magnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 10f, null, bb),
                ImageFilter.magnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 20f, null, bb),
                ImageFilter.offset(10, 10, null, bb),
                ImageFilter.paint(fill, bb),
                ImageFilter.tile(Rect.makeXYWH(10, 10, 40, 40), Rect.makeXYWH(0, 0, 60, 60), null),
                ImageFilter.dilate(2, 2, null, bb),
                ImageFilter.erode(2, 2, null, bb),
                ImageFilter.colorFilter(
                    ColorFilter.blend(0x800000FF, BlendMode.SRC_OVER),
                    ImageFilter.dropShadow(0, 0, 10, 10, 0xFF000000),
                    bb),
                ImageFilter.image(image, Rect.makeXYWH(200, 200, 200, 200), Rect.makeXYWH(10, 10, 40, 40), ImageFilter.FilterQuality.LOW),
            };

            for (var filter: filters) {
                fill.setImageFilter(filter);
                canvas.drawPath(path, fill);
                canvas.translate(70, 0);
                filter.close();
            }
        }
        canvas.restore();
        canvas.translate(0, 70);
    }

    private void drawLights(Canvas canvas) {
        canvas.save();
        try (Paint fill = new Paint().setColor(0xFFFF9F1B);
             Path path = new Path())
        {
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(10, 10).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);

            IRect bb = IRect.makeXYWH(0, 0, 60, 60);
            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.distantLitDiffuse( 0,  1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.distantLitDiffuse( 0, -1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.distantLitDiffuse( 1,  0, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.distantLitDiffuse(-1,  0, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.distantLitDiffuse(-1, -1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.pointLitDiffuse(0, 0, 30, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.spotLitDiffuse(0, 0, 30, 30, 30, 0, 1f, 30, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.distantLitSpecular(-1, -1, 1, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
                ImageFilter.pointLitSpecular(0, 0, 30, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
                ImageFilter.spotLitSpecular(0, 0, 30, 30, 30, 0, 1f, 30, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
            };

            for (var filter: filters) {
                fill.setImageFilter(filter);
                canvas.drawPath(path, fill);
                canvas.translate(70, 0);
                filter.close();
            }
        }
        canvas.restore();
        canvas.translate(0, 70);
    }
}