package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import org.jetbrains.skija.*;

public class TextScene implements Scene {
    private Typeface jbMonoRegular;
    private Typeface interRegular;
    private Typeface interVariable;

    private FontAxisInfo[] axes;
    private Map<FontVariation, Typeface> interVariations = new HashMap<>();

    public TextScene() {
        jbMonoRegular = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf");
        interRegular  = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        interVariable = Typeface.makeFromFile("fonts/Inter-V.otf");
        // axes = interVariable.hbFace.getAxes();
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);

        canvas.save();
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

        float percent = Math.abs((System.currentTimeMillis() % 3000) / 10f - 150f) - 25f;
        percent = Math.round(Math.max(0f, Math.min(100f, percent)));
        // for (FontAxisInfo axis: axes) {
        //     float value = Math.round(axis.minValue + percent * 0.01f * (axis.maxValue - axis.minValue));
        //     var variation = new FontVariation(axis.tag, value);
        //     try (var typeface = interVariable.makeClone(new FontVariation[] { variation });) {
        //         drawBlob(canvas, 0xff000000, typeface, 18, "Inter Variable 18px " + axis.name + "=" + value, Float.POSITIVE_INFINITY); 
        //     }
        // }

        drawBlob(canvas, 0xFF454A6F, interRegular, 18, "This TextBlob here is going to span multiple lines. If it doesn’t, please contact application administrator", 100 + percent);
        canvas.restore();

        canvas.translate(600, 0);
        canvas.save();
        // var axis = axes[0];
        // for (float w = 0; w <= 1f; w += 0.025) {
        //     float value = Math.round(axis.minValue + w * (axis.maxValue - axis.minValue));
        //     var variation = new FontVariation(axis.tag, value);
        //     var color = ((int) value) % 100 == 0 ? 0xffcc3333 : 0xff000000;
        //     var typeface = interVariations.computeIfAbsent(variation, v -> interVariable.makeClone(new FontVariation[] { v }));
        //     drawBlob(canvas, color, typeface, 13, "Inter Variable 13px " + axis.name + "=" + value, Float.POSITIVE_INFINITY);
        // }
        canvas.restore();
    }

    private void drawBlob(Canvas canvas, int color, Typeface typeface, float size, String text, float width) {
        try (
            var font = new Font(typeface, size);
            var blob = font.shape(text, width);
            var paint = new Paint().setColor(0x10000000 | (color & 0xFFFFFF))
        ) {
            // text bounds
            Rect bounds = blob.getBounds();
            canvas.drawRect(bounds, paint);

            // container bounds
            paint.setColor(0x40000000 | (color & 0xFFFFFF)).setStyle(Paint.Style.STROKE).setStrokeWidth(1);
            canvas.drawRect(Rect.makeLTRB(0, 0, width, bounds.bottom), paint);

            // text
            paint.setColor(0xFF000000 | (color & 0xFFFFFF)).setStyle(Paint.Style.FILL);
            canvas.drawTextBlob(blob, 0, 0, font, paint);

            canvas.translate(0, bounds.getHeight());
        }
    }

    public void drawText(Canvas canvas, int color, Typeface typeface, float size, String text) {
        drawText(canvas, color, typeface, size, text, FontFeature.EMPTY);
    }

    public void drawText(Canvas canvas, int color, Typeface typeface, float size, String text, FontFeature[] features) {
        FontFeature[] fontFeatures = Arrays.copyOf(features, features.length / 2);
        FontFeature[] shapeFeatures = Arrays.copyOfRange(features, features.length / 2, features.length);
        
        try (
                Font font = new Font(typeface, size); // , fontFeatures);
                // TextBuffer buffer = font.hbFont.shape(text, shapeFeatures);
                Paint paint = new Paint().setColor(0x10000000 | (color & 0xFFFFFF))
        ) {
            // FontExtents extents = font.hbFont.getHorizontalExtents();
            // float[] advances = buffer.getAdvances();
    
            canvas.save();
            // canvas.translate(0, extents.getAscenderAbs());
    
            // bounds
            // canvas.drawRect(Rect.makeLTRB(0, -extents.getAscenderAbs(), advances[0], extents.descender), paint);
        
            // baseline
            paint.setColor(0x40000000 | (color & 0xFFFFFF)).setStrokeWidth(1);
            // canvas.drawLine(0, 0, advances[0], 0, paint);
        
            // text
            paint.setColor(0xFF000000 | (color & 0xFFFFFF));
            // canvas.drawTextBuffer(buffer, 0, 0, skFont, paint);
        
            canvas.restore();
            // canvas.translate(0, extents.getLineHeight() + 10);
        }
    }
}