package org.jetbrains.skija.examples.lwjgl;

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
    private int[] colors;
    private FontCollection fc = null;

    @SneakyThrows
    public WallOfTextScene() {
        words = Files.lines(java.nio.file.Path.of("texts/google-10000-english.txt"))
                    .sorted()
                    .limit(1000)
                    .toArray(String[]::new);
        text = String.join(" ", words);
        _variants = new String[] {
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
        _variantIdx = 8;
        colors = new int[] {
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
            // fc.getParagraphCache().reset();
            fc.getParagraphCache().printStatistics();
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
                shaper.shape(text, font, FontMgr.getDefault(), null, true, textWidth, handler);
                return handler.makeBlob();
            }
        } else
            return shaper.shape(text, font, textWidth);
    }

    void drawByWords(Shaper shaper, Canvas canvas, float padding, float textWidth) {
        var x = padding;
        var y = padding;
        for (int i = 0; i < words.length; ++i) {
            try (var blob = makeBlob(words[i], shaper, Float.POSITIVE_INFINITY);) {
                var bounds = blob.getBounds();
                var wordWidth = bounds.getRight();
                if (x + wordWidth > textWidth) {
                    x = padding;
                    y += bounds.getHeight();
                }
                canvas.drawTextBlob(blob, x, y, font, fill);
                x += wordWidth;
            }
        }
    }

    public void drawTogether(Shaper shaper, Canvas canvas, float padding, float textWidth) {
        try (var blob = makeBlob(text, shaper, textWidth);) {
            canvas.drawTextBlob(blob, padding, padding, font, fill);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {        
        var padding = 20f * dpi;
        var textWidth = width * dpi - padding * 2;
        var fontSize = 14.5f * dpi;
        var variant = _variants[_variantIdx];

        if (variant.startsWith("Paragraph"))
            drawParagraph(canvas, fontSize, padding, textWidth);
        else {
            if (font == null)
                font = new Font(inter, fontSize).setSubpixel(true);
            fill.setColor(colors[_variantIdx]);

            Shaper shaper = null;
            if ("Primitive".equals(variant))
                shaper = Shaper.makePrimitive();
            else if ("ShaperDrivenWrapper".equals(variant))
                shaper = Shaper.makeShaperDrivenWrapper();
            else if ("ShapeDontWrapOrReorder".equals(variant))
                shaper = Shaper.makeShapeDontWrapOrReorder();
            else if ("CoreText".equals(variant))
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