package org.jetbrains.skija;

import java.util.NoSuchElementException;
import org.jetbrains.skija.test.Executable;
import org.jetbrains.skija.test.TestRunner;

import static org.jetbrains.skija.test.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.TestRunner.assertEquals;
import static org.jetbrains.skija.test.TestRunner.assertNotEquals;
import static org.jetbrains.skija.test.TestRunner.assertThrows;

public class FontTest implements Executable {
    @Override
    public void execute() throws Exception {
        TestRunner.testMethod(this, "fontManager");
        TestRunner.testMethod(this, "fontCollection");
    }

    public void fontManager() throws Exception {
        // FontManager
        TypefaceFontProvider fm = new TypefaceFontProvider();
        SkTypeface jbMono = SkTypeface.makeFromFile("src/test/resources/FontTest/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        SkTypeface jbMonoBold = SkTypeface.makeFromFile("src/test/resources/FontTest/JetBrainsMono-Bold.ttf", 0);
        fm.registerTypeface(jbMonoBold);
        SkTypeface inter = SkTypeface.makeFromFile("src/test/resources/FontTest/Inter-Regular.ttf", 0);
        fm.registerTypeface(inter, "Interface");

        assertEquals(2, fm.countFamilies());
        assertEquals("JetBrains Mono", fm.getFamilyName(0));
        assertEquals("Interface", fm.getFamilyName(1));

        try (var ss = fm.createStyleSet(0)) {
            assertEquals(0, ss.count()); // ?
        }

        try (var ss = fm.createStyleSet(1)) {
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
            try (var face = ss.createTypeface(0)) {
                assertEquals(3, jbMono.getRefCount());
                assertEquals(jbMono, face);
            }
            assertEquals(2, jbMono.getRefCount());

            assertEquals(2, jbMonoBold.getRefCount());
            try (var face = ss.createTypeface(1)) {
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

        assertEquals(null, fm.matchFaceStyle(jbMono, FontStyle.BOLD)); // ?
        assertEquals(null, fm.matchFaceStyle(jbMonoBold, FontStyle.NORMAL)); // ?

        try (var data = Data.makeFromFileName("src/test/resources/FontTest/JetBrainsMono-Italic.ttf");
             var face = fm.makeFromData(data);
             var ss = fm.matchFamily("JetBrains Mono"); )
        {
            assertEquals(2, fm.countFamilies());
            assertEquals(2, ss.count()); // ?
        }

        fm.close();
    }


    public void fontCollection() throws Exception {
        TypefaceFontProvider fm = new TypefaceFontProvider();
        SkTypeface jbMono = SkTypeface.makeFromFile("src/test/resources/FontTest/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        SkTypeface inter = SkTypeface.makeFromFile("src/test/resources/FontTest/Inter-Regular.ttf", 0);
        fm.registerTypeface(inter, "Interface");

        // FontCollection
        FontCollection fc = new FontCollection();
        fc.setAssetFontManager(fm);
        assertEquals(1L, fc.getFontManagersCount());

        assertEquals(2, jbMono.getRefCount());
        try (var jbMono2 = fc.findTypefaces(new String[] { "JetBrains Mono" }, FontStyle.NORMAL)[0]) {
            assertEquals(4, jbMono.getRefCount());
            assertEquals(4, jbMono2.getRefCount());
            try (var jbMono3 = fc.findTypefaces(new String[] { "JetBrains Mono" }, FontStyle.NORMAL)[0]) {
                assertEquals(5, jbMono.getRefCount());
                assertEquals(5, jbMono2.getRefCount());
                assertEquals(5, jbMono3.getRefCount());
            }
            assertEquals(4, jbMono.getRefCount());
            assertEquals(4, jbMono2.getRefCount());
        }
        assertEquals(3, jbMono.getRefCount());

        assertArrayEquals(new SkTypeface[] {}, fc.findTypefaces(new String[] { "No Such Font" }, FontStyle.NORMAL));
        assertArrayEquals(new SkTypeface[] { jbMono }, fc.findTypefaces(new String[] { "JetBrains Mono" }, FontStyle.NORMAL));
        assertArrayEquals(new SkTypeface[] {}, fc.findTypefaces(new String[] { "Inter" }, FontStyle.NORMAL));
        assertArrayEquals(new SkTypeface[] { inter }, fc.findTypefaces(new String[] { "Interface" }, FontStyle.NORMAL));
        assertArrayEquals(new SkTypeface[] { jbMono, inter }, fc.findTypefaces(new String[] { "JetBrains Mono", "Interface" }, FontStyle.NORMAL));

        // default fm
        var defaultFM = FontManager.getDefault();
        fc.setDefaultFontManager(defaultFM);

        assertEquals(2L, fc.getFontManagersCount());
        assertEquals(3, defaultFM.getRefCount());
        try (var ffm = fc.getFallbackManager()) {
            assertEquals(4, defaultFM.getRefCount());
            assertEquals(defaultFM, ffm);
        }
        assertEquals(3, defaultFM.getRefCount());

        // Fallback fonts
        
        // skbug.com/10325
        // try (var t1 = fc.defaultFallback();) {
        //     assertEquals(2, t1.getRefCount());
        //     try (var t2 = fc.defaultFallback();) {
        //         assertEquals(3, t1.getRefCount());
        //         assertEquals(3, t2.getRefCount());
        //         assertEquals(t1, t2);
        //         try (var t3 = fc.defaultFallback();) {
        //             assertEquals(4, t1.getRefCount());
        //             assertEquals(4, t2.getRefCount());
        //             assertEquals(4, t3.getRefCount());
        //             assertEquals(t1, t3);
        //         }
        //         assertEquals(3, t1.getRefCount());
        //         assertEquals(3, t2.getRefCount());
        //     }
        //     assertEquals(2, t1.getRefCount());
        // }

        try (var t1 = fc.defaultFallback(65 /* A */, FontStyle.NORMAL, "en-US");) {
            assertEquals(2, t1.getRefCount());
            try (var t2 = fc.defaultFallback(65 /* A */, FontStyle.NORMAL, "en-US");) {
                assertEquals(3, t1.getRefCount());
                assertEquals(3, t2.getRefCount());
                assertEquals(t1, t2);
            }
            assertEquals(2, t1.getRefCount());
        }

        fc.close();
        fm.close();
    }
}