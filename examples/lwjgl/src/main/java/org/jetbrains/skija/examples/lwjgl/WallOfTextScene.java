package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class WallOfTextScene extends Scene {
    private Font font = null;
    private String[] words;
    private String text;
    private Paint fill = new Paint();
    private int[] colors;

    @SneakyThrows
    public WallOfTextScene() {
        words = Files.lines(java.nio.file.Path.of("texts/google-10000-english.txt"))
                    .sorted()
                    .limit(1000)
                    .toArray(String[]::new);
        text = String.join(" ", words);
        _variants = new String[] {
            "ShapeThenWrap",
            "ShapeThenWrap by word",
            "Java Handler",
            "Java Handler by word",
            "ShaperDrivenWrapper",
            "Primitive",
            "CoreText",
            "ShapeDontWrapOrReorder",
        };
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
    
    TextBlob makeBlob(String text, Shaper shaper, float textWidth) {
        if (_variants[_variantIdx].startsWith("Java Handler")) {
            try (var handler = new DebugTextBlobHandler();) {
                shaper.shape(text, font, FontMgr.getDefault(), null, true, textWidth, handler);
                return handler.makeBlob();
            }
        } else
            return shaper.shape(text, font, textWidth);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {        
        var padding = 20f * dpi;
        var textWidth = width * dpi - padding * 2;
        Shaper shaper = null;
        var variant = _variants[_variantIdx];
        if (font == null)
            font = new Font(inter, 14.5f * dpi).setSubpixel(true);
        fill.setColor(colors[_variantIdx]);

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
            if (variant.endsWith("by word")) {
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
            } else {
                try (var blob = makeBlob(text, shaper, textWidth);) {
                    canvas.drawTextBlob(blob, padding, padding, font, fill);
                }
            }
            shaper.close();
        }
    }

    @Override
    public boolean scale() {
        return false;
    }
}