package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class PixelGridScene extends Scene {
    @Override
    public boolean scale() {
        return false;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        try (var fill = new Paint().setColor(0xFF000000);
             var stroke = new Paint().setColor(0xFF000000).setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            Rect rect;

            // 3px rect
            rect = Rect.makeLTRB(0, 0, 3, 3);
            canvas.drawRect(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawRect(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            rect = Rect.makeLTRB(0.5f, 0.5f, 2.5f, 2.5f);
            canvas.drawRect(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawRect(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            // 10px rect
            rect = Rect.makeLTRB(0, 0, 10, 10);
            canvas.drawRect(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawRect(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            rect = Rect.makeLTRB(0.5f, 0.5f, 9.5f, 9.5f);
            canvas.drawRect(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawRect(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            // Circle
            canvas.drawCircle(5, 5, 5, stroke);
            canvas.drawCircle(25, 5, 5, fill);
            canvas.drawString("5", 40, 10, inter13, fill);
            canvas.translate(0, 40);

            canvas.drawCircle(5, 5, 4.5f, stroke);
            canvas.drawCircle(25, 5, 4.5f, fill);
            canvas.drawString("4.5", 40, 10, inter13, fill);
            canvas.translate(0, 40);

            // Oval
            rect = Rect.makeLTRB(0, 0, 10, 10);
            canvas.drawOval(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawOval(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            rect = Rect.makeLTRB(0.5f, 0.5f, 9.5f, 9.5f);
            canvas.drawOval(rect, stroke);
            canvas.translate(20, 0);
            canvas.drawOval(rect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            // RRect
            RRect rrect;
            rrect = RRect.makeLTRB(0, 0, 10, 10, 3);
            canvas.drawRRect(rrect, stroke);
            canvas.translate(20, 0);
            canvas.drawRRect(rrect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rrect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            rrect = RRect.makeLTRB(0.5f, 0.5f, 9.5f, 9.5f, 3);
            canvas.drawRRect(rrect, stroke);
            canvas.translate(20, 0);
            canvas.drawRRect(rrect, fill);
            canvas.translate(20, 0);
            canvas.drawString(rrect.toString(), 0, 10, inter13, fill);
            canvas.translate(-40, 40);

            // Touching rects
            canvas.drawRect(Rect.makeLTRB(0, 0, 5.5f, 14f), fill);
            canvas.drawRect(Rect.makeLTRB(5.5f, 2f, 10.333333f, 16f), fill);
            canvas.drawRect(Rect.makeLTRB(10.333333f, 4f, 15.125f, 18f), fill);
            canvas.drawRect(Rect.makeLTRB(15.125f, 6f, 20f, 20f), fill);
            canvas.drawString("0 .. 5.5 .. 10.333333 .. 15.125 .. 20", 40, 15, inter13, fill);
            canvas.translate(0, 50);

            // Lines
            canvas.drawLine(0.5f, 0f, 0.5f, 10f, stroke);
            canvas.drawLine(10.5f, 0.5f, 10.5f, 9.5f, stroke);
            stroke.setStrokeCap(PaintStrokeCap.SQUARE);
            canvas.drawLine(20.5f, 0f, 20.5f, 10f, stroke);
            canvas.drawLine(30.5f, 0.5f, 30.5f, 9.5f, stroke);
            stroke.setStrokeCap(PaintStrokeCap.BUTT);
            canvas.drawString("0-10, 0.5-9.5 BUTT, 0-10, 0.5-9.5 SQUARE", 40, 10, inter13, fill);
            canvas.translate(0, 40);
        }
    }
}