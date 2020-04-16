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

    public WallOfTextScene(boolean useCache) {
        var face = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        font = new Font(face, 13);
        try {
            words = Files.readAllLines(Path.of("google-10000-english.txt"));
            Collections.sort(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (useCache)
            cache = new HashMap<>();
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        FontExtents extents = font.mHBFont.getHorizontalExtents();
        try (Paint paint = new Paint().setColor(0xFF000000)) {
            var paddingH = 20f;
            var paddingV = 20f;
            float wordGap;
            if (cache == null)
                try (TextBuffer buffer = font.mHBFont.shape(" ")) { wordGap = buffer.getAdvances()[0]; }
            else
                wordGap = cache.computeIfAbsent(" ", w -> font.mHBFont.shape(w)).getAdvances()[0];
            var lineHeight = -extents.ascender + extents.descender;
            var lineGap = extents.lineGap + 0;
            var x = paddingH;
            var y = paddingV;

            for (var word : words) {
                TextBuffer buffer = cache != null ? cache.computeIfAbsent(word, w -> font.mHBFont.shape(w)) : font.mHBFont.shape(word);
                try {
                    float[] advances = buffer.getAdvances();
                    if (x + advances[0] > width - paddingH) {
                        x = paddingH;
                        y = y + lineHeight + lineGap;
                    }

                    if (y + lineHeight > height - paddingV)
                        break;

                    canvas.drawTextBuffer(buffer, x, y - extents.ascender, font.mSkFont, paint);
                    x = x + advances[0] + wordGap;
                } finally {
                    if (cache == null)
                        buffer.close();
                }
            }
        }
    }
}