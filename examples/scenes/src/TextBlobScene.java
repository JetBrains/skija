package org.jetbrains.skija.examples.scenes;

import java.util.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

public class TextBlobScene extends Scene {
    private Font font;
    private float gap = 40;

    public TextBlobScene() {
        font = new Font(inter, 24);
    }
    
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(gap, gap);

        drawPosH(canvas);
        drawPos(canvas);
        drawRSXform(canvas);
        drawBuilder(canvas);
        drawBounds(canvas);
    }

    public void drawPosH(Canvas canvas) {
        short[] glyphs = font.getStringGlyphs("Fibonacci numbers");
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
            canvas.drawTextBlob(TextBlob.makeFromPosH(glyphs, xpos, 20, font), 0, 0, fill);
        }

        canvas.translate(0, gap);
    }

    public void drawPos(Canvas canvas) {
        short[] glyphs = font.getStringGlyphs("Sine Cosine Tangent Cotangent");
        float[] widths = font.getWidths(glyphs);
        float distance = 0;

        Point[] pos = new Point[glyphs.length];
        float offset = phase() * 2 * (float) Math.PI;

        for (int i = 0; i < pos.length; ++i) {
            pos[i] = new Point(distance, (float) Math.sin(distance + offset) * 3);
            distance += widths[i];
        }

        try (Paint fill = new Paint().setColor(0xffdc2f02)) {
            canvas.drawTextBlob(TextBlob.makeFromPos(glyphs, pos, font), 0, 20, fill);
        }

        canvas.translate(0, gap);
    }

    public void drawRSXform(Canvas canvas) {
        short[] glyphs = font.getStringGlyphs("Circular Text");
        float[] widths = font.getWidths(glyphs);
        RSXform[] xforms = new RSXform[glyphs.length];

        float radius = 50;

        try (Path path = new Path().addCircle(0, 0, radius);
             PathMeasure measure = new PathMeasure(path);
             Paint fill = new Paint().setColor(0xffffba08);
             Paint stroke = new Paint().setColor(0xff3a86ff).setMode(PaintMode.STROKE).setStrokeWidth(1f);)
        {
            float length = measure.getLength();
            float distance = phase() * length;

            for (int i=0; i < xforms.length; ++i) {
                float w = widths[i];
                distance += w / 2;
                while (distance > length)
                    distance -= length;
                Point p = measure.getPosition(distance);
                Point t = measure.getTangent(distance);
                xforms[i] = new RSXform(t.getX(), t.getY(), p.getX() - w / 2f  * t.getX(), p.getY() - w / 2f  * t.getY());
                distance += w / 2;
            }

            canvas.save();
            canvas.translate(radius + gap, radius + gap);
            canvas.drawPath(path, stroke);
            canvas.drawTextBlob(TextBlob.makeFromRSXform(glyphs, xforms, font), 0, 0, fill);
            canvas.restore();
        }

        canvas.translate(0, (radius + gap) * 2);
    }

    public void drawBuilder(Canvas canvas) {
        try (var builder = new TextBlobBuilder();) {
            builder.appendRun(font, "appendRun", 0, 0, Rect.makeXYWH(0, 0, 100, 20));

            short[] glyphs = font.getStringGlyphs("appendRunPosH");
            float[] posX = new float[glyphs.length];
            for (int i = 0; i < posX.length; ++i)
                posX[i] = i * 20; 
            builder.appendRunPosH(font, glyphs, posX, gap);

            glyphs = font.getStringGlyphs("appendRunPos");
            Point[] pos = new Point[glyphs.length];
            for (int i = 0; i < pos.length; ++i)
                pos[i] = new Point(i * 20, 2 * gap + (float) Math.sin(i) * 5f); 
            builder.appendRunPos(font, glyphs, pos);

            glyphs = font.getStringGlyphs("appendRSXform");
            RSXform[] xform = new RSXform[glyphs.length];
            for (int i = 0; i < xform.length; ++i) {
                double angle = i * Math.PI / xform.length;
                xform[i] = new RSXform((float) Math.cos(angle), (float) Math.sin(angle), i * 20, 3 * gap + (float) Math.sin(i) * 5f); 
            }
            builder.appendRunRSXform(font, glyphs, xform);

            try (var blob = builder.build();
                 var fill = new Paint().setColor(0xff454a6f);) {
                canvas.drawTextBlob(blob, 0, 0, fill);
            }
        }

        canvas.translate(0, 4 * gap);
    }

    public void drawBounds(Canvas canvas) {
        float width = 100 + phase() * 300;
        int color = 0xFF454A6F;

        try (var shaper = Shaper.make();
             var blob   = shaper.shape("This TextBlob here is going to span multiple lines. If it doesn’t, please contact application administrator", font, width);
             var bg     = new Paint().setColor(Color.withA(color, 32));
             var underline = new Paint().setColor(Color.withA(color, 128));
             var stroke = new Paint().setColor(Color.withA(color, 64)).setMode(PaintMode.STROKE).setStrokeWidth(1);
             var fill   = new Paint().setColor(color);)
        {
            // text bounds
            Rect bounds = blob.getBounds();
            canvas.drawRect(bounds, bg);

            // container bounds
            canvas.drawRect(Rect.makeLTRB(0, 0, width, bounds.getBottom()), stroke);

            // text
            canvas.drawTextBlob(blob, 0, 0, fill);

            // underline
            float lower = 54;
            float upper = 56;
            float[] intercepts = blob.getIntercepts(lower, upper, fill);
            float start = 0;
            float end = width;
            int i = 0;
            while (i < intercepts.length) {
                if (start < intercepts[i] - 2)
                    canvas.drawRect(Rect.makeLTRB(start, lower, intercepts[i] - 2, upper), underline);
                i++;
                start = intercepts[i] + 2;
                i++;
            }
            canvas.drawRect(Rect.makeLTRB(start, lower, end, upper), underline);

            canvas.translate(0, bounds.getHeight() + gap);

            // serialize
            canvas.drawTextBlob(TextBlob.makeFromData(blob.serializeToData()), 0, 0, fill);
            canvas.translate(0, bounds.getHeight() + gap);
        }
    }
}