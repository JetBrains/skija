package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public class PixelGridScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);

        try (var fill = new Paint().setColor(0xFF000000);
             var stroke = new Paint().setColor(0xFF000000).setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            Rect rect;

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
        }
    }
}