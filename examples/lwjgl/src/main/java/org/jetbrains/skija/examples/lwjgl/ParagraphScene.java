package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import org.jetbrains.skija.*;

public class ParagraphScene implements Scene {
    FontCollection fc = new FontCollection();
    
    public ParagraphScene() {
        fc.setDefaultFontManager(FontManager.getDefault());
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        float percent = Math.abs((System.currentTimeMillis() % 3000) / 10f - 150f) - 25f;
        percent = Math.round(Math.max(0f, Math.min(100f, percent)));

        try (TextStyle ts = new TextStyle();
             ParagraphStyle ps = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);
             Paint boundaries = new Paint().setColor(0xFFFAA6B2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);)
        {
            ts.setColor(0xFF2a9d8f);
            ts.setFontSize(16);
            pb.pushStyle(ts);
            pb.addText("Current time is ");

            ts.setColor(0xFFe9c46a);
            ts.setFontSize(32);
            pb.pushStyle(ts);
            pb.addText(Long.toString(System.currentTimeMillis()));
            pb.popStyle();

            pb.addText(" ms\n\n");

            ts.setFontSize(20);
            ts.setColor(0xFFe76f51);
            pb.pushStyle(ts);
            pb.addText("Shall I compare thee to a summer's day?\n"
                       + "Thou art more lovely and more temperate:\n"
                       + "Rough winds do shake the darling buds of May,\n"
                       + "And summer's lease hath all too short a date:\n"
                       + "Sometime too hot the eye of heaven shines,\n"
                       + "And often is his gold complexion dimm'd;\n"
                       + "And every fair from fair sometime declines,\n"
                       + "By chance or nature's changing course untrimm'd;\n"
                       + "But thy eternal summer shall not fade\n"
                       + "Nor lose possession of that fair thou owest;\n"
                       + "Nor shall Death brag thou wander'st in his shade,\n"
                       + "When in eternal lines to time thou growest:\n"
                       + "So long as men can breathe or eyes can see,\n"
                       + "So long lives this and this gives life to thee.\n"
                       + "\n");
            pb.popStyle();

            pb.addText("Под новогодний бой курантов\n"
                       + "Олег с улыбкой на лице\n"
                       + "Всегда загадывает чтобы\n"
                       + "Ничьи желанья не сбылись");
            // pb.addText("The quick brown fox 🦊 ate a zesty hamburgerfons 🍔.\nThe 👩‍👩‍👧‍👧 laughed.");
            try (Paragraph p = pb.build();) {
                p.layout(Float.POSITIVE_INFINITY);
                float minW = p.getMinIntrinsicWidth();
                float maxW = p.getMaxIntrinsicWidth();
                float w = minW + (maxW - minW) * percent / 100;
                p.layout(w);
                
                p.paint(canvas, 0, 0);
                float h = p.getHeight();
                canvas.drawRect(Rect.makeXYWH(0, 0, w, h),               boundaries);
                canvas.drawRect(Rect.makeXYWH(0, 0, minW, h),            boundaries);
                canvas.drawRect(Rect.makeXYWH(0, 0, maxW, h),            boundaries);
                canvas.drawRect(Rect.makeXYWH(0, 0, p.getMaxWidth(), h), boundaries);
            }
        }
    }
}