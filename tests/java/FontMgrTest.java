package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertEquals;

import org.jetbrains.skija.Data;
import org.jetbrains.skija.FontStyle;
import org.jetbrains.skija.Typeface;
import org.jetbrains.skija.paragraph.FontCollection;
import org.jetbrains.skija.paragraph.TypefaceFontProvider;
import org.jetbrains.skija.test.runner.Executable;
import org.jetbrains.skija.test.runner.TestRunner;

public class FontMgrTest implements Executable {
    @Override
    public void execute() throws Exception {
        // FontManager
        TypefaceFontProvider fm = new TypefaceFontProvider();
        Typeface jbMono = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        Typeface jbMonoBold = Typeface.makeFromFile("fonts/JetBrainsMono-Bold.ttf", 0);
        fm.registerTypeface(jbMonoBold);
        Typeface inter = Typeface.makeFromFile("fonts/InterHinted-Regular.ttf", 0);
        fm.registerTypeface(inter, "Interface");

        assertEquals(2, fm.getFamiliesCount());
        assertEquals("JetBrains Mono", fm.getFamilyName(0));
        assertEquals("Interface", fm.getFamilyName(1));

        try (var ss = fm.makeStyleSet(0)) {
            assertEquals(0, ss.count()); // ?
        }

        try (var ss = fm.makeStyleSet(1)) {
            assertEquals(0, ss.count()); // ?
        }

        // FontStyleSet
        try (var ss = fm.matchFamily("JetBrains Mono")) {
            assertEquals(2, ss.count());
            assertEquals(FontStyle.NORMAL, ss.getStyle(0));
            assertEquals("JetBrains Mono", ss.getStyleName(0));

            assertEquals(FontStyle.BOLD, ss.getStyle(1));
            assertEquals("JetBrains Mono", ss.getStyleName(1));

            assertEquals(2, jbMono.getRefCount());
            try (var face = ss.getTypeface(0)) {
                assertEquals(3, jbMono.getRefCount());
                assertEquals(jbMono, face);
            }
            assertEquals(2, jbMono.getRefCount());

            assertEquals(2, jbMonoBold.getRefCount());
            try (var face = ss.getTypeface(1)) {
                assertEquals(3, jbMonoBold.getRefCount());
                assertEquals(jbMonoBold, face);
            }
            assertEquals(2, jbMonoBold.getRefCount());

            assertEquals(2, jbMono.getRefCount());
            try (var face = ss.matchStyle(FontStyle.NORMAL)) {
                assertEquals(3, jbMono.getRefCount());
                assertEquals(jbMono, face);
            }
            assertEquals(2, jbMono.getRefCount());            

            assertEquals(2, jbMonoBold.getRefCount());
            try (var face = ss.matchStyle(FontStyle.BOLD)) {
                assertEquals(3, jbMonoBold.getRefCount());
                assertEquals(jbMonoBold, face);
            }
            assertEquals(2, jbMonoBold.getRefCount());

            assertEquals(jbMono, ss.matchStyle(FontStyle.ITALIC)); // ?
        }

        assertEquals(null, fm.matchFamilyStyle("JetBrains Mono", FontStyle.BOLD)); // ?
        assertEquals(null, fm.matchFamilyStyle("Interface", FontStyle.NORMAL)); // ?

        assertEquals(null, fm.matchFamilyStyleCharacter("JetBrains Mono", FontStyle.BOLD, new String[] {"en-US"}, 65 /* A */)); // ?

        try (var data = Data.makeFromFileName("fonts/JetBrainsMono-Italic.ttf");
             var face = fm.makeFromData(data);
             var ss = fm.matchFamily("JetBrains Mono"); )
        {
            assertEquals(2, fm.getFamiliesCount());
            assertEquals(2, ss.count()); // ?
        }

        fm.close();
    }
}