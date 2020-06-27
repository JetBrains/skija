package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.FontMgr;
import org.jetbrains.skija.FontStyle;
import org.jetbrains.skija.Typeface;

import org.jetbrains.skija.paragraph.*;

public class ParagraphStyleScene implements Scene {
    public FontCollection fc = new FontCollection();
    public Typeface slabo;

    public ParagraphStyleScene() {
        fc.setDefaultFontManager(FontMgr.getDefault());
        slabo = Typeface.makeFromFile("fonts/Slabo13px-Regular.ttf");
    }

    public void drawLine(Canvas canvas, String text, ParagraphStyle ps, float width) {
        try (ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
        {
            pb.addText(text);
            try (Paragraph p = pb.build();) {
                p.layout(Math.min(200, width));
                p.paint(canvas, 0, 0);
                canvas.translate(0, p.getHeight());
            }
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        try (TextStyle ts = new TextStyle().setColor(0xFF000000);) {

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts)) {
                drawLine(canvas, "Full many a glorious morning have I seen", ps, width);
            }

            var fontFamilies = new String[] { "System Font", "Apple Color Emoji" };
            try (StrutStyle ss = new StrutStyle()
                   .setFontFamilies(fontFamilies)
                   .setFontStyle(FontStyle.BOLD_ITALIC)
                   .setFontSize(20)
                   .setHeight(2)
                   .setLeading(3)
                   .setEnabled(true)
                   .setHeightForced(true)
                   .setHeightOverridden(true);
                 ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setStrutStyle(ss)) {
                assert ss.equals(ps.getStrutStyle());
                assert Arrays.equals(fontFamilies, ss.getFontFamilies());
                assert FontStyle.BOLD_ITALIC.equals(ss.getFontStyle());
                assert 20 == ss.getFontSize();
                assert 2 == ss.getHeight();
                assert 3 == ss.getLeading();
                assert ss.isEnabled();
                assert ss.isHeightForced();
                assert ss.isHeightOverridden();
                drawLine(canvas, "Flatter the mountain tops with sovereign eye,", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setDirection(Direction.RTL)) {
                assert Direction.RTL == ps.getDirection();
                drawLine(canvas, "Kissing with golden face the meadows green,", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setAlignment(Alignment.CENTER)) {
                assert Alignment.CENTER == ps.getAlignment();
                assert Alignment.CENTER == ps.getEffectiveAlignment();
                drawLine(canvas, "Gilding pale streams with heavenly alchemy;", ps, width);

                assert null == ps.getEllipsis();
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setMaxLinesCount(2).setEllipsis(",,,")) {
                assert 2 == ps.getMaxLinesCount();
                assert ",,,".equals(ps.getEllipsis()) : "Expected ',,,', got '" + ps.getEllipsis() + "'";
                drawLine(canvas, "Anon permit the basest clouds to ride\nWith ugly rack on his celestial face,", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setHeight(40)) {
                assert 40 == ps.getHeight();
                drawLine(canvas, "And from the forlorn world his visage hide,", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setHeightMode(HeightMode.DISABLE_FIRST_ASCENT)) {
                assert HeightMode.DISABLE_FIRST_ASCENT == ps.getHeightMode();
                drawLine(canvas, "Stealing unseen to west with this disgrace:", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setHeightMode(HeightMode.DISABLE_LAST_DESCENT)) {
                assert HeightMode.DISABLE_LAST_DESCENT == ps.getHeightMode();
                drawLine(canvas, "Even so my sun one early morn did shine,", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).setHeightMode(HeightMode.DISABLE_ALL)) {
                assert HeightMode.DISABLE_ALL == ps.getHeightMode();
                drawLine(canvas, "With all triumphant splendour on my brow;", ps, width);
            }

            try (ParagraphStyle ps = new ParagraphStyle().setTextStyle(ts).disableHinting()) {
                assert !ps.isHintingEnabled();
                drawLine(canvas, "But out, alack, he was but one hour mine,", ps, width);
            }
        }
// The region cloud hath mask’d him from me now.
// Yet him for this my love no whit disdaineth;
// Suns of the world may stain when heaven’s sun staineth.
    }
}
