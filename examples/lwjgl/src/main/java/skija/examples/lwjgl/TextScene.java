package skija.examples.lwjgl;

import skija.*;

public class TextScene implements Scene {
    private HBFace hbJBMonoRegular;
    private SkTypeface skJBMonoRegular;

    private HBFace hbInterRegular;
    private SkTypeface skInterRegular;

    public TextScene() {
        hbJBMonoRegular = HBFace.makeFromFile("fonts/JetBrainsMono-Regular.ttf", 0);
        skJBMonoRegular = SkTypeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf", 0);

        hbInterRegular = HBFace.makeFromFile("fonts/Inter-Regular.ttf", 0);
        skInterRegular = SkTypeface.makeFromFile("fonts/Inter-Regular.ttf", 0);
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        drawText(canvas, hbJBMonoRegular, skJBMonoRegular, 13, "JetBrains Mono Regular 13px: <=>", null);
        drawText(canvas, hbJBMonoRegular, skJBMonoRegular, 24, "JetBrains Mono Regular 24px: <=>", null);
        drawText(canvas, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: 013469 Illegal ß", null);
        drawText(canvas, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: 013469 Illegal ß (zero ss01 ss02 cv07)", "");
    }

    public void drawText(Canvas canvas, HBFace hbFace, SkTypeface skTypeface, float size, String text, String options) {
        HBFont hbFont = new HBFont(hbFace, size, options);
        HBExtents extents = hbFont.getHorizontalExtents();

        HBBuffer buffer = hbFont.shape(text);
        float[] advances = buffer.getAdvances();

        canvas.save();
        canvas.translate(0, -extents.ascender);

        // bounds
        Paint paint = new Paint().setColor(0x2033CC33);
        canvas.drawRect(Rect.makeLTRB(0, extents.ascender, advances[0], extents.descender), paint);

        // baseline
        paint.setColor(0x4033CC33).setStrokeWidth(1);
        canvas.drawLine(0, 0, advances[0], 0, paint);

        // text
        SkFont skFont = new SkFont(skTypeface, size);
        paint.setColor(0xFF000000);
        canvas.drawHBBuffer(buffer, 0, 0, skFont, paint);

        canvas.restore();
        canvas.translate(0, -extents.ascender + extents.descender + extents.lineGap + 10);

        paint.release();
        skFont.release();
        buffer.release();
        hbFont.release();
    }
}