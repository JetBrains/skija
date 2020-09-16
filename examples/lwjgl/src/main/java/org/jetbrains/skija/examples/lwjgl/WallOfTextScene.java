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
    private String text;
    private Paint textColor;
    private TextBlob cachedBlob;
    private int cachedWidth;

    public WallOfTextScene() {
        var face = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        font = new Font(face, 13);
        try {
            text = Files.lines(Path.of("texts/google-10000-english.txt"))
                        .sorted()
                        .limit(2000)
                        .reduce("2000 words", (a, b) -> a + " " + b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        textColor = new Paint().setColor(0xFF783245);
    }
    
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {        
        var paddingH = 20f;
        var paddingV = 20f;

        if (cachedWidth != width || cachedBlob == null) {
            if (cachedBlob != null)
                cachedBlob.close();
            cachedBlob = font.shape(text, width - paddingH * 2);
            cachedWidth = width;
        }

        canvas.drawTextBlob(cachedBlob, 20f, 20f, font, textColor);
    }
}