package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;
import org.jetbrains.skija.*;

public class ShadowsScene extends Scene {
    protected final Image figma;
    protected final Image firefox;
    protected final Image safari;

    public ShadowsScene() {
        try {
            figma = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/shadows/figma.png"))));
            firefox = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/shadows/firefox.png"))));
            safari = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/shadows/safari.png"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(width / 2 - 175, 20);

        canvas.drawString("Figma Reference", 350, 50, inter13, blackFill);
        canvas.drawImageRect(figma, Rect.makeXYWH(0, 0, 350, 100));

        canvas.translate(0, 100);
        canvas.drawString("Firefox Reference", 350, 50, inter13, blackFill);
        canvas.drawImageRect(firefox, Rect.makeXYWH(0, 0, 350, 100));

        canvas.translate(0, 100);
        canvas.drawString("Safari Reference", 350, 50, inter13, blackFill);
        canvas.drawImageRect(safari, Rect.makeXYWH(0, 0, 350, 100));

        canvas.translate(0, 100);
        canvas.drawString("ImageFilter", 350, 50, inter13, blackFill);
        try (var paint = new Paint().setColor(0xFFE54322);) {
            float blurFactor = 0.5f;
            try (var filter = ImageFilter.makeDropShadow(0, 10, 10 * blurFactor, 10 * blurFactor, 0x40000000)) {
                paint.setImageFilter(filter);
                canvas.drawRect(Rect.makeXYWH(50, 25, 50, 50), paint);
            }
            try (var filter = ImageFilter.makeDropShadow(0, 0, 10 * blurFactor, 10 * blurFactor, 0xFF000000)) {
                paint.setImageFilter(filter);
                canvas.drawRRect(RRect.makeXYWH(150, 25, 50, 50, 20), paint);
            }
            try (var filter = ImageFilter.makeDropShadowOnly(0, 0, 2 * blurFactor, 2 * blurFactor, 0xFF000000)) {
                paint.setImageFilter(filter);
                canvas.drawRRect((RRect) Rect.makeXYWH(250, 25, 50, 50).inflate(10), paint);
                paint.setImageFilter(null);
                canvas.drawRect(Rect.makeXYWH(250, 25, 50, 50), paint);
            }
        }

        canvas.translate(0, 100);
        canvas.drawString("Canvas.drawRectShadow", 350, 50, inter13, blackFill);
        try (var paint = new Paint().setColor(0xFFE54322);) {
            canvas.drawRectShadow(Rect.makeXYWH(50, 25, 50, 50), 0, 10, 10, 0, 0x40000000);
            canvas.drawRect(Rect.makeXYWH(50, 25, 50, 50), paint);
            
            canvas.drawRectShadow(RRect.makeXYWH(150, 25, 50, 50, 20), 0, 0, 10, 0, 0xFF000000);
            canvas.drawRRect(RRect.makeXYWH(150, 25, 50, 50, 20), paint);

            canvas.drawRectShadow(Rect.makeXYWH(250, 25, 50, 50), 0, 0, 2, 10, 0xFF000000);
            canvas.drawRect(Rect.makeXYWH(250, 25, 50, 50), paint);
        }

        canvas.translate(0, 100);
        canvas.drawString("Shadow Utils", 350, 50, inter13, blackFill);
        try (var paint = new Paint().setColor(0xFFE54322);
             var path = new Path().addRect(Rect.makeWH(50, 50));
             var path2 = new Path().addRRect(RRect.makeXYWH(0, 0, 50, 50, 20));)
        {
            var zPlaneParams = new Point3(0, 0, 100);
            var lightPos = new Point3(width / 2 * dpi, 0, 3000);
            var lightRadius = 1000;
            var ambientColor = 0x80ff0000;
            var spotColor = 0x000000ff;

            canvas.save();
            canvas.translate(50, 25);
            ShadowUtils.drawShadow(canvas, path, new Point3(0, 0, 100), lightPos, 1000, 0x40000000, spotColor, false, false);
            canvas.drawPath(path, paint);

            canvas.translate(100, 0);
            ShadowUtils.drawShadow(canvas, path2, new Point3(0, 0, 50), lightPos, 100, 0xFF000000, spotColor, false, false);
            canvas.drawPath(path2, paint);

            canvas.translate(100, 0);
            ShadowUtils.drawShadow(canvas, path, new Point3(0, 0, 25), lightPos, 10, 0xFF000000, spotColor, false, false);
            canvas.drawPath(path, paint);

            canvas.restore();
        }
            
        canvas.translate(0, 125);
        canvas.drawString("x = 0",        50, 0, inter13, blackFill);
        canvas.drawString("y = 10",       50, 20, inter13, blackFill);
        canvas.drawString("blur = 10",    50, 40, inter13, blackFill);
        canvas.drawString("spread = 0",   50, 60, inter13, blackFill);
        canvas.drawString("alpha = 0.25", 50, 80, inter13, blackFill);

        canvas.drawString("x = 0",        150, 0, inter13, blackFill);
        canvas.drawString("y = 0",        150, 20, inter13, blackFill);
        canvas.drawString("blur = 10",    150, 40, inter13, blackFill);
        canvas.drawString("spread = 0",   150, 60, inter13, blackFill);
        canvas.drawString("alpha = 1",    150, 80, inter13, blackFill);

        canvas.drawString("x = 0",        250, 0, inter13, blackFill);
        canvas.drawString("y = 0",        250, 20, inter13, blackFill);
        canvas.drawString("blur = 2",     250, 40, inter13, blackFill);
        canvas.drawString("spread = 10",  250, 60, inter13, blackFill);
        canvas.drawString("alpha = 1",    250, 80, inter13, blackFill);
    }
}