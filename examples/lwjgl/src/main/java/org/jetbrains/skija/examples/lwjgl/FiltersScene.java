package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public class FiltersScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        drawShadows(canvas); 
        drawBlurs(canvas);
    }

    private void drawShadows(Canvas canvas) {
        canvas.save();
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

            try (ImageFilter shadow = ImageFilter.dropShadowOnly(0, 0, 2, 2, 0xFFCC3333);
                 Path path = new Path())
            {
                fill.setImageFilter(shadow);
                path.setFillType(Path.FillType.EVEN_ODD).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);
                canvas.drawPath(path, fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter shadow = ImageFilter.dropShadow(0, 0, 2, 2, 0xFFCC3333, null, IRect.makeLTRB(10, 10, 30, 30));
                 Path path = new Path())
            {
                fill.setImageFilter(shadow);
                path.setFillType(Path.FillType.EVEN_ODD).rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);
                canvas.drawPath(path, fill);
                canvas.translate(50, 0);
            }

            try (ImageFilter s1 = ImageFilter.dropShadow(-2, -2, 2, 2, 0xFFCC3333);
                 ImageFilter s2 = ImageFilter.dropShadow(2, 2, 2, 2, 0xFF3333CC, s1))
            {
                fill.setImageFilter(s2);
                canvas.drawRect(Rect.makeXYWH(0, 0, 40, 40), fill);
                canvas.translate(50, 0);
            }
        }
        canvas.restore();
        canvas.translate(0, 50);
    }

    private void drawBlurs(Canvas canvas) {
        try (Paint fig1 = new Paint().setColor(0xFF8E86C9);
             Paint fig2 = new Paint().setColor(0xFF8E86C9);
             Paint fig3 = new Paint().setColor(0xFF8E86C9);) {

            canvas.save();
            for (var tileMode: TileMode.values()) {
                canvas.drawRect(Rect.makeXYWH(0, 0, 20, 20), fig1);
                canvas.drawRect(Rect.makeXYWH(30, 20, 40, 20), fig2);
                canvas.drawCircle(30, 50, 10, fig3);
                try (ImageFilter blur = ImageFilter.blur(2, 2, tileMode);
                     Paint paint = new Paint().setColor(0x90CC3333).setImageFilter(blur)) {
                    canvas.drawRect(Rect.makeXYWH(10, 10, 40, 40), paint);
                }
                canvas.translate(80, 0);
            }
            canvas.restore();
            canvas.translate(0, 70);
        }
    }
}