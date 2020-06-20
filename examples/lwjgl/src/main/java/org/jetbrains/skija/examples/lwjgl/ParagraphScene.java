package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import org.jetbrains.skija.*;
import org.jetbrains.skija.paragraph.*;

public class ParagraphScene implements Scene {
    public FontCollection fc = new FontCollection();
    
    public ParagraphScene() {
        fc.setDefaultFontManager(FontMgr.getDefault());
        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        Typeface jbMono = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        fm.registerTypeface(jbMono);
        Typeface inter = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        fm.registerTypeface(inter, "Interface");
        fc.setAssetFontManager(fm);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        drawSonnet(canvas);
        canvas.translate(0, 300);
        drawMetrics(canvas, xpos - 30f, ypos - 330f);
    }

    public void drawSonnet(Canvas canvas) {
        canvas.save();

        try (TextStyle defaultTs = new TextStyle().setColor(0xFF000000);
             ParagraphStyle ps = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);
             Paint boundaries = new Paint().setColor(0xFFFAA6B2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);)
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

            // emojis
            try (TextStyle ts  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "JetBrains Mono" });
                 TextStyle ts2  = new TextStyle().setColor(0xFF2a9d8f).setFontFamilies(new String[] { "Interface" });
                 TextStyle ts3  = new TextStyle().setColor(0xFF000000).setFontFamilies(new String[] { "Inter" });) {
                pb.pushStyle(ts);
                pb.addText("Shall I compare ");
                pb.popStyle().pushStyle(ts2);
                pb.addText("thee to a ");
                pb.popStyle().pushStyle(ts3);
                pb.addText("summer’s day?\n");
                pb.popStyle();
            }

            try (Paragraph p = pb.build();) {
                p.layout(Float.POSITIVE_INFINITY);
                float minW = p.getMinIntrinsicWidth();
                float maxW = p.getMaxIntrinsicWidth();
                float range = maxW - minW;
                for (float w = maxW; w >= minW; w -= range / 5) {
                    p.layout(w);
                    p.paint(canvas, 0, 0);
                    float h = p.getHeight();
                    canvas.drawRect(Rect.makeXYWH(0, 0, minW, h), boundaries);
                    canvas.drawRect(Rect.makeXYWH(0, 0, w,    h), boundaries);
                    // canvas.drawRect(Rect.makeXYWH(0, 0, maxW, h), boundaries);
                    canvas.translate(w + 15, 0);
                }
            }
        }

        canvas.restore();
    }

    public void drawMetrics(Canvas canvas, float dx, float dy) {
        try (TextStyle defaultTs = new TextStyle().setFontSize(24).setColor(0xFF000000);
             TextStyle largeTs   = new TextStyle().setFontSize(36).setColor(0xFF000000);
             TextStyle smallTs   = new TextStyle().setFontSize(12).setColor(0xFF000000);
             ParagraphStyle ps   = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);
             Paint boundaries    = new Paint().setColor(0xFFFAA6B2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);)
        {
            // default style
            pb.pushStyle(defaultTs);
            
            pb.addText("The following ");
            pb.pushStyle(largeTs);
            pb.addText("sentence");
            pb.popStyle();
            pb.addText(" is true\n");

            pb.pushStyle(largeTs);
            pb.addText("The previous ");
            pb.popStyle();
            pb.addText("sentence");
            pb.pushStyle(largeTs);
            pb.addText(" is false\n");
            pb.popStyle();

            pb.addText("— Vicious circularity, \n");
            pb.pushStyle(smallTs);
            pb.addText("  or infinite regress");
            pb.popStyle();

            try (Paragraph p = pb.build();) {
                p.layout(Float.POSITIVE_INFINITY);

                // getLineMetrics
                for (LineMetrics lm: p.getLineMetrics()) {
                    canvas.drawRect(Rect.makeXYWH((float) lm.getLeft(), (float) (lm.getBaseline() - lm.getAscent()), (float) lm.getWidth(), (float) (lm.getAscent() + lm.getDescent())), boundaries);
                    canvas.drawLine((float) lm.getLeft(), (float) lm.getBaseline(), (float) (lm.getLeft() + lm.getWidth()), (float) lm.getBaseline(), boundaries);
                }

                // getGlyphPositionAtCoordinate
                int glyphIdx = p.getGlyphPositionAtCoordinate(dx, dy).getPosition();

                try (var blue   = new Paint().setColor(0x80b3d7ff);
                     var orange = new Paint().setColor(0x80ffd7b3);) {
                    
                    // getRectsForRange    
                    for (Paragraph.TextBox box: p.getRectsForRange(0, glyphIdx, Paragraph.RectHeightStyle.TIGHT, Paragraph.RectWidthStyle.TIGHT)) {
                        canvas.drawRect(box.getRect(), blue);
                    }

                    // getWordBoundary
                    IRange word = p.getWordBoundary(glyphIdx);
                    for (Paragraph.TextBox box: p.getRectsForRange(word.getStart(), word.getEnd(), Paragraph.RectHeightStyle.TIGHT, Paragraph.RectWidthStyle.TIGHT)) {
                        canvas.drawRect(box.getRect(), orange);
                    }
                }
                p.paint(canvas, 0, 0);
                canvas.translate(0, p.getHeight());

                try (var typeface = fc.defaultFallback();
                     var font = new Font(typeface, 16);
                     var blob = font.shape("idx: " + glyphIdx, Float.POSITIVE_INFINITY);
                     var paint = new Paint().setColor(0xFFcc3333)) {
                    canvas.drawTextBlob(blob, 0, 0, font, paint);
                    canvas.translate(0, blob.getBounds().getHeight());
                }
            }
        }
    }
}