package org.jetbrains.skija.examples.scenes;

import java.util.stream.*;
import org.jetbrains.skija.*;

public class BreakIteratorScene extends Scene {
    Font mono11 = new Font(jbMono, 11);
    int x, y;

    public BreakIteratorScene() {
        _variants = new String[] { "ICU", "java.text" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        x = 20;
        y = 60;

        var text = "🐉☺️❤️👮🏿👨‍👩‍👧‍👦🚵🏼‍♀️🇷🇺🏴󠁧󠁢󠁥󠁮󠁧󠁿*️⃣ǍǍZ̵̡̢͇͓͎͖͎̪͑͜ͅͅबिक्";
        if ("ICU".equals(_variants[_variantIdx]))
            drawCharacterICU(canvas, height, text);
        else
            drawCharacterJavaText(canvas, height, text);
        x = width / 2 + 10;
        y = 60;

        text = "One, (two; three). FoUr,five!";
        if ("ICU".equals(_variants[_variantIdx]))
            drawWordICU(canvas, height, text);
        else
            drawWordJavaText(canvas, height, text);
    }

    public void drawUnicode(Canvas canvas, String str) {
        var decoded = str.codePoints()
            .mapToObj(c -> String.format("U+%4s", Long.toString(c, 16).toUpperCase()).replaceAll(" ", "0"))
            .collect(Collectors.joining(" "));

        try (var line = TextLine.make(decoded, mono11); ) {
            canvas.drawTextLine(line, x + 50, y, blackFill);
        }
    }

    public void drawSubstring(Canvas canvas, String str, int height) {
        try (var line = TextLine.make(str, inter13); ) {
            canvas.drawTextLine(line, x, y, blackFill);
        }

        y += 20;
        if (y + 20 > height - 20) {
            x += 100;
            y = 20;
        }
    }

    public void drawCharacterICU(Canvas canvas, int height, String text) {
        try (var iter = BreakIterator.makeCharacterInstance();) {
            iter.setText(text);
            int start = iter.first();
            while (true) {
                int end = iter.next();
                if (end == BreakIterator.DONE)
                    break;
                drawUnicode(canvas, text.substring(start, end));
                drawSubstring(canvas, text.substring(start, end), height);
                start = end;
            }
        }
    }

    public void drawCharacterJavaText(Canvas canvas, int height, String text) {
        var iter = java.text.BreakIterator.getCharacterInstance();
        iter.setText(text);
        int start = iter.first();
        while (true) {
            int end = iter.next();
            if (end == java.text.BreakIterator.DONE)
                break;
            drawUnicode(canvas, text.substring(start, end));
            drawSubstring(canvas, text.substring(start, end), height);
            start = end;
        }
    }

    public void drawWordICU(Canvas canvas, int height, String text) {
        try (var iter = BreakIterator.makeWordInstance();) {
            iter.setText(text);
            int start = iter.first();
            while (true) {
                int end = iter.next();
                if (end == BreakIterator.DONE)
                    break;
                if (iter.getRuleStatus() != BreakIterator.WORD_NONE)
                    drawSubstring(canvas, text.substring(start, end), height);
                start = end;
            }
        }
    }

    public void drawWordJavaText(Canvas canvas, int height, String text) {
        var iter = java.text.BreakIterator.getWordInstance();
        iter.setText(text);
        int start = iter.first();
        while (true) {
            int end = iter.next();
            if (end == java.text.BreakIterator.DONE)
                break;
            drawSubstring(canvas, text.substring(start, end), height);
            start = end;
        }
    }
}