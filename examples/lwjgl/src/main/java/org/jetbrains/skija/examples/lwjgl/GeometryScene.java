package org.jetbrains.skija.examples.lwjgl;

import org.jetbrains.skija.*;

public class GeometryScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var borderStroke = new Paint().setColor(0xFFFF0751).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
        canvas.drawRect(Rect.makeLTRB(10, 10, width - 10, height - 10), borderStroke);
        canvas.translate(30, 30);

        drawPoints(canvas);
        drawLines(canvas);
        drawArcs(canvas);
        drawRotate(canvas);
        drawSkew(canvas);
        drawRectInscribed(canvas, new Paint().setColor(0xFF1D7AA2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f));
        drawRectInscribed(canvas, new Paint().setColor(0xFF6DC1B3).setStyle(Paint.Style.STROKE).setStrokeWidth(5f));
        drawRectInscribed(canvas, new Paint().setColor(0xFF9BC730));
        drawClips(canvas);
        drawRegions(canvas, dpi);
    }

    public void drawPoints(Canvas canvas) {
        var strokeHalfPx = new Paint().setStrokeWidth(0.5f);
        var stroke1px = new Paint().setStrokeWidth(1f);
        var stroke2px = new Paint().setStrokeWidth(2f);
        var stroke5px = new Paint().setStrokeWidth(5f);
        var stroke5pxRound = new Paint().setStrokeWidth(5f).setStrokeCap(Paint.Cap.ROUND);
        var stroke5pxSquare = new Paint().setStrokeWidth(5f).setStyle(Paint.Style.STROKE).setStrokeCap(Paint.Cap.SQUARE);

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

        canvas.drawPath(new Path().addPoly(new float[] { 0, 30, 10, 10, 20, 20, 30, 0 }, false), stroke5pxSquare);
        canvas.translate(40, 0);

        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawLines(Canvas canvas) {
        var stroke1px = new Paint().setStrokeWidth(1f);
        var stroke5px = new Paint().setStrokeWidth(5f);
        var stroke5pxRound = new Paint().setStrokeWidth(5f).setStrokeCap(Paint.Cap.ROUND);
        var stroke5pxSquare = new Paint().setStrokeWidth(5f).setStrokeCap(Paint.Cap.SQUARE);

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
        var stroke1px = new Paint().setStrokeWidth(1f);
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

    public void drawSkew(Canvas canvas) {
        Paint paint = new Paint().setColor(0xFFFFCC66);

        canvas.save();

        canvas.save();
        canvas.skew(0.3F, 0);
        canvas.drawRect(Rect.makeXYWH(0, 0, 65, 40), paint);
        canvas.restore();
        canvas.translate(90, 0);

        canvas.save();
        canvas.skew(0F, 0.1F);
        canvas.drawRect(Rect.makeXYWH(0, 0, 65, 40), paint);
        canvas.restore();
        canvas.translate(80, 0);

        canvas.save();
        canvas.skew(-0.2F, 0F);
        canvas.drawOval(Rect.makeXYWH(0, 0, 65, 40), paint);
        canvas.restore();
        canvas.translate(75, 0);

        canvas.restore();
        canvas.translate(0, 60);
    }

    public void drawArcs(Canvas canvas) {
        var stroke1px = new Paint().setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
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
            canvas.drawArc(0, 0, 40, 40, 180 - angle, angle * 2, true, fill);
            canvas.translate(50, 0);
        }
        canvas.restore();
        canvas.translate(0, 50);
    }

    public void drawRectInscribed(Canvas canvas, Paint paint) {
        canvas.save();

        canvas.drawRect(Rect.makeXYWH(0, 0, 65, 40), paint);
        canvas.translate(75, 0);

        canvas.drawOval(Rect.makeXYWH(0, 0, 65, 40), paint);
        canvas.translate(75, 0);

        canvas.drawCircle(20, 20, 20, paint);
        canvas.translate(50, 0);

        canvas.drawRRect(RRect.makeXYWH(0, 0, 65, 40, 10), paint);
        canvas.translate(75, 0);

        canvas.drawRRect(RRect.makeXYWH(0, 0, 65, 40, 10, 20), paint);
        canvas.translate(75, 0);

        canvas.drawRRect(RRect.makeXYWH(0, 0, 65, 40, 4, 8, 12, 16), paint);
        canvas.translate(75, 0);

        canvas.drawRRect(RRect.makeNinePatchXYWH(0, 0, 65, 40, 4, 8, 12, 16), paint);
        canvas.translate(75, 0);

        canvas.drawRRect(RRect.makeComplexXYWH(0, 0, 65, 40, new float[] {2, 4, 6, 8, 10, 12, 14, 16}), paint);
        canvas.translate(75, 0);

        canvas.drawDRRect(RRect.makeXYWH(0, 0, 65, 40, 0), RRect.makeXYWH(10, 10, 45, 20, 20, 10), paint);
        canvas.translate(75, 0);

        canvas.drawDRRect(RRect.makeXYWH(0, 0, 65, 40, 10), RRect.makeXYWH(5, 5, 55, 30, 5, 5), paint);
        canvas.translate(75, 0);

        canvas.restore();
        canvas.translate(0, 50);
    }

    private void drawClips(Canvas canvas) {
        canvas.save();

        canvas.save();
        canvas.clipRect(Rect.makeXYWH(0, 0, 40, 40));
        canvas.drawPaint(new Paint().setColor(0xFF50514F));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        canvas.clipRect(Rect.makeXYWH(0, 0, 30, 30));
        canvas.clipRect(Rect.makeXYWH(10, 10, 30, 30), Canvas.ClipOp.INTERSECT);
        canvas.drawPaint(new Paint().setColor(0xFFF55E58));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        canvas.clipRect(Rect.makeXYWH(0, 0, 30, 30));
        canvas.clipRect(Rect.makeXYWH(10, 10, 30, 30), Canvas.ClipOp.DIFFERENCE);
        canvas.drawPaint(new Paint().setColor(0xFFFFE15C));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        canvas.clipRect(Rect.makeXYWH(0, 0, 40, 40));
        canvas.translate(20, 20);
        canvas.rotate(15);
        canvas.clipRect(Rect.makeXYWH(-15, -15, 30, 30), Canvas.ClipOp.DIFFERENCE, false);
        canvas.drawPaint(new Paint().setColor(0xFF1D7AA2));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        canvas.clipRect(Rect.makeXYWH(0, 0, 40, 40));
        canvas.translate(20, 20);
        canvas.rotate(15);
        canvas.clipRect(Rect.makeXYWH(-15, -15, 30, 30), Canvas.ClipOp.DIFFERENCE, true);
        canvas.drawPaint(new Paint().setColor(0xFF6DC1B3));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        canvas.clipRRect(RRect.makeXYWH(0, 0, 40, 40, 10), false);
        canvas.clipRRect(RRect.makePillXYWH(10, 10, 30, 20), Canvas.ClipOp.DIFFERENCE, true);
        canvas.drawPaint(new Paint().setColor(0xFFFF928A));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.save();
        Path path = new Path()
            .moveTo(0, 12.5f).rCubicTo(0, -5f, 0, -7.5f, 2.5f, -10f).rCubicTo(2.5f, -2.5f, 5f, -2.5f, 10f, -2.5f)
            .rLineTo(15f, 0).rCubicTo(5, 0, 7.5f, 0, 10, 2.5f).rCubicTo(2.5f, 2.5f, 2.5f, 5f, 2.5f, 10f)
            .lineTo(40, 22f).arcTo(Rect.makeLTRB(22f, 22f, 40, 40), 0, 90, false)
            .lineTo(18f, 40).arcTo(Rect.makeLTRB(0, 22f, 18f, 40), 90, 90, false)
            .closePath();
        canvas.clipPath(path, true);
        canvas.drawPaint(new Paint().setColor(0xFF3F80A7));
        canvas.restore();
        canvas.translate(50, 0);

        canvas.restore();
        canvas.translate(0, 50);
    }

    private void drawRegions(Canvas canvas, float dpi) {
        Paint bgPaint = new Paint().setColor(0xFFEBD3AA);
        Paint stroke1 = new Paint().setColor(0xFF3F80A7).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
        Paint stroke2 = new Paint().setColor(0xFFF55E58).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);
        int xOffset = 30;
        int yOffset = 480;

        canvas.save();

        // setRect
        Region r = new Region();
        r.setRect(IRect.makeLTRB((int) (xOffset * dpi), (int) (yOffset * dpi), (int) ((xOffset + 40) * dpi), (int) ((yOffset + 40) * dpi)));
        canvas.save();
        canvas.clipRegion(r);
        canvas.drawPaint(bgPaint);
        canvas.restore();
        canvas.translate(50, 0);
        xOffset += 50;

        // setRects
        r.setRects(new IRect[] { IRect.makeLTRB((int) ((xOffset + 0) * dpi), (int) ((yOffset + 0) * dpi), (int) ((xOffset + 10) * dpi), (int) ((yOffset + 40) * dpi)),
                                 IRect.makeLTRB((int) ((xOffset + 30) * dpi), (int) ((yOffset + 0) * dpi), (int) ((xOffset + 40) * dpi), (int) ((yOffset + 40) * dpi)),
                                 IRect.makeLTRB((int) ((xOffset + 5) * dpi), (int) ((yOffset + 15) * dpi), (int) ((xOffset + 35) * dpi), (int) ((yOffset + 25) * dpi)) });
        canvas.save();
        canvas.clipRegion(r);
        canvas.drawPaint(bgPaint);
        canvas.restore();
        canvas.translate(50, 0);
        xOffset += 50;

        // setPath
        Path path = new Path().setFillType(Path.FillType.EVEN_ODD).moveTo(xOffset * dpi, yOffset * dpi)
            .rMoveTo(20 * dpi, 1.6f * dpi).rLineTo(11.7f * dpi, 36.2f * dpi).rLineTo(-30.8f * dpi, -22.4f * dpi).rLineTo(38.1f * dpi, 0f * dpi).rLineTo(-30.8f * dpi, 22.4f * dpi).closePath();
        Region r2 = new Region();
        r2.setRect(IRect.makeLTRB((int) ((xOffset + 7) * dpi), (int) ((yOffset + 7) * dpi), (int) ((xOffset + 33) * dpi), (int) ((yOffset + 33) * dpi)));
        r.setEmpty();
        r.setPath(path, r2);
        canvas.save();
        canvas.clipRegion(r);
        canvas.drawPaint(bgPaint);
        canvas.restore();
        canvas.translate(50, 0);
        xOffset += 50;

        // op(IRect), getBounds, getBoundaryPath
        for (var op : Region.Op.values()) {
            r.setRect(IRect.makeLTRB((int) (xOffset * dpi), (int) (yOffset * dpi), (int) ((xOffset + 30) * dpi), (int) ((yOffset + 40) * dpi)));
            r.op(IRect.makeLTRB((int) ((xOffset + 10) * dpi), (int) ((yOffset + 10) * dpi), (int) ((xOffset + 40) * dpi), (int) ((yOffset + 30) * dpi)), op);

            canvas.save();
            canvas.clipRegion(r);
            canvas.drawPaint(bgPaint);
            canvas.restore();

            IRect bounds = r.getBounds();
            Path boundaryPath = new Path();
            r.getBoundaryPath(boundaryPath);
            canvas.save();
            canvas.translate(-xOffset, -yOffset);
            canvas.scale(1f/dpi, 1f/dpi);
            canvas.drawRect(Rect.makeLTRB(bounds.getLeft(), bounds.getTop(), bounds.getRight(), bounds.getBottom()), stroke1);
            canvas.drawPath(boundaryPath, stroke2);
            canvas.restore();

            for (int x = 5; x < 40; x += 10) {
                for (int y = 5; y < 40; y += 10) {
                    var absX = (xOffset + x) * dpi;
                    var absY = (yOffset + y) * dpi;
                    if (r.contains((int) absX, (int) absY))
                        canvas.drawPoint(x, y, stroke1);
                }
            }

            canvas.translate(50, 0);
            xOffset += 50;
        }

        // op(Region, Region), getBounds, getBoundaryPath
        for (var op : Region.Op.values()) {
            Region rA = new Region();
            rA.setRect(IRect.makeLTRB((int) (xOffset * dpi), (int) (yOffset * dpi), (int) ((xOffset + 30) * dpi), (int) ((yOffset + 40) * dpi)));
            Region rB = new Region();
            rB.setRect(IRect.makeLTRB((int) ((xOffset + 10) * dpi), (int) ((yOffset + 10) * dpi), (int) ((xOffset + 40) * dpi), (int) ((yOffset + 30) * dpi)));
            r.op(rA, rB, op);

            canvas.save();
            canvas.clipRegion(r);
            canvas.drawPaint(bgPaint);
            canvas.restore();

            IRect bounds = r.getBounds();
            Path boundaryPath = new Path();
            r.getBoundaryPath(boundaryPath);
            canvas.save();
            canvas.translate(-xOffset, -yOffset);
            canvas.scale(1f/dpi, 1f/dpi);
            canvas.drawRect(Rect.makeLTRB(bounds.getLeft(), bounds.getTop(), bounds.getRight(), bounds.getBottom()), stroke1);
            canvas.drawPath(boundaryPath, stroke2);
            canvas.restore();

            for (int x = 5; x < 40; x += 10) {
                for (int y = 5; y < 40; y += 10) {
                    var absX = (xOffset + x) * dpi;
                    var absY = (yOffset + y) * dpi;
                    if (r.contains((int) absX, (int) absY))
                        canvas.drawPoint(x, y, stroke1);
                }
            }

            canvas.translate(50, 0);
            xOffset += 50;
        }

        canvas.restore();
        canvas.translate(0, 50);
    }

}
