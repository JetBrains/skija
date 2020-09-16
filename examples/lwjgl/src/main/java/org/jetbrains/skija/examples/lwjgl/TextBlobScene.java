package org.jetbrains.skija.examples.lwjgl;

import java.util.Arrays;
import org.jetbrains.skija.*;

public class TextBlobScene implements Scene {
    private Font font;
    private float gap = 40;

    public TextBlobScene() {
        var face = Typeface.makeFromFile("fonts/Inter-Regular.ttf");
        font = new Font(face, 24);
    }
    
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(gap, gap);

        drawPosH(canvas);
        drawPos(canvas);
        drawRSXform(canvas);
        drawBounds(canvas);
    }

    public void drawPosH(Canvas canvas) {
        short[] glyphs = font.getStringGlyphIds("Fibonacci numbers");
        float[] widths = font.getWidths(glyphs);
        float[] xpos = new float[glyphs.length];
        float distance = 0, a = 0, b = 1;
        for (int i = 0; i < xpos.length; ++i) {
            xpos[i] = distance;
            distance += widths[i] + a;
            b = a + b;
            a = b - a;
        }

        try (Paint fill = new Paint().setColor(0xff6a040f)) {
            canvas.drawTextBlob(TextBlob.makeFromPosH(glyphs, xpos, 20, font), 0, 0, font, fill);
        }

        canvas.translate(0, gap);
    }

    public void drawPos(Canvas canvas) {
        short[] glyphs = font.getStringGlyphIds("Sine Cosine Tangent Cotangent");
        float[] widths = font.getWidths(glyphs);
        float distance = 0;

        Point[] pos = new Point[glyphs.length];
        float offset = System.currentTimeMillis() % 1000 / 1000f * 2 * (float) Math.PI;

        for (int i = 0; i < pos.length; ++i) {
            pos[i] = new Point(distance, (float) Math.sin(distance + offset) * 3);
            distance += widths[i];
        }

        try (Paint fill = new Paint().setColor(0xffdc2f02)) {
            canvas.drawTextBlob(TextBlob.makeFromPos(glyphs, pos, font), 0, 20, font, fill);
        }

        canvas.translate(0, gap);
    }

    public void drawRSXform(Canvas canvas) {
        short[] glyphs = font.getStringGlyphIds("Circular Text");
        float[] widths = font.getWidths(glyphs);
        RSXform[] xforms = new RSXform[glyphs.length];

        float radius = 50;
        int period = 3000;

        try (Path path = new Path().addCircle(0, 0, radius);
             PathMeasure measure = new PathMeasure(path);
             Paint fill = new Paint().setColor(0xffffba08);
             Paint stroke = new Paint().setColor(0xff3a86ff).setMode(PaintMode.STROKE).setStrokeWidth(1f);)
        {
            float length = measure.getLength();
            float relativeOffset = System.currentTimeMillis() % period / (float) period;
            float distance = relativeOffset * length;

            for (int i=0; i < xforms.length; ++i) {
                // Point p = measure.getPosition(distance);
                // Point t = measure.getTangent(distance);
                // xforms[i] = new RSXform(t.getX(), t.getY(), p.getX(), p.getY());
                xforms[i] = measure.getRSXform(distance);
                
                distance += widths[i];
                while (distance > length)
                    distance -= length;
            }

            canvas.save();
            canvas.translate(radius + gap, radius + gap);
            canvas.drawPath(path, stroke);
            canvas.drawTextBlob(TextBlob.makeFromRSXform(glyphs, xforms, font), 0, 0, font, fill);
            canvas.restore();
        }

        canvas.translate(0, (radius + gap) * 2);
    }

    public void drawBounds(Canvas canvas) {
        float percent = Math.abs((System.currentTimeMillis() % 10000) / 33f - 150f) - 25f;
        percent = Math.round(Math.max(0f, Math.min(100f, percent)));
        float width = 100 + percent * 3;
        int color = 0xFF454A6F;

        try (var blob   = font.shape("This TextBlob here is going to span multiple lines. If it doesn’t, please contact application administrator", width);
             var bg     = new Paint().setColor(Color.withAlpha(color, 32));
             var stroke = new Paint().setColor(Color.withAlpha(color, 64)).setMode(PaintMode.STROKE).setStrokeWidth(1);
             var fill   = new Paint().setColor(color);)
        {
            // text bounds
            Rect bounds = blob.getBounds();
            canvas.drawRect(bounds, bg);

            // container bounds
            canvas.drawRect(Rect.makeLTRB(0, 0, width, bounds.getBottom()), stroke);

            // text
            canvas.drawTextBlob(blob, 0, 0, font, fill);

            // intercepts
            float lower = 32;
            float upper = 56;
            float[] intercepts = blob.getIntercepts(lower, upper, fill);
            canvas.drawLine(0, lower, width, lower, stroke);
            canvas.drawLine(0, upper, width, upper, stroke);
            for (int i = 0; i < intercepts.length; i += 2) {
                canvas.drawRect(Rect.makeLTRB(intercepts[i], lower, intercepts[i+1], upper), bg);
            }

            canvas.translate(0, bounds.getHeight() + gap);

            // serialize
            canvas.drawTextBlob(TextBlob.makeFromData(blob.serializeToData()), 0, 0, font, fill);
            canvas.translate(0, bounds.getHeight() + gap);
        }
    }
}