package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import org.jetbrains.skija.Font;
import org.jetbrains.skija.TextLine;
import org.jetbrains.skija.Typeface;
import org.jetbrains.skija.test.runner.*;

public class TextLineTest implements Executable {
    public Font inter36 = new Font(Typeface.makeFromFile("fonts/InterHinted-Regular.ttf"), 36);
    public Font firaCode36 = new Font(Typeface.makeFromFile("fonts/FiraCode-Regular.ttf"), 36);
    public Font jbMono36 = new Font(Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf"), 36);

    @Override
    public void execute() throws Exception {
        testAbc();
        testLigatures();
        testCombining();
        testEmoji();
    }

    public void testAbc() {
        try (TextLine line = TextLine.make("abc", inter36);) {
            assertArrayEquals(new short[] {503, 574, 581}, line.getGlyphs());
            assertArrayEquals(new int[] {0, 1, 2, 3}, TextLine._nGetBreakOffsets(line._ptr));
            assertArrayEquals(new float[] {0f, 20f, 42f, 62f}, TextLine._nGetBreakPositions(line._ptr));

            pushStack("getOffsetAtCoord");
            assertEquals(0, line.getOffsetAtCoord(-10f)); // before “a”

            assertEquals(0, line.getOffsetAtCoord(0f));   // beginning of “a”
            assertEquals(0, line.getOffsetAtCoord(5f));   // left half of “a”
            assertEquals(1, line.getOffsetAtCoord(10f));  // center of “a”
            assertEquals(1, line.getOffsetAtCoord(15f));  // right half of “a”

            assertEquals(1, line.getOffsetAtCoord(20f));  // between “a” and “b”
            assertEquals(1, line.getOffsetAtCoord(25f));  // left half of “b”
            assertEquals(2, line.getOffsetAtCoord(31f));  // center of “b”
            assertEquals(2, line.getOffsetAtCoord(36f));  // right half of “b”

            assertEquals(2, line.getOffsetAtCoord(42f));  // between “b” and “c”
            assertEquals(2, line.getOffsetAtCoord(47f));  // left half of “c”
            assertEquals(3, line.getOffsetAtCoord(52f));  // center of “c”
            assertEquals(3, line.getOffsetAtCoord(57f));  // right half of “c”
            
            assertEquals(3, line.getOffsetAtCoord(62f));  // end of “c”
            assertEquals(3, line.getOffsetAtCoord(100f)); // after “c”
            popStack();

            pushStack("getLeftOffsetAtCoord");
            assertEquals(0, line.getLeftOffsetAtCoord(-10f)); // before “a”

            assertEquals(0, line.getLeftOffsetAtCoord(0f));   // beginning of “a”
            assertEquals(0, line.getLeftOffsetAtCoord(5f));   // left half of “a”
            assertEquals(0, line.getLeftOffsetAtCoord(10f));  // center of “a”
            assertEquals(0, line.getLeftOffsetAtCoord(15f));  // right half of “a”

            assertEquals(1, line.getLeftOffsetAtCoord(20f));  // between “a” and “b”
            assertEquals(1, line.getLeftOffsetAtCoord(25f));  // left half of “b”
            assertEquals(1, line.getLeftOffsetAtCoord(31f));  // center of “b”
            assertEquals(1, line.getLeftOffsetAtCoord(36f));  // right half of “b”

            assertEquals(2, line.getLeftOffsetAtCoord(42f));  // between “b” and “c”
            assertEquals(2, line.getLeftOffsetAtCoord(47f));  // left half of “c”
            assertEquals(2, line.getLeftOffsetAtCoord(52f));  // center of “c”
            assertEquals(2, line.getLeftOffsetAtCoord(57f));  // right half of “c”
            
            assertEquals(3, line.getLeftOffsetAtCoord(62f));  // end of “c”
            assertEquals(3, line.getLeftOffsetAtCoord(100f)); // after “c”
            popStack();

            pushStack("getCoordAtOffset");
            assertClose(0f, line.getCoordAtOffset(0));
            assertClose(20f, line.getCoordAtOffset(1));
            assertClose(42f, line.getCoordAtOffset(2));
            assertClose(62f, line.getCoordAtOffset(3));
            popStack();
        }
    }

    public void testLigatures() {
        try (TextLine line = TextLine.make("<=>->", inter36);) {
            assertArrayEquals(new short[] {1712, 1701}, line.getGlyphs());
            assertArrayEquals(new int[] {0, 1, 2, 3, 4, 5}, TextLine._nGetBreakOffsets(line._ptr));
            assertArrayEquals(new float[] {0f, 16f, 32f, 48f, 65f, 82f}, TextLine._nGetBreakPositions(line._ptr));
 
            pushStack("getOffsetAtCoord");
            assertEquals(0, line.getOffsetAtCoord(0f));
            assertEquals(1, line.getOffsetAtCoord(16f));
            assertEquals(2, line.getOffsetAtCoord(32f));
            assertEquals(3, line.getOffsetAtCoord(48f));
            assertEquals(4, line.getOffsetAtCoord(65f));
            assertEquals(5, line.getOffsetAtCoord(82f));
            popStack();

            pushStack("getCoordAtOffset");
            assertClose(0f, line.getCoordAtOffset(0));
            assertClose(16f, line.getCoordAtOffset(1));
            assertClose(32f, line.getCoordAtOffset(2));
            assertClose(48f, line.getCoordAtOffset(3));
            assertClose(65f, line.getCoordAtOffset(4));
            assertClose(82f, line.getCoordAtOffset(5));
            popStack();
        }
   }

   public void testCombining() {
        // u   U+0075  LATIN SMALL LETTER U
        // ̈    U+0308  COMBINING DIAERESIS
        // a   U+0061  LATIN SMALL LETTER A
        // ̧    U+0327  COMBINING CEDILLA
        try (TextLine line = TextLine.make("üa̧", inter36);) {
            assertArrayEquals(new short[] {898 /* ü */, 503 /* a */, 1664 /* ̧  */}, line.getGlyphs());
            assertArrayEquals(new int[] {0, 2, 4}, TextLine._nGetBreakOffsets(line._ptr));
            assertArrayEquals(new float[] {0, 21, 41}, TextLine._nGetBreakPositions(line._ptr));
 
            pushStack("getOffsetAtCoord");
            assertEquals(0, line.getOffsetAtCoord(0f));
            assertEquals(0, line.getOffsetAtCoord(10f));
            assertEquals(2, line.getOffsetAtCoord(12f));
            assertEquals(2, line.getOffsetAtCoord(21f));
            assertEquals(2, line.getOffsetAtCoord(30f));
            assertEquals(4, line.getOffsetAtCoord(32f));
            assertEquals(4, line.getOffsetAtCoord(41f));
            popStack();

            pushStack("getCoordAtOffset");
            assertClose(0, line.getCoordAtOffset(0));
            assertClose(0, line.getCoordAtOffset(1));
            assertClose(21, line.getCoordAtOffset(2));
            assertClose(41, line.getCoordAtOffset(3));
            assertClose(41, line.getCoordAtOffset(4));
            popStack();
        }

        // a    U+0061  LATIN SMALL LETTER A
        // ̆     U+0306  COMBINING BREVE
        try (TextLine line = TextLine.make("ă", jbMono36);) {
            // JetBrains Mono supports “a” and “ă” but not “ ̆ ” separately
            // assertArrayEquals(new short[] {226 /* ă */}, line.getGlyphs()); // FIXME #106
        }

        // a   U+0061  LATIN SMALL LETTER A
        // a   U+0061  LATIN SMALL LETTER A
        // ̧    U+0327  COMBINING CEDILLA
        try (TextLine line = TextLine.make("aa̧", jbMono36);) {
            // JetBrains Mono supports “a” but not “ ̧ ”
            // Second grapheme cluster should fall back together, second “a” should resolve to different glyph
            assertNotEquals(line.getGlyphs()[0], line.getGlyphs()[1]);
        }
   }

   public void testEmoji() {
        try (TextLine misc = TextLine.make("☺", firaCode36);    // U+263A, in Fira Code
             // TextLine misc2 = TextLine.make("☺︎", firaCode36);    // U+263A U+FE0E, in Fira Code + Non-Emoji Variant Selector
             TextLine emoji = TextLine.make("☺️", firaCode36);) // U+263A U+FE0F, has to fallback
        {
            assertArrayEquals(new short[] {1706}, misc.getGlyphs());
            assertEquals(1, emoji.getGlyphs().length);
            assertNotEquals(misc.getGlyphs()[0], emoji.getGlyphs()[0]);

            // FIXME #106
            // assertArrayEquals(new short[] {1706}, misc2.getGlyphs());
            // assertArrayEquals(misc.getGlyphs(), misc2.getGlyphs());
        }
    }
}