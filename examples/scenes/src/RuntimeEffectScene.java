package org.jetbrains.skija.examples.scenes;

import java.nio.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.io.*;
import org.jetbrains.skija.*;

public class RuntimeEffectScene extends Scene {
    public final Shader _texture;
    public final RuntimeEffect _effect;

    public RuntimeEffectScene() {
        try {
            _texture = Image.makeFromEncoded(Files.readAllBytes(Path.of(file("images/triangle.png")))).makeShader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        _effect = RuntimeEffect.makeForShader(
            "uniform float xScale;\n" +
            "uniform float xBias;\n" +
            "uniform float yScale;\n" +
            "uniform float yBias;\n" +
            "uniform shader input;\n" +
            "half4 main(float2 xy) {\n" +
            "  half4 tex = sample(input, mod(xy, 100));\n" +
            "  return half4((xy.x - xBias) /  xScale / 2 + 0.5, (xy.y - yBias) / yScale / 2 + 0.5, tex.b, 1);\n" +
            "}"
        );
    }

    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var bb = ByteBuffer.allocate(4 * 4).order(ByteOrder.nativeOrder());
        bb.putFloat((float) width);
        bb.putFloat((float) xpos);
        bb.putFloat((float) height);
        bb.putFloat((float) ypos);
        try (var data = Data.makeFromBytes(bb.array());
             var paint = new Paint();
             var shader = _effect.makeShader(data, new Shader[] { _texture }, null, true);)
        {
            paint.setShader(shader);
            canvas.drawPaint(paint);
        }
    }
}
