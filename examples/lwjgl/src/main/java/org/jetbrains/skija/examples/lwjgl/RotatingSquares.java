package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.RoundedRect;
import org.jetbrains.skija.Shader;

public class RotatingSquares implements Scene {
    static long start = System.currentTimeMillis();

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        final int squareWidth = 45;
        final int margin = 14;
        var shader = Shader.linearGradient(0, -squareWidth / 2f, 0, squareWidth / 2f, new int[] { 0xFFFFA500, 0x00000000 } );
        var gradient = new Paint().setStyle(Paint.Style.FILL).setShader(shader);

        int currentX = margin;
        int currentY = margin;
        int i = 0;
        while (true) {
            final long now = System.currentTimeMillis();
            var dur = (now - start) + (i * 50);
            var pos = (dur % 3000) / 3000f;
            var bouncePos = Math.abs(pos * 2f - 1f);

            int r = Math.round((bouncePos * 50f + 50f) / 100f * 255f);
            int b = Math.round((1 - bouncePos) * 50f  / 100f * 255f);
            var color = 0xFF000000 | r << 16 | b;
            var solid = new Paint().setColor(color).setStyle(Paint.Style.FILL);

            var rotation = Math.round(pos * 360f);
            var radius = bouncePos * 25f;

            canvas.save();
            canvas.translate(currentX + squareWidth / 2.0f, currentY + squareWidth / 2.0f);
            canvas.rotate(rotation);
            canvas.drawRoundedRect(RoundedRect.makeXYWH(-squareWidth / 2.0f, -squareWidth / 2.0f, squareWidth, squareWidth, radius), solid);
            canvas.drawRoundedRect(RoundedRect.makeXYWH(-squareWidth / 2.0f, -squareWidth / 2.0f, squareWidth, squareWidth, radius), gradient);
            canvas.restore();

            currentX += (squareWidth + margin);
            if ((currentX + (squareWidth + margin)) >= width) {
                currentX = margin;
                currentY += (squareWidth + margin);
                if ((currentY + (squareWidth + margin)) >= height) break;
            }
            i++;
        }
    }
}
