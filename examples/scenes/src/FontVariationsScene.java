package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;

public class FontVariationsScene extends Scene {
    public final Typeface _interV;
    public final Map<FontVariation, Font> fontCache = new HashMap<>();

    public FontVariationsScene() {
        _interV = Typeface.makeFromFile(file("fonts/Inter-V.ttf"));
    }

    public void drawLine(Canvas canvas, Font font, int color) {
        try (var fill = new Paint().setColor(color);
             var face = font.getTypeface();)
        {
            FontVariation[] variations = face.getVariations();
            String text = "Inter Variable 16px";
            for (var v: variations) {
                text += " " + v.getTag() + "=" + v.getValue();
            }
            canvas.drawString(text, 0, 0, font, fill);
            canvas.translate(0, 28);
        }
    }

    @Override
    public void draw(Canvas canvas, int windowWidth, int windowHeight, float dpi, int xpos, int ypos) {
        canvas.translate(24, 24);

        try (var fill = new Paint().setColor(0xFFCC3333);) {
            FontVariationAxis[] axes = _interV.getVariationAxes();

            canvas.save();
            var font = fontCache.computeIfAbsent(null, v -> new Font(_interV, 16));
            drawLine(canvas, font, 0xFF000000);
            canvas.restore();
            canvas.translate((windowWidth - 48) / (axes.length + 1), 0);

            for (var axis: axes) {
                canvas.save();
                for (int percent = 0; percent <= 100; percent += 5) {
                    var value = axis.getMinValue() + (axis.getMaxValue() - axis.getMinValue()) * percent / 100f;
                    var variation = new FontVariation(axis.getTag(), value);
                    
                    font = fontCache.computeIfAbsent(variation, v -> new Font(_interV.makeClone(v), 16));
                    drawLine(canvas, font, Color.makeLerp(0xFFf72585, 0xFF4361ee, percent / 100f));
                }
                canvas.restore();
                canvas.translate((windowWidth - 48) / (axes.length + 1), 0);
            }
        }
    }
}