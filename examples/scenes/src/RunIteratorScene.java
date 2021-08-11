package org.jetbrains.skija.examples.scenes;

import java.text.*;
import java.util.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class RunIteratorScene extends Scene {
    public final Font lato36;
    public final Font fira36;
    public final Font inter36;
    public final Font inter11;
    public final FontMetrics inter11Metrics;
    public final Font geeza36;
    public final Font emoji36;
    public final Paint boundsStroke = new Paint().setColor(0x403333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint boundsFill = new Paint().setColor(0x403333CC);
    public final Paint textFill = new Paint().setColor(0xFF000000);

    public RunIteratorScene() {
        lato36  = new Font(Typeface.makeFromFile(file("fonts/Lato-Regular.ttf")), 36);
        fira36  = new Font(Typeface.makeFromFile(file("fonts/FiraCode-Regular.ttf")), 36);
        inter36 = new Font(inter, 36);
        inter11 = new Font(inter, 11);
        inter11Metrics = inter11.getMetrics();
        geeza36 = new Font(Typeface.makeFromFile(file("fonts/Geeza Pro Regular.ttf")), 36);

        Typeface emoji = FontMgr.getDefault().matchFamilyStyleCharacter(null, FontStyle.NORMAL, null, "😀".codePointAt(0));
        emoji36 = emoji == null ? null : new Font(emoji, 36);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        String text = "oneΩπΩдва😀🕵️‍♀️👩‍❤️‍👨ثلاثة1234خمسة";

        try (var shaper = Shaper.makeShapeThenWrap();) { // Shaper.makeCoreText();
            try (var handler = new DebugTextBlobHandler().withRuns();)
            {
                var fontIter = new TrivialFontRunIterator(text, lato36);
                var bidiIter = new TrivialBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);
                var scriptIter = new TrivialScriptRunIterator(text, "latn");
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "All trivial");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();
                 var fontIter = new FontMgrRunIterator(text, lato36);)
            {
                var bidiIter = new TrivialBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);
                var scriptIter = new TrivialScriptRunIterator(text, "latn");
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "FontMgrRunIterator");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();)
            {
                Map<Pair<Integer, Integer>, Font> fonts = new HashMap(Map.of(
                    new Pair(0, 0x7F), lato36,
                    new Pair(0x400, 0x4FF), fira36,
                    new Pair(0x600, 0x6FF), geeza36));
                if (emoji36 != null)
                    fonts.put(new Pair(0x1F300, 0x1FADF), emoji36);

                var fontIter = new CustomFontRunIterator(text, fonts, inter36);
                var bidiIter = new TrivialBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);
                var scriptIter = new TrivialScriptRunIterator(text, "latn");
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "CustomFontRunIterator");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();
                 var bidiIter = new IcuBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);)
            {
                var fontIter = new TrivialFontRunIterator(text, lato36);
                var scriptIter = new TrivialScriptRunIterator(text, "latn");
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "IcuBidiRunIterator");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();)
            {
                var fontIter = new TrivialFontRunIterator(text, lato36);
                var bidiIter = new JavaTextBidiRunIterator(text);
                var scriptIter = new TrivialScriptRunIterator(text, "latn");
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "JavaTextBidiRunIterator");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();
                 var scriptIter = new HbIcuScriptRunIterator(text);)
            {
                var fontIter = new TrivialFontRunIterator(text, lato36);
                var bidiIter = new TrivialBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "HbIcuScriptRunIterator");
            }

            try (var handler = new DebugTextBlobHandler().withRuns();
                 var fontIter = new FontMgrRunIterator(text, lato36);
                 var bidiIter = new IcuBidiRunIterator(text, Bidi.DIRECTION_LEFT_TO_RIGHT);
                 var scriptIter = new HbIcuScriptRunIterator(text);)
            {
                var langIter = new TrivialLanguageRunIterator(text, "en-US");
                shaper.shape(text, fontIter, bidiIter, scriptIter, langIter, ShapingOptions.DEFAULT, width - 40, handler);
                drawBlob(canvas, handler, "All native");
            }
        }
    }

    private void drawBlob(Canvas canvas, DebugTextBlobHandler handler, String comment) {
        canvas.drawString(comment, 0, -inter11Metrics.getAscent(), inter11, textFill);
        canvas.translate(0, inter11Metrics.getHeight());
        var blob = handler.makeBlob();
        if (blob != null) {
            try (blob) {
                canvas.drawTextBlob(blob, 0, 0, textFill);
            
                for (var run: handler._runs) {
                    canvas.drawRect(run.getBounds(), boundsStroke);
                }

                canvas.translate(0, blob.getBounds().getBottom() + 20);
            }
        }
    }
}

class CustomFontRunIterator implements Iterator<FontRun> {
    public String _text;
    public Map<Pair<Integer, Integer>, Font> _fonts;
    public Font _fallbackFont;
    public int _pos;
    public Pair<Integer, Integer> _range;

    public CustomFontRunIterator(String text, Map<Pair<Integer, Integer>, Font> fonts, Font fallbackFont) {
        _text = text;
        _fonts = fonts;
        _fallbackFont = fallbackFont;
    }

    public Pair<Integer, Integer> getRange(int ch) {
        return _fonts.keySet().stream().filter(pair -> pair.getFirst() <= ch && ch < pair.getSecond()).findFirst().orElse(null);
    }

    @Override
    public FontRun next() {
        int codePoint = _text.codePointAt(_pos);
        _range = getRange(codePoint);

        while (true) {
            _pos += codePoint > 0x10000 ? 2 : 1;

            if (_pos >= _text.length())
                break;

            codePoint = _text.codePointAt(_pos);
            if (_range != getRange(codePoint))
                break;
        }

        Font font = _range == null ? null : _fonts.get(_range);        
        return new FontRun(_pos, font != null ? font : _fallbackFont);
    }

    @Override
    public boolean hasNext() {
        return _pos < _text.length();
    }
}
