package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShapersScene implements Scene {
    public final Font firaCode11;
    public final Paint stroke = new Paint().setColor(0x203333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFF000000);

    public ShapersScene() {
        firaCode11 = new Font(Typeface.makeFromFile("fonts/FiraCode-Regular.ttf"), 11);
    }

    public void drawWithShaper(Canvas canvas, float maxWidth, int color, String name, Shaper shaper) {
        fill.setColor(color);
        String text = "Incomprehensibilities ararar123456 +++ *** <-> " + name;

        try (var blob = shaper.shape(text, firaCode11, null, true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        try (var blob = shaper.shape(text + " RTL", firaCode11, null, false, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        String features = "ss01 cv01 onum -calt";
        try (var blob = shaper.shape(text + " " + features, firaCode11, FontFeature.parse(features), true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        features = "ss01[22:25] cv01[22:25] onum[28:31] calt[43:44]=0";
        try (var blob = shaper.shape(text + " " + features, firaCode11, FontFeature.parse(features), true, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        float height = 0;

        try (var blob = shaper.shape(text, firaCode11, null, true, 75, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            Rect bounds = blob.getBounds();
            canvas.drawRect(Rect.makeXYWH(0, 0, 75, bounds.getHeight()), stroke);
            canvas.translate(0, bounds.getHeight());
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var fontMgr = FontMgr.getDefault();

        try (var shaper = Shaper.makePrimitive()) {
            canvas.save();
            canvas.translate(20, 20);
            drawWithShaper(canvas, width / 2 - 30, 0xFFf94144, "Primitive", shaper);
            canvas.restore();
        }

        try (var shaper = Shaper.make()) {
            canvas.save();
            canvas.translate(20, height / 3);
            drawWithShaper(canvas, width / 2 - 30, 0xFF577590, "make", shaper);
            canvas.restore();
        }

        if (System.getProperty("os.name").equals("Mac OS X")) {
            canvas.save();
            canvas.translate(20, height * 2 / 3);
            try (var shaper = Shaper.makeCoreText()) {
                drawWithShaper(canvas, width / 2 - 30, 0xFFf3722c, "CoreText", shaper);
            }
            canvas.restore();
        }


        try (var shaper = Shaper.makeShaperDrivenWrapper()) {
            canvas.save();
            canvas.translate(width / 2 + 10, 20);
            drawWithShaper(canvas, width / 2 - 30, 0xFF43aa8b, "ShaperDrivenWrapper", shaper);
            canvas.restore();
        }

        try (var shaper = Shaper.makeShapeThenWrap()) {
            canvas.save();
            canvas.translate(width / 2 + 10, height / 3);
            drawWithShaper(canvas, width / 2 - 30, 0xFFf8961e, "ShapeThenWrap", shaper);
            canvas.restore();
        }

        try (var shaper = Shaper.makeShapeDontWrapOrReorder()) {
            canvas.save();
            canvas.translate(width / 2 + 10, height * 2/ 3);
            drawWithShaper(canvas, width / 2 - 30, 0xFF90be6d, "ShapeDontWrapOrReorder", shaper);
            canvas.restore();
        }

        canvas.restore();
    }
}