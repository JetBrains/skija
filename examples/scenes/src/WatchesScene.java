package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class WatchesScene extends Scene {

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var watchFill = new Paint().setColor(0xFFFFFFFF);
        var watchStroke = new Paint().setColor(0xFF000000).setMode(PaintMode.STROKE).setStrokeWidth(1f).setAntiAlias(false);
        var watchStrokeAA = new Paint().setColor(0xFF000000).setMode(PaintMode.STROKE).setStrokeWidth(1f);
        var watchFillHover = new Paint().setColor(0xFFE4FF01);

        for (var x = 0f; x < width - 50; x += 50) {
            for (var y = 0f; y < height - 50; y += 50) {
                var hover = xpos > x + 0 && xpos < x + 50 && ypos > y + 0 && ypos < y + 50;
                var fill = hover ? watchFillHover : watchFill;
                var stroke = x > width / 2 ? watchStrokeAA : watchStroke;

                canvas.drawOval(Rect.makeXYWH(x + 5, y + 5, 40, 40), fill);
                canvas.drawOval(Rect.makeXYWH(x + 5, y + 5, 40, 40), stroke);

                for (var angle = 0f; angle < 2f * Math.PI; angle += 2f * Math.PI / 12f) {
                    canvas.drawLine(
                      (float) (x + 25 - 17 * Math.sin(angle)),
                      (float) (y + 25 + 17 * Math.cos(angle)),
                      (float) (x + 25 - 20 * Math.sin(angle)),
                      (float) (y + 25 + 20 * Math.cos(angle)),
                      stroke);
                }

                var time = System.currentTimeMillis() % 60000 + x / width * 5000 + y / width * 5000;

                var angle1 = time / 5000 * 2f * Math.PI;
                canvas.drawLine(x + 25, y + 25, (float) (x + 25 - 15 * Math.sin(angle1)), (float) (y + 25 + 15 * Math.cos(angle1)), stroke);

                var angle2 = time / 60000 * 2f * Math.PI;
                canvas.drawLine(x + 25, y + 25, (float) (x + 25 - 10 * Math.sin(angle2)), (float) (y + 25 + 10 * Math.cos(angle2)), stroke);
            }
        }
    }
}