package org.jetbrains.skija.examples.scenes;

import org.jetbrains.skija.*;
import java.util.*;

public class ShadowUtilsScene extends Scene {
    @Override
    public void draw(Canvas canvas, int width, int height, float dpi, int xpos, int ypos) {
        try (Path path = new Path().moveTo(100, 8)
                            .lineTo(158, 189)
                            .lineTo(4, 77)
                            .lineTo(195, 77)
                            .lineTo(41, 189)
                            .closePath();
             Paint stroke = new Paint().setColor(0xFFFFFFFF);)
        {
            canvas.translate(xpos - 400, ypos - 100);

            var zPlaneParams = new Point3(0, 0, 100);
            var lightPos = new Point3(width / 2 * dpi, 0, 5000);
            var lightRadius = 1000;
            var ambientColor = 0x80ff0000;
            var spotColor = 0x800000ff;
            
            ShadowUtils.drawShadow(canvas, path, zPlaneParams, lightPos, lightRadius, ambientColor, spotColor, false, false);
            canvas.drawPath(path, stroke);

            path.offset(200, 0);
            ShadowUtils.drawShadow(canvas, path, zPlaneParams, lightPos, lightRadius, ambientColor, spotColor, true, false);
            canvas.drawPath(path, stroke);

            path.offset(200, 0);
            ShadowUtils.drawShadow(canvas, path, zPlaneParams, lightPos, lightRadius, ambientColor, spotColor, false, true);
            canvas.drawPath(path, stroke);

            path.offset(200, 0);
            ShadowUtils.drawShadow(canvas, path, zPlaneParams, lightPos, lightRadius, ambientColor, spotColor, true, true);
            canvas.drawPath(path, stroke);
        }

        assert 0x80000000 == ShadowUtils.computeTonalAmbientColor(0x80ff0000, 0x800000ff) : ShadowUtils.computeTonalAmbientColor(0x80ff0000, 0x800000ff);
        assert 0xC900007D == ShadowUtils.computeTonalSpotColor(0x80ff0000, 0x800000ff) : ShadowUtils.computeTonalSpotColor(0x80ff0000, 0x800000ff);
    }
}