package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class ShaperScene implements Scene {
    public final Font firaCode11;
    public final Paint stroke = new Paint().setColor(0x203333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFF333333);

    public ShaperScene() {
        firaCode11 = new Font(Typeface.makeFromFile("fonts/FiraCode-Regular.otf"), 11);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        canvas.drawLine(0, 0, 0, height, stroke);
        canvas.drawLine(300, 0, 300, height, stroke);
        canvas.drawLine(350, 0, 350, height, stroke);

        var fontMgr = FontMgr.getDefault();

        for (var pair: new Pair [] {
            new Pair("Primitive", Shaper.makePrimitive()),
            new Pair("ShaperDrivenWrapper", Shaper.makeShaperDrivenWrapper()),
            new Pair("ShaperDrivenWrapper + FM", Shaper.makeShaperDrivenWrapper(fontMgr)),
            new Pair("ShapeThenWrap", Shaper.makeShapeThenWrap()),
            new Pair("ShapeThenWrap + FM", Shaper.makeShapeThenWrap(fontMgr)),
            new Pair("ShapeDontWrapOrReorder", Shaper.makeShapeDontWrapOrReorder()),
            new Pair("ShapeDontWrapOrReorder + FM", Shaper.makeShapeDontWrapOrReorder(fontMgr)),
            new Pair("CoreText", System.getProperty("os.name").equals("Mac OS X") ? Shaper.makeCoreText() : null),
            new Pair("make", Shaper.make()),
            new Pair("make + FM", Shaper.make(fontMgr))
        }) {
            String name = (String) pair.getFirst();
            Shaper shaper = (Shaper) pair.getSecond();
            if (null == shaper) continue;
            String text = name + " -> +++ *** ---" ;

            try (var blob = shaper.shape(text, firaCode11, true, Float.POSITIVE_INFINITY, new Point(0, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            }

            try (var blob = shaper.shape(text, firaCode11, true, 50f, new Point(300, 0)); ) {
                canvas.drawTextBlob(blob, 0, 0, firaCode11, fill);
            }

            canvas.drawLine(0, 0, width, 0, stroke);

            canvas.translate(0, 80);

            shaper.close();
        }

        
    }
}