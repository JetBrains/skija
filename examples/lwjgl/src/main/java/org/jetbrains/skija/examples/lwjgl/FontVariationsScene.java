package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import org.jetbrains.skija.*;

public class FontVariationsScene implements Scene {
    public final Typeface _interV;
    public final Map<FontVariation, Font> fontCache = new HashMap<>();

    public FontVariationsScene() {
        _interV = Typeface.makeFromFile("fonts/Inter-V.otf");
    }

    @Override
    public void draw(Canvas canvas, int windowWidth, int windowHeight, float dpi, int xpos, int ypos) {
        canvas.translate(24, 24);

        try (var fill = new Paint().setColor(0xFFCC3333);) {
            FontVariationAxis[] axes = _interV.getVariationAxes();
            for (var axis: axes) {
                canvas.save();
                for (int percent = 0; percent <= 100; percent += 5) {
                    var value = axis.getMinValue() + (axis.getMaxValue() - axis.getMinValue()) * percent / 100f;
                    var variation = new FontVariation(axis.getTag(), value);
                    
                    var font = fontCache.get(variation);
                    if (font == null) {
                        var face = _interV.makeClone(variation);
                        font = new Font(face, 13);
                        fontCache.put(variation, font);
                    }

                    fill.setColor(Color.makeLerp(0xFFf72585, 0xFF4361ee, percent / 100f));
                    canvas.drawString("Inter Variable 13px " + axis.getTag() + "=" + value, 0, 0, font, fill);
                    canvas.translate(0, 24);
                }
                canvas.restore();
                canvas.translate((windowWidth - 48) / axes.length, 0);
            }
        }
    }
}