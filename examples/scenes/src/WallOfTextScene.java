package org.jetbrains.skija.examples.scenes;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;
import lombok.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;
import org.jetbrains.skija.paragraph.*;

public class WallOfTextScene extends Scene {
    private Font font = null;
    private String[] words;
    private String text;
    private Paint fill = new Paint();
    private Paint boundsStroke = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private int[] colors;
    private FontCollection fc = null;

    @SneakyThrows
    public WallOfTextScene() {
        words = Files.lines(java.nio.file.Path.of(file("texts/google-10000-english.txt")))
                    .sorted()
                    .limit(1000)
                    .toArray(String[]::new);
        text = String.join(" ", words);
        _variants = new String[] {
            "TextLine by words",
            "ShapeThenWrap",
            "ShapeThenWrap by words",
            "JVM RunHandler",
            "JVM RunHandler by words",
            "ShaperDrivenWrapper",
            "Primitive",
            "CoreText",
            "ShapeDontWrapOrReorder",
            "Paragraph with cache",
            "Paragraph no cache"
        };
        _variantIdx = 0;
        colors = new int[] {
            0xFF000000,
            0xFFf94144,
            0xFFf3722c,
            0xFFf8961e,
            0xFFf9844a,
            0xFFf9c74f,
            0xFF90be6d,
            0xFF43aa8b,
            0xFF4d908e,
            0xFF577590,
            0xFF277da1
        };
    }
    
    void drawParagraph(Canvas canvas, float fontSize, float padding, float textWidth) {
        if (fc == null) {
            fc = new FontCollection();
            fc.setDefaultFontManager(FontMgr.getDefault());
            var fm = new TypefaceFontProvider();
            fm.registerTypeface(inter, "Inter");
            fc.setAssetFontManager(fm);
        }

        if (_variants[_variantIdx].endsWith("no cache")) {
            fc.getParagraphCache().reset();
            // fc.getParagraphCache().printStatistics();
        }

        try (var ts = new TextStyle()
                          .setFontFamilies(new String[] { "Inter" })
                          .setFontSize(fontSize)
                          .setColor(colors[_variantIdx]);
             var ps = new ParagraphStyle();
             var pb = new ParagraphBuilder(ps, fc);)
        {
            for (int i = 0; i < words.length; ++i) {
                ts.setColor(colors[i % colors.length]);
                pb.pushStyle(ts);
                pb.addText(words[i] + " ");
                pb.popStyle();
            }

            try (Paragraph p = pb.build();) {
                p.layout(textWidth);
                p.paint(canvas, padding, padding);
            }
        }
    }

    TextBlob makeBlob(String text, Shaper shaper, float textWidth) {
        if (_variants[_variantIdx].startsWith("JVM RunHandler")) {
            try (var handler = new DebugTextBlobHandler();) {
                shaper.shape(text, font, ShapingOptions.DEFAULT, textWidth, handler);
                return handler.makeBlob();
            }
        } else
            return shaper.shape(text, font, textWidth);
    }

    void drawByWords(Shaper shaper, Canvas canvas, float padding, float textWidth) {
        var x = padding;
        var y = padding;
        var space = font.measureTextWidth(" ");
        var lineHeight = font.getMetrics().getHeight();
        for (int i = 0; i < words.length; ++i) {
            try (var blob = makeBlob(words[i], shaper, Float.POSITIVE_INFINITY);) {
                var bounds = blob.getTightBounds();
                var wordWidth = bounds.getWidth();
                if (x + wordWidth > textWidth) {
                    x = padding;
                    y += lineHeight;
                }
                fill.setColor(colors[i % colors.length]);
                canvas.drawRect(bounds.offset(x, y), boundsStroke);
                canvas.drawTextBlob(blob, x, y, fill);
                x += wordWidth + space;
            }
        }
    }

    public void drawTextLinesByWords(Canvas canvas, float padding, float textWidth) {
        var x = padding;
        var y = padding;
        var space = font.measureTextWidth(" ");
        var lineHeight = font.getMetrics().getHeight();
        for (int i = 0; i < words.length; ++i) {
            try (var line = TextLine.make(words[i], font);) {
                var wordWidth = line.getWidth();
                if (x + wordWidth > textWidth) {
                    x = padding;
                    y += lineHeight;
                }
                fill.setColor(colors[i % colors.length]);
                canvas.drawRect(Rect.makeXYWH(x, y, line.getWidth(), line.getHeight()), boundsStroke);
                canvas.drawTextLine(line, x, y - line.getAscent(), fill);
                x += wordWidth + space;
            }
        }
    }

    public void drawTogether(Shaper shaper, Canvas canvas, float padding, float textWidth) {
        try (var blob = makeBlob(text, shaper, textWidth);) {
            canvas.drawTextBlob(blob, padding, padding, fill);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {        
        var padding = 20f * dpi;
        var textWidth = width * dpi - padding * 2;
        var fontSize = 14.5f * dpi;
        var variant = _variants[_variantIdx];

        if (font == null)
            font = new Font(inter, fontSize).setSubpixel(true);

        fill.setColor(colors[_variantIdx]);

        if (variant.startsWith("Paragraph"))
            drawParagraph(canvas, fontSize, padding, textWidth);
        else if (variant.startsWith("TextLine"))
            drawTextLinesByWords(canvas, padding, textWidth);
        else {
            Shaper shaper = null;
            if (variant.startsWith("Primitive"))
                shaper = Shaper.makePrimitive();
            else if (variant.startsWith("ShaperDrivenWrapper"))
                shaper = Shaper.makeShaperDrivenWrapper();
            else if (variant.startsWith("ShapeDontWrapOrReorder"))
                shaper = Shaper.makeShapeDontWrapOrReorder();
            else if (variant.startsWith("CoreText"))
                shaper = "Mac OS X".equals(System.getProperty("os.name")) ? Shaper.makeCoreText() : null;
            else
                shaper = Shaper.makeShapeThenWrap();

            if (shaper != null) {
                if (variant.endsWith("by words"))
                    drawByWords(shaper, canvas, padding, textWidth);
                else
                    drawTogether(shaper, canvas, padding, textWidth);
                shaper.close();
            }
        }
    }

    @Override
    public boolean scale() {
        return false;
    }
}