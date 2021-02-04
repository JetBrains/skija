package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class FontRenderingScene extends Scene {
    public Typeface _inter;
    public Typeface _interHinted;
    public Typeface _interV;
    public Paint    _paint;
    public float    _dpi = 0;

    public FontRenderingScene() {
        _inter       = Typeface.makeFromFile(file("fonts/Inter-Regular.otf"));
        _interV      = Typeface.makeFromFile(file("fonts/Inter-V.ttf"));
        _interHinted = inter;
        _paint       = new Paint().setColor(0xFF1d3557);
        _variants    = new String[] { "Identity", "Scaled" };
    }

    public float _drawLine(Canvas canvas, String text, Font font) {
        var blob = Shaper.make().shape(text, font);
        if (blob != null) {
            var bounds = blob.getBounds();
            canvas.drawTextBlob(blob, 0, 0, _paint);
            canvas.translate(0, bounds.getHeight());
            return bounds.getHeight();
        }
        return 0;
    }

    @Override
    public void draw(Canvas canvas, int windowWidth, int windowHeight, float dpi, int xpos, int ypos) {
        float scale = "Identity".equals(variantTitle()) ? dpi : 1f;
        canvas.translate(30 * scale, 30 * scale);
        drawModes(canvas, scale);
    }

    @Override
    public boolean scale() {
        return "Scaled".equals(variantTitle());
    }

    public void drawModes(Canvas canvas, float scale) {
        String common = "1006 Component Fix Position Scrolling ";

        for (var typeface: Pair.arrayOf("", _inter,
                                        "Hinted ", _interHinted,
                                        "Variable ", _interV))
        {
            for (var subpixel: Pair.arrayOf("", false, "Subpixel ", true)) {
                for (var linear: Pair.arrayOf("", false, "LinearMetrics ", true)) {
                    for (var hinting: Pair.arrayOf("Hinting=NONE ",   FontHinting.NONE,
                                                   "Hinting=SLIGHT ", FontHinting.SLIGHT,
                                                   "Hinting=NORMAL ", FontHinting.NORMAL,
                                                   "Hinting=FULL ",   FontHinting.FULL)) {
                        try (var font = new Font(typeface.getSecond(), 11 * scale)) {
                            font.setSubpixel(subpixel.getSecond());
                            font.setMetricsLinear(linear.getSecond());
                            if (hinting.getSecond() != null)
                                font.setHinting(hinting.getSecond());
                            _drawLine(canvas, common + "Inter " + (11 * scale) + " " + typeface.getFirst() + subpixel.getFirst() + linear.getFirst() + hinting.getFirst(), font);
                        }
                    }
                }
            }
            canvas.translate(0, 20 * scale);
        }
    }
}