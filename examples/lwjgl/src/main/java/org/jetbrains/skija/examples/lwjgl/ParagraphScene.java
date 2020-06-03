package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import org.jetbrains.skija.*;

public class ParagraphScene implements Scene {
    FontCollection fc = new FontCollection();
    
    public ParagraphScene() {
        fc.setDefaultFontManager(FontManager.getDefault());
        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        SkTypeface jbMono = SkTypeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        SkTypeface inter = SkTypeface.makeFromFile("fonts/Inter-Regular.ttf", 0);
        fm.registerTypeface(inter, "Interface");
        fc.setAssetFontManager(fm);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);

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

            //            + "Thou art more lovely and more temperate:\n"
            //            + "Rough winds do shake the darling buds of May,\n"
            //            + "And summer's lease hath all too short a date:\n"
            //            + "Sometime too hot the eye of heaven shines,\n"
            //            + "And often is his gold complexion dimm'd;\n"
            //            + "And every fair from fair sometime declines,\n"
            //            + "By chance or nature's changing course untrimm'd;\n"
            //            + "But thy eternal summer shall not fade\n"
            //            + "Nor lose possession of that fair thou owest;\n"
            //            + "Nor shall Death brag thou wander'st in his shade,\n"
            //            + "When in eternal lines to time thou growest:\n"
            //            + "So long as men can breathe or eyes can see,\n"
            //            + "So long lives this and this gives life to thee.\n"
            //            + "\n");

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
                    canvas.translate(w + 40, 0);
                }
            }
        }
    }
}