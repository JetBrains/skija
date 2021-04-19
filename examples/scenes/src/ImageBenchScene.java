package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.jetbrains.skija.*;

public class ImageBenchScene extends Scene {
    Image[] images;
    float[] xs, ys, dx, dy;
    int[] phases;
    int sprites = 50000;
    long lastPhaseChange = 0;
    int[] frames;

    public ImageBenchScene() throws IOException {
        images = new Image[14];
        frames = new int[14];
        for (int i = 0; i < 14; ++i) {
            images[i] = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/sprites/bunny" + i + ".png"))));
            frames[i] = images[i].getWidth() / 32;
        }
        _variants = new String[] {"1000", "5000", "50000"};
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (xs == null) {
            xs = new float[sprites];
            ys = new float[sprites];
            dx = new float[sprites];
            dy = new float[sprites];
            phases = new int[sprites];
            Random random = new Random();
            for (int i = 0; i < sprites; ++i) {
                xs[i] = random.nextFloat() * (width - 32);
                ys[i] = random.nextFloat() * (height - 32);
                dx[i] = (random.nextFloat() - 0.5f) * 6f;
                dy[i] = (random.nextFloat() - 0.5f) * 6f;
                phases[i] = random.nextInt(4);
            }
        }

        long visible = Long.parseLong(_variants[_variantIdx]);
        boolean updatePhase = System.currentTimeMillis() - lastPhaseChange > 300;
        if (updatePhase)
            lastPhaseChange = System.currentTimeMillis();
        for (int i = 0; i < visible; ++i) {
            int image = (int) Math.ceil(14 * i / visible);
            if (updatePhase)
                phases[i] = ((phases[i] + 1) % frames[image]);
            canvas.drawImageRect(images[image],
                                 Rect.makeXYWH(32 * phases[i], 0, 32, 32),
                                 Rect.makeXYWH(xs[i], ys[i], 32, 32),
                                 SamplingMode.DEFAULT,
                                 null,
                                 true);
            
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
