package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;

public class FontSizeScene extends Scene {
    Paint grayStroke  = new Paint().setColor(0x80808080).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint redStroke   = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint greenStroke = new Paint().setColor(0x8033CC33).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint blueStroke  = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);

    Paint grayFill  = new Paint().setColor(0xFF808080);
    Paint redFill   = new Paint().setColor(0xFFCC3333);
    Paint greenFill = new Paint().setColor(0xFF33CC33);
    Paint blueFill  = new Paint().setColor(0xFF3333CC);

    Typeface[] faces;
    Font inter9 = new Font(inter, 9);

    public FontSizeScene() {
        var names = new String[] {
            "Inconsolata",
            "Anonymous Pro",
            "Hasklig",
            "Mono Lisa",
            "Hack",
            "PragmataPro Mono",

            "Anonymous Pro",
            "Bitstream Vera Sans Mono",
            "Cascadia Code",
            "Consolas",
            "Courier",
            "Courier New",
            "Fira Code",
            "Hack",
            "IBM Plex Mono",
            "Input Mono",
            "Iosevka",
            "JetBrains Mono",
            "Menlo",
            "Monaco",
            "Mono Lisa",
            "M+ 2m",
            "PragmataPro Mono",
            "PT Mono",
            "SF Mono",
            "Source Code Pro",
            "Ubuntu Mono",
            "Victor Mono"
        };

        faces = Arrays.stream(names)
                    .map(name -> {
                        var face = Typeface.makeFromName(name, FontStyle.NORMAL);
                        return name.equals(face.getFamilyName()) ? face : null;
                    })
                    .filter(face -> face != null)
                    .toArray(Typeface[]::new);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 100);

        // canvas.drawString("Font Size = 32", 0, -50, inter13, blackFill);

        var fonts = Arrays.stream(faces)
                        .map(face -> new Font(face, 32))
                        // .sorted((a, b) -> (int) Math.signum(b.getMetrics().getHeight() - a.getMetrics().getHeight()))
                        .toArray(Font[]::new);

        float x = width;
        canvas.translate(0, -150);
        for (var font: fonts) {
            if (x + 100 > width) {
                canvas.translate(0, 150);
                x = 60;
                canvas.drawString("Ascent",     0, 35, inter9, greenFill);
                canvas.drawString("Cap Height", 0, 50, inter9, redFill);
                canvas.drawString("x-Height",   0, 65, inter9, blueFill);
                canvas.drawString("Descent",    0, 80, inter9, greenFill);
                canvas.drawString("Height", 0, 95, inter9, grayFill);
            }

            try (var line = TextLine.make("Andy", font);) {
                canvas.drawLine(x - 10, 0, x + line.getWidth() + 10, 0, grayStroke);
                canvas.drawLine(x - 10, font.getMetrics().getAscent(), x + line.getWidth() + 10, font.getMetrics().getAscent(), greenStroke);
                canvas.drawLine(x - 10, -font.getMetrics().getCapHeight(), x + line.getWidth() + 10, -font.getMetrics().getCapHeight(), redStroke);
                canvas.drawLine(x - 10, -font.getMetrics().getXHeight(), x + line.getWidth() + 10, -font.getMetrics().getXHeight(), blueStroke);
                canvas.drawLine(x - 10, font.getMetrics().getDescent(), x + line.getWidth() + 10, font.getMetrics().getDescent(), greenStroke);
                
                canvas.drawTextLine(line, x, 0, blackFill);

                canvas.save();
                canvas.clipRect(Rect.makeXYWH(x, 0, line.getWidth() + 10, 25));
                canvas.drawString(font.getTypeface().getFamilyName(),    x, 20, inter9, blackFill);
                canvas.restore();

                canvas.drawString(String.format("%.2f", (-font.getMetrics().getAscent())), x, 35, inter9, greenFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getCapHeight()), x, 50, inter9, redFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getXHeight()),   x, 65, inter9, blueFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getDescent()),   x, 80, inter9, greenFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getHeight()),   x, 95, inter9, grayFill);

                x += line.getWidth() + 20;
            }
            font.close();
        }
        canvas.translate(0, 40);
    }
}