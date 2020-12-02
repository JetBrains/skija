package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public abstract class Scene {
    public static final Typeface inter = Typeface.makeFromFile("fonts/InterHinted-Regular.ttf");
    public static final Font     inter13 = new Font(inter, 13).setSubpixel(true);
    public static final Paint    blackFill = new Paint().setColor(0xFF000000);

    public String[] _variants = new String[] { "Default" };
    public int _variantIdx = 0;
    
    public boolean scale() {
        return true;
    }

    public String variantTitle() {
        return _variants[_variantIdx];
    }

    public void changeVariant(int delta) {
        _variantIdx = (_variantIdx + _variants.length + delta) % _variants.length;
    }

    public abstract void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos);

    public void onScroll(float dx, float dy) {}

    public static void drawStringCentered(Canvas canvas, String text, float x, float y, Font font, Paint paint) {
        Rect bounds = font.measureText(text, paint);
        float lineHeight = font.getMetrics().getCapHeight();
        canvas.drawString(text,
            x - bounds.getRight() / 2,
            y + lineHeight / 2,
            font, paint);
    }
}