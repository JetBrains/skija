package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class TextBlobBoundsScene extends Scene {
    private Paint fill = new Paint().setColor(0xFFCC3333);
    private Paint blueFill = new Paint().setColor(0xFF3333CC);
    private Paint selectionFill = new Paint().setColor(0x80b3d7ff);
    private Paint stroke = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Font inter11 = new Font(inter, 11);
    private Font inter18 = new Font(inter, 18);
    private Font zapfino18 = new Font(FontMgr.getDefault().matchFamilyStyle("Zapfino", FontStyle.NORMAL), 18);

    public TextBlobBoundsScene() {
        _variants = new String[] { "Block", "Tight", "Loose" };
    }
    
    public float drawLine(Canvas canvas, String text, Font font, Point cursor) {
        return drawLine(canvas, text, font, Float.POSITIVE_INFINITY, cursor);
    }

    public float drawLine(Canvas canvas, String text, Font font, float width, Point cursor) {
        return drawLine(canvas, new String[] { text }, font, width, cursor);
    }

    public float drawLine(Canvas canvas, String[] texts, Font font, Point cursor) {
        return drawLine(canvas, texts, font, Float.POSITIVE_INFINITY, cursor);
    }

    public float drawLine(Canvas canvas, String[] texts, Font font, float width, Point cursor) {
        float bottom = 0;
        canvas.save();
        for (var text: texts) {
            try (var shaper = Shaper.makeShapeThenWrap();
                 var blob = shaper.shape(text, font, width);)
            {
                Rect bounds;
                if ("Block".equals(variantTitle()))
                    bounds = blob.getBlockBounds();
                else if ("Tight".equals(variantTitle()))
                    bounds = blob.getTightBounds();
                else // if ("Loose".equals(variantTitle()))
                    bounds = blob.getBounds();
                    
                canvas.translate(-bounds.getLeft(), -bounds.getTop());
                cursor = cursor.offset(-bounds.getLeft(), -bounds.getTop());
                int offset = blob.getOffsetAtCoord(cursor.getX(), cursor.getY());

                // selection
                Point coord = blob.getCoordAtOffset(offset);
                canvas.drawRect(Rect.makeLTRB(0, 0, coord.getX(), bounds.getHeight()), selectionFill);

                // origin
                canvas.drawCircle(0, 0, 3, stroke);
                
                // bounds
                canvas.drawRect(bounds, stroke);

                // text
                canvas.drawTextBlob(blob, 0, 0, font, fill);

                // coords
                for (int i = 0; i < text.length() + 1; ++i) {
                    coord = blob.getCoordAtOffset(i);
                    canvas.drawLine(coord.getX(), coord.getY() - 2, coord.getX(), coord.getY() + 2, stroke);
                }

                // extra info
                canvas.drawString("coord: (" + (int) cursor.getX() + ", " + (int) cursor.getY() + ")", 0, bounds.getBottom() + 10, inter11, blueFill);
                canvas.drawString("offset: " + offset, 0, bounds.getBottom() + 20, inter11, blueFill);

                canvas.translate(bounds.getRight() + 50, 0);
                cursor = cursor.offset(-bounds.getRight() - 50, 0);
                bottom = Math.max(bottom, bounds.getBottom());
            }
        }
        canvas.restore();
        canvas.translate(0, bottom + 50);
        return bottom + 50;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        Point cursor = new Point(xpos, ypos);
        canvas.translate(20, 20);
        cursor = cursor.offset(-20, -20);
        cursor = cursor.offset(0, -drawLine(canvas, "onerun", inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, "xyfx", inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, new String[] { "one", "два", "three", "one два three" }, inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, "multiple different lines", inter18, 50, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, new String[] { "ace", "الخطوط", "🧛", "ace الخطوط 🧛🧑🏿👩‍👩‍👧‍👧" }, inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, "واحد اثنين ثلاثة", inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, "fiz officiad", zapfino18, cursor));
    }
}