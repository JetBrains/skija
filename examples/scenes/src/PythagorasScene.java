package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class PythagorasScene extends Scene {
    private int    color1   = 0xFFcc5333;
    private int    color2   = 0xFF70e92d;
    private double minAngle = 0;
    private int    maxDepth = 10;
    private Paint  paint    = new Paint();

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        double angle   = (90 - (2f * minAngle)) * (1.0 - (double) xpos / (double) width) + minAngle;
        angle = Math.max(minAngle, angle);
        angle = Math.min(90 - minAngle, angle);
        var startWidth = height / 4.0;
        
        canvas.save();
        canvas.translate(((float) ((double) width - startWidth)) / 2f, height);
        drawFractal(canvas, Math.toRadians(angle), startWidth, height - ypos, maxDepth);
        canvas.restore();
    }

    //      2 *
    //       /|\
    //      / | \
    //     /  |  \
    //    /   |   \
    // 0 *----|----* 1
    //        3

    private void drawFractal(Canvas canvas, double a0, double l01, double treeHeight, int depth) {
        var height = Math.min(l01, treeHeight);

        canvas.save();
        canvas.translate(0, (float) -height);
        paint.setColor(Color.makeLerp(color1, color2, (float) depth / maxDepth));
        canvas.drawRect(Rect.makeXYWH(0, 0, (float) l01, (float) height), paint);

        var leftoverHeight = treeHeight - height;
        if (depth > 0 && leftoverHeight > 0) {
            // calc left
            var l02 = Math.cos(a0) * l01;
            var l03 = Math.cos(a0) * l02;
            var l23 = Math.sin(a0) * l02;

            // scale 23 proportional to leftover height / 02
            var scale = l02 < leftoverHeight ? 1 : leftoverHeight / l02;
            var l23prime = l23 * scale;
            var a0prime = Math.atan2(l23prime, l03);
            var l02prime = l03 / Math.cos(a0prime);

            // calc right
            var l13 = l01 - l03;
            var a1prime = Math.atan2(l23prime, l13);
            var l12 = l13 / Math.cos(a1prime);
            var a2prime = Math.PI - a0prime - a1prime;

            // draw children
            canvas.rotate((float) Math.toDegrees(-a0prime));
            drawFractal(canvas, a0, l02prime, leftoverHeight, depth - 1);
            canvas.translate((float) l02prime, 0);
            canvas.rotate((float) Math.toDegrees(Math.PI - a2prime));
            drawFractal(canvas, a0, l12, leftoverHeight, depth - 1);
        }
        canvas.restore();
    }
}