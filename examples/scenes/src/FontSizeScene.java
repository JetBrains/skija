package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;

public class FontSizeScene extends Scene {
    Paint grayStroke  = new Paint().setColor(0x40808080).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint redStroke   = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint greenStroke = new Paint().setColor(0x8033CC33).setMode(PaintMode.STROKE).setStrokeWidth(1);
    Paint blueStroke  = new Paint().setColor(0x403333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);

    Paint grayFill  = new Paint().setColor(0xFF808080);
    Paint redFill   = new Paint().setColor(0xFFCC3333);
    Paint greenFill = new Paint().setColor(0xFF33CC33);
    Paint blueFill  = new Paint().setColor(0xFF3333CC);

    Typeface[] faces;
    Font inter9 = new Font(inter, 9);

    public FontSizeScene() {
        _variants = new String[] {
            "Font Size",
            "Cap Height",
            "x-Height"
        };
        var names = new String[] {
            "Anonymous Pro",
            "Bitstream Vera Sans Mono",
            "Cascadia Code",
            "Consolas",
            "Courier",
            "Courier New",
            "Fantasque Sans Mono",
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

    public float capHeight(Font font) {
        if ("Ubuntu Mono".equals(font.getTypeface().getFamilyName()))
            return font.getSize() * 0.619f;
        else
            return font.getMetrics().getCapHeight();
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(0, 50);

        // canvas.drawString("Font Size = 32", 0, -50, inter13, blackFill);

        var fonts = Arrays.stream(faces)
                        .map(face -> new Font(face, 32).setSubpixel(true))
                        // .sorted((a, b) -> (int) Math.signum(b.getMetrics().getHeight() - a.getMetrics().getHeight()))
                        .toArray(Font[]::new);

        float x = width;
        canvas.translate(0, -150);
        for (var font: fonts) {
            if ("Cap Height".equals(_variants[_variantIdx])) {
                var capHeight = capHeight(font);
                var size = 32f / (capHeight / 22f);
                font.setSize(size);
            } else if ("x-Height".equals(_variants[_variantIdx])) {
                var xHeight = font.getMetrics().getXHeight();
                var size = 32f / (xHeight / 16f);
                font.setSize(size);
            }

            if (x + 100 > width) {
                canvas.translate(0, 170);
                x = 85;
                var y = 10;
                canvas.drawString("Family",    25, y += 15, inter9, grayFill);
                canvas.drawString("Size, pt",  25, y += 15, inter9, grayFill);
                canvas.drawString("Ascent",    25, y += 15, inter9, greenFill);
                canvas.drawString("Cap, px",   25, y += 15, inter9, redFill);
                canvas.drawString("x-Height",  25, y += 15, inter9, blueFill);
                canvas.drawString("Descent",   25, y += 15, inter9, greenFill);
                canvas.drawString("Height",    25, y += 15, inter9, grayFill);
                canvas.drawString("Width",     25, y += 15, inter9, grayFill);
            }

            try (var line = TextLine.make("Andy", font);) {
                canvas.drawLine(x - 10, font.getMetrics().getAscent(), x + 90, font.getMetrics().getAscent(), greenStroke);
                canvas.drawLine(x - 10, -capHeight(font), x + 90, -capHeight(font), redStroke);
                canvas.drawLine(x - 10, -font.getMetrics().getXHeight(), x + 90, -font.getMetrics().getXHeight(), blueStroke);
                canvas.drawLine(x - 10, 0, x + 90, 0, grayStroke);
                canvas.drawLine(x - 10, font.getMetrics().getDescent(), x + 90, font.getMetrics().getDescent(), greenStroke);
                
                canvas.drawTextLine(line, x, 0, blackFill);

                var y = 10;
                canvas.save();
                canvas.clipRect(Rect.makeXYWH(x, 0, 90, 25));
                canvas.drawString(font.getTypeface().getFamilyName(),    x, y += 15, inter9, grayFill);
                canvas.restore();


                canvas.drawString(String.format("%.2f", font.getSize()), x, y += 15, inter9, grayFill);
                canvas.drawString(String.format("%.2f", (-font.getMetrics().getAscent())), x, y += 15, inter9, greenFill);
                canvas.drawString(String.format("%.2f", capHeight(font)), x, y += 15, inter9, redFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getXHeight()),   x, y += 15, inter9, blueFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getDescent()),   x, y += 15, inter9, greenFill);
                canvas.drawString(String.format("%.2f", font.getMetrics().getHeight()),   x, y += 15, inter9, grayFill);
                try (var ch = TextLine.make("x", font)) {
                    canvas.drawString(String.format("%.2f", ch.getWidth()),   x, y += 15, inter9, grayFill);
                }

                x += 100;
            }
            font.close();
        }
        canvas.translate(0, 40);
    }
}