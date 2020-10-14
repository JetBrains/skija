package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShaperScene implements Scene {
    public final Font firaCode11;
    public final Paint stroke = new Paint().setColor(0x203333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFF000000);

    public ShaperScene() {
        firaCode11 = new Font(Typeface.makeFromFile("fonts/FiraCode-Regular.ttf"), 11);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        var fontMgr = FontMgr.getDefault();

        for (var pair: new Pair [] {
            new Pair("Primitive", Shaper.makePrimitive()),
            new Pair("ShaperDrivenWrapper", Shaper.makeShaperDrivenWrapper()),
            new Pair("ShapeThenWrap", Shaper.makeShapeThenWrap()),
            new Pair("ShapeDontWrapOrReorder", Shaper.makeShapeDontWrapOrReorder()),
            new Pair("CoreText", System.getProperty("os.name").equals("Mac OS X") ? Shaper.makeCoreText() : null),
            new Pair("make", Shaper.make())
        }) {
            String name = (String) pair.getFirst();
            Shaper shaper = (Shaper) pair.getSecond();
            if (null == shaper) continue;
            String text = "Incomprehensibilities ararar123456 +++ *** <-> " + name;

            try (var blob = shaper.shape(text, firaCode11, null, true, Float.POSITIVE_INFINITY, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                canvas.translate(0, blob.getBounds().getHeight());
            }

            try (var blob = shaper.shape(text + " RTL", firaCode11, null, false, Float.POSITIVE_INFINITY, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                canvas.translate(0, blob.getBounds().getHeight());
            }

            FontFeature[] features = new FontFeature[] { new FontFeature("ss01"), new FontFeature("cv01"), new FontFeature("onum"), new FontFeature("calt", 0) };
            try (var blob = shaper.shape(text + " ss01 cv01 onum -calt", firaCode11, features, true, Float.POSITIVE_INFINITY, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                canvas.translate(0, blob.getBounds().getHeight());
            }

            features = new FontFeature[] { new FontFeature("ss01", 1, 22, 25), new FontFeature("cv01", 1, 22, 25), new FontFeature("onum", 1, 28, 31), new FontFeature("calt", 0, 43, 44) };
            try (var blob = shaper.shape(text + " ss01[22:25] cv01[22:25] onum[28:31] -calt[43:44]", firaCode11, features, true, Float.POSITIVE_INFINITY, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                canvas.translate(0, blob.getBounds().getHeight());
            }

            try (var blob = shaper.shape(text, firaCode11, null, true, 75, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
                Rect bounds = blob.getBounds();
                canvas.drawRect(Rect.makeXYWH(0, 0, 75, bounds.getHeight()), stroke);
                canvas.translate(0, bounds.getHeight());
            }

            canvas.translate(0, 20);

            shaper.close();
        }

        
    }
}