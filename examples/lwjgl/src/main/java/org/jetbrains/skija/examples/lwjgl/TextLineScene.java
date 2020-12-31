package org.jetbrains.skija.examples.lwjgl;

import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class TextLineScene extends Scene {
    private Paint fill = new Paint().setColor(0xFFCC3333);
    private Paint blueFill = new Paint().setColor(0xFF3333CC);
    private Paint selectionFill = new Paint().setColor(0x80b3d7ff);
    private Paint stroke = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Paint strokeRed = new Paint().setColor(0x80CC3333).setMode(PaintMode.STROKE).setStrokeWidth(1);
    private Font inter9 = new Font(inter, 9).setSubpixel(true);
    private Font inter18 = new Font(inter, 36);
    private Font zapfino18;
    private Font emoji36 = new Font(FontMgr.getDefault().matchFamilyStyleCharacter(null, FontStyle.NORMAL, null, "🧛".codePointAt(0)), 36);
    
    public TextLineScene() {
        _variants = new String[] { "Subpixel", "No subpixel" };
        if ("Mac OS X".equals(System.getProperty("os.name")))
            zapfino18 = new Font(FontMgr.getDefault().matchFamilyStyle("Zapfino", FontStyle.NORMAL), 18);
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
                    positions += String.format(Locale.ENGLISH, "%.02f, ", positionsXY[i]);
                positions = (positions.length() <= 2 ? positions : positions.substring(0, positions.length() - 2)) +  "] .. " + String.format(Locale.ENGLISH, "%.02f", lineWidth);
                var tableSize = drawTable(canvas, new String[] {
                    "Chars",     text.chars().mapToObj(c -> String.format("U+%04X", c)).collect(Collectors.joining(" ")),
                    "Clusters",  Arrays.toString(line.getClusters()),
                    "Glyphs",    Arrays.toString(line.getGlyphs()),
                    "Positions", positions,
                    "Coord",     Integer.toString((int) cursor.getX()),
                    "Offset",    offset + " / " + text.length(),
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

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        for (var font: new Font[] { inter18, zapfino18, emoji36 }) {
            if (font != null) {
                if ("Subpixel".equals(_variants[_variantIdx]) && !font.isSubpixel())
                    font.setSubpixel(true);
                else if ("No subpixel".equals(_variants[_variantIdx]) && font.isSubpixel())
                    font.setSubpixel(false);
            }
        }

        Point cursor = new Point(xpos, ypos);
        canvas.translate(20, 20);
        cursor = cursor.offset(-20, -20);
        cursor = cursor.offset(0, -drawLine(canvas, new String[] { "", "one", "yf", "два", "الخطوط", "🧛", "one yf الخطوط два 🧛" }, inter18, cursor));
        if (zapfino18 != null)
            cursor = cursor.offset(0, -drawLine(canvas, "fiz officiad", zapfino18, cursor)); // swashes
        // cursor = cursor.offset(0, -drawLine(canvas, "sixستةten", inter18, cursor));
        cursor = cursor.offset(0, -drawLine(canvas, new String[] {"الكلب", "sixستةten", "one اثنان 12 واحد two"}, inter18, cursor)); // RTL
        cursor = cursor.offset(0, -drawLine(canvas, new String[] {"<->", "a<->b", "🧑🏿", "a🧑🏿b"}, inter18, cursor)); // Ligatures
        cursor = cursor.offset(0, -drawLine(canvas, new String[] {"x̆x̞̊x̃", "c̝̣̱̲͈̝ͨ͐̈ͪͨ̃ͥͅh̙̬̿̂a̯͎͍̜͐͌͂̚o̬s͉̰͊̀"}, inter18, cursor)); // Zero-witdh modifiers
        cursor = cursor.offset(0, -drawLine(canvas, new String[] {"क्", "क्‍", "👨👩👧👦", "👨‍👩‍👧‍👦", "a👨‍👩‍👧‍👦b"}, inter18, cursor)); // ZWJ
        cursor = cursor.offset(0, -drawLine(canvas, new String[] {"می‌خواهم‎", "میخواهم"}, inter18, cursor)); // ZWNJ
    }
}