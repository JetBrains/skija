package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShapersScene extends Scene {
    public final Font firaCode11;
    public final Paint stroke = new Paint().setColor(0x203333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFF000000);

    public ShapersScene() {
        firaCode11 = new Font(Typeface.makeFromFile(file("fonts/FiraCode-Regular.ttf")), 11);
    }

    public void drawWithShaper(Canvas canvas, float maxWidth, int color, String name, Shaper shaper) {
        fill.setColor(color);
        String text = "Incomprehensibilities word word word word ararar123456 +++ <-> *** c̝̣̱̲͈̝ͨ͐̈ͪͨ̃ͥͅh̙̬̿̂a̯͎͍̜͐͌͂̚o̬s͉̰͊̀ " + name;

        try (var blob = shaper.shape(text, firaCode11, ShapingOptions.DEFAULT, maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        try (var blob = shaper.shape(text + " RTL", firaCode11, ShapingOptions.DEFAULT.withLeftToRight(false), maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        String features = "ss01 cv01 onum -calt";
        try (var blob = shaper.shape(text + " " + features, firaCode11, ShapingOptions.DEFAULT.withFeatures(features), maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        features = "ss01[42:45] cv01[42:45] onum[48:51] calt[59:60]=0";
        try (var blob = shaper.shape(text + " " + features, firaCode11, ShapingOptions.DEFAULT.withFeatures(features), maxWidth, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, fill);
            canvas.translate(0, blob.getBounds().getHeight());
        }

        float height = 0;

        try (var blob = shaper.shape(text, firaCode11, ShapingOptions.DEFAULT, 75, new Point(0, 0)); ) {
            canvas.drawTextBlob(blob, 0, 0, fill);
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