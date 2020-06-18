package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.skija.impl.Internal;

@EqualsAndHashCode(callSuper = true)
@ToString
public class RRect extends Rect {
    public final float[] _radii;

    @Internal
    public RRect(float l, float t, float r, float b, float[] radii) {
        super(l, t, r, b);
        this._radii = radii;
    }

    public static RRect makeLTRB(float l, float t, float r, float b, float radius) {
        return new RRect(l, t, r, b, new float[] { radius } );
    }

    public static RRect makeLTRB(float l, float t, float r, float b, float xRad, float yRad) {
        return new RRect(l, t, r, b, new float[] { xRad, yRad } );
    }

    public static RRect makeLTRB(float l, float t, float r, float b, float tlRad, float trRad, float brRad, float blRad) {
        return new RRect(l, t, r, b, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RRect makeNinePatchLTRB(float l, float t, float r, float b, float lRad, float tRad, float rRad, float bRad) {
        return new RRect(l, t, r, b, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RRect makeComplexLTRB(float l, float t, float r, float b, float[] radii) {
        return new RRect(l, t, r, b, radii);
    }

    public static RRect makeOvalLTRB(float l, float t, float r, float b) {
        return new RRect(l, t, r, b, new float[] { Math.abs(r - l) / 2f, Math.abs(b - t) / 2f } );
    }

    public static RRect makePillLTRB(float l, float t, float r, float b) {
        return new RRect(l, t, r, b, new float[] { Math.min(Math.abs(r - l), Math.abs(t - b)) / 2f } );
    }

    public static RRect makeXYWH(float l, float t, float w, float h, float radius) {
        return new RRect(l, t, l + w, t + h, new float[] { radius } );
    }

    public static RRect makeXYWH(float l, float t, float w, float h, float xRad, float yRad) {
        return new RRect(l, t, l + w, t + h, new float[] { xRad, yRad } );
    }

    public static RRect makeXYWH(float l, float t, float w, float h, float tlRad, float trRad, float brRad, float blRad) {
        return new RRect(l, t, l + w, t + h, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RRect makeNinePatchXYWH(float l, float t, float w, float h, float lRad, float tRad, float rRad, float bRad) {
        return new RRect(l, t, l + w, t + h, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RRect makeComplexXYWH(float l, float t, float w, float h, float[] radii) {
        return new RRect(l, t, l + w, t + h, radii);
    }

    public static RRect makeOvalXYWH(float l, float t, float w, float h) {
        return new RRect(l, t, l + w, t + h, new float[] { w / 2f, h / 2f } );
    }

    public static RRect makePillXYWH(float l, float t, float w, float h) {
        return new RRect(l, t, l + w, t + h, new float[] { Math.min(w, h) / 2f } );
    }
}