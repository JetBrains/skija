package org.jetbrains.skija.examples.bitmap;

import org.jetbrains.skija.*;

public class RenderToBitmap {
    public static void main(String [] args) throws Exception {
        var surface = Surface.makeRasterN32Premul(640, 360);
        var canvas = surface.getCanvas();

        canvas.clear(0xFFFFFFFF);

        canvas.drawTriangles(new Point[] {
                               new Point(320, 70),
                               new Point(194, 287),
                               new Point(446, 287)
                             },
                             new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF },
                             new Paint());
        
        Path path = new Path().moveTo(253, 216)
                      .cubicTo(283, 163.5f, 358, 163.5f, 388, 216)
                      .cubicTo(358, 268.5f, 283, 268.5f, 253, 216)
                      .closePath();
        canvas.drawPath(path, new Paint().setColor(0xFFFFFFFF));

        canvas.drawCircle(320, 217, 16, new Paint().setColor(0xFF000000));

        byte[] pngBytes = surface.makeImageSnapshot().encodeToData().getBytes();
        java.nio.file.Files.write(java.nio.file.Path.of("output.png"), pngBytes);
    }
}
