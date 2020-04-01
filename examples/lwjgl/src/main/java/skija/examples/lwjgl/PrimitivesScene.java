package skija.examples.lwjgl;

import skija.*;

public class PrimitivesScene implements Scene {
    public void draw(Canvas canvas, int width, int height, int xpos, int ypos) {
        var borderStroke = new Paint().setColor(0xFFFF0751).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
        canvas.drawRectInscribed(RectInscribed.rect(10, 10, width - 20, height - 20), borderStroke);
        canvas.translate(30, 30);
        
        drawPoints(canvas);
        drawLines(canvas);
        drawArcs(canvas);
        drawRotate(canvas);
        drawRectInscribed(canvas, new Paint().setColor(0xFF1D7AA2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f));
        drawRectInscribed(canvas, new Paint().setColor(0xFF6DC1B3).setStyle(Paint.Style.STROKE).setStrokeWidth(5f));
        drawRectInscribed(canvas, new Paint().setColor(0xFFB1DCBE));
    }

    public void drawPoints(Canvas canvas) {
        var strokeHalfPx = new Paint().setColor(0xFF000000).setStrokeWidth(0.5f);
        var stroke1px = new Paint().setColor(0xFF000000).setStrokeWidth(1f);
        var stroke2px = new Paint().setColor(0xFF000000).setStrokeWidth(2f);
        var stroke5px = new Paint().setColor(0xFF000000).setStrokeWidth(5f);
        var stroke5pxRound = new Paint().setColor(0xFF000000).setStrokeWidth(5f).setStrokeCap(Paint.Cap.ROUND);
        var stroke5pxSquare = new Paint().setColor(0xFF000000).setStrokeWidth(5f).setStrokeCap(Paint.Cap.SQUARE);

        canvas.save();
        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, strokeHalfPx);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0.5f, 30.5f, 10.5f, 10.5f, 20.5f, 20.5f, 30.5f, 0.5f }, strokeHalfPx);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke1px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke2px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5pxRound);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POINTS, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5pxSquare);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.LINES, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, strokeHalfPx);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke1px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke2px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5px);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5pxRound);
        canvas.translate(40, 0);

        canvas.drawPoints(Canvas.PointMode.POLYGON, new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, stroke5pxSquare);
        canvas.translate(40, 0);

        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawLines(Canvas canvas) {
        var stroke1px = new Paint().setColor(0xFF000000).setStrokeWidth(1f);
        var stroke5px = new Paint().setColor(0xFF000000).setStrokeWidth(5f);
        var stroke5pxRound = new Paint().setColor(0xFF000000).setStrokeWidth(5f).setStrokeCap(Paint.Cap.ROUND);
        var stroke5pxSquare = new Paint().setColor(0xFF000000).setStrokeWidth(5f).setStrokeCap(Paint.Cap.SQUARE);

        canvas.save();

        canvas.drawLine(0, 0, 0, 30, stroke1px);
        canvas.drawLine(10, 0, 10, 30, stroke5px);
        canvas.drawLine(20, 0, 20, 30, stroke5pxRound);
        canvas.drawLine(30, 0, 30, 30, stroke5pxSquare);

        canvas.drawLine(40, 0, 70, 0, stroke1px);
        canvas.drawLine(40, 10, 70, 10, stroke5px);
        canvas.drawLine(40, 20, 70, 20, stroke5pxRound);
        canvas.drawLine(40, 30, 70, 30, stroke5pxSquare);

        canvas.drawLine(80, 0, 110, 30, stroke1px);
        canvas.drawLine(100, 0, 130, 30, stroke5px);
        canvas.drawLine(120, 0, 150, 30, stroke5pxRound);
        canvas.drawLine(140, 0, 170, 30, stroke5pxSquare);

        canvas.drawLine(160, 0, 190, 30, stroke1px.setAntiAlias(false));
        canvas.drawLine(180, 0, 210, 30, stroke5px.setAntiAlias(false));
        canvas.drawLine(200, 0, 230, 30, stroke5pxRound.setAntiAlias(false));
        canvas.drawLine(220, 0, 250, 30, stroke5pxSquare.setAntiAlias(false));

        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawRotate(Canvas canvas) {
        var stroke1px = new Paint().setColor(0xFF000000).setStrokeWidth(1f);
        canvas.save();
        canvas.translate(20, 20);
        for (float i=0; i < 360; i += 30) {
            canvas.save();
            canvas.rotate(i);
            canvas.drawLine(5, 0, 20, 0, stroke1px);
            canvas.restore();    
        }
        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawArcs(Canvas canvas) {
        var stroke1px = new Paint().setColor(0xFF000000).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
        var fill = new Paint().setColor(0xFFFC7901);

        canvas.save();
        for (var angle = 60f; angle <= 360f; angle += 60) {
            canvas.drawArc(0, 0, 40, 40, 0, angle, false, stroke1px);
            canvas.translate(50, 0);
        }

        for (var angle = 60f; angle <= 360f; angle += 60) {
            canvas.drawArc(0, 0, 40, 40, 0, -angle, true, stroke1px);
            canvas.translate(50, 0);
        }
        canvas.restore();
        canvas.translate(0, 50);

        canvas.save();
        for (var angle = 30f; angle <= 180f; angle += 30) {
            canvas.drawArc(0, 0, 40, 40, angle, -angle * 2, false, fill);
            canvas.translate(50, 0);
        }

        for (var angle = 30f; angle <= 180f; angle += 30) {
            canvas.drawArc(0, 0, 40, 40, -angle, angle * 2, true, fill);
            canvas.translate(50, 0);
        }
        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawRectInscribed(Canvas canvas, Paint paint) {
        canvas.save();

        canvas.drawRectInscribed(RectInscribed.rect(0, 0, 65, 40), paint);
        canvas.translate(75, 0);

        canvas.drawRectInscribed(RectInscribed.oval(0, 0, 65, 40), paint);
        canvas.translate(75, 0);

        canvas.drawRectInscribed(RectInscribed.roundedRect(0, 0, 65, 40, 10), paint);
        canvas.translate(75, 0);

        canvas.drawRectInscribed(RectInscribed.roundedRect(0, 0, 65, 40, 4, 8, 12, 16), paint);
        canvas.translate(75, 0);

        canvas.drawRectInscribed(RectInscribed.ninePatch(0, 0, 65, 40, 4, 8, 12, 16), paint);
        canvas.translate(75, 0);

        canvas.drawRectInscribed(RectInscribed.complex(0, 0, 65, 40, new float[] {2, 4, 6, 8, 10, 12, 14, 16}), paint);
        canvas.translate(75, 0);        

        canvas.restore();
        canvas.translate(0, 50);
    }
}
