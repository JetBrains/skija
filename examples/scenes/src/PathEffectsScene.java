package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class PathEffectsScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        int x = 0;
        int y = 0;

        try (Path  pattern = new Path().moveTo(-5f, -3f).lineTo(5f, 0).lineTo(-5f, 3f).closePath();
             Path  dash = new Path().lineTo(10, 0).lineTo(10, 1).lineTo(0, 1).closePath();
             Paint stroke = new Paint().setColor(0x20457b9d).setMode(PaintMode.STROKE).setStrokeWidth(1);
             Paint fill   = new Paint().setColor(0xFFe76f51).setMode(PaintMode.STROKE).setStrokeWidth(1);
             Path  figure = new Path().moveTo(100, 10).lineTo(190, 190).lineTo(10, 190).closePath();)
        {
            float offset = 1f - System.currentTimeMillis() % 1000 / 1000f;

            PathEffect[] effects = new PathEffect[] {
                PathEffect.makePath1D(pattern, 10, 10 * offset, PathEffect.Style.TRANSLATE),
                PathEffect.makePath1D(pattern, 20, 20 * offset, PathEffect.Style.TRANSLATE),
                PathEffect.makePath1D(pattern, 20, 20 * offset, PathEffect.Style.ROTATE),
                PathEffect.makePath1D(pattern, 20, 20 * offset, PathEffect.Style.MORPH),
                PathEffect.makePath1D(dash, 15, 15 * offset, PathEffect.Style.MORPH),
                PathEffect.makePath2D(Matrix33.makeScale(15), pattern),
                PathEffect.makeLine2D(1, Matrix33.makeScale(3, 3)),
                PathEffect.makeLine2D(1, Matrix33.makeScale(3,3).makeConcat(Matrix33.makeRotate(30))),
                PathEffect.makeCorner(10),
                PathEffect.makeCorner(30),
                PathEffect.makeDash(new float[] {10, 10}, 20 * offset),
                PathEffect.makeDash(new float[] {10, 5}, 15 * offset),
                PathEffect.makeDash(new float[] {10, 5, 2, 5}, 22 * offset),
                PathEffect.makeDiscrete(5, 2, (int) (System.currentTimeMillis() / 100)),
                PathEffect.makeDash(new float[] {10, 5, 2, 5}, 22 * offset).makeCompose(PathEffect.makeCorner(50)),
                PathEffect.makeDash(new float[] {10, 5, 2, 5}, 22 * offset).makeSum(PathEffect.makeCorner(50)),
            };

            for (PathEffect effect: effects) {
                if (x > 0 && x + 200 > width) {
                    x = 0;
                    y += 200;
                }

                canvas.save();
                canvas.translate(x, y);

                canvas.drawPath(figure, stroke);

                fill.setPathEffect(effect);
                canvas.drawPath(figure, fill);
                fill.setPathEffect(null);
                effect.close();

                canvas.restore();

                x += 200;
            }
        }
   }
}