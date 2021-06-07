package org.jetbrains.skija.examples.scenes;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.stream.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.svg.*;

public class SVGScalingScene extends Scene {
    public final Paint border = new Paint().setColor(0xFF3333CC).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public final Paint fill = new Paint().setColor(0xFFCC3333);

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(40, 60);

        canvas.save();
        for (var img: new String[] { "images/svg/bug_none.svg",
                                     "images/svg/bug_viewport.svg",
                                     "images/svg/bug_viewbox.svg",
                                     "images/svg/bug_viewport_viewbox.svg" })
        {
            canvas.drawString(img, 0, -20, inter13, fill);

            try (var data = Data.makeFromFileName(file(img));
                 var svg = new SVGDOM(data);)
            {
                var containerSize = svg.getContainerSize();
                if (!containerSize.isEmpty())
                    drawIcon(canvas, containerSize, svg);
                drawIcon(canvas, new Point(8, 8),   svg);
                drawIcon(canvas, new Point(16, 16), svg);
                drawIcon(canvas, new Point(32, 32), svg);
                drawIcon(canvas, new Point(64, 64), svg);
            }

            canvas.translate(40, 0);
        }
        canvas.restore();
        canvas.translate(0, 100);

        canvas.save();
        canvas.scale(3, 3);
        try (var data = Data.makeFromFileName(file("images/svg/ratio.svg"));
             var dom  = new SVGDOM(data);)
        {
            // (width > height) meet
            drawRatio(canvas, dom,   0,  0, 20, 10, SVGPreserveAspectRatioAlign.XMID_YMID, SVGPreserveAspectRatioScale.MEET);
            drawRatio(canvas, dom,  25,  0, 20, 10, SVGPreserveAspectRatioAlign.XMIN_YMID, SVGPreserveAspectRatioScale.MEET);
            drawRatio(canvas, dom,  50,  0, 20, 10, SVGPreserveAspectRatioAlign.XMAX_YMID, SVGPreserveAspectRatioScale.MEET);
            
            // (width > height) slice
            drawRatio(canvas, dom,   0, 15, 20, 10, SVGPreserveAspectRatioAlign.XMID_YMID, SVGPreserveAspectRatioScale.SLICE);
            drawRatio(canvas, dom,  25, 15, 20, 10, SVGPreserveAspectRatioAlign.XMID_YMIN, SVGPreserveAspectRatioScale.SLICE);
            drawRatio(canvas, dom,  50, 15, 20, 10, SVGPreserveAspectRatioAlign.XMID_YMAX, SVGPreserveAspectRatioScale.SLICE);

            // (width < height) meet
            drawRatio(canvas, dom,  75,  0, 10, 25, SVGPreserveAspectRatioAlign.XMID_YMIN, SVGPreserveAspectRatioScale.MEET);
            drawRatio(canvas, dom,  90,  0, 10, 25, SVGPreserveAspectRatioAlign.XMIN_YMID, SVGPreserveAspectRatioScale.MEET);
            drawRatio(canvas, dom, 105,  0, 10, 25, SVGPreserveAspectRatioAlign.XMAX_YMAX, SVGPreserveAspectRatioScale.MEET);
            
            // (width < height) slice
            drawRatio(canvas, dom, 120,  0, 10, 25, SVGPreserveAspectRatioAlign.XMIN_YMID, SVGPreserveAspectRatioScale.SLICE);
            drawRatio(canvas, dom, 135,  0, 10, 25, SVGPreserveAspectRatioAlign.XMID_YMID, SVGPreserveAspectRatioScale.SLICE);
            drawRatio(canvas, dom, 150,  0, 10, 25, SVGPreserveAspectRatioAlign.XMAX_YMID, SVGPreserveAspectRatioScale.SLICE);

            // none
            drawRatio(canvas, dom,   0,  30, 160, 60, SVGPreserveAspectRatioAlign.NONE, SVGPreserveAspectRatioScale.MEET);
            drawRatio(canvas, dom,   0, 100, 160, 60, SVGPreserveAspectRatioAlign.NONE, SVGPreserveAspectRatioScale.SLICE);
        }
        canvas.restore();
    }

    public void drawIcon(Canvas canvas, Point bounds, SVGDOM svg) {
        try (SVGSVG root = svg.getRoot()) {
            SVGLengthContext lc = new SVGLengthContext(bounds);
            var width = lc.resolve(root.getWidth(), SVGLengthType.HORIZONTAL);
            var height = lc.resolve(root.getHeight(), SVGLengthType.VERTICAL);
            svg.setContainerSize(bounds);
            var scale = Math.min(bounds.getX() / width, bounds.getY() / height);
            canvas.save();
            canvas.scale(scale, scale);
            svg.render(canvas);
            canvas.restore();
        }

        canvas.drawRect(Rect.makeWH(bounds.getX(), bounds.getY()), border);
        
        canvas.drawString(String.format("%.0f×%.0f", bounds.getX(), bounds.getY()), 0, bounds.getY() + 20, inter13, fill);
        canvas.translate(bounds.getX() + 30, 0);
    }

    public void drawRatio(Canvas canvas, SVGDOM dom, float x, float y, float w, float h, SVGPreserveAspectRatioAlign align, SVGPreserveAspectRatioScale scale) {
        canvas.save();
        canvas.translate(x, y);
        canvas.clipRect(Rect.makeXYWH(0, 0, w, h));
        canvas.clear(0xFFEEEEEE);
        try (var root = dom.getRoot();) {
            root.setWidth(new SVGLength(w))
                .setHeight(new SVGLength(h))
                .setPreserveAspectRatio(new SVGPreserveAspectRatio(align, scale));
        }
        dom.render(canvas);
        canvas.restore();
    }
}
