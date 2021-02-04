package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class DrawableScene extends Scene {
    public final Font inter18 = new Font(inter, 18);
    public final Paint bounds = new Paint().setColor(0x803333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        // System.out.println("\n--- new frame ---");
        canvas.translate(20, 40);
        
        // default
        var drawable = new SomeDrawable(inter18);
        var generation0 = drawable.getGenerationId();
        drawable.draw(canvas);
        assert generation0 == drawable.getGenerationId() : "Expected " + generation0 + " == " + drawable.getGenerationId();
        canvas.drawRect(drawable.getBounds(), bounds);
        canvas.translate(0, 36);

        drawable.draw(canvas, 100, 0);
        assert generation0 == drawable.getGenerationId() : "Expected " + generation0 + " == " + drawable.getGenerationId();
        canvas.drawRect(drawable.getBounds(), bounds);
        canvas.translate(0, 36);

        drawable.draw(canvas, Matrix33.makeScale(4, 1.5f));
        assert generation0 == drawable.getGenerationId() : "Expected " + generation0 + " == " + drawable.getGenerationId();
        canvas.translate(0, 36);

        // changed
        drawable.setColor(0xFFCC3333);
        drawable.setText("changed");
        var generation1 = drawable.getGenerationId();
        assert generation0 != generation1 : "Expected " + generation0 + " != " + generation1;
        assert generation1 == drawable.getGenerationId() : "Expected " + generation1 + " == " + drawable.getGenerationId();

        canvas.drawDrawable(drawable);
        assert generation1 == drawable.getGenerationId() : "Expected " + generation1 + " == " + drawable.getGenerationId();
        canvas.translate(0, 36);

        canvas.drawDrawable(drawable, 100, 0);
        assert generation1 == drawable.getGenerationId() : "Expected " + generation1 + " == " + drawable.getGenerationId();
        canvas.translate(0, 36);

        canvas.drawDrawable(drawable, Matrix33.makeScale(4, 1.5f));
        assert generation1 == drawable.getGenerationId() : "Expected " + generation1 + " == " + drawable.getGenerationId();
        canvas.translate(0, 36);

        // picture
        drawable.setColor(0xFF3333CC);
        drawable.setText("picture");
        var generation2 = drawable.getGenerationId();
        try (var picture = drawable.makePictureSnapshot()) {
            assert generation2 == drawable.getGenerationId() : "Expected " + generation2 + " == " + drawable.getGenerationId();
            canvas.drawPicture(picture);
            assert generation2 == drawable.getGenerationId() : "Expected " + generation2 + " == " + drawable.getGenerationId();
            canvas.translate(0, 36);

            canvas.drawPicture(picture);
            canvas.translate(0, 36);
        }
        assert generation2 == drawable.getGenerationId() : "Expected " + generation2 + " == " + drawable.getGenerationId();

        try (var picture = drawable.makePictureSnapshot()) {
            assert generation2 == drawable.getGenerationId() : "Expected " + generation2 + " == " + drawable.getGenerationId();
            canvas.drawPicture(picture);
            canvas.translate(0, 36);
        }

        // drawable.close();
    }
}

class SomeDrawable extends Drawable {
    private Font font;
    private int color = 0xFF000000;
    private String text = "default";

    public SomeDrawable(Font font) {
        this.font = font;
    }

    @Override
    public void onDraw(Canvas canvas) {
        // System.out.println(getGenerationId() + " Drawable::onDraw(color = " + color + ", text = \"" + text + "\")");
        try (var p = new Paint().setColor(color);) {
            canvas.drawString(text, 0, 0, font, p);
        }
    }

    @Override
    public Rect onGetBounds() {
        // System.out.println(getGenerationId() + " Drawable::onGetBounds(color = " + color + ", text = \"" + text + "\")");
        return font.measureText(text);
    }

    public void setColor(int color) {
        this.color = color;
        var prevGen =getGenerationId();
        notifyDrawingChanged();
        // System.out.println(prevGen + " -> " + getGenerationId() + " Drawable::notifyDrawingChanged");
    }

    public void setText(String text) {
        this.text = text;
        var prevGen = getGenerationId();
        notifyDrawingChanged();
        // System.out.println(prevGen + " -> " + getGenerationId() + " Drawable::notifyDrawingChanged");
    }
}