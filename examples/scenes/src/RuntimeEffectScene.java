package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;

public class RuntimeEffectScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        var sksl = "const half3 iColor = half3(0, 0.5, 0.75);\n"
                    + "half4 main(float2 coord) {\n"
                    + "  return iColor.rgb1;\n"
                    + "}";

        var effect = RuntimeEffect.makeForShader(sksl);
        var myShader = effect.makeShader(null, null, null, false);

        var p = new Paint();
        p.setShader(myShader);
        canvas.drawPaint(p);
    }
}
