package skija.examples.lwjgl;

import skija.Canvas;

public interface Scene {
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos);
}