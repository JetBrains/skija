package org.jetbrains.skija.examples.scenes;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;
import org.jetbrains.skija.paragraph.*;

public class TextLineScene extends Scene {
    private Paint fill = new Paint().setColor(0xFFCC3333);
    private Paint blueFill = new Paint().setColor(0xFF3333CC);
    private Paint selectionFill = new Paint().setColor(0x80b3d7ff);
    private Paint stroke = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Paint strokeRed = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Font inter9 = new Font(inter, 9).setSubpixel(true);
    private Font inter36 = new Font(inter, 36);
    private Font jbMono36 = new Font(jbMono, 36);
    private Font zapfino18;
    // private Font emoji36 = new Font(FontMgr.getDefault().matchFamilyStyleCharacter(null, FontStyle.NORMAL, null, "🧛".codePointAt(0)), 36);
    public FontCollection fc = new FontCollection();

    public TextLineScene() {
        _variants = new String[] { "Set 1", "Set 2", "Set 3 Text Line", "Set 3 Paragraph" };
        _variantIdx = 2;

        fc.setDefaultFontManager(FontMgr.getDefault());
        TypefaceFontProvider fm = new TypefaceFontProvider();
        fm.registerTypeface(inter);
        fm.registerTypeface(jbMono);
        fc.setAssetFontManager(fm);

        if ("Mac OS X".equals(System.getProperty("os.name"))) {
            zapfino18 = new Font(FontMgr.getDefault().matchFamilyStyle("Zapfino", FontStyle.NORMAL), 18);
            fm.registerTypeface(zapfino18.getTypeface());
        }
    }

    public Point drawTable(Canvas canvas, String[] data) {
        float leftWidth = 0;
        float leftHeight = 0;
        for (int i = 0; i < data.length; i += 2) {
            try (var line = TextLine.make(data[i], inter9); ) {
                canvas.drawTextLine(line, 0, leftHeight - line.getAscent(), blueFill);
                leftWidth = Math.max(leftWidth, line.getWidth());
                leftHeight += line.getHeight();
            }
        }

        float rightWidth = 0;
        float rightHeight = 0;
        for (int i = 1; i < data.length; i += 2) {
            try (var line = TextLine.make(data[i], inter9);) {
                canvas.drawTextLine(line, leftWidth + 5, rightHeight - line.getAscent(), blueFill);
                rightWidth = Math.max(rightWidth, line.getWidth());
                rightHeight += line.getHeight();
            }
        }
        return new Point(leftWidth + 5 + rightWidth, Math.max(leftHeight, rightHeight));
    }

    public float drawLine(Canvas canvas, String text, Font font, Point cursor) {
        return drawLine(canvas, new String[] { text }, font, cursor);
    }

    public float drawLine(Canvas canvas, String[] texts, Font font, Point cursor) {
        float bottom = 0;
        canvas.save();
        for (var text: texts) {
            try (var line = TextLine.make(text, font);
                 var blob = line.getTextBlob();)
            {
                int offset = line.getOffsetAtCoord(cursor.getX());

                // bounds
                float lineWidth = line.getWidth();
                float lineHeight = line.getHeight();
                float baseline = -line.getAscent();
                canvas.drawRect(Rect.makeXYWH(0, 0, lineWidth, lineHeight), stroke);
                canvas.drawLine(0, baseline, lineWidth, baseline, stroke);

                // canvas.drawRect(blob.getBounds().translate(0, baseline), strokeRed);
                // canvas.drawRect(blob.getTightBounds().translate(0, baseline), strokeRed);
                // canvas.drawRect(blob.getBlockBounds().translate(0, baseline), strokeRed);

                // selection
                float coord = line.getCoordAtOffset(offset);
                canvas.drawRect(Rect.makeLTRB(0, 0, coord, lineHeight), selectionFill);

                // strict left selection
                int leftOffset = line.getLeftOffsetAtCoord(cursor.getX());
                coord = line.getCoordAtOffset(leftOffset);
                canvas.drawRect(Rect.makeLTRB(0, 0, coord, lineHeight), selectionFill);

                // text
                canvas.drawTextLine(line, 0, baseline, fill);

                // coords
                for (int i = 0; i < text.length() + 1; ++i) {
                    coord = line.getCoordAtOffset(i);
                    canvas.drawLine(coord, baseline, coord, baseline + 4, stroke);
                }

                // extra info
                canvas.save();
                canvas.translate(0, lineHeight + 5);
                float[] positionsXY = line.getPositions();
                String positions = "[";
                for (int i = 0; i < positionsXY.length; i += 2)
                    positions += formatFloat(positionsXY[i]) + ", ";
                positions = (positions.length() <= 2 ? positions : positions.substring(0, positions.length() - 2)) +  "] .. " + formatFloat(lineWidth);

                var tableSize = drawTable(canvas, new String[] {
                    "Chars",            text.chars().mapToObj(c -> String.format(c <= 256 ? "%02X" : "%04X", c)).collect(Collectors.joining(" ")),
                    "Code points",      text.codePoints().mapToObj(c -> String.format(c <= 256 ? "%02X" : "%04X", c)).collect(Collectors.joining(" ")),
                    "Break Positions",  formatFloatArray(TextLine._nGetBreakPositions(line._ptr)),
                    "Break Offsets",    Arrays.toString(TextLine._nGetBreakOffsets(line._ptr)),
                    "Glyphs",           Arrays.toString(line.getGlyphs()),
                    "Positions",        positions,
                    "Coord",            Integer.toString((int) cursor.getX()),
                    "Offset",           offset + " (" + leftOffset + ")  /" + " " + text.length(),
                });
                canvas.restore();

                float offsetLeft = Math.max(100, Math.max(lineWidth, tableSize.getX())) + 10;
                canvas.translate(offsetLeft, 0);
                cursor = cursor.offset(-offsetLeft, 0);
                bottom = Math.max(bottom, lineHeight + 5 + tableSize.getY());
            }
        }
        canvas.restore();
        canvas.translate(0, bottom + 10);
        return bottom + 10;
    }

    public float drawParagraph(Canvas canvas, String[] texts, Font font, Point cursor) {
        float bottom = 0;
        canvas.save();
        for (var text: texts) {
            try (TextStyle defaultTs = new TextStyle().setFontFamily(font.getTypeface().getFamilyName()).setFontSize(36).setColor(0xFFCC3333);
                 ParagraphStyle ps   = new ParagraphStyle();
                 ParagraphBuilder pb = new ParagraphBuilder(ps, fc);)
            {
                pb.pushStyle(defaultTs);
                pb.addText(text);
                try (Paragraph p = pb.build();) {
                    p.layout(Float.POSITIVE_INFINITY);
                    LineMetrics lm = p.getLineMetrics()[0];

                    int offset = p.getGlyphPositionAtCoordinate(cursor.getX(), 0).getPosition();

                    // bounds
                    float lineWidth = (float) lm.getWidth();
                    float lineHeight = (float) lm.getHeight();
                    float baseline = (float) lm.getAscent();
                    canvas.drawRect(Rect.makeXYWH(0, 0, lineWidth, lineHeight), stroke);
                    canvas.drawLine(0, baseline, lineWidth, baseline, stroke);

                    // selection
                    float coord = 0;
                    TextBox[] rects = p.getRectsForRange(0, offset, RectHeightMode.TIGHT, RectWidthMode.TIGHT);
                    for (var rect: rects) {
                        canvas.drawRect(rect.getRect(), selectionFill);
                        coord = rect.getRect().getRight();
                    }

                    // text
                    p.paint(canvas, 0, 0);

                    // coords
                    for (int i = 0; i < text.length() + 1; ++i) {
                        rects = p.getRectsForRange(0, i, RectHeightMode.TIGHT, RectWidthMode.TIGHT);
                        for (var rect: rects) {
                            coord = rect.getRect().getRight();
                            canvas.drawLine(coord, baseline, coord, baseline + 4, stroke);
                        }
                    }

                    // extra info
                    canvas.save();
                    canvas.translate(0, lineHeight + 5);
                    var tableSize = drawTable(canvas, new String[] {
                        "Chars",  text.chars().mapToObj(c -> String.format(c <= 256 ? "%02X" : "%04X", c)).collect(Collectors.joining(" ")),
                        "Coord",  Integer.toString((int) cursor.getX()),
                        "Offset", offset + " " + text.length(),
                    });
                    canvas.restore();

                    float offsetLeft = Math.max(100, Math.max(lineWidth, tableSize.getX())) + 10;
                    canvas.translate(offsetLeft, 0);
                    cursor = cursor.offset(-offsetLeft, 0);
                    bottom = Math.max(bottom, lineHeight + 5 + tableSize.getY());
                }
            }
        }
        canvas.restore();
        canvas.translate(0, bottom + 10);
        return bottom + 10;
    }

    public float drawIt(Canvas canvas, String[] texts, Font font, Point cursor) {
        if (_variants[_variantIdx].endsWith("Paragraph"))
            return drawParagraph(canvas, texts, font, cursor);
        else
            return drawLine(canvas, texts, font, cursor);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        Point cursor = new Point(xpos, ypos);
        canvas.translate(20, 20);
        cursor = cursor.offset(-20, -20);
        if (_variants[_variantIdx].startsWith("Set 1")) {
            cursor = cursor.offset(0, -drawIt(canvas, new String[] { "", "one", "yf", "два", "الخطوط", "🧛", "one yf الخطوط два 🧛" }, inter36, cursor));
            if (zapfino18 != null)
                cursor = cursor.offset(0, -drawIt(canvas, new String[] {"fiz officiad"}, zapfino18, cursor)); // swashes
            // cursor = cursor.offset(0, -drawIt(canvas, "sixستةten", inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"الكلب", "sixستةten", "one اثنان 12 واحد two"}, inter36, cursor)); // RTL
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"<->", "a<->b", "🧑🏿", "a🧑🏿b"}, inter36, cursor)); // Ligatures
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"x̆x̞̊x̃", "c̝̣̱̲͈̝ͨ͐̈ͪͨ̃ͥͅh̙̬̿̂a̯͎͍̜͐͌͂̚o̬s͉̰͊̀"}, inter36, cursor)); // Zero-witdh modifiers
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"क्", "क्‍", "👨👩👧👦", "👨‍👩‍👧‍👦", "a👨‍👩‍👧‍👦b"}, inter36, cursor)); // ZWJ
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"می‌خواهم‎", "میخواهم"}, inter36, cursor)); // ZWNJ
        } else if (_variants[_variantIdx].startsWith("Set 2")) {
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"", "🧛", "🧛🧛", "🧛a🧛", "a🧛a🧛a", "🧛 🧛"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"☹️", "☹️☹️", "☹️a☹️", "a☹️a☹️a", "☹️ ☹️"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"🤵🏽", "👨‍🏭", "🇦🇱", "*️⃣"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"🏴󠁧󠁢󠁳󠁣󠁴󠁿", "🚵🏻‍♀️"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"😮‍💨", "❤️‍🔥", "🧔‍♀", "👨🏻‍❤️‍💋‍👨🏼"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"Ǎ", "Ǎ", "x̆x̞̊x̃", "<->"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"Z̵̡̢͇͓͎͖͎̪͑͜ͅͅ"}, inter36, cursor));
        } else if (_variants[_variantIdx].startsWith("Set 3")) {
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"abcdef"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"-><=><->=>"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"aăå̞ã dấu hỏi k̟t̠ c̝̣̱̲͈̝ͨ͐̈ͪͨ̃ͥͅh̙̬̿̂a̯͎͍̜͐͌͂̚o̬s͉̰͊̀"}, jbMono36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"☹️🤵🏽👨‍🏭🇦🇱*️⃣🏴󠁧󠁢󠁳󠁣󠁴󠁿🚵🏻‍♀️🤦🏼‍♂"}, inter36, cursor));
            cursor = cursor.offset(0, -drawIt(canvas, new String[] {"🧔‍♀👨🏻‍❤️‍💋‍👨🏼"}, inter36, cursor));
        }
    }

    // Furthermore, العربية نص جميل. द क्विक ब्राउन फ़ॉक्स jumps over the lazy 🐕.
    // A true 🕵🏽‍♀️ will spot the tricky selection in this BiDi text: ניפגש ב09:35 בחוף הים
}