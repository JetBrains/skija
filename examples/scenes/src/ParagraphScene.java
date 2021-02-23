package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;
import org.jetbrains.skija.paragraph.*;

public class ParagraphScene extends Scene {
    public FontCollection fc = new FontCollection();
    public long lastUpdate = 0;
    
    public ParagraphScene() {
        fc.setDefaultFontManager(FontMgr.getDefault());
        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        fm.registerTypeface(jbMono);
        fm.registerTypeface(inter, "Interface");
        fc.setAssetFontManager(fm);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (System.currentTimeMillis() - lastUpdate > 1000) {
            ParagraphCache cache = fc.getParagraphCache();
            // if ((System.currentTimeMillis() / 1000) % 2 == 0) {
            //     System.out.println("ParagrapCache.enabled = true");
            //     cache.reset();
            //     cache.setEnabled(true);
            // } else {
            //     System.out.println("ParagrapCache.enabled = false");
            //     // cache.abandon();
            //     cache.setEnabled(false);
            // }
            // cache.printStatistics();
            lastUpdate = System.currentTimeMillis();
        }

        canvas.translate(30, 30);

        try (TextStyle defaultTs = new TextStyle().setColor(0xFF000000);
             ParagraphStyle ps = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);
             Paint boundaries = new Paint().setColor(0xFFFAA6B2).setMode(PaintMode.STROKE).setStrokeWidth(1f);
             Paint placeholders = new Paint().setColor(0xFFFAA6B2);)
        {
            // default style
            pb.pushStyle(defaultTs);

            // single style
            try (TextStyle ts = new TextStyle().setColor(0xFF2a9d8f);) {
                pb.pushStyle(ts);
                pb.addText("Shall I compare thee to a summer’s day?\n");
                pb.popStyle();
            }

            // mixed colors
            try (TextStyle ts = new TextStyle().setColor(0xFF2a9d8f);) {
                pb.addText("Thou art");
                pb.pushStyle(ts);
                pb.addText(" more lovely and");
                pb.popStyle();
                pb.addText(" more temperate:\n");
            }

            // mixing font sizes
            try (TextStyle ts = new TextStyle().setColor(0xFF000000).setFontSize(18);
                 TextStyle ts2 = new TextStyle().setColor(0xFF000000).setFontSize(9);) {
                pb.addText("Rough winds");            
                pb.pushStyle(ts);
                pb.addText(" do shake");
                pb.popStyle().pushStyle(ts2);
                pb.addText(" the darling buds");
                pb.popStyle();
                pb.addText(" of May,\n");
            }

            // pushing twice
            try (TextStyle ts  = new TextStyle().setColor(0xFF000000).setFontSize(18);
                 TextStyle ts2 = new TextStyle().setColor(0xFF2a9d8f);) {
                pb.addText("And summer’s");
                pb.pushStyle(ts);
                pb.addText(" lease hath");
                pb.pushStyle(ts2);
                pb.addText(" all");
                pb.popStyle();
                pb.addText(" too short");
                pb.popStyle();
                pb.addText(" a date:\n");
            }

            // cyrillic
            pb.addText("То нам слепит глаза небесный глаз,\n"); // + "Sometime too hot the eye of heaven shines,\n"

            // mixing fonts
            try (TextStyle ts  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "Verdana" }); 
                 TextStyle ts2 = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "Georgia" });
                 TextStyle ts3 = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "Courier New" });) {
                pb.pushStyle(ts);
                pb.addText("And often");
                pb.popStyle().pushStyle(ts2);
                pb.addText(" is his gold");
                pb.popStyle().pushStyle(ts3);
                pb.addText(" complexion dimm’d;\n");
                pb.popStyle();
            }

            // emojis
            try (TextStyle ts  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "System Font", "Apple Color Emoji" });) {
                pb.pushStyle(ts);
                pb.addText("And every 🧑🏿‍🦰 fair 🦾 from\nfair 🥱 sometime 🧑🏾‍⚕️ declines 👩‍👩‍👧‍👧,\n");
                pb.popStyle();
            }

            // fonts
            try (TextStyle ts  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "JetBrains Mono" });
                 TextStyle ts2  = new TextStyle().setColor(0xFF2a9d8f).setFontFamilies(new String[] { "Interface" });
                 TextStyle ts3  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "Inter" });) {
                pb.pushStyle(ts);
                pb.addText("By chance ");
                pb.popStyle().pushStyle(ts2);
                pb.addText("or nature’s changing course ");
                pb.popStyle().pushStyle(ts3);
                pb.addText("untrimm’d\n");
                pb.popStyle();
            }

            // placeholders
            // pb.setParagraphStyle(ps);
            pb.addText("But thy");
            pb.addPlaceholder(new PlaceholderStyle(20, 40, PlaceholderAlignment.BASELINE, BaselineMode.ALPHABETIC, 0));
            pb.addText("eternal");
            pb.addPlaceholder(new PlaceholderStyle(20, 40, PlaceholderAlignment.ABOVE_BASELINE, BaselineMode.ALPHABETIC, 0));
            pb.addText("summer");
            pb.addPlaceholder(new PlaceholderStyle(20, 40, PlaceholderAlignment.BELOW_BASELINE, BaselineMode.IDEOGRAPHIC, 0));
            pb.addText("shall");
            pb.addPlaceholder(new PlaceholderStyle(40, 20, PlaceholderAlignment.TOP, BaselineMode.ALPHABETIC, 0));
            pb.addText("not");
            pb.addPlaceholder(new PlaceholderStyle(20, 40, PlaceholderAlignment.MIDDLE, BaselineMode.ALPHABETIC, 0));
            pb.addText("fade,\n");

            // Nor lose possession of that fair thou ow’st;
            // Nor shall death brag thou wander’st in his shade,
            // When in eternal lines to time thou grow’st:
            //    So long as men can breathe or eyes can see,
            //    So long lives this, and this gives life to thee.

            try (Paragraph p = pb.build();) {
                p.layout(Float.POSITIVE_INFINITY);
                float minW = p.getMinIntrinsicWidth();
                float maxW = p.getMaxIntrinsicWidth();
                float range = maxW - minW;
                for (float w = maxW; w >= minW; w -= range / 5) {
                    p.layout((float) Math.ceil(w));
                    p.paint(canvas, 0, 0);
                    float h = p.getHeight();
                    canvas.drawRect(Rect.makeXYWH(0, 0, minW, h), boundaries);
                    canvas.drawRect(Rect.makeXYWH(0, 0, w,    h), boundaries);
                    // canvas.drawRect(Rect.makeXYWH(0, 0, maxW, h), boundaries);

                    for (TextBox placeholder: p.getRectsForPlaceholders()) {
                        canvas.drawRect(placeholder.getRect(), placeholders);
                    }

                    canvas.translate(w + 15, 0);
                }
            }
        }
   }
}