package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public class FiltersScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        try (Paint fill = new Paint().setColor(0xFF8E86C9)) {
            try (ImageFilter shadow = ImageFilter.dropShadow(0, 0, 10, 10, 0xFF000000)) {
                fill.setImageFilter(shadow);
                canvas.drawRect(Rect.makeXYWH(0, 0, 40, 40), fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter shadow = ImageFilter.dropShadow(0, 0, 2, 2, 0xFF000000)) {
                fill.setImageFilter(shadow);
                canvas.drawRect(Rect.makeXYWH(0, 0, 40, 40), fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter shadow = ImageFilter.dropShadow(2, 2, 0, 0, 0xFF000000)) {
                fill.setImageFilter(shadow);
                canvas.drawRect(Rect.makeXYWH(0, 0, 40, 40), fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter shadow = ImageFilter.dropShadow(0, 0, 10, 10, 0xFFF42372)) {
                fill.setImageFilter(shadow);
                canvas.drawRect(Rect.makeXYWH(0, 0, 40, 40), fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter shadow = ImageFilter.dropShadow(0, 0, 2, 2, 0xFFCC3333);
                 Path path = new Path())
            {
                fill.setImageFilter(shadow);
                path.setFillType(Path.FillType.EVEN_ODD).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);
                canvas.drawPath(path, fill);
                canvas.translate(50, 0);
            }
        }
    }
}