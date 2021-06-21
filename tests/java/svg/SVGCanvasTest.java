package org.jetbrains.skija.test.svg;

import static org.jetbrains.skija.test.runner.TestRunner.*;

import java.io.*;
import java.nio.charset.*;

import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.OutputWStream;
import org.jetbrains.skija.Paint;
import org.jetbrains.skija.PaintMode;
import org.jetbrains.skija.Path;
import org.jetbrains.skija.Rect;
import org.jetbrains.skija.test.*;
import org.jetbrains.skija.test.runner.*;
import org.jetbrains.skija.svg.SVGCanvas;

public class SVGCanvasTest implements Executable {
    @Override
    public void execute() throws Exception {
        try (var bout = new ByteArrayOutputStream();
             var wout = new OutputWStream(bout);)
        {
            var canvas = SVGCanvas.make(Rect.makeLTRB(0, 0, 200, 200), wout);
            draw(canvas);
            canvas.close();
            String expected = ("<?xml version='1.0' encoding='utf-8' ?>\n" + 
                               "<svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' width='200' height='200'>\n" + 
                               "	<ellipse fill='#1C4' fill-opacity='0.25098041' cx='100' cy='100' rx='100' ry='100'/>\n" + 
                               "	<rect fill='none' stroke='#C34' stroke-width='2' stroke-miterlimit='4' transform='translate(10 20)' x='10' y='20' width='30' height='40'/>\n" + 
                               "	<path fill='white' stroke='white' stroke-width='2' stroke-miterlimit='4' transform='matrix(0 1 -1 0 10 20)' d='M0 0L50 60L60 70L70 50L0 0Z'/>\n" + 
                               "	<path fill='none' stroke='black' stroke-width='2' stroke-miterlimit='4' stroke-opacity='0.1254902' d='M0 0L200 200'/>\n" + 
                               "</svg>\n").replaceAll("'", "\"");
            String svg = bout.toString(StandardCharsets.UTF_8);
            assertEquals(expected, svg);
        }

        try (var bout = new ByteArrayOutputStream();
             var wout = new OutputWStream(bout);)
        {
            var canvas = SVGCanvas.make(Rect.makeLTRB(0, 0, 200, 200), wout, false, false);
            draw(canvas);
            canvas.close();
            String expected = ("<?xml version='1.0' encoding='utf-8' ?>" + 
                               "<svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' width='200' height='200'>" + 
                               "<ellipse fill='#1C4' fill-opacity='0.25098041' cx='100' cy='100' rx='100' ry='100'/>" + 
                               "<rect fill='none' stroke='#C34' stroke-width='2' stroke-miterlimit='4' transform='translate(10 20)' x='10' y='20' width='30' height='40'/>" + 
                               "<path fill='white' stroke='white' stroke-width='2' stroke-miterlimit='4' transform='matrix(0 1 -1 0 10 20)' d='M0 0L50 60L60 70L70 50L0 0Z'/>" + 
                               "<path fill='none' stroke='black' stroke-width='2' stroke-miterlimit='4' stroke-opacity='0.1254902' d='M0 0L200 200'/>" + 
                               "</svg>").replaceAll("'", "\"");
            String svg = bout.toString(StandardCharsets.UTF_8);
            assertEquals(expected, svg);
        }
    }

    public void draw(Canvas canvas) {
        try (var paint = new Paint()) {
            
            paint.setColor(0x4011cc44);
            canvas.drawCircle(100, 100, 100, paint);

            canvas.save();
            canvas.translate(10, 20);
            paint.setColor(0xFFCC3344).setMode(PaintMode.STROKE).setStrokeWidth(2);
            canvas.drawRect(Rect.makeXYWH(10, 20, 30, 40), paint);

            canvas.rotate(90);
            paint.setColor(0xFFFFFFFF).setMode(PaintMode.STROKE_AND_FILL);
            canvas.drawPath(new Path().lineTo(50, 60).lineTo(60, 70).lineTo(70, 50).closePath(), paint);
            canvas.restore();

            paint.setColor(0x20000000).setMode(PaintMode.FILL);
            canvas.drawLine(0, 0, 200, 200, paint);

            // not supported
            Path path = new Path().moveTo(253, 216)
                          .cubicTo(283, 163.5f, 358, 163.5f, 388, 216)
                          .cubicTo(358, 268.5f, 283, 268.5f, 253, 216)
                          .closePath();
            canvas.drawPath(path, paint);
        }
    }
}