package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class DecorationsBenchScene extends Scene {
    public DecorationsBenchScene() {
        _variants = new String[] { "PathEffect", "Bitmap" };
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
    }
}