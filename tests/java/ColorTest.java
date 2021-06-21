package org.jetbrains.skija.test;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import java.util.*;

import org.jetbrains.skija.Color;
import org.jetbrains.skija.Color4f;
import org.jetbrains.skija.test.runner.*;

public class ColorTest implements Executable {
    @Override
    public void execute() throws Exception {
        Map<Integer, Color4f> cases = new HashMap<>();
        cases.put(0x00000000, new Color4f(0, 0, 0, 0));
        cases.put(0xFF000000, new Color4f(0, 0, 0, 1));
        cases.put(0x00FF0000, new Color4f(1, 0, 0, 0));
        cases.put(0x0000FF00, new Color4f(0, 1, 0, 0));
        cases.put(0x000000FF, new Color4f(0, 0, 1, 0));
        cases.put(0x80808080, new Color4f(128/255f, 128/255f, 128/255f, 128/255f));

        for (var entry: cases.entrySet()) {
            int color = entry.getKey();
            Color4f color4f = entry.getValue();
            pushStack(Integer.toString(color, 16) + " <-> " + color4f);
            assertEquals(color, color4f.toColor());
            assertEquals(new Color4f(color), color4f);
            popStack();
        }

        assertEquals(0x12, Color.getA(0x12345678));
        assertEquals(0x34, Color.getR(0x12345678));
        assertEquals(0x56, Color.getG(0x12345678));
        assertEquals(0x78, Color.getB(0x12345678));
        assertEquals(0xFE345678, Color.withA(0x12345678, 0xFE));
        assertEquals(0x12FE5678, Color.withR(0x12345678, 0xFE));
        assertEquals(0x1234FE78, Color.withG(0x12345678, 0xFE));
        assertEquals(0x123456FE, Color.withB(0x12345678, 0xFE));
    }
}