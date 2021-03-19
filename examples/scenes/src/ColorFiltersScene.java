package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.skija.*;

public class ColorFiltersScene extends Scene {
    Image image;

    public ColorFiltersScene() {
        try {
            image = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/circus.jpg"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);

        byte[] tablePosterize = new byte[256];
        for (int i = 0; i < 256; ++i)
            tablePosterize[i] = (byte) (i & 0x80);

        byte[] tableInv = new byte[256];
        for (int i = 0; i < 256; ++i)
            tableInv[i] = (byte) (255 - i);

        ColorFilter[][] filters = new ColorFilter[][] {
            new ColorFilter[] {
                ColorFilter.makeBlend(0x80CC3333, BlendMode.SRC_OVER),
                ColorFilter.makeBlend(0xFFCC3333, BlendMode.SCREEN),
                ColorFilter.makeBlend(0xFFCC3333, BlendMode.OVERLAY),
                ColorFilter.makeComposed(
                    ColorFilter.makeBlend(0x80CC3333, BlendMode.SRC_OVER),
                    ColorFilter.makeBlend(0x803333CC, BlendMode.SRC_OVER)
                ),
                ColorFilter.makeMatrix(new ColorMatrix(
                    0.21f, 0.72f, 0.07f, 0, 0,
                    0.21f, 0.72f, 0.07f, 0, 0,
                    0.21f, 0.72f, 0.07f, 0, 0,
                    0,     0,     0,     1, 0
                )),
                ColorFilter.makeHSLAMatrix(new ColorMatrix(
                    0, 0, 0, 0, phase(),
                    0, 1, 0, 0, 0,
                    0, 0, 1, 0, 0,
                    0, 0, 0, 1, 0
                )),
                ColorFilter.makeLerp(ColorFilter.makeBlend(0x80CC3333, BlendMode.SRC_OVER), ColorFilter.makeBlend(0x803333CC, BlendMode.SRC_OVER), phase()),
                ColorFilter.makeLighting(0x80CC3333, 0x803333CC),
            },

            new ColorFilter[] {
                ColorFilter.makeHighContrast(true, InversionMode.NO, 0),
                ColorFilter.makeHighContrast(false, InversionMode.NO, 0.5f),
                ColorFilter.makeHighContrast(false, InversionMode.NO, -0.5f),
                ColorFilter.makeHighContrast(false, InversionMode.BRIGHTNESS, 0),
                ColorFilter.makeHighContrast(false, InversionMode.LIGHTNESS, 0),
                ColorFilter.makeHighContrast(true, InversionMode.LIGHTNESS, 1),
                ColorFilter.makeTable(tablePosterize),
                ColorFilter.makeTableARGB(null, tableInv, null, null),
            },

            new ColorFilter[] {
                ColorFilter.getLinearToSRGBGamma(),
                ColorFilter.getSRGBToLinearGamma(),
                ColorFilter.getLuma()
            }
        };

        
        try (Paint paint = new Paint()) {
            for (int i = 0; i < filters.length; ++i) {
                canvas.save();
                for (ColorFilter filter: filters[i]) {
                    paint.setColorFilter(filter);
                    canvas.drawImageRect(image, Rect.makeWH(image.getWidth(), image.getHeight()), Rect.makeXYWH(0, 0, 160, 160), SamplingMode.LINEAR, paint, false);
                    if (i < filters.length - 1)
                       filter.close();
                    canvas.translate(170, 0);
                }
                canvas.restore();
                canvas.translate(0, 170);
            }
        }
    }
}