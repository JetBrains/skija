package org.jetbrains.skija.examples.scenes;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;
import org.jetbrains.skija.paragraph.*;

public class TextShapeBenchScene extends Scene {
    public final Font font;
    public final FontMetrics metrics;
    public final FontCollection fc;
    public final Paint redStroke = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public float fontSize = 20;
    public float padding = fontSize * 2;

    public TextShapeBenchScene() {
        _variants = new String[] {
            "Tabs Paragraph",    "Tabs Paragraph No-Cache",    "Tabs TextLine",
            "Emoji Paragraph",   "Emoji Paragraph No-Cache",   "Emoji TextLine",
            "Greek Paragraph",   "Greek Paragraph No-Cache",   "Greek TextLine", "Greek TextLine No-Approx", 
            "Notdef Paragraph",  "Notdef Paragraph No-Cache",  "Notdef TextLine",
            "English Paragraph", "English Paragraph No-Cache", "English TextLine",
        };
        _variantIdx = 9;
    
        font = new Font(jbMono, fontSize).setSubpixel(true);
        metrics = font.getMetrics();

        fc = new FontCollection();
        fc.setDefaultFontManager(FontMgr.getDefault());        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        fm.registerTypeface(jbMono);
        fc.setAssetFontManager(fm);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var variant = _variants[_variantIdx].split(" ");
        String text = variant[0];
        if ("Tabs".equals(variant[0]))
            text = "										";
        else if ("Emoji".equals(variant[0]))
            text = "😀 😃 😄 😁 😆 😅 😂 ☺️ 😇 😍";
        else if ("Greek".equals(variant[0]))
            text = "ἔοικα γοῦν τούτου γε σμικρῷ τινι αὐτῷ τούτῳ σοφώτερος εἶναι, ὅτι ἃ μὴ οἶδα οὐδὲ οἴομαι εἰδέναι";
        else if ("Notdef".equals(variant[0]))
            text = "\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0";
        else if ("English".equals(variant[0]))
            text = "In girum imus nocte et consumimur igni";

        if ("Paragraph".equals(variant[1])) {
            if (variant.length > 2) // No-Cache
                fc.getParagraphCache().reset();
            for (int i = 1; true; ++i) {
                float y = i * padding;
                if (y > height - padding) break;
                try (TextStyle ts = new TextStyle().setColor(0xFF000000).setFontFamilies(new String [] {"JetBrains Mono"}).setFontSize(fontSize);
                     ParagraphStyle ps = new ParagraphStyle();
                     ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
                {
                    pb.pushStyle(ts);
                    pb.addText(i + " [" + text + "]");
                    try (Paragraph p = pb.build();) {
                        p.layout(Float.POSITIVE_INFINITY);
                        p.paint(canvas, padding, y);
                    }
                }
            }
        } else { // TextLine
            try (var shaper = Shaper.makeShapeDontWrapOrReorder();) {
                for (int i = 1; true; ++i) {
                    float y = i * padding;
                    if (y > height - padding) break;
                    try (var line = variant.length > 2 // No-Approx
                                    ? shaper.shapeLine(i + " [" + text + "]", font, ShapingOptions.DEFAULT.withApproximateSpaces(false).withApproximatePunctuation(false))
                                    : shaper.shapeLine(i + " [" + text + "]", font))
                    {
                        canvas.drawTextLine(line, padding, y - metrics.getAscent(), blackFill);
                        canvas.drawRect(Rect.makeXYWH(padding, y, line.getWidth(), metrics.getHeight()), redStroke);
                        for (float x: TextLine._nGetRunPositions(line._ptr))
                            canvas.drawLine(padding + x, y, padding + x, y + metrics.getHeight(), redStroke);
                    }
                }
            }
        }
    }
}