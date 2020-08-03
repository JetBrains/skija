package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.Canvas;

public interface Scene {
    void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos);
    default void onScroll(float dx, float dy) {}
}