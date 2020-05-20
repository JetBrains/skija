package org.jetbrains.skija.examples.lwjgl;

import java.util.Iterator;
import org.jetbrains.skija.*;

public class PathsScene implements Scene {
    public PathsScene() {
        testIter();
    }

    public void testIter() {
        try (Path p = new Path().moveTo(10, 10).lineTo(20, 0).lineTo(20, 20).closePath();
             var i = p.iterator();) {
            assert i.hasNext();
            Path.Segment s = i.next();
            assert s.verb == Path.Verb.MOVE : s;
            assert s.p0.equals(new Point(10, 10)) : s;
            assert s.isClosedContour;

            assert i.hasNext();
            s = i.next();
            assert s.verb == Path.Verb.LINE : s;
            assert s.p0.equals(new Point(10, 10)) : s;
            assert s.p1.equals(new Point(20, 0)) : s;
            assert !s.isCloseLine;

            assert i.hasNext();
            s = i.next();
            assert s.verb == Path.Verb.LINE : s;
            assert s.p0.equals(new Point(20, 0)) : s;
            assert s.p1.equals(new Point(20, 20)) : s;
            assert !s.isCloseLine;

            assert i.hasNext();
            s = i.next();
            assert s.verb == Path.Verb.LINE : s;
            assert s.p0.equals(new Point(20, 20)) : s;
            assert s.p1.equals(new Point(10, 10)) : s;
            assert s.isCloseLine;

            assert i.hasNext();
            s = i.next();
            assert s.verb == Path.Verb.CLOSE : s;
            assert s.p0.equals(new Point(10, 10)) : s;

            assert i.hasNext() == false;
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(30, 30);

        drawPaths(canvas, new Paint().setColor(0xFFF6BC01));
        drawPaths(canvas, new Paint().setColor(0xFF437AA0).setStyle(Paint.Style.STROKE).setStrokeWidth(1f));
        drawFillPaths(canvas);
    }

    public void drawPaths(Canvas canvas, Paint paint) {
        var tangentStroke = new Paint().setColor(0xFFFAA6B2).setStyle(Paint.Style.STROKE).setStrokeWidth(1f);

        canvas.save();

        // moveTo, lineTo, close
        Path path = new Path();
        path.setFillType(Path.FillType.WINDING);
        path.moveTo(20, 1.6f);
        path.lineTo(31.7f, 37.8f);
        path.lineTo(0.9f, 15.4f);
        path.lineTo(39f, 15.4f);
        path.lineTo(8.2f, 37.8f);
        path.closePath();
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // rMoveTo, rLineTo
        path.reset().setFillType(Path.FillType.EVEN_ODD);
        path.rMoveTo(20, 1.6f).rLineTo(11.7f, 36.2f).rLineTo(-30.8f, -22.4f).rLineTo(38.1f, 0f).rLineTo(-30.8f, 22.4f);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // quadTo, rQuadTo
        canvas.drawLine(0, 20, 20, 0, tangentStroke);
        canvas.drawLine(20, 0, 40, 20, tangentStroke);
        canvas.drawLine(0, 20, 20, 40, tangentStroke);
        canvas.drawLine(20, 40, 40, 20, tangentStroke);

        path.reset().rMoveTo(0, 20).quadTo(20, 0, 40, 20).rQuadTo(-20, 20, -40, 0);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // conicTo, rConicTo
        canvas.drawLine(0, 20, 20, 0, tangentStroke);
        canvas.drawLine(20, 0, 40, 20, tangentStroke);
        canvas.drawLine(0, 20, 20, 40, tangentStroke);
        canvas.drawLine(20, 40, 40, 20, tangentStroke);

        path.reset().rMoveTo(0, 20).conicTo(20, 0, 40, 20, 0.5f).rConicTo(-20, 20, -40, 0, 2);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // cubicTo, rCubicTo
        canvas.drawLine(0, 20, 0, 0, tangentStroke);
        canvas.drawLine(40, 0, 40, 20, tangentStroke);
        canvas.drawLine(0, 20, 10, 30, tangentStroke);
        canvas.drawLine(40, 20, 30, 30, tangentStroke);

        path.reset().rMoveTo(0, 20).cubicTo(0, 0, 40, 0, 40, 20).rCubicTo(-10, 10, -30, 10, -40, 0);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // cubic apple rounding
        path.reset().lineTo(0, 20).arcTo(Rect.makeLTRB(0, 0, 40, 40), 180, -90, false).lineTo(40, 40).lineTo(40, 0).closePath();
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        path.reset().lineTo(0, 10).cubicTo(0, 30, 10, 40, 30, 40).lineTo(40, 40).lineTo(40, 0).closePath();
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // Infinity
        canvas.translate(25, 20);
        path.reset().moveTo(0, 0); // Center
        path.cubicTo(  5,     -5,      9.5f, -10,     15, -10); // To top of right loop
        path.cubicTo( 20.5f, -10,     25,     -5.5f,  25,   0); // To far right of right loop
        path.cubicTo( 25,      5.5f,  20.5f,  10,     15,  10); // To bottom of right loop
        path.cubicTo(  9.5f,  10,      5,      5,      0,   0); // Back to center
        path.cubicTo( -5,     -5,     -9.5f, -10,    -15, -10); // To top of left loop
        path.cubicTo(-20.5f, -10,    -25,     -5.5f, -25,   0); // To far left of left loop
        path.cubicTo(-25,      5.5f, -20.5f,  10,    -15,  10); // To bottom of left loop
        path.cubicTo( -9.5f,  10,     -5,      5,      0,   0); // Back to center
        path.closePath();
        canvas.drawPath(path, paint);
        canvas.translate(35, -20);

        // arcTo
        canvas.drawOval(Rect.makeXYWH(0, 0, 40, 40), tangentStroke);
        canvas.drawLine(20, 20, 20, 0, tangentStroke);
        canvas.drawLine(20, 20, 40, 20, tangentStroke);
        path.reset().moveTo(0, 0).arcTo(Rect.makeLTRB(0, 0, 40, 40), -90, 90, false);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // arcTo
        canvas.drawOval(Rect.makeXYWH(0, 0, 40, 40), tangentStroke);
        canvas.drawLine(20, 20, 20, 0, tangentStroke);
        canvas.drawLine(20, 20, 40, 20, tangentStroke);
        path.reset().moveTo(0, 0).arcTo(Rect.makeLTRB(0, 0, 40, 40), -90, 90, true);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // tangentArcTo
        canvas.drawLine(0, 20, 20, 0, tangentStroke);
        canvas.drawLine(20, 0, 40, 20, tangentStroke);
        canvas.drawOval(Rect.makeXYWH(10, 4, 20, 20), tangentStroke);
        path.reset().moveTo(0, 20).tangentArcTo(20, 0, 40, 20, 10);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // ellipticalArcTo, getBounds
        path.reset().moveTo(0, 30).ellipticalArcTo(30, 15, 30, Path.ArcSize.SMALL, Path.Direction.CLOCKWISE, 40, 30);
        Rect bounds = path.getBounds();
        canvas.drawRect(bounds, tangentStroke);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // ellipticalArcTo, getBounds
        path.reset().moveTo(0, 30).ellipticalArcTo(30, 15, 30, Path.ArcSize.LARGE, Path.Direction.CLOCKWISE, 40, 30);
        bounds = path.getBounds();
        canvas.drawRect(bounds, tangentStroke);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // rEllipticalArcTo, getTightBounds
        path.reset().moveTo(0, 10).rEllipticalArcTo(30, 15, 30, Path.ArcSize.SMALL, Path.Direction.COUNTER_CLOCKWISE, 40, 0);
        bounds = path.computeTightBounds();
        canvas.drawRect(bounds, tangentStroke);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // rEllipticalArcTo, getTightBounds
        path.reset().moveTo(0, 10).rEllipticalArcTo(30, 15, 30, Path.ArcSize.LARGE, Path.Direction.COUNTER_CLOCKWISE, 40, 0);
        bounds = path.computeTightBounds();
        canvas.drawRect(bounds, tangentStroke);
        canvas.drawPath(path, paint);
        canvas.translate(70, 0);

        // addPoly
        path.reset().addPoly(new float[] { 40, 0, 40, 40, 30, 40, 10, 20, 10, 40, 0, 40, 0, 0, 10, 0, 30, 20, 30, 0 }, false);
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        // multi shapes
        path.reset().setFillType(Path.FillType.EVEN_ODD);
        path.arcTo(Rect.makeLTRB(0, 5, 35, 40), 0, 359, true);
        path.closePath();
        path.addPoly(new float[] { 5, 0, 35, 0, 35, 30, 5, 30 }, true);
        path.moveTo(5, 35);
        path.lineTo(20, 5);
        path.lineTo(35, 35);
        path.closePath();
        canvas.drawPath(path, paint);
        canvas.translate(50, 0);

        canvas.restore();
        canvas.translate(0, 50);
    }
    
    private void drawFillPaths(Canvas canvas) {
        canvas.save();

        try (Path path = new Path().moveTo(20, 5).lineTo(35, 35).lineTo(5, 35).closePath();
             Paint stroke1 = new Paint().setColor(0xFF247ba0).setStyle(Paint.Style.STROKE).setStrokeWidth(1))
        {
            Paint[] paints = new Paint[] {
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10),
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10).setStrokeMiter(2),
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10).setStrokeJoin(Paint.Join.ROUND),
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10).setStrokeJoin(Paint.Join.BEVEL)
            };

            for (Paint stroke5: paints) {

                canvas.drawPath(path, stroke5);
                canvas.drawPath(path, stroke1);
                canvas.translate(50, 0);

                try (Path fillPath = stroke5.getFillPath(path)) {
                    canvas.drawPath(fillPath, stroke1);
                    canvas.translate(50, 0);
                }

                try (Path fillPath = stroke5.getFillPath(path, Rect.makeLTRB(15, 15, 25, 25), 0.1f)) {
                    canvas.drawPath(fillPath, stroke1);
                    canvas.translate(50, 0);
                }

                stroke5.close();
            }
        }

        try (Path path = new Path().moveTo(5, 35).lineTo(15, 10).lineTo(25, 30).lineTo(35, 5);
             Paint stroke1 = new Paint().setColor(0xFF247ba0).setStyle(Paint.Style.STROKE).setStrokeWidth(1))
        {
            Paint[] paints = new Paint[] {
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10),
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10).setStrokeCap(Paint.Cap.ROUND),
                new Paint().setColor(0xFFffe066).setStyle(Paint.Style.STROKE).setStrokeWidth(10).setStrokeCap(Paint.Cap.SQUARE)
            };
            for (Paint stroke5: paints) {
                canvas.drawPath(path, stroke5);
                canvas.drawPath(path, stroke1);
                canvas.translate(50, 0);
                try (Path fillPath = stroke5.getFillPath(path)) {
                    canvas.drawPath(fillPath, stroke1);
                }
                canvas.translate(50, 0);
                stroke5.close();
            }
        }        

        canvas.restore();
        canvas.translate(0, 50);
    }
}
