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

    public TextShapeBenchScene() {
        _variants = new String[] {
            "Tabs Paragraph", "Tabs TextLine",
            "Emoji Paragraph", "Emoji TextLine",
            "Greek Paragraph", "Greek TextLine", 
            "Notdef Paragraph", "Notdef TextLine"
        };
    
        font = new Font(jbMono, 10).setSubpixel(true);
        metrics = font.getMetrics();

        fc = new FontCollection();
        fc.setDefaultFontManager(FontMgr.getDefault());        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        fm.registerTypeface(jbMono);
        fc.setAssetFontManager(fm);
        fc.getParagraphCache().setEnabled(false);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var variant = _variants[_variantIdx].split(" ");
        String text = variant[0];
        if ("Tabs".equals(variant[0]))
            text = "										";
        else if ("Emoji".equals(variant[0]))
            text = "😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺️";
        else if ("Greek".equals(variant[0]))
            text = "ἔοικα γοῦν τούτου γε σμικρῷ τινι αὐτῷ τούτῳ σοφώτερος εἶναι, ὅτι ἃ μὴ οἶδα οὐδὲ οἴομαι εἰδέναι";
        else if ("Notdef".equals(variant[0]))
            text = "\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0\u20C0";

        if ("Paragraph".equals(variant[1])) {
            fc.getParagraphCache().reset();
            for (int y = 20; y < height - 20; y += 20) {
                try (TextStyle ts = new TextStyle().setColor(0xFF000000).setFontFamilies(new String [] {"JetBrains Mono"}).setFontSize(10);
                     ParagraphStyle ps = new ParagraphStyle();
                     ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
                {
                    pb.pushStyle(ts);
                    pb.addText(y + " [" + text + "]");
                    try (Paragraph p = pb.build();) {
                        p.layout(Float.POSITIVE_INFINITY);
                        p.paint(canvas, 20, y);
                    }
                }
            }
        } else {
            try (var shaper = Shaper.makeShapeDontWrapOrReorder();) {
                for (int y = 20; y < height - 20; y += 20) {
                    try (var line = shaper.shapeLine(y + " [" + text + "]", font, null, true);) {
                        canvas.drawTextLine(line, 20, y - metrics.getAscent(), blackFill);
                    }
                }
            }
        }
    }
}