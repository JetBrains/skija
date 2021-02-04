package org.jetbrains.skija.examples.scenes;

import lombok.*;
import java.nio.file.Files;
import java.util.*;
import org.jetbrains.skija.*;

public class FigmaScene extends Scene {
    private Font uiFont;
    private Font uiFontSmall;
    private Font uiFontBold;
    private Map<Font, FontMetrics> metrics = new HashMap<>();
    private Map<String, Path> icons = new HashMap<>();
    private Image avatar;
    private float offsetX = 0, offsetY = 0;

    @SneakyThrows
    public FigmaScene() {
        uiFont = new Font(inter, 11);
        uiFont.setSubpixel(true);
        metrics.put(uiFont, uiFont.getMetrics());

        uiFontSmall = new Font(inter, 9);
        uiFontSmall.setSubpixel(true);
        metrics.put(uiFontSmall, uiFontSmall.getMetrics());

        try (var medium = Typeface.makeFromFile(file("fonts/InterHinted-Bold.ttf"));) {
            uiFontBold = new Font(medium, 11);
            uiFontBold.setSubpixel(true);
            metrics.put(uiFontBold, uiFontBold.getMetrics());
        }
        // Typeface uiTypefaceBold = FontMgr.getDefault().matchFamilyStyle("System Font", FontStyle.BOLD);
        
        icons.put("Search",      new Path().moveTo(0, 0.5f).rLineTo(14, 0).moveTo(0, 5.5f).rLineTo(14, 0).moveTo(0, 10.5f).rLineTo(14, 0));
        icons.put("Move",        new Path().moveTo(0.5f, 0.5f).lineTo(3, 13).lineTo(6, 9).lineTo(11, 7).closePath());
        icons.put("Chevron",     new Path().lineTo(3, 3).lineTo(6, 0));
        icons.put("Frame",       new Path().moveTo(3.5f, 0).lineTo(3.5f, 16).moveTo(12.5f, 0).lineTo(12.5f, 16).moveTo(0, 3.5f).lineTo(16, 3.5f).moveTo(0, 12.5f).lineTo(16, 12.5f));
        icons.put("Rectangle",   new Path().addRect(Rect.makeXYWH(0.5f, 0.5f, 15, 15)));
        icons.put("Pen Stroke",  new Path().lineTo(2.5f, 9).rCubicTo(1, 3, 3, 3, 6.5f, 3).lineTo(11, 14).lineTo(14, 11).lineTo(12, 9).rCubicTo(0, -3.5f, 0, -5.5f, -3, -6.5f).lineTo(0, 0).moveTo(0, 0).lineTo(6, 6));
        icons.put("Pen Fill",    new Path().addCircle(7, 7, 1.5f));
        icons.put("Text",        new Path().moveTo(0, 4).lineTo(0, 0.5f).lineTo(12.5f, 0.5f).lineTo(12.5f, 4).moveTo(6.5f, 0.5f).lineTo(6.5f, 15.5f).moveTo(3, 15.5f).rLineTo(7, 0));
        icons.put("Hand Tool",   new Path().moveTo(4.5f, 11).lineTo(2.5f, 10).rCubicTo(-2f, -1f, -3.5f, 1f, -2f, 2.5f).cubicTo(1f, 13f, 7f, 19.5f, 9f, 19.5f).lineTo(12, 19.5f).rCubicTo(2f, 0f, 4.5f, -2.5f, 4.5f, -5f).lineTo(16.5f, 6)
                                   .rCubicTo(0, -2, -3, -2, -3, 0).lineTo(13.5f, 9).lineTo(13.5f, 3).rCubicTo(0, -2, -3, -2, -3, 0).lineTo(10.5f, 9).lineTo(10.5f, 2).rCubicTo(0, -2, -3, -2, -3, 0)
                                   .lineTo(7.5f, 9).lineTo(7.5f, 4).rCubicTo(0, -2, -3, -2, -3, 0).closePath());
        icons.put("Comments",    new Path().moveTo(0, 15.5f).lineTo(5.5f, 14.36f).cubicTo(6.54f, 15.19f, 7.74f, 15.5f, 9, 15.5f).cubicTo(13.14f, 15.5f, 16.5f, 12.14f, 16.5f, 8f)
                                   .cubicTo(16.5f, 3.86f, 13.14f, 0.5f, 9, 0.5f).cubicTo(4.86f, 0.5f, 1.5f, 3.86f, 1.5f, 8).cubicTo(1.5f, 9.26f, 1.81f, 10.46f, 2.37f, 11.5f).closePath());
        icons.put("Reset",       new Path().moveTo(28.5f, 19.5f).lineTo(20.5f, 11.5f).lineTo(12.5f, 19.5f).lineTo(20.5f, 27.5f).lineTo(24.5f, 23.5f)
                                   .moveTo(21, 17).lineTo(18.5f, 19.5f).lineTo(21, 22)
                                   .moveTo(18.5f, 19.5f).lineTo(22, 19.5f).cubicTo(26, 19.5f, 29.5f, 24.5f, 26, 29));
        icons.put("Component",   new Path().moveTo(23, 15).rLineTo(-3, -3).rLineTo(-3, 3).rLineTo(3, 3).closePath()
                                   .moveTo(18, 20).rLineTo(-3, -3).rLineTo(-3, 3).rLineTo(3, 3).closePath()
                                   .moveTo(23, 25).rLineTo(-3, -3).rLineTo(-3, 3).rLineTo(3, 3).closePath()
                                   .moveTo(28, 20).rLineTo(-3, -3).rLineTo(-3, 3).rLineTo(3, 3).closePath());
        icons.put("Mask Stroke", new Path().addCircle(20, 20, 7.5f));
        icons.put("Mask Fill",   new Path().moveTo(16.75f, 13.24f).cubicTo(14.24f, 14.45f, 12.5f, 17.02f, 12.5f, 20).cubicTo(12.5f, 22.98f, 14.24f, 25.55f, 16.75f, 26.76f)
                                   .cubicTo(19.26f, 25.55f, 21, 22.98f, 21, 20).cubicTo(21, 17.02f, 19.26f, 14.45f, 16.75f, 13.24f));
        icons.put("Union",       new Path().moveTo(13, 12).lineTo(13, 23).lineTo(18, 23).lineTo(18, 28).lineTo(29, 28).lineTo(29, 17).lineTo(24, 17).lineTo(24, 12).closePath());
        avatar = Image.makeFromEncoded(Files.readAllBytes(java.nio.file.Path.of(file("images/IMG_5563.png"))));
        icons.put("Present",     new Path().moveTo(15.5f, 15).lineTo(15.5f, 25).lineTo(23, 20).closePath());
        icons.put("Layer Frame", new Path().moveTo(3.5f, 0.5f).lineTo(3.5f, 11.5f).moveTo(8.5f, 0.5f).lineTo(8.5f, 11.5f).moveTo(0.5f, 3.5f).lineTo(11.5f, 3.5f).moveTo(0.5f, 8.5f).lineTo(11.5f, 8.5f));
        icons.put("Layer Image", new Path().addRRect(RRect.makeXYWH(0.5f, 0.5f, 11, 11, 1)).addCircle(8, 4, 1.5f).moveTo(0.5f, 8.5f).lineTo(3.5f, 5.5f).lineTo(9.5f, 11.5f));
        icons.put("Layer Rect",  new Path().addRect(Rect.makeLTRB(1.5f, 4, 10.5f, 8)));
        icons.put("Layer Comp",  new Path().moveTo(6, 0.5f).lineTo(11.5f, 6).lineTo(6, 11.5f).lineTo(0.5f, 6).closePath());
        icons.put("Align Left Stroke", new Path().moveTo(2.5f, 1.5f).lineTo(2.5f, 14.5f));
        icons.put("Align Left Fill",   new Path().addRect(Rect.makeLTRB(5, 4.5f, 15, 6.5f)).addRect(Rect.makeLTRB(5, 9.5f, 11, 11.5f)));
        icons.put("Align Center Stroke", new Path().moveTo(8, 1.5f).lineTo(8, 14.5f));
        icons.put("Align Center Fill",   new Path().addRect(Rect.makeLTRB(2.5f, 4.5f, 13.5f, 6.5f)).addRect(Rect.makeLTRB(4.5f, 9.5f, 11, 11.5f)));
        icons.put("Align Right Stroke", new Path().moveTo(14.5f, 1.5f).lineTo(14.5f, 14.5f));
        icons.put("Align Right Fill",   new Path().addRect(Rect.makeLTRB(2, 4.5f, 12, 6.5f)).addRect(Rect.makeLTRB(6, 9.5f, 12, 11.5f)));
        icons.put("Align Top Stroke", new Path().moveTo(1.5f, 2.5f).lineTo(14.5f, 2.5f));
        icons.put("Align Top Fill",   new Path().addRect(Rect.makeLTRB(4.5f, 5f, 6.5f, 15f)).addRect(Rect.makeLTRB(9.5f, 5f, 11.5f, 11f)));
        icons.put("Align Middle Stroke", new Path().moveTo(1.5f, 8f).lineTo(14.5f, 8f));
        icons.put("Align Middle Fill",   new Path().addRect(Rect.makeLTRB(4.5f, 2.5f, 6.5f, 13.5f)).addRect(Rect.makeLTRB(9.5f, 4.5f, 11.5f, 11.5f)));
        icons.put("Align Bottom Stroke", new Path().moveTo(1.5f, 14.5f).lineTo(14.5f, 14.5f));
        icons.put("Align Bottom Fill",   new Path().addRect(Rect.makeLTRB(4.5f, 2f, 6.5f, 12f)).addRect(Rect.makeLTRB(9.5f, 6f, 11.5f, 12f)));
        icons.put("Align More Stroke", new Path().moveTo(2.5f, 1.5f).lineTo(2.5f, 14.5f).moveTo(13.5f, 1.5f).lineTo(13.5f, 14.5f));
        icons.put("Align More Fill",   new Path().addRect(Rect.makeLTRB(7, 4.5f, 9, 11.5f)));

        icons.put("Angle",  new Path().moveTo(4.5f, 4).lineTo(4.5f, 11.5f).lineTo(12, 11.5f).moveTo(4.5f, 7.5f).cubicTo(7, 7.5f, 8.5f, 9, 8.5f, 11.5f));
        icons.put("Radius", new Path().moveTo(13, 4.5f).lineTo(9f, 4.5f).cubicTo(6.5f, 4.5f, 4.5f, 6.5f, 4.5f, 9f).lineTo(4.5f, 13));
        icons.put("Constrain", new Path().moveTo(10.5f, 6f).lineTo(10.5f, 4f).cubicTo(10.5f, 2.62f, 9.38f, 1.5f, 8f, 1.5f).cubicTo(6.62f, 1.5f, 5.5f, 2.62f, 5.5f, 4).lineTo(5.5f, 6)
                                         .moveTo(5.5f, 10).lineTo(5.5f, 12).cubicTo(5.5f, 13.38f, 6.62f, 14.5f, 8, 14.5f).cubicTo(9.38f, 14.5f, 10.5f, 13.38f, 10.5f, 12).lineTo(10.5f, 10));
        icons.put("Corners", new Path().moveTo(6, 3.5f).lineTo(3.5f, 3.5f).lineTo(3.5f, 6).moveTo(3.5f, 10).lineTo(3.5f, 12.5f).lineTo(6, 12.5f)
                                       .moveTo(10, 12.5f).lineTo(12.5f, 12.5f).lineTo(12.5f, 10).moveTo(12.5f, 6).lineTo(12.5f, 3.5f).lineTo(10, 3.5f));
        icons.put("Unchecked", new Path().addRRect(RRect.makeXYWH(2.5f, 2.5f, 11, 11, 2)));
        icons.put("Constraints Horizontal", new Path().moveTo(6f, 6.5f).lineTo(4.5f, 8.5f).lineTo(6, 10.5f).moveTo(4.5f, 8.5f).lineTo(12.5f, 8.5f).moveTo(11, 6.5f).lineTo(12.5f, 8.5f).lineTo(11, 10.5f));
        icons.put("Constraints Vertical", new Path().moveTo(6.5f, 6f).lineTo(8.5f, 4.5f).lineTo(10.5f, 6).moveTo(8.5f, 4.5f).lineTo(8.5f, 12.5f).moveTo(6.5f, 11).lineTo(8.5f, 12.5f).lineTo(10.5f, 11));
        icons.put("Plus", new Path().moveTo(8, 2.5f).lineTo(8, 13.5f).moveTo(2.5f, 8).lineTo(13.5f, 8));
        icons.put("Transparency", new Path().moveTo(8, 4).cubicTo(6.5f, 5.5f, 4.5f, 7.07f, 4.5f, 9).cubicTo(4.5f, 10.93f, 6.07f, 12.5f, 8, 12.5f).cubicTo(9.93f, 12.5f, 11.5f, 10.93f, 11.5f, 9)
                                            .cubicTo(11.5f, 7.07f, 9.5f, 5.5f, 8, 4).closePath());
        icons.put("Eye Stroke", new Path().moveTo(2, 8).cubicTo(5.5f, 3.5f, 10.5f, 3.5f, 14, 8).cubicTo(10.5f, 12.5f, 5.5f, 12.5f, 2, 8).closePath());
        icons.put("Eye Fill",   new Path().addCircle(8, 8, 2));
        icons.put("Style",      new Path().addCircle(5, 5, 1.5f).addCircle(11, 5, 1.5f).addCircle(5, 11, 1.5f).addCircle(11, 11, 1.5f));
        icons.put("Eye Closed", new Path().moveTo(14, 8).cubicTo(10.5f, 12.5f, 5.5f, 12.5f, 2, 8).moveTo(3.4f, 9.5f).lineTo(1.5f, 11.5f).moveTo(6.5f, 11.19f).lineTo(5.5f, 14)
                                          .moveTo(9.5f, 11.19f).lineTo(10.5f, 14).moveTo(12.6f, 9.5f).lineTo(14.5f, 11.5f));
        icons.put("Minus",      new Path().moveTo(2.5f, 8).lineTo(13.5f, 8));
    }

    @Override
    public void onScroll(float dx, float dy) {
        offsetX += dx;
        offsetY += dy;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        drawTabbar(canvas, width, height);
        drawToolbar(canvas, width, height);
        drawLeft(canvas, width, height, dpi);
        drawRight(canvas, width, height, dpi);
        drawCanvas(canvas, width, height, dpi, xpos, ypos);
    }

    public void drawTabbar(Canvas canvas, int width, int height) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            int level = canvas.save();

            // Background
            fill.setColor(0xFF222222);
            canvas.drawRect(Rect.makeXYWH(0, 0, width, 38), fill);

            // Traffic light
            stroke.setColor(0xFF595959);
            canvas.drawOval(Rect.makeXYWH(13, 13, 12, 12), stroke);
            canvas.drawOval(Rect.makeXYWH(33, 13, 12, 12), stroke);
            canvas.drawOval(Rect.makeXYWH(53, 13, 12, 12), stroke);
            
            // Figma logo
            canvas.translate(93, 12);
            stroke.setColor(0xFF7A7A7A);
            canvas.drawRRect(RRect.makeXYWH(0, 0, 5, 5, 3, 0, 0, 3), stroke);
            canvas.drawRRect(RRect.makeXYWH(0, 5, 5, 5, 3, 0, 0, 3), stroke);
            canvas.drawRRect(RRect.makeXYWH(0, 10, 5, 5, 3, 0, 3, 3), stroke);
            canvas.drawRRect(RRect.makeXYWH(5, 0, 5, 5, 0, 3, 3, 0), stroke);
            canvas.drawRRect(RRect.makeXYWH(5, 5, 5, 5, 3, 3, 3, 3), stroke);

            // Active tab
            canvas.translate(27, -12);

            Rect textBox = uiFont.measureText("Layouts", fill);
            fill.setColor(0xFF2C2C2C);
            canvas.drawRect(Rect.makeXYWH(0, 0, 12 + textBox.getWidth() + 10 + 6 + 10, 38), fill);

            fill.setColor(0xFFFFFFFF);
            drawTextLeft(canvas, "Layouts", 12, 0, 38, uiFont, fill);

            stroke.setColor(0xFF565656).setStrokeWidth(2).setStrokeCap(PaintStrokeCap.SQUARE);
            canvas.translate(12 + textBox.getWidth() + 10, 0);
            canvas.drawLines(new float[] { 0, 16, 6, 22, 6, 16, 0, 22 }, stroke);
            canvas.translate(16, 0);

            // New tab
            canvas.translate(11, 15);
            stroke.setColor(0xFF7A7A7A).setStrokeWidth(1);
            canvas.drawLines(new float[] { 0, 4, 8, 4, 4, 0, 4, 8 }, stroke);

            canvas.restoreToCount(level);
            canvas.translate(0, 38);
        }
    }

    public void drawToolbar(Canvas canvas, int width, int height) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            int level = canvas.save();

            // Background
            fill.setColor(0xFF2C2C2C);
            canvas.drawRect(Rect.makeXYWH(0, 0, width, 40), fill);

            // left ones
            canvas.translate(13, 14);
            stroke.setColor(0xFFD5D5D5);
            canvas.drawPath(icons.get("Search"), stroke);
            canvas.translate(27, -14);

            fill.setColor(0xFF18A0FB);
            canvas.drawRect(Rect.makeXYWH(0, 0, 48, 40), fill);
            canvas.translate(15, 13);
            stroke.setColor(0xFFFFFFFF);
            canvas.drawPath(icons.get("Move"), stroke);
            canvas.translate(21, 6);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.translate(12, -19);

            canvas.translate(14, 12);
            stroke.setColor(0xFFD5D5D5);
            canvas.drawPath(icons.get("Frame"), stroke);
            canvas.translate(22, 7);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.translate(12, -19);

            canvas.translate(14, 13);
            canvas.drawPath(icons.get("Pen Stroke"), stroke);
            fill.setColor(0xFFD5D5D5);
            canvas.drawPath(icons.get("Pen Fill"), fill);
            canvas.translate(22, 6);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.translate(12, -19);

            canvas.translate(13, 12);
            canvas.drawPath(icons.get("Text"), stroke);

            canvas.translate(38, -2);
            canvas.drawPath(icons.get("Hand Tool"), stroke);

            canvas.translate(40, 2);
            canvas.drawPath(icons.get("Comments"), stroke);
            canvas.restore();

            // middle ones
            canvas.save();
            canvas.translate((width - 168) / 2f, 0);
            canvas.drawPath(icons.get("Reset"), stroke);
            canvas.translate(40, 0);
            canvas.drawPath(icons.get("Component"), stroke);
            canvas.translate(40, 0);
            canvas.drawPath(icons.get("Mask Fill"), fill);
            canvas.drawPath(icons.get("Mask Stroke"), stroke);
            canvas.translate(40, 0);
            canvas.drawPath(icons.get("Union"), fill);
            canvas.translate(36, 19);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();

            // right ones
            canvas.save();
            canvas.translate(width - 216, 0);

            // avatar
            canvas.save();
            canvas.clipRRect(RRect.makeXYWH(8, 8, 24, 24, 12), ClipMode.INTERSECT, true);
            canvas.drawImageRect(avatar, Rect.makeXYWH(8, 8, 24, 24));
            canvas.restore();
            canvas.translate(40, 0);

            // share
            Rect textBox = uiFont.measureText("Share", fill);
            fill.setColor(0xFF18A0FB);
            canvas.drawRRect(RRect.makeXYWH(12, 5, 24 + textBox.getWidth(), 30, 6), fill);
            fill.setColor(0xFFFFFFFF);
            drawTextLeft(canvas, "Share", 24, 0, 40, uiFont, fill);
            canvas.translate(74, 0);

            // present
            canvas.drawPath(icons.get("Present"), stroke);
            canvas.translate(40, 0);

            // zoom
            fill.setColor(0xFFD5D5D5);
            drawTextLeft(canvas, "31%", 13, 0, 40, uiFont, fill);
            canvas.translate(41, 19);
            canvas.drawPath(icons.get("Chevron"), stroke);

            canvas.restoreToCount(level);
            canvas.translate(0, 40);
        }
    }

    public void drawLeft(Canvas canvas, int width, int height, float dpi) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);
             Region clip = new Region();)
        {
            int level = canvas.save();
            clip.setRect(IRect.makeLTRB(0, (int) (78 * dpi), (int) (241 * dpi), (int) (height * dpi)));
            canvas.clipRegion(clip);

            fill.setColor(0xFFFFFFFF);
            canvas.drawRect(Rect.makeXYWH(0, 0, 240, height - 78), fill);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(240.5f, 0, 240.5f, height - 78, stroke);

            // tabs
            fill.setColor(0xFF333333);
            float w1 = drawTextLeft(canvas, "Layers", 16, 0, 40, uiFontBold, fill);

            fill.setColor(0xFFB3B3B3);
            drawTextLeft(canvas, "Assets", 16 + w1 + 16, 0, 40, uiFontBold, fill);

            stroke.setColor(0xFFB3B3B3);
            canvas.save();
            canvas.translate(217, 19);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();

            fill.setColor(0xFF333333);
            drawTextRight(canvas, "Page 1", 240 - 27, 0, 40, uiFont, fill);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 40.5f, 240f, 40.5f, stroke);

            canvas.translate(0, 40);

            // layers

            canvas.save();
            canvas.translate(18, 10);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Layer Frame"), stroke);
            canvas.restore();
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "layouts", 40, 0, 32, uiFontBold, fill);
            canvas.translate(0, 32);

            stroke.setColor(0xFFB3B3B3);
            for (int i = 0; i < 7; ++i) {
                canvas.save();
                canvas.translate(42, 10);
                canvas.drawPath(icons.get("Layer Image"), stroke);
                canvas.restore();
                drawTextLeft(canvas, "IMG_5740 " + i, 65, 0, 32, uiFont, fill);
                canvas.translate(0, 32);
            }

            canvas.save();
            canvas.translate(42, 10);
            canvas.drawPath(icons.get("Layer Rect"), stroke);
            canvas.restore();
            drawTextLeft(canvas, "Rectangle", 65, 0, 32, uiFont, fill);
            canvas.translate(0, 32);

            stroke.setColor(0xFFCAC0FF);
            fill.setColor(0xFF7B61FF);
            int components = (height - 300 - 78) / 32;
            for (int i = 0; i < components; ++i) {
                canvas.save();
                canvas.translate(42, 10);
                canvas.drawPath(icons.get("Layer Comp"), stroke);
                canvas.restore();
                drawTextLeft(canvas, "Component " + (components - i), 65, 0, 32, uiFont, fill);
                canvas.translate(0, 32);
            }

            canvas.restoreToCount(level);
        }
    }

    public void drawAlign(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            // tabs
            int level = canvas.save();
            fill.setColor(0xFF333333);
            canvas.translate(16 + drawTextLeft(canvas, "Design", 16, 0, 40, uiFontBold, fill), 0);
            fill.setColor(0xFFB3B3B3);
            canvas.translate(16 + drawTextLeft(canvas, "Prototype", 16, 0, 40, uiFont, fill), 0);
            canvas.translate(16 + drawTextLeft(canvas, "Code", 16, 0, 40, uiFont, fill), 0);
            canvas.restoreToCount(level);

            // align
            fill.setColor(0xFF333333);
            stroke.setColor(0xFF333333);

            level = canvas.save();
            canvas.translate(16, 48);
            
            canvas.drawPath(icons.get("Align Left Stroke"), stroke);
            canvas.drawPath(icons.get("Align Left Fill"), fill);
            canvas.translate(32, 0);

            canvas.drawPath(icons.get("Align Center Stroke"), stroke);
            canvas.drawPath(icons.get("Align Center Fill"), fill);
            canvas.translate(32, 0);

            canvas.drawPath(icons.get("Align Right Stroke"), stroke);
            canvas.drawPath(icons.get("Align Right Fill"), fill);
            canvas.translate(32, 0);

            canvas.drawPath(icons.get("Align Top Stroke"), stroke);
            canvas.drawPath(icons.get("Align Top Fill"), fill);
            canvas.translate(32, 0);

            canvas.drawPath(icons.get("Align Middle Stroke"), stroke);
            canvas.drawPath(icons.get("Align Middle Fill"), fill);
            canvas.translate(32, 0);

            canvas.drawPath(icons.get("Align Right Stroke"), stroke);
            canvas.drawPath(icons.get("Align Right Fill"), fill);
            canvas.translate(28, 0);

            canvas.drawPath(icons.get("Align More Stroke"), stroke);
            canvas.drawPath(icons.get("Align More Fill"), fill);
            canvas.translate(17, 6);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restoreToCount(level);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 80.5f, 240, 80.5f, stroke);
            canvas.translate(0, 80);
        }
    }

    public void drawProps(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);

            canvas.save();
            fill.setColor(0xFF333333);
            float w = drawTextLeft(canvas, "Frame", 16, 0, 16, uiFont, fill);
            canvas.translate(16 + w + 6, 7);
            stroke.setColor(0xFFB3B3B3);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextCenter(canvas, "X", 8, 0, 16, uiFont, fill);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "1006", 0, 0, 16, uiFont, fill);
            canvas.translate(72, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextCenter(canvas, "Y", 8, 0, 16, uiFont, fill);
            fill.setColor(0xFF333333);
            canvas.translate(24, 0);
            drawTextLeft(canvas, "2353", 0, 0, 16, uiFont, fill);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextCenter(canvas, "W", 8, 0, 16, uiFont, fill);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "56", 0, 0, 16, uiFont, fill);
            canvas.translate(72, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextCenter(canvas, "H", 8, 0, 16, uiFont, fill);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "86", 0, 0, 16, uiFont, fill);
            canvas.translate(72, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Constrain"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            stroke.setColor(0xFFB3B3B3);
            canvas.translate(16, 0);
            canvas.drawPath(icons.get("Angle"), stroke);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "0°", 0, 0, 16, uiFont, fill);
            canvas.translate(72, 0);
            canvas.drawPath(icons.get("Radius"), stroke);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "0", 0, 0, 16, uiFont, fill);
            canvas.translate(72, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Corners"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            canvas.drawPath(icons.get("Unchecked"), stroke);
            canvas.translate(24, 0);
            drawTextLeft(canvas, "Clip content", 0, 0, 16, uiFont, fill);
            canvas.restore();
            canvas.translate(0, 32);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);
        }
    }

    public void drawInstance(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);

            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "Instance", 0, 0, 16, uiFontBold, fill);
            canvas.translate(180, -12);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Reset"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            float w = drawTextLeft(canvas, "Component 1", 0, 0, 16, uiFont, fill);
            canvas.translate(w + 6, 7);
            stroke.setColor(0xFFB3B3B3);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            fill.setColor(0xFF18A0FB);
            drawTextLeft(canvas, "Go to Main Component", 16, 0, 16, uiFont, fill);
            canvas.translate(0, 32);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);   
        }
    }

    public void drawConstraints(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);
            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "Constraints", 0, 0, 16, uiFontBold, fill);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            stroke.setColor(0xFFE5E5E5);
            canvas.drawRRect(RRect.makeXYWH(1.5f, 1.5f, 62, 62, 3), stroke);
            try (var dash = PathEffect.makeDash(new float[] {3, 2}, 0)) {
                stroke.setPathEffect(dash);
                canvas.drawRRect(RRect.makeXYWH(20.5f, 20.5f, 24, 24, 2), stroke);
                stroke.setPathEffect(null);
            }
            stroke.setColor(0xFF333333);
            canvas.drawLine(24, 32.5f, 41, 32.5f, stroke);
            canvas.drawLine(32.5f, 24, 32.5f, 41, stroke);
            canvas.drawLine(49, 32.5f, 59, 32.5f, stroke);
            canvas.drawLine(32.5f, 49, 32.5f, 59, stroke);
            fill.setColor(0xFF18A0FB);
            canvas.drawRect(Rect.makeXYWH(31, 6, 3, 10), fill);
            canvas.drawRect(Rect.makeXYWH(6, 31, 10, 3), fill);

            canvas.translate(80, 8);
            canvas.save();
            stroke.setColor(0xFFB3B3B3);
            canvas.drawPath(icons.get("Constraints Horizontal"), stroke);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            float w = drawTextLeft(canvas, "Left", 0, 0, 16, uiFont, fill);
            canvas.translate(w + 6, 7);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();
            
            canvas.translate(0, 32);
            canvas.save();
            canvas.drawPath(icons.get("Constraints Vertical"), stroke);
            canvas.translate(24, 0);
            w = drawTextLeft(canvas, "Top", 0, 0, 16, uiFont, fill);
            canvas.translate(w + 6, 7);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();

            canvas.restore();
            canvas.translate(0, 80);

            canvas.save();
            canvas.translate(16, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Unchecked"), stroke);
            canvas.translate(24, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "Fix position when scrolling", 0, 0, 16, uiFont, fill);
            canvas.restore();
            canvas.translate(0, 32);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);   
        }
    }

    public void drawGrid(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);
            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextLeft(canvas, "Layout Grid", 0, 0, 16, uiFontBold, fill);

            canvas.translate(192, 0);
            stroke.setColor(0xFFE5E5E5);
            canvas.drawPath(icons.get("Plus"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);
        }
    }

    public void drawLayer(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "Layer", 16, 0, 16, uiFontBold, fill);
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            stroke.setColor(0xFFB3B3B3);
            canvas.drawPath(icons.get("Transparency"), stroke);
            canvas.translate(24, 0);
            float w = drawTextLeft(canvas, "Pass Through", 0, 0, 16, uiFont, fill);
            canvas.translate(w + 6, 7);
            canvas.drawPath(icons.get("Chevron"), stroke);
            canvas.restore();

            canvas.save();
            canvas.translate(160, 0);
            drawTextLeft(canvas, "100%", 0, 0, 16, uiFont, fill);
            canvas.translate(48, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Eye Stroke"), stroke);
            canvas.drawPath(icons.get("Eye Fill"), fill);
            canvas.restore();

            canvas.translate(0, 32);
            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);
        }
    }

    public void drawFill(Canvas canvas) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);)
        {
            canvas.translate(0, 16);
            canvas.save();
            canvas.translate(16, 0);
            fill.setColor(0xFF333333);
            drawTextLeft(canvas, "Fill", 0, 0, 16, uiFontBold, fill);
            canvas.translate(160, 0);
            fill.setColor(0xFF333333);
            canvas.drawPath(icons.get("Style"), fill);
            canvas.translate(32, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Plus"), stroke);
            canvas.restore();
            canvas.translate(0, 32);

            canvas.save();
            canvas.translate(16, 0);
            stroke.setColor(0xFFE5E5E5).setStrokeWidth(0.5f);
            canvas.drawRect(Rect.makeXYWH(0.25f, 0.25f, 15.5f, 15.5f), stroke);
            stroke.setStrokeWidth(1f);
            canvas.translate(24, 0);
            fill.setColor(0xFFB3B3B3);
            drawTextLeft(canvas, "FFFFFF", 0, 0, 16, uiFont, fill);
            canvas.restore();

            canvas.save();
            canvas.translate(112, 0);
            drawTextLeft(canvas, "100%", 0, 0, 16, uiFont, fill);
            canvas.restore();

            canvas.save();
            canvas.translate(176, 0);
            stroke.setColor(0xFF333333);
            canvas.drawPath(icons.get("Eye Closed"), stroke);
            canvas.translate(32, 0);
            canvas.drawPath(icons.get("Minus"), stroke);
            canvas.restore();

            canvas.translate(0, 32);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(0, 0.5f, 240, 0.5f, stroke);
        }
    }

    public void drawRight(Canvas canvas, int width, int height, float dpi) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);
             Region clip = new Region();)
        {
            int level = canvas.save();
            clip.setRect(IRect.makeLTRB((int) ((width - 241) * dpi), (int) (78 * dpi), (int) (width * dpi), (int) (height * dpi)));
            canvas.clipRegion(clip);
            canvas.translate(width - 240, 0);

            // bg
            fill.setColor(0xFFFFFFFF);
            canvas.drawRect(Rect.makeXYWH(0, 0, 240, height - 78), fill);

            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(-0.5f, 0, -0.5f, height - 78, stroke);

            drawAlign(canvas);
            drawProps(canvas);
            drawInstance(canvas);
            drawConstraints(canvas);
            drawGrid(canvas);
            drawLayer(canvas);
            drawFill(canvas);

            canvas.restoreToCount(level);
        }
    }

    public void drawCanvas(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        try (Paint fill = new Paint();
             Paint stroke = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(1);
             Region clip = new Region();)
        {
            int level = canvas.save();
            clip.setRect(IRect.makeLTRB((int) (241 * dpi), (int) (78 * dpi), (int) ((width - 241) * dpi), (int) (height * dpi)));
            canvas.clipRegion(clip);

            // background
            canvas.translate(241, 0);
            fill.setColor(0xFFE5E5E5);
            canvas.drawRect(Rect.makeLTRB(20, 20, width - 241 - 241, height - 78), fill);

            // top ruler
            fill.setColor(0xFFFCFCFC);
            canvas.drawRect(Rect.makeLTRB(20, 0, width - 241 - 241, 20), fill);

            canvas.save();
            canvas.translate(20, 0);
            fill.setColor(0xFFB8B8B8);
            stroke.setColor(0xFFB1B1B1);
            for (int x = (int) Math.ceil((-offsetX-20) / 50f) * 50; x + offsetX < width - 241 - 241; x += 50) {
                drawTextCenter(canvas, "" + x, offsetX + x + 0.5f, 0, 14, uiFontSmall, fill);
                canvas.drawLine(offsetX + x + 0.5f, 16, offsetX + x + 0.5f, 20, stroke);
            }
            canvas.restore();

            // left ruler
            fill.setColor(0xFFFCFCFC);
            canvas.drawRect(Rect.makeLTRB(0, 20, 20, height - 78), fill);
            canvas.save();
            canvas.translate(0, 20);
            canvas.rotate(-90);
            fill.setColor(0xFFB8B8B8);
            stroke.setColor(0xFFB1B1B1);
            for (int y = (int) Math.ceil((-offsetY-20) / 50f) * 50; y + offsetY < height - 78; y += 50) {
                drawTextCenter(canvas, "" + y, -offsetY - y - 0.5f, 0, 14, uiFontSmall, fill);
                canvas.drawLine(-offsetY - y - 0.5f, 16, -offsetY - y - 0.5f, 20, stroke);
            }
            canvas.restore();

            // cursor pos
            if (clip.contains((int) (xpos*dpi), (int) (ypos*dpi))) {
                float worldX = xpos - 241 - 20 - offsetX;
                float worldY = ypos - 78 - 20 - offsetY;

                fill.setColor(0xFF24A5FB);
                drawTextCenter(canvas, "" + (int) worldX, 20 + offsetX + worldX + 0.5f, 0, 14, uiFontSmall, fill);
                stroke.setColor(0xFFB1B1B1);
                canvas.drawLine(20 + offsetX + worldX + 0.5f, 16, 20 + offsetX + worldX + 0.5f, 20, stroke);

                canvas.save();
                canvas.translate(0, 20);
                canvas.rotate(-90);
                drawTextCenter(canvas, "" + (int) worldY, -offsetY - worldY - 0.5f, 0, 14, uiFontSmall, fill);
                canvas.drawLine(-offsetY - worldY - 0.5f, 16, -offsetY - worldY - 0.5f, 20, stroke);
                canvas.restore();                
            }

            // edge
            fill.setColor(0xFFFCFCFC);
            canvas.drawRect(Rect.makeXYWH(0, 0, 20, 20), fill);
            stroke.setColor(0xFFE5E5E5);
            canvas.drawLine(20.5f, 0, 20.5f, 21, stroke);
            canvas.drawLine(0, 20.5f, 21, 20.5f, stroke);

            canvas.restoreToCount(level);
        }
    }

    public float drawTextLeft(Canvas canvas, String text, float x, float y, float height, Font font, Paint paint) {
        Rect inner = font.measureText(text, paint);
        FontMetrics fontMetrics = metrics.get(font);

        canvas.drawString(text,
            x, 
            y + (height - fontMetrics.getHeight()) / 2f - fontMetrics.getAscent(),
            font, paint);

        return inner.getWidth();
    }

    public float drawTextCenter(Canvas canvas, String text, float x, float y, float height, Font font, Paint paint) {
        Rect inner = font.measureText(text, paint);
        FontMetrics fontMetrics = metrics.get(font);

        canvas.drawString(text,
            x - inner.getWidth() / 2f, 
            y + (height - fontMetrics.getHeight()) / 2f - fontMetrics.getAscent(),
            font, paint);

        return inner.getWidth();
    }

    public float drawTextRight(Canvas canvas, String text, float x, float y, float height, Font font, Paint paint) {
        Rect inner = font.measureText(text, paint);
        FontMetrics fontMetrics = metrics.get(font);

        canvas.drawString(text,
            x - inner.getWidth(), 
            y + (height - fontMetrics.getHeight()) / 2f - fontMetrics.getAscent(),
            font, paint);

        return inner.getWidth();
    }
}