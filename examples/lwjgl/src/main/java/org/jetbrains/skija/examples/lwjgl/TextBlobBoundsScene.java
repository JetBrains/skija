package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class TextBlobBoundsScene extends Scene {
    private Paint fill = new Paint().setColor(0xFFCC3333);
    private Paint stroke = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Font inter18 = new Font(inter, 18);
    private Font zapfino18 = new Font(FontMgr.getDefault().matchFamilyStyle("Zapfino", FontStyle.NORMAL), 18);

    public TextBlobBoundsScene() {
        _variants = new String[] { "Tight", "Loose" };
    }
    
    public void drawLine(Canvas canvas, String text, Font font) {
        drawLine(canvas, text, font, Float.POSITIVE_INFINITY);
    }

    public void drawLine(Canvas canvas, String text, Font font, float width) {
        drawLine(canvas, new String[] { text }, font, width);
    }

    public void drawLine(Canvas canvas, String[] texts, Font font) {
        drawLine(canvas, texts, font, Float.POSITIVE_INFINITY);
    }

    public void drawLine(Canvas canvas, String[] texts, Font font, float width) {
        canvas.save();
        float bottom = 0;
        for (var text: texts) {
            try (var shaper = Shaper.makeShapeThenWrap();
                 var blob = shaper.shape(text, font, width);)
            {
                var bounds = "Tight".equals(variantTitle()) ? blob.getTightBounds() : blob.getBounds();
                canvas.drawCircle(0, 0, 3, stroke);
                canvas.drawRect(bounds, stroke);
                canvas.drawTextBlob(blob, 0, 0, font, fill);
                canvas.translate(bounds.getRight() + 20, 0);
                bottom = Math.max(bottom, bounds.getBottom());
            }
        }
        canvas.restore();
        canvas.translate(0, bottom + 20);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        drawLine(canvas, "onerun", inter18);
        drawLine(canvas, "xyfx", inter18);
        drawLine(canvas, new String[] { "one", "два", "three", "one два three" }, inter18);
        drawLine(canvas, "multiple different lines", inter18, 50);
        drawLine(canvas, new String[] { "ace", "الخطوط", "🧛", "ace الخطوط 🧛" }, inter18);
        drawLine(canvas, "واحد اثنين ثلاثة", inter18);
        drawLine(canvas, "fiz", zapfino18);
    }
}