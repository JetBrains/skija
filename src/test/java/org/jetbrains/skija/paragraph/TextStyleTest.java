package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.*;
import org.jetbrains.skija.paragraph.*;
import org.jetbrains.skija.test.*;

import static org.jetbrains.skija.test.TestRunner.*;

public class TextStyleTest implements Executable {
    @Override
    public void execute() throws Exception {
        TestRunner.testMethod(this, "test");
    }

    public void test() throws Exception {
        assertEquals(new TextStyle(), new TextStyle());

        try (var ts1 = new TextStyle();
             var ts2 = new TextStyle();) {
            assertEquals(false, ts1.equals(TextStyleAttribute.NONE, ts2));
            assertEquals(false, ts2.equals(TextStyleAttribute.NONE, ts1));

            for (var attr: new TextStyleAttribute[] {
                TextStyleAttribute.ALL_ATTRIBUTES,
                TextStyleAttribute.FONT,
                TextStyleAttribute.FOREGROUND,
                TextStyleAttribute.BACKGROUND,
                TextStyleAttribute.SHADOW,
                TextStyleAttribute.DECORATIONS,
                TextStyleAttribute.LETTER_SPACING,
                TextStyleAttribute.WORD_SPACING,
                TextStyleAttribute.FONT_EXACT})
            {
                pushStack(attr.toString());
                assertEquals(true, ts1.equals(attr, ts2));
                assertEquals(true, ts2.equals(attr, ts1));
                popStack();
            }
        }

        assertEquals(false, new TextStyle().setColor(0xFFcc3300).equals(TextStyleAttribute.ALL_ATTRIBUTES, new TextStyle().setColor(0xFF00cc33)));
        assertEquals(true, new TextStyle().setColor(0xFFcc3300).equals(TextStyleAttribute.BACKGROUND, new TextStyle().setColor(0xFF00cc33)));
    }
}
