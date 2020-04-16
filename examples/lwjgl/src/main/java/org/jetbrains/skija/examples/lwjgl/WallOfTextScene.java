package org.jetbrains.skija.examples.lwjgl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.skija.*;

public class WallOfTextScene implements Scene {
    private Font font;
    private List<String> words;
    private Map<String, TextBuffer> cache;
    private Paint textColor;
    private Paint highlightColor;

    public WallOfTextScene(boolean useCache) {
        var face = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        font = new Font(face, 13);
        try {
            words = Files.readAllLines(Path.of("google-10000-english.txt"));
            Collections.sort(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (useCache) {
            cache = new HashMap<>();
            textColor = new Paint().setColor(0xFF487D58);
            highlightColor = new Paint().setColor(0xFFEEA002);
        } else {
            textColor = new Paint().setColor(0xFF454A6F);
            highlightColor = new Paint().setColor(0xFFE7582A); 
        }
    }
    
    private TextBuffer getTextBuffer(String s) {
        if (cache == null) 
            return font.hbFont.shape(s);
        else
            return cache.computeIfAbsent(s, w -> font.hbFont.shape(w));
    }
    
    private void releaseTextBuffer(TextBuffer b) {
        if (cache == null)
            b.close();
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        FontExtents extents = font.hbFont.getHorizontalExtents();
        
        var paddingH = 20f;
        var paddingV = 20f;
        TextBuffer b = getTextBuffer(" ");
        var wordGap = b.getAdvances()[0];
        releaseTextBuffer(b);
        var lineHeight = -extents.ascender + extents.descender;
        var lineGap = extents.lineGap + 0;
        TextBuffer countBuffer = getTextBuffer("8888 words");
        var x = paddingH + countBuffer.getAdvances()[0] + wordGap;
        var y = paddingV;
        releaseTextBuffer(countBuffer);
        
        var count = 0;

        for (var word: words) {
            TextBuffer buffer = getTextBuffer(word);
            try {
                float[] advances = buffer.getAdvances();
                if (x + advances[0] > width - paddingH) {
                    x = paddingH;
                    y = y + lineHeight + lineGap;
                }

                if (y + lineHeight > height - paddingV)
                    break;

                canvas.drawTextBuffer(buffer, x, y - extents.ascender, font.skFont, textColor);
                x = x + advances[0] + wordGap;
            } finally {
                releaseTextBuffer(buffer);
            }
            ++count;
        }
        
        try (var buf = font.hbFont.shape(count + " words")) {
            canvas.drawTextBuffer(buf, paddingH, paddingV - extents.ascender, font.skFont, highlightColor);
        }
    }
}