package org.jetbrains.skija.platform;

import static org.jetbrains.skija.platform.test.TestRunner.*;

import org.jetbrains.skija.BlendMode;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.platform.test.*;

public class PaintTest implements Executable {
    @Override
    public void execute() throws Exception {
        assertEquals(new Paint().setColor(0x12345678), new Paint().setColor(0x12345678));
        assertEquals(new Paint().setColor(0x12345678).hashCode(), new Paint().setColor(0x12345678).hashCode());
        assertNotEquals(new Paint().setColor(0x12345678), new Paint().setColor(0xFF345678));
        assertNotEquals(new Paint().setColor(0x12345678).hashCode(), new Paint().setColor(0xFF345678).hashCode());
        assertNotEquals(new Paint(), new Paint().setAntiAlias(false));
        assertNotEquals(new Paint(), new Paint().setDither(true));

        try (var p = new Paint().setColor(0x12345678);) {
            assertEquals(false, p == p.makeClone());
            assertEquals(p, p.makeClone());
            assertEquals(p.hashCode(), p.makeClone().hashCode());
        }

        try (var p = new Paint();) {
            assertEquals(false, p.hasNothingToDraw());

            p.setBlendMode(BlendMode.DST);
            assertEquals(true, p.hasNothingToDraw());

            p.setBlendMode(BlendMode.SRC_OVER);
            assertEquals(false, p.hasNothingToDraw());

            p.setAlpha(0);
            assertEquals(true, p.hasNothingToDraw());
        }
    }
}