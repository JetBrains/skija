package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;

public class BenchCirclesScene extends Scene {
    public float[] _arr; // x, y, dx, dy
    public int[] _colors;
    public float radius = 4;

    public BenchCirclesScene() {
        _variants = new String[] { "10000", "1000", "100000" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (_arr == null) {
            var random = new Random();
            _arr = new float[100000 * 4];
            _colors = new int[100000];
            for (int i = 0; i < 100000; ++i) {
                _arr[i * 4] = radius + random.nextFloat() * (width - radius);
                _arr[i * 4 + 1] = radius + random.nextFloat() * (height - radius);
                _arr[i * 4 + 2] = random.nextFloat() - 0.5f;
                _arr[i * 4 + 3] = random.nextFloat() - 0.5f;
                _colors[i] = 0xFF000000 | random.nextInt(0xFFFFFF);
            }
        }

        int circles = Integer.parseInt(variantTitle());

        try (var fill = new Paint().setColor(0xFFCC3333)) {
            for (int i = 0; i < circles; ++i) {
                var x = _arr[i * 4];
                var dx = _arr[i * 4 + 2];
                if ((x + dx > width - radius && dx > 0) || (x + dx < radius && dx < 0)) {
                    dx = -dx;
                    _arr[i * 4 + 2] = dx;
                }
                _arr[i * 4] = x + dx;

                var y = _arr[i * 4 +1];
                var dy = _arr[i * 4 + 3];
                if ((y + dy > height - radius && dy > 0) || (y + dy < radius && dy < 0)) {
                    dy = -dy;
                    _arr[i * 4 + 3] = dy;
                }
                _arr[i * 4 + 1] = y + dy;

                fill.setColor(_colors[i]);
                canvas.drawCircle(_arr[i * 4], _arr[i * 4 + 1], radius, fill);
            }
        }
    }
}