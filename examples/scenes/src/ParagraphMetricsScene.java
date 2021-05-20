package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;
import org.jetbrains.skija.paragraph.*;

public class ParagraphMetricsScene extends Scene {
    public FontCollection fc = new FontCollection();
    public long lastUpdate = 0;

    public Font inter13;
    public Paint detailsFill;
    public Paint boundariesStroke;
    
    public ParagraphMetricsScene() {
        fc.setDefaultFontManager(FontMgr.getDefault());
        
        TypefaceFontProvider fm = new TypefaceFontProvider();
        fm.registerTypeface(jbMono);
        fm.registerTypeface(inter, "Interface");
        fc.setAssetFontManager(fm);

        inter13 = new Font(inter, 13);
        detailsFill = new Paint().setColor(0xFFCC3333);
        boundariesStroke = new Paint().setColor(0xFFFAA6B2).setMode(PaintMode.STROKE).setStrokeWidth(1f);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.save();
        canvas.translate(30, 30);
        drawSelection(canvas, xpos - 30f, ypos - 30f);
        canvas.restore();

        canvas.save();
        canvas.translate(30, height / 2 + 15);
        drawIndices(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(width / 2 + 15, 30);
        drawPositions(canvas);
        canvas.restore();
    }

    public void drawSelection(Canvas canvas, float dx, float dy) {
        try (TextStyle defaultTs = new TextStyle().setFontSize(24).setColor(0xFF000000).setHeight(1.2f);
             TextStyle largeTs   = new TextStyle().setFontSize(36).setColor(0xFF000000);
             TextStyle smallTs   = new TextStyle().setFontSize(12).setColor(0xFF000000);
             TextStyle ligaTs    = new TextStyle().setFontSize(24).setColor(0xFF000000).setFontFamilies(new String[] { "Interface" });
             TextStyle zapfinoTs = new TextStyle().setFontSize(24).setColor(0xFF000000).setFontFamilies(new String[] { "Zapfino" });
             ParagraphStyle ps   = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);
             ParagraphStyle ps2  = new ParagraphStyle().setAlignment(Alignment.RIGHT);)
        {
            // default style
            pb.pushStyle(defaultTs);
            
            pb.addText("123 567 ");
            pb.pushStyle(ligaTs);
            pb.addText("<-> ");
            pb.popStyle();
            pb.addText("👩 👩👩 🧑🏿 🧑🏿🧑🏿 👮‍♀️ 👮‍♀️👮‍♀️ 👩‍👩‍👧‍👧 👩‍👩‍👧‍👧👩‍👩‍👧‍👧\n");

            pb.addText("The following ");
            pb.pushStyle(largeTs);
            pb.addText("sentence");
            pb.popStyle();
            pb.addText(" is true\n");

            pb.pushStyle(largeTs);
            pb.addText("The previous       ");
            pb.popStyle();
            pb.addText("sentence");
            pb.pushStyle(largeTs);
            pb.addText(" is false\n");
            pb.popStyle();

            pb.setParagraphStyle(ps2);
            pb.pushStyle(defaultTs);
            pb.addText("— Vicious circularity, \n");
            pb.pushStyle(smallTs);
            pb.addText("  or infinite regress\n");
            pb.popStyle();

            pb.addText("hello мир дружба <<<");
            pb.addPlaceholder(new PlaceholderStyle(50, 2f, PlaceholderAlignment.BASELINE, BaselineMode.ALPHABETIC, 0f));
            pb.addText(">>> fi fl 👃 one two ثلاثة 12 👂 34 خمسة\n");
            pb.addText("x̆x̞̊x̃ c̝̣̱̲͈̝ͨ͐̈ͪͨ̃ͥͅh̙̬̿̂a̯͎͍̜͐͌͂̚o̬s͉̰͊̀ ");
            pb.pushStyle(zapfinoTs);
            pb.addText("fiz officiad\n");
            pb.popStyle();

            try (Paragraph p = pb.build();) {
                p.layout(600f);

                // getLineMetrics
                for (LineMetrics lm: p.getLineMetrics()) {
                    canvas.drawRect(Rect.makeXYWH((float) lm.getLeft(),
                                                  (float) (lm.getBaseline() - lm.getAscent()),
                                                  (float) lm.getWidth(),
                                                  (float) (lm.getAscent() + lm.getDescent())), boundariesStroke);
                    canvas.drawLine((float) lm.getLeft(), (float) lm.getBaseline(), (float) (lm.getLeft() + lm.getWidth()), (float) lm.getBaseline(), boundariesStroke);
                }

                // getGlyphPositionAtCoordinate
                int glyphx = p.getGlyphPositionAtCoordinate(dx, dy).getPosition();

                try (var blue   = new Paint().setColor(0x80b3d7ff);
                     var orange = new Paint().setColor(0x80ffd7b3);) {
                    
                    // getRectsForRange    
                    for (TextBox box: p.getRectsForRange(0, glyphx, RectHeightMode.TIGHT, RectWidthMode.TIGHT)) {
                        canvas.drawRect(box.getRect(), blue);
                    }

                    // getWordBoundary
                    IRange word = p.getWordBoundary(glyphx);
                    for (TextBox box: p.getRectsForRange(word.getStart(), word.getEnd(), RectHeightMode.TIGHT, RectWidthMode.TIGHT)) {
                        canvas.drawRect(box.getRect(), orange);
                    }
                }
                p.paint(canvas, 0, 0);
                canvas.drawString("idx: " + glyphx, 630, 20, inter13, detailsFill);
            }
        }
    }

    public void drawIndices(Canvas canvas) {
        try (TextStyle defaultTs = new TextStyle().setFontSize(16).setColor(0xFF000000).setFontFamilies(new String[] { "Interface" }).setHeight(1.5f);
             ParagraphStyle ps   = new ParagraphStyle();
             ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
        {
            pb.pushStyle(defaultTs);

            String text = "12345\n"
                + "абвгд\n"
                + "space     \n"
                // https://bugs.chromium.org/p/skia/issues/detail?id=11986
                + (System.getProperty("os.name").toLowerCase().contains("linux") ? "" : "\\r\\n\r\n")
                + "\\n\\n\n\n"
                + "👩👩👩👩👩\n"
                + "<-><->\n"
                + "🧑🏿🧑🏿🧑🏿🧑🏿🧑🏿\n"
                + "👮‍♀️👮‍♀️👮‍♀️👮‍♀️👮‍♀️\n"
                + "👩‍👩‍👧‍👧👩‍👩‍👧‍👧👩‍👩‍👧‍👧👩‍👩‍👧‍👧👩‍👩‍👧‍👧";

            pb.addText(text);
            try (Paragraph paragraph = pb.build();) {               
                paragraph.layout(600f);
                paragraph.paint(canvas, 0, 0);
                for (LineMetrics lm: paragraph.getLineMetrics()) {
                    canvas.drawRect(Rect.makeXYWH((float) lm.getLeft(),
                                                  (float) (lm.getBaseline() - lm.getAscent()),
                                                  (float) lm.getWidth(),
                                                  (float) (lm.getAscent() + lm.getDescent())), boundariesStroke);
                    canvas.drawLine((float) lm.getLeft(), (float) lm.getBaseline(), (float) (lm.getLeft() + lm.getWidth()), (float) lm.getBaseline(), boundariesStroke);

                    canvas.drawString(
                        String.format("start=%d, end w/o space=%d, end=%d, end w/ newline=%d",
                            lm.getStartIndex(),
                            lm.getEndExcludingWhitespaces(),
                            lm.getEndIndex(),
                            lm.getEndIncludingNewline()),
                        150, (float) lm.getBaseline(), inter13, detailsFill);
                }
            }
        }
    }

    public void drawPositions(Canvas canvas) {
        for (String text: new String[] {
            "ggg",
            "жжж",
            "𨭎",
            "𨭎𨭎",
            "ggg𨭎𨭎𨭎",
            "👩",
            "ggg👩👩👩",
            "👨👩👧👦",
            "👩‍👩‍👧‍👧",
            "👩‍👩‍👧‍👧👩‍👩‍👧‍👧👩‍👩‍👧‍👧",
            "sixستةten",
            "x̆x̞̊x̃",
            "<->"
        }) {
            try (TextStyle defaultTs = new TextStyle().setFontSize(16).setColor(0xFF000000).setFontFamilies(new String[] { "Interface" }).setHeight(1.5f);
                 ParagraphStyle ps   = new ParagraphStyle();
                 ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
            {
                pb.pushStyle(defaultTs);
                pb.addText(text);
                try (Paragraph paragraph = pb.build();) {
                    paragraph.layout(600f);
                    paragraph.paint(canvas, 0, 0);

                    LineMetrics lm = paragraph.getLineMetrics()[0];
                    String s = "";
                    for (int x = 0; x < lm.getWidth() + 10; ++x) {
                        s += paragraph.getGlyphPositionAtCoordinate(x, (float) lm.getBaseline()).getPosition() + " ";
                    }
                    canvas.drawString(s, 100, (float) lm.getBaseline(), inter13, detailsFill);

                    canvas.translate(0, paragraph.getHeight());
                }
            }
        }
    }
}