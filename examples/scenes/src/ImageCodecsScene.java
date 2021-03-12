package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import org.jetbrains.skija.*;

public class ImageCodecsScene extends Scene {
    public List<Pair<String, Image>> images = new ArrayList<>();

    public void loadImage(String file) {
        try {
            var path = Path.of(file("images/codecs/" + file));
            images.add(new Pair(file, Image.makeFromEncoded(Files.readAllBytes(path))));
        } catch (IllegalArgumentException e) {
            images.add(new Pair(file, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageCodecsScene() {
        loadImage("bmp.bmp");
        loadImage("gif.gif");
        loadImage("favicon.ico");
        loadImage("dotpeek.ico");
        loadImage("jpeg.jpg");
        loadImage("png.png");
        loadImage("webp_lossy.webp");
        loadImage("webp_loseless.webp");
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        int x = 20;
        int y = 20;

        try (var stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1)) {
            for (var pair: images) {
                var name = pair.getFirst();
                var image = pair.getSecond();
                canvas.save();
                canvas.translate(x, y);
                if (image != null)
                    canvas.drawImageRect(image, Rect.makeXYWH(0, 0, 200, 200));
                else {
                    canvas.drawRect(Rect.makeXYWH(0, 0, 200, 200), stroke);
                    canvas.drawLine(0, 0, 200, 200, stroke);
                    canvas.drawLine(0, 200, 200, 0, stroke);
                }
                canvas.drawString(name, 0, 220, inter13, blackFill);
                canvas.restore();

                x += 220;
                if (x + 220 >= width) {
                    x = 20;
                    y += 240;
                }
            }
        }
    }
}