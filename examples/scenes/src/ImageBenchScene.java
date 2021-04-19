package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import org.jetbrains.skija.*;

public class ImageBenchScene extends Scene {
    Image[] sprites;
    Image all;
    float[] xs, ys, dx, dy;
    int[] phases;
    long lastPhaseChange = 0;

    public ImageBenchScene() throws IOException {
        sprites = new Image[14];
        for (int i = 0; i < 14; ++i) {
            sprites[i] = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/sprites/bunny" + i + ".png"))));
        }
        all = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/sprites/all.png"))));
        _variants = new String[] {"One image", "14 images"};
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (xs == null) {
            xs = new float[50000];
            ys = new float[50000];
            dx = new float[50000];
            dy = new float[50000];
            phases = new int[50000];
            Random random = new Random();
            for (int i = 0; i < 50000; ++i) {
                xs[i] = random.nextFloat() * (width - 32);
                ys[i] = random.nextFloat() * (height - 32);
                dx[i] = (random.nextFloat() - 0.5f) * 6f;
                dy[i] = (random.nextFloat() - 0.5f) * 6f;
                phases[i] = random.nextInt(4);
            }
        }

        boolean updatePhase = System.currentTimeMillis() - lastPhaseChange > 300;
        if (updatePhase)
            lastPhaseChange = System.currentTimeMillis();
        for (int i = 0; i < 50000; ++i) {

            if ("One image".equals(_variants[_variantIdx])) {
                int image = i % 14;
                canvas.drawImageRect(all,
                                     Rect.makeXYWH(32 * phases[i], image * 32, 32, 32),
                                     Rect.makeXYWH(xs[i], ys[i], 32, 32),
                                     SamplingMode.DEFAULT,
                                     null,
                                     true);
            } else {
                int image = (int) Math.ceil(14 * i / 50000);
                canvas.drawImageRect(sprites[image],
                                     Rect.makeXYWH(32 * phases[i], 0, 32, 32),
                                     Rect.makeXYWH(xs[i], ys[i], 32, 32),
                                     SamplingMode.DEFAULT,
                                     null,
                                     true);
            }

            if (updatePhase)
                phases[i] = (phases[i] + 1) % 4;
            
            if (xs[i] + dx[i] + 32 >= width && dx[i] > 0)
                dx[i] = -dx[i];
            if (xs[i] + dx[i] <= 0 && dx[i] < 0)
                dx[i] = -dx[i];
            xs[i] = xs[i] + dx[i];

            if (ys[i] + dy[i] + 32 >= height && dy[i] > 0)
                dy[i] = -dy[i];
            if (ys[i] + dy[i] <= 0 && dy[i] < 0)
                dy[i] = -dy[i];
            ys[i] = ys[i] + dy[i];
        }
    }
}
