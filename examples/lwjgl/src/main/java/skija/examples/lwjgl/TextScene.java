package skija.examples.lwjgl;

import java.util.Arrays;
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
        drawText(canvas, 0xff9BC730, hbJBMonoRegular, skJBMonoRegular, 13, "JetBrains Mono Regular 13px: <=>");
        drawText(canvas, 0xff9BC730, hbJBMonoRegular, skJBMonoRegular, 16, "JetBrains Mono Regular 16px: <=>");
        drawText(canvas, 0xff9BC730, hbJBMonoRegular, skJBMonoRegular, 20, "JetBrains Mono Regular 20px: <=>");
        drawText(canvas, 0xff9BC730, hbJBMonoRegular, skJBMonoRegular, 24, "JetBrains Mono Regular 24px: <=>");
        drawText(canvas, 0xffEA3C6E, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: 013469 Illegal ß");
        drawText(canvas, 0xffEA3C6E, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: 013469 Illegal ß (+zero +ss01 +ss02 +cv07)",
            new FontFeature[] { new FontFeature("zero"), new FontFeature("ss01"), new FontFeature("ss02"), new FontFeature("cv07") });
        drawText(canvas, 0xff006690, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY");
        drawText(canvas, 0xff006690, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY (-calt)",
            new FontFeature[] { new FontFeature("calt", false) });
        drawText(canvas, 0xff006690, hbInterRegular, skInterRegular, 18, "Inter Regular 18px: [m] [M] 7*4 7×4 War, Tea, LAY (-kern)",
            new FontFeature[] { new FontFeature("kern", false) });
    }

    public void drawText(Canvas canvas, int color, HBFace hbFace, SkTypeface skTypeface, float size, String text) {
        drawText(canvas, color, hbFace, skTypeface, size, text, new FontFeature[0]);
    }

    public void drawText(Canvas canvas, int color, HBFace hbFace, SkTypeface skTypeface, float size, String text, FontFeature[] features) {
        FontFeature[] fontFeatures = Arrays.copyOf(features, features.length / 2);
        FontFeature[] shapeFeatures = Arrays.copyOfRange(features, features.length / 2, features.length);

        HBFont hbFont = new HBFont(hbFace, size, fontFeatures);
        HBExtents extents = hbFont.getHorizontalExtents();

        HBBuffer buffer = hbFont.shape(text, shapeFeatures);
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
        SkFont skFont = new SkFont(skTypeface, size);
        paint.setColor(0xFF000000 | (color & 0xFFFFFF));
        canvas.drawHBBuffer(buffer, 0, 0, skFont, paint);

        canvas.restore();
        canvas.translate(0, -extents.ascender + extents.descender + extents.lineGap + 10);

        paint.release();
        skFont.release();
        buffer.release();
        hbFont.release();
    }
}