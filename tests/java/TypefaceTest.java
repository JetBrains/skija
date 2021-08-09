package org.jetbrains.skija.test;

import java.util.*;

import org.jetbrains.skija.impl.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.test.runner.*;

import static org.jetbrains.skija.test.runner.TestRunner.*;

public class TypefaceTest implements Executable {
    @Override
    public void execute() throws Exception {
        Typeface inter = Typeface.makeFromFile("fonts/InterHinted-Regular.ttf");
        Typeface interV = Typeface.makeFromFile("fonts/Inter-V.ttf");
        Typeface jbMono = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        Typeface jbMonoBold = Typeface.makeFromData(Data.makeFromFileName("fonts/JetBrainsMono-Bold.ttf"));

        assertEquals(FontStyle.NORMAL, inter.getFontStyle());
        assertEquals(false, inter.isBold());
        assertEquals(false, inter.isItalic());

        assertEquals(FontStyle.BOLD, jbMonoBold.getFontStyle());
        assertEquals(true, jbMonoBold.isBold());
        assertEquals(false, jbMonoBold.isItalic());

        assertEquals(false, inter.isFixedPitch());
        assertEquals(true, jbMono.isFixedPitch());

        assertArrayEquals(null, inter.getVariationAxes());
        assertArrayEquals(null, inter.getVariations());

        var axes = new FontVariationAxis[] { new FontVariationAxis("wght", 100f, 400f, 900f),
                                             new FontVariationAxis("slnt", -10f, 0f, 0f) };
        assertArrayEquals(axes, interV.getVariationAxes());
        if (Platform.CURRENT != Platform.LINUX)
            assertArrayEquals(FontVariation.parse("wght=400 slnt=0"), interV.getVariations());
        
        Typeface inter500 = interV.makeClone(new FontVariation("wght", 500));
        assertNotEquals(inter500, interV);
        assertArrayEquals(FontVariation.parse("wght=500 slnt=0"), inter500.getVariations());

        Typeface inter400 = interV.makeClone(new FontVariation("wght", 400));
        // assertEquals(inter400, interV);

        assertNotEquals(inter.getUniqueId(), interV.getUniqueId());
        assertNotEquals(inter, interV);
        
        assertNotEquals(null, Typeface.makeDefault());

        if (Platform.CURRENT != Platform.LINUX)
            assertEquals("Arial", Typeface.makeFromName("Arial", FontStyle.NORMAL).getFamilyName());

        int[] Skia = new int[] { 83, 107, 105, 97 };
        assertArrayEquals(new short[] { 394, 713, 677, 503 }, inter.getUTF32Glyphs(Skia));
        assertArrayEquals(new short[] { 394, 713, 677, 503 }, inter.getStringGlyphs("Skia"));
        assertEquals(Short.valueOf((short) 394), inter.getUTF32Glyph(83));

        assertEquals(2548, interV.getGlyphsCount());

        assertEquals(17, inter.getTablesCount());
        // assertArrayEquals(null, inter.getTableTags());
        assertArrayEquals(new String[] {"GDEF", "GPOS", "GSUB", "OS/2", "cmap", "cvt ", "fpgm", "gasp", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name", "post", "prep"}, inter.getTableTags());

        assertEquals(true, inter.getTableData("loca").getSize() > 0);

        assertEquals(2816, inter.getUnitsPerEm());

        assertArrayEquals(null, jbMono.getKerningPairAdjustments(null));
        assertArrayEquals(null, jbMono.getKerningPairAdjustments(jbMono.getStringGlyphs("TAV")));
        // assertEquals(null, interV.getKerningPairAdjustments(null));
        // assertArrayEquals(null, interV.getKerningPairAdjustments(interV.getStringGlyphs("TAV")));

        assertArrayEquals(new FontFamilyName[] { new FontFamilyName("Inter", "en-US") }, interV.getFamilyNames());
        assertEquals("Inter", interV.getFamilyName());
        // if (Platform.CURRENT != Platform.LINUX)
        //     assertEquals(Rect.makeLTRB(-0.7386364f, -1.0909119f, 2.5830965f, 0.31959534f), interV.getBounds());
    }
}

