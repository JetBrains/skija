package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class DecorationsBenchScene extends Scene {
    public Path element = new Path().moveTo(0, -1.5f).lineTo(2, 0.5f).lineTo(4, -1.5f).lineTo(4, -0.5f).lineTo(2, 1.5f).lineTo(0, -0.5f).closePath().transform(Matrix33.makeScale(1));
    public PathEffect effect = PathEffect.makePath1D(element, 1 * 4, 0, PathEffect.Style.TRANSLATE);
    public int odd = 0xffde8ece;
    public int even = 0xff03c5cf;
    public Paint effectStrokeOdd = new Paint().setColor(odd).setPathEffect(effect);
    public Paint effectStrokeEven = new Paint().setColor(even).setPathEffect(effect);

    public Paint pathStrokeOdd = new Paint().setColor(odd).setMode(PaintMode.STROKE).setStrokeWidth(1);
    public Paint pathStrokeEven = new Paint().setColor(even).setMode(PaintMode.STROKE).setStrokeWidth(1);

    public Paint shaderStrokeOdd;
    public Paint shaderStrokeEven;

    public DecorationsBenchScene() {
        _variants = new String[] { "Path Effect", "Path", "Image" };
        _variantIdx = 2;
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        if (shaderStrokeOdd == null) {
            try (var surface = Surface.makeRasterN32Premul((int) Math.ceil(4 * dpi), (int) Math.ceil(3 * dpi));) {
                var c = surface.getCanvas();
                c.scale(dpi, dpi);
                c.translate(0, 1.5f);
                c.drawPath(element, new Paint().setColor(odd));
                try (var shader = surface.makeImageSnapshot().makeShader(FilterTileMode.REPEAT, FilterTileMode.REPEAT, Matrix33.makeScale(1f / dpi))) {
                    shaderStrokeOdd  = new Paint().setShader(shader);
                }
            }
        }

        if (shaderStrokeEven == null) {
            try (var surface = Surface.makeRasterN32Premul((int) Math.ceil(4 * dpi), (int) Math.ceil(3 * dpi));) {
                var c = surface.getCanvas();
                c.scale(dpi, dpi);
                c.translate(0, 1.5f);
                c.drawPath(element, new Paint().setColor(even));
                try (var shader = surface.makeImageSnapshot().makeShader(FilterTileMode.REPEAT, FilterTileMode.REPEAT, Matrix33.makeScale(1f / dpi))) {
                    shaderStrokeEven  = new Paint().setShader(shader);
                }
            }
        }

        for (var y = 20; y < height - 20; y += 17) {
            for (var x = 20; x < width - 20; x += 47) {
                var right = Math.min(width - 20, x + 47);
                var odd = (x + y) % 2 == 1;

                if ("Path Effect".equals(_variants[_variantIdx]))
                    drawPathEffect(canvas, x, right, y, odd);
                else if ("Path".equals(_variants[_variantIdx]))
                    drawPath(canvas, x, right, y, odd);
                else if ("Image".equals(_variants[_variantIdx]))
                    drawImage(canvas, x, right, y, odd);
            }
        }
    }

    public void drawPathEffect(Canvas canvas, float left, float right, float y, boolean odd) {
        canvas.save();
        canvas.clipRect(Rect.makeLTRB(left, y, right, y + 3));
        canvas.drawLine(left, y + 1.5f, right, y + 1.5f, odd ? effectStrokeOdd : effectStrokeEven);
        canvas.restore();
    }

    public void drawPath(Canvas canvas, float left, float right, float y, boolean odd) {
        canvas.save();
        int offset = ((int) left) / 4 * 4;
        canvas.translate(offset, y);
        int segments = (int) Math.ceil((right - offset + 4) / 2);
        float[] coords = new float[segments * 2];
        for (int i = 0; i < segments; ++i) {
            coords[i * 2] = i * 2;
            coords[i * 2 + 1] = i % 2 == 0 ? 0 : 3;
        }
        canvas.clipRect(Rect.makeLTRB(left - offset, -2, right - offset, 5));
        canvas.drawPolygon(coords, odd ? pathStrokeOdd : pathStrokeEven);
        canvas.restore();
    }

    public void drawImage(Canvas canvas, float left, float right, float y, boolean odd) {
        canvas.save();
        int offset = ((int) left) / 4 * 4;
        canvas.translate(offset, y);
        canvas.drawRect(Rect.makeLTRB(left - offset, 0, right - offset, 3), odd ? shaderStrokeOdd : shaderStrokeEven);
        canvas.restore();
    }
}