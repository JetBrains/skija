package skija.examples.lwjgl;

import java.util.Arrays;
import skija.*;

public class TextScene implements Scene {
    private Typeface jbMonoRegular;
    private Typeface interRegular;

    public TextScene() {
        jbMonoRegular = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        interRegular = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);
        drawText(canvas, 0xff9BC730, jbMonoRegular, 13, "JetBrains Mono Regular 13px: <=>");
        drawText(canvas, 0xff9BC730, jbMonoRegular, 16, "JetBrains Mono Regular 16px: <=>");
        drawText(canvas, 0xff9BC730, jbMonoRegular, 20, "JetBrains Mono Regular 20px: <=>");
        drawText(canvas, 0xff9BC730, jbMonoRegular, 24, "JetBrains Mono Regular 24px: <=>");
        drawText(canvas, 0xffEA3C6E, interRegular,  18, "Inter Regular 18px: 013469 Illegal ß");
        drawText(canvas, 0xffEA3C6E, interRegular,  18, "Inter Regular 18px: 013469 Illegal ß (+zero +ss01 +ss02 +cv07)",
            new FontFeature[] { new FontFeature("zero"), new FontFeature("ss01"), new FontFeature("ss02"), new FontFeature("cv07") });
        drawText(canvas, 0xff006690, interRegular,  18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY");
        drawText(canvas, 0xff006690, interRegular,  18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY (-calt)",
            new FontFeature[] { new FontFeature("calt", false) });
        drawText(canvas, 0xff006690, interRegular,  18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY (-kern)",
            new FontFeature[] { new FontFeature("kern", false) });
    }

    public void drawText(Canvas canvas, int color, Typeface typeface, float size, String text) {
        drawText(canvas, color, typeface, size, text, HBFont.NO_FEATURES);
    }

    public void drawText(Canvas canvas, int color, Typeface typeface, float size, String text, FontFeature[] features) {
        FontFeature[] fontFeatures = Arrays.copyOf(features, features.length / 2);
        FontFeature[] shapeFeatures = Arrays.copyOfRange(features, features.length / 2, features.length);

        Font font = new Font(typeface, size, fontFeatures);
        FontExtents extents = font.getHorizontalExtents();

        TextBuffer buffer = font.shape(text, shapeFeatures);
        float[] advances = buffer.getAdvances();

        canvas.save();
        canvas.translate(0, -extents.ascender);

        // bounds
        Paint paint = new Paint().setColor(0x10000000 | (color & 0xFFFFFF));
        canvas.drawRect(Rect.makeLTRB(0, extents.ascender, advances[0], extents.descender), paint);

        // baseline
        paint.setColor(0x40000000 | (color & 0xFFFFFF)).setStrokeWidth(1);
        canvas.drawLine(0, 0, advances[0], 0, paint);

        // text
        paint.setColor(0xFF000000 | (color & 0xFFFFFF));
        canvas.drawTextBuffer(buffer, 0, 0, font.mSkFont, paint);

        canvas.restore();
        canvas.translate(0, -extents.ascender + extents.descender + extents.lineGap + 10);

        paint.release();
        buffer.release();
        font.release();
    }
}