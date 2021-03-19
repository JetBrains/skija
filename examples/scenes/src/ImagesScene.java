package org.jetbrains.skija.examples.scenes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.skija.*;

public class ImagesScene extends Scene {
    protected final Image circus;
    protected final Image circusCropped;
    protected final Image cloud;
    protected final Image ducks;
    protected final Image[] tests;

    public ImagesScene() {
        try {
            circus = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/circus.jpg"))));
            circusCropped = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/circus.jpg"))));
            cloud = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/cloud.png"))));
            ducks = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/ducks.jpg"))));
            tests = new Image[] {
                Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/icc-v2-gbr.jpg")))),
                Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/purple-displayprofile.png")))),
                Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/wide-gamut.png")))),
                Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/wide_gamut_yellow_224_224_64.jpeg")))),
                Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/webkit_logo_p3.png")))),
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);

        canvas.save();
        canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 640, 640), Rect.makeXYWH(0, 0, 160, 160), null, true);
        canvas.translate(170, 0);
        canvas.drawImageRect(circusCropped, Rect.makeXYWH(0, 0, 320, 320), Rect.makeXYWH(0, 0, 160, 160), null, true);
        canvas.translate(170, 0);
        canvas.drawImageRect(cloud, Rect.makeXYWH(0, 0, 666, 456), Rect.makeXYWH(0, 0, 160, 110), null, true);
        canvas.drawImageRect(cloud, Rect.makeXYWH(0, 0, 666, 456), Rect.makeXYWH(0, 50, 160, 110), null, true);
        canvas.translate(170, 0);
        canvas.drawImageRect(ducks, Rect.makeXYWH(0, 0, 640, 640), Rect.makeXYWH(0, 0, 80, 160), null, true);
        canvas.translate(90, 0);
        canvas.drawImageRect(ducks, Rect.makeXYWH(0, 0, 640, 640), Rect.makeXYWH(0, 0, 160, 80), null, true);
        canvas.translate(170, 0);
        canvas.drawImageRect(ducks, Rect.makeXYWH(300, 300, 40, 40), Rect.makeXYWH(0, 0, 160, 160), null, false);
        canvas.translate(170, 0);
        canvas.drawImageRect(ducks, Rect.makeXYWH(300, 300, 40, 40), Rect.makeXYWH(0, 0, 160, 160), SamplingMode.MITCHELL, null, false);
        canvas.translate(170, 0);
        canvas.drawImageRect(ducks, Rect.makeXYWH(300, 300, 40, 40), Rect.makeXYWH(0, 0, 160, 160), SamplingMode.CATMULL_ROM, null, false);
        canvas.translate(170, 0);
        canvas.restore();
        canvas.translate(0, 170);

        canvas.save();
        try (Paint paint = new Paint().setBlendMode(BlendMode.SCREEN)) {
            canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 160, 160));
            canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 160, 160), paint);
        }
        canvas.translate(170, 0);
        try (Paint paint = new Paint().setBlendMode(BlendMode.OVERLAY)) {
            canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 160, 160));
            canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 160, 160), paint);
        }
        canvas.translate(170, 0);
        try (ImageFilter blur = ImageFilter.makeBlur(5, 5, FilterTileMode.DECAL);
             Paint paint = new Paint().setImageFilter(blur)) {
            canvas.drawImageRect(circus, Rect.makeXYWH(0, 0, 160, 160), paint);
        }
        canvas.translate(170, 0);
        canvas.restore();
        canvas.translate(0, 170);

        canvas.save();
        var maxH = 0;
        for (Image image: tests) {
            maxH = Math.max(maxH, image.getHeight());
            canvas.drawImage(image, 0, 0);
            canvas.translate(image.getWidth() + 10, 0);
        }
        canvas.restore();
        canvas.translate(0, maxH + 10);
    }
}