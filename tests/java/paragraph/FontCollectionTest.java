package org.jetbrains.skija.test.paragraph;

import static org.jetbrains.skija.test.runner.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.runner.TestRunner.assertEquals;

import org.jetbrains.skija.FontMgr;
import org.jetbrains.skija.FontStyle;
import org.jetbrains.skija.Typeface;
import org.jetbrains.skija.test.*;
import org.jetbrains.skija.paragraph.FontCollection;
import org.jetbrains.skija.paragraph.TypefaceFontProvider;
import org.jetbrains.skija.test.runner.Executable;
import org.jetbrains.skija.test.runner.TestRunner;

public class FontCollectionTest implements Executable {
    @Override
    public void execute() throws Exception {
        TypefaceFontProvider fm = new TypefaceFontProvider();
        Typeface jbMono = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf", 0);
        fm.registerTypeface(jbMono);
        Typeface inter = Typeface.makeFromFile("fonts/InterHinted-Regular.ttf", 0);
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

        assertArrayEquals(new Typeface[] {}, fc.findTypefaces(new String[] { "No Such Font" }, FontStyle.NORMAL));
        assertArrayEquals(new Typeface[] { jbMono }, fc.findTypefaces(new String[] { "JetBrains Mono" }, FontStyle.NORMAL));
        assertArrayEquals(new Typeface[] {}, fc.findTypefaces(new String[] { "Inter" }, FontStyle.NORMAL));
        assertArrayEquals(new Typeface[] { inter }, fc.findTypefaces(new String[] { "Interface" }, FontStyle.NORMAL));
        assertArrayEquals(new Typeface[] { jbMono, inter }, fc.findTypefaces(new String[] { "JetBrains Mono", "Interface" }, FontStyle.NORMAL));

        // default fm
        var defaultFM = FontMgr.getDefault();
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
            int refCnt = t1.getRefCount();
            try (var t2 = fc.defaultFallback(65 /* A */, FontStyle.NORMAL, "en-US");) {
                assertEquals(refCnt + 1, t1.getRefCount());
                assertEquals(refCnt + 1, t2.getRefCount());
                assertEquals(t1, t2);
            }
            assertEquals(refCnt, t1.getRefCount());
        }

        fc.close();
        fm.close();
    }
}