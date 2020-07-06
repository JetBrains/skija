package org.jetbrains.skija;

import java.util.Arrays;
import org.jetbrains.skija.test.Executable;
import org.jetbrains.skija.test.TestRunner;

import static org.jetbrains.skija.test.TestRunner.assertArrayEquals;
import static org.jetbrains.skija.test.TestRunner.assertEquals;
import static org.jetbrains.skija.test.TestRunner.assertNotEquals;

public class TypefaceTest implements Executable {
    @Override
    public void execute() throws Exception {
        boolean isLinux = System.getProperty("os.name").equals("Linux");

        Typeface inter = Typeface.makeFromFile("src/test/resources/fonts/Inter-Regular.ttf");
        Typeface interV = Typeface.makeFromFile("src/test/resources/fonts/Inter-V.otf");
        Typeface jbMono = Typeface.makeFromFile("src/test/resources/fonts/JetBrainsMono-Regular.ttf");
        Typeface jbMonoBold = Typeface.makeFromData(Data.makeFromFileName("src/test/resources/fonts/JetBrainsMono-Bold.ttf"));

        assertEquals(FontStyle.NORMAL, inter.getFontStyle());
        assertEquals(false, inter.isBold());
        assertEquals(false, inter.isItalic());

        assertEquals(FontStyle.BOLD, jbMonoBold.getFontStyle());
        assertEquals(true, jbMonoBold.isBold());
        assertEquals(false, jbMonoBold.isItalic());

        assertEquals(false, inter.isFixedPitch());
        assertEquals(true, jbMono.isFixedPitch());

        assertArrayEquals(null, inter.getVariationPosition());
        assertArrayEquals(new FontVariation[] { new FontVariation("wght", 400), new FontVariation("slnt", 0) }, interV.getVariationPosition());
        
        Typeface inter500 = interV.makeClone(new FontVariation("wght", 500));
        assertNotEquals(inter500, interV);
        assertArrayEquals(new FontVariation[] { new FontVariation("wght", 500), new FontVariation("slnt", 0) }, inter500.getVariationPosition());

        Typeface inter400 = interV.makeClone(new FontVariation("wght", 400));
        // assertEquals(inter400, interV);

        assertNotEquals(inter.getUniqueId(), interV.getUniqueId());
        assertNotEquals(inter, interV);
        
        assertNotEquals(null, Typeface.makeDefault());

        if (!isLinux)
            assertEquals("Arial", Typeface.makeFromName("Arial", FontStyle.NORMAL).getFamilyName());

        int[] Skia = new int[] { 83, 107, 105, 97 };
        assertArrayEquals(new short[] { 393, 709, 673, 501 }, inter.getUTF32GlyphIds(Skia));
        assertArrayEquals(new short[] { 393, 709, 673, 501 }, inter.getStringGlyphIds("Skia"));
        assertEquals(Short.valueOf((short) 393), inter.getUTF32GlyphId(83));

        assertEquals(2538, interV.getGlyphsCount());

        assertEquals(17, inter.getTablesCount());
        // assertArrayEquals(null, inter.getTableTags());
        assertArrayEquals(new String[] {"GDEF", "GPOS", "GSUB", "OS/2", "cmap", "cvt ", "fpgm", "gasp", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name", "post", "prep"}, inter.getTableTags());

        assertEquals(true, inter.getTableData("loca").getSize() > 0);

        assertEquals(2816, inter.getUnitsPerEm());

        assertArrayEquals(null, jbMono.getKerningPairAdjustments(null));
        assertArrayEquals(null, jbMono.getKerningPairAdjustments(jbMono.getStringGlyphIds("TAV")));
        // assertEquals(null, interV.getKerningPairAdjustments(null));
        // assertArrayEquals(null, interV.getKerningPairAdjustments(interV.getStringGlyphIds("TAV")));

        assertArrayEquals(new FontFamilyName[] { new FontFamilyName("Inter V", "en-US") }, interV.getFamilyNames());
        assertEquals("Inter V", interV.getFamilyName());
        if (!isLinux)
            assertEquals(Rect.makeLTRB(-0.7386364f, -1.0909119f, 2.5830965f, 0.31959534f), interV.getBounds());
    }
}

