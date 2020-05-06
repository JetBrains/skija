package org.jetbrains.skija.examples.lwjgl;

import java.util.stream.IntStream;
import org.jetbrains.skija.*;

public class ShadersScene implements Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        canvas.translate(20, 20);
        drawShaders(canvas);
        drawGradients(canvas);
        drawLinearCS(canvas);
        drawBlending(canvas);
    }

    private void drawShaders(Canvas canvas) {
        canvas.save();

        float percent = Math.abs((System.currentTimeMillis() % 3000) / 10f - 150f) - 25f;
        percent = Math.round(Math.max(0f, Math.min(100f, percent)));

        Shader[] shaders = new Shader[] {
            Shader.empty(),
            Shader.color(0xFF247ba0),
            Shader.color(new Color4f(0.5f, 0.5f, 0.5f), ColorSpace.getSRGBLinear()),
            Shader.lerp(percent / 100f, Shader.color(0xFFFF0000), Shader.color(0xFF00FF00)),
            Shader.blend(BlendMode.SRC_OVER, Shader.color(0xFFFF0000), Shader.color(0x9000FF00)),
            Shader.blend(BlendMode.SCREEN, Shader.color(0xFFFF0000), Shader.color(0x9000FF00)),
            Shader.blend(BlendMode.OVERLAY, Shader.color(0xFFFF0000), Shader.color(0x9000FF00)),
            Shader.blend(BlendMode.DARKEN, Shader.color(0xFFFF0000), Shader.color(0x9000FF00)),
            Shader.blend(BlendMode.LIGHTEN, Shader.color(0xFFFF0000), Shader.color(0x9000FF00)),
            Shader.linearGradient(0, 0, 60, 0, new int[] { 0xFF247ba0, 0xFFf3ffbd }).withColorFilter(ColorFilter.blend(0xFFCC3333, BlendMode.SCREEN)),
        };

        try (Paint fill = new Paint()) {
            for (Shader sh: shaders) {
                fill.setShader(sh);
                canvas.drawRect(Rect.makeXYWH(0, 0, 60, 60), fill);
                canvas.translate(70, 0);
                sh.close();
            }
        }

        canvas.restore();
        canvas.translate(0, 70);
    }

    private void drawGradients(Canvas canvas) {
        canvas.save();

        Shader[] shaders = new Shader[] {
            Shader.linearGradient( 0,  0, 60,  0, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.linearGradient(20,  0, 40,  0, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.linearGradient(20,  0, 40,  0, new int[] { 0xFF247ba0, 0xFFf3ffbd }, null, Shader.GradientOptions.DEFAULT.with(TileMode.REPEAT)),
            Shader.linearGradient(20,  0, 40,  0, new int[] { 0xFF247ba0, 0xFFf3ffbd }, null, Shader.GradientOptions.DEFAULT.with(TileMode.MIRROR)),
            Shader.linearGradient(20,  0, 40,  0, new int[] { 0xFF247ba0, 0xFFf3ffbd }, null, Shader.GradientOptions.DEFAULT.with(TileMode.DECAL)),
            Shader.linearGradient( 0,  0,  0, 60, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.linearGradient( 0,  0,  0, 60, new int[] { 0xFF247ba0, 0xFFf3ffbd }, null, Shader.GradientOptions.DEFAULT.with(Matrix.rotate(45))),
            Shader.linearGradient( 0,  0, 60, 60, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.linearGradient( 0,  0, 60,  0, new int[] { 0xFF247ba0, 0x00000000 }),
            Shader.linearGradient( 0,  0, 60,  0, new int[] { 0xFF247ba0, 0xFFff1654, 0xFF70c1b3, 0xFFf3ffbd, 0xFFb2dbbf }),
            Shader.linearGradient( 0,  0, 60,  0, new int[] { 0xFF247ba0, 0xFFff1654, 0xFF70c1b3, 0xFFf3ffbd, 0xFFb2dbbf }, new float[] {0f, 0.1f, 0.2f, 0.9f, 1f}),

            Shader.radialGradient(30, 30, 30, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.radialGradient(30, 30, 10, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.radialGradient(30, 30, 10, new int[] { 0xFF247ba0, 0xFFf3ffbd }, null, Shader.GradientOptions.DEFAULT.with(TileMode.REPEAT)),

            Shader.twoPointConicalGradient(20, 20, 10, 40, 40, 40, new int[] { 0xFF247ba0, 0xFFf3ffbd }),

            Shader.sweepGradient(30, 30, new int[] { 0xFF247ba0, 0xFFf3ffbd }),
            Shader.sweepGradient(30, 30, 45, 315, new int[] { 0xFF247ba0, 0xFFff1654, 0xFF70c1b3, 0xFFf3ffbd, 0xFFb2dbbf }, null, Shader.GradientOptions.DEFAULT.with(TileMode.DECAL)),
        };
            
        try (Paint fill = new Paint()) {
            for (Shader sh: shaders) {
                fill.setShader(sh);
                canvas.drawRect(Rect.makeXYWH(0, 0, 60, 60), fill);
                canvas.translate(70, 0);
                sh.close();
            }
        }

        try (var sh = Shader.sweepGradient(30, 30, new int[] { 0xFFFF00FF, 0xFF00FFFF, 0xFFFFFF00, 0xFFFF00FF });
             var stroke = new Paint().setShader(sh).setStyle(Paint.Style.STROKE).setStrokeWidth(10))
        {
            canvas.drawCircle(30, 30, 30, stroke);
            canvas.translate(70, 0);
        }

        try (Path p1 = new Path().lineTo(30.1f, 0).lineTo(0, 32.5f).closePath();
             Shader s1 = Shader.linearGradient(0, 32.5f, 30.1f, 0, 
                new int[] {0xFF0095D5, 0xFF3C83DC, 0xFF6D74E1, 0xFF806EE3},
                new float[] {0.1183f, 0.4178f, 0.6962f, 0.8333f});
             Paint f1 = new Paint().setShader(s1);

             Path p2 = new Path().moveTo(30.1f, 0).lineTo(0, 31.7f).lineTo(0, 60).lineTo(30.1f, 29.9f).lineTo(60, 0).closePath();
             Shader s2 = Shader.linearGradient(0, 60, 60, 0,
                new int[] {0xFFC757BC, 0xFFD0609A, 0xFFE1725C, 0xFFEE7E2F, 0xFFF58613, 0xFFF88909},
                new float[] {0.1075f, 0.2138f, 0.4254f, 0.6048f, 0.743f, 0.8232f});
             Paint f2 = new Paint().setShader(s2);

             Path p3 = new Path().moveTo(0, 60).lineTo(30.1f, 29.9f).lineTo(60, 60).closePath();
             Shader s3 = Shader.linearGradient(0, 60, 30.1f, 29.9f,
                new int[] { 0xFF0095D5, 0xFF238AD9, 0xFF557BDE, 0xFF7472E2, 0xFF806EE3 },
                new float[] {0f, 0.3f, 0.62f, 0.8643f, 1f});
             Paint f3 = new Paint().setShader(s3);)
        {
            canvas.drawPath(p1, f1);
            canvas.drawPath(p2, f2);
            canvas.drawPath(p3, f3);
            canvas.translate(70, 0);
        }

        canvas.restore();
        canvas.translate(0, 70);
    }

    private Shader linearCS(float x0, float y0, float x1, float y1, int colorFrom, int colorTo, ColorSpace cs, int steps) {
        Color4f from = ColorSpace.getSRGB().convert(cs, new Color4f(colorFrom));
        Color4f to = ColorSpace.getSRGB().convert(cs, new Color4f(colorTo));
        Color4f[] colors = new Color4f[steps + 1];
        float[] pos = new float[steps + 1];
        for (int i = 0; i < steps + 1; ++i) {
            pos[i] = (float) i / steps;
            colors[i] = new Color4f(from.r * (1 - pos[i]) + to.r * pos[i],
                                    from.g * (1 - pos[i]) + to.g * pos[i],
                                    from.b * (1 - pos[i]) + to.b * pos[i],
                                    from.a * (1 - pos[i]) + to.a * pos[i]);
        }
        return Shader.linearGradient(x0, y0, x1, y1, colors, cs, pos, Shader.GradientOptions.DEFAULT);
    } 

    private void drawLinearCS(Canvas canvas) {
        for (ColorSpace cs: new ColorSpace[] {null, ColorSpace.getSRGB(), ColorSpace.getSRGBLinear()}) {
            canvas.save();

            int[][] colors = new int[][] {
                new int[] {0xFFFF0000, 0xFF00FF00},
                new int[] {0xFFFF0000, 0xFF0000FF},
                new int[] {0xFFFF0000, 0xFF00FFFF},
                new int[] {0xFF0000FF, 0xFF00FF00},
                new int[] {0xFF00FFFF, 0xFFFF00FF},
                new int[] {0xFF00FF00, 0xFFFF00FF},
                new int[] {0xFF000000, 0xFFFFFFFF},
                new int[] {0xFF555555, 0xFFAAAAAA},
            };
                
            try (Paint fill = new Paint()) {
                for (int[] pair: colors) {
                    try (var shader = linearCS(31, 0, 129, 0, pair[0], pair[1], cs, 10)) {
                        fill.setColor(pair[0]);
                        canvas.drawRect(Rect.makeXYWH(0, 0, 30, 30), fill);

                        fill.setShader(shader);
                        canvas.drawRect(Rect.makeXYWH(31, 0, 98, 30), fill);
                        fill.setShader(null);

                        fill.setColor(pair[1]);
                        canvas.drawRect(Rect.makeXYWH(130, 0, 30, 30), fill);

                        canvas.translate(170, 0);
                    }
                }
            }

            canvas.restore();
            canvas.translate(0, 40);
        }
    }

    private void drawBlending(Canvas canvas) {
        int pink = 0xFFFF0080;
        int purple = 0xFF8000FF;
        int orange = 0xFFFFA500;
        int transparentBlack = 0x00000000;

        try (Paint fill = new Paint();
             Shader gr1 = Shader.linearGradient(0, 0, 0, 100, new int[] {pink, purple});
             Shader gr2 = Shader.linearGradient(0, 0, 400, 0, new int[] {orange, transparentBlack});
             Shader gr3 = Shader.linearGradient(0, 0, 400, 0, new int[] {orange, transparentBlack}, null, Shader.GradientOptions.DEFAULT.unpremul());
             Shader gr4 = Shader.blend(BlendMode.SRC_OVER, gr1, gr2);)
        {
            canvas.save();
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setColor(pink));
            canvas.translate(90, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 400, 80), fill.setShader(gr1));
            canvas.translate(410, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setShader(null).setColor(purple));
            canvas.restore();

            canvas.translate(0, 90);
            canvas.save();
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setColor(orange));
            canvas.translate(90, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 400, 80), fill.setShader(gr2));
            canvas.translate(410, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setShader(null).setColor(transparentBlack));
            canvas.restore();

            canvas.translate(0, 90);
            canvas.save();
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setColor(orange));
            canvas.translate(90, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 400, 80), fill.setShader(gr3));
            canvas.translate(410, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setShader(null).setColor(transparentBlack));
            canvas.restore();

            canvas.translate(0, 90);
            canvas.save();
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setColor(orange));
            canvas.translate(90, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 400, 80), fill.setShader(gr4));
            canvas.translate(410, 0);
            canvas.drawRect(Rect.makeXYWH(0, 0, 80, 80), fill.setShader(gr1));
            canvas.restore();
        }
    }
}