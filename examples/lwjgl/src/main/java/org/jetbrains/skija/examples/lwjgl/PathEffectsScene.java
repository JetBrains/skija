package org.jetbrains.skija.examples.lwjgl;

import java.util.stream.IntStream;
import org.jetbrains.skija.*;

public class PathEffectsScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        int x = 0;
        int y = 0;

        try (Path  pattern = new Path().moveTo(-5f, -3f).lineTo(5f, 0).lineTo(-5f, 3f).closePath();
             Path  dash = new Path().lineTo(10, 0).lineTo(10, 1).lineTo(0, 1).closePath();
             Paint stroke = new Paint().setColor(0x20457b9d).setStyle(Paint.Style.STROKE).setStrokeWidth(1);
             Paint fill   = new Paint().setColor(0xFFe76f51).setStyle(Paint.Style.STROKE).setStrokeWidth(1);
             Path  figure = new Path().moveTo(100, 10).lineTo(190, 190).lineTo(10, 190).closePath();)
        {

            PathEffect[] effects = new PathEffect[] {
                PathEffect.path1D(pattern, 10,  0, PathEffect.Style.TRANSLATE),
                PathEffect.path1D(pattern, 20, 0, PathEffect.Style.TRANSLATE),
                PathEffect.path1D(pattern, 20, 2, PathEffect.Style.TRANSLATE),
                PathEffect.path1D(pattern, 20, 0, PathEffect.Style.ROTATE),
                PathEffect.path1D(pattern, 20, 0, PathEffect.Style.MORPH),
                PathEffect.path1D(dash, 15, 5, PathEffect.Style.MORPH),
                PathEffect.path2D(Matrix.scale(15), pattern),
                PathEffect.line2D(1, Matrix.scale(3, 3)),
                PathEffect.line2D(1, Matrix.scale(Matrix.rotate(30), 3, 3)),
                PathEffect.corner(10),
                PathEffect.corner(30),
                PathEffect.dash(new float[] {10, 10}, 0),
                PathEffect.dash(new float[] {10, 10}, 5),
                PathEffect.dash(new float[] {10, 5}, 0),
                PathEffect.dash(new float[] {10, 5, 2, 5}, 0),
                PathEffect.discrete(5, 2, 0),
                PathEffect.discrete(5, 2, (int) (System.currentTimeMillis() / 100)),
                PathEffect.dash(new float[] {10, 5, 2, 5}, 0).compose(PathEffect.corner(50)),
                PathEffect.dash(new float[] {10, 5, 2, 5}, 0).sum(PathEffect.corner(50)),
            };

            for (PathEffect effect: effects) {
                if (x > 0 && x + 200 > width) {
                    x = 0;
                    y += 200;
                }

                canvas.save();
                canvas.translate(x, y);

                canvas.drawPath(figure, stroke);
                Rect bb = effect.computeFastBounds(Rect.makeLTRB(10, 10, 190, 190));
                canvas.drawRect(bb, stroke);

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