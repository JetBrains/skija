package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;

import org.jetbrains.skija.*;

public class ImageFiltersScene extends Scene {
    protected final Image image;

    public ImageFiltersScene() {
        try {
            image = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/circus.jpg"))));
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
            path.setFillMode(PathFillMode.EVEN_ODD);
            path.lineTo(0, 60).lineTo(60, 60).lineTo(60, 0).closePath();
            path.moveTo(10, 5).lineTo(55, 10).lineTo(50, 55).lineTo(5, 50).closePath();

            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.makeDropShadow(0, 0, 10, 10, 0xFF000000),
                ImageFilter.makeDropShadow(2, 2, 0, 0, 0xFF000000),
                ImageFilter.makeDropShadow(0, 0, 10, 10, 0xFFF42372),
                ImageFilter.makeDropShadowOnly(0, 0, 2, 2, 0xFFCC3333),
                ImageFilter.makeDropShadow(0, 0, 2, 2, 0xFFCC3333, null, IRect.makeXYWH(30, 30, 30, 30)),
                ImageFilter.makeDropShadow(2, 2, 2, 2, 0xFF3333CC, ImageFilter.makeDropShadow(-2, -2, 2, 2, 0xFFCC3333), null),
                ImageFilter.makeBlur(2, 2, FilterTileMode.CLAMP),
                ImageFilter.makeBlur(2, 2, FilterTileMode.REPEAT),
                ImageFilter.makeBlur(2, 2, FilterTileMode.MIRROR),
                ImageFilter.makeBlur(2, 2, FilterTileMode.DECAL),
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
            path.setFillMode(PathFillMode.EVEN_ODD);
            path.moveTo(10, 10).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);

            IRect bb = IRect.makeXYWH(0, 0, 60, 60);
            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.makeOffset(0, 0, null, bb),
                ImageFilter.makeMagnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 5f, null, bb),
                ImageFilter.makeMagnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 10f, null, bb),
                ImageFilter.makeMagnifier(Rect.makeXYWH(0 * dpi, 0 * dpi, 60 * dpi, 60 * dpi), 20f, null, bb),
                ImageFilter.makeOffset(10, 10, null, bb),
                ImageFilter.makePaint(fill, bb),
                ImageFilter.makeTile(Rect.makeXYWH(10, 10, 40, 40), Rect.makeXYWH(0, 0, 60, 60), null),
                ImageFilter.makeDilate(2, 2, null, bb),
                ImageFilter.makeErode(2, 2, null, bb),
                ImageFilter.makeColorFilter(
                    ColorFilter.makeBlend(0x800000FF, BlendMode.SRC_OVER),
                    ImageFilter.makeDropShadow(0, 0, 10, 10, 0xFF000000),
                    bb),
                ImageFilter.makeImage(image, Rect.makeXYWH(200, 200, 200, 200), Rect.makeXYWH(10, 10, 40, 40), CubicResampler.MITCHELL),
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
            path.setFillMode(PathFillMode.EVEN_ODD);
            path.moveTo(10, 10).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);

            IRect bb = IRect.makeXYWH(0, 0, 60, 60);
            ImageFilter[] filters = new ImageFilter[] {
                ImageFilter.makeDistantLitDiffuse( 0,  1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeDistantLitDiffuse( 0, -1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeDistantLitDiffuse( 1,  0, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeDistantLitDiffuse(-1,  0, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeDistantLitDiffuse(-1, -1, 1, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makePointLitDiffuse(0, 0, 30, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeSpotLitDiffuse(0, 0, 30, 30, 30, 0, 1f, 30, 0xFFFF9F1B, 1, 0.5f, null, bb),
                ImageFilter.makeDistantLitSpecular(-1, -1, 1, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
                ImageFilter.makePointLitSpecular(0, 0, 30, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
                ImageFilter.makeSpotLitSpecular(0, 0, 30, 30, 30, 0, 1f, 30, 0xFFFF9F1B, 1, 1.1f, 1.1f, null, bb),
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