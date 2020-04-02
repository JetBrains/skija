package skija;

public class RoundedRect extends Rect {
    public final float[] radii;

    protected RoundedRect(float l, float t, float r, float b, float[] radii) {
        super(l, t, r, b);
        this.radii = radii;
    }

    public static RoundedRect makeLTRB(float l, float t, float r, float b, float radius) {
        return new RoundedRect(l, t, r, b, new float[] { radius } );
    }

    public static RoundedRect makeLTRB(float l, float t, float r, float b, float xRad, float yRad) {
        return new RoundedRect(l, t, r, b, new float[] { xRad, yRad } );
    }

    public static RoundedRect makeLTRB(float l, float t, float r, float b, float tlRad, float trRad, float brRad, float blRad) {
        return new RoundedRect(l, t, r, b, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RoundedRect makeNinePatchLTRB(float l, float t, float r, float b, float lRad, float tRad, float rRad, float bRad) {
        return new RoundedRect(l, t, r, b, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RoundedRect makeComplexLTRB(float l, float t, float r, float b, float[] radii) {
        return new RoundedRect(l, t, r, b, radii);
    }

    public static RoundedRect makeOvalLTRB(float l, float t, float r, float b) {
        return new RoundedRect(l, t, r, b, new float[] { Math.abs(r - l) / 2f, Math.abs(b - t) / 2f } );
    }

    public static RoundedRect makePillLTRB(float l, float t, float r, float b) {
        return new RoundedRect(l, t, r, b, new float[] { Math.min(Math.abs(r - l), Math.abs(t - b)) / 2f } );
    }

    public static RoundedRect makeXYWH(float l, float t, float w, float h, float radius) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { radius } );
    }

    public static RoundedRect makeXYWH(float l, float t, float w, float h, float xRad, float yRad) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { xRad, yRad } );
    }

    public static RoundedRect makeXYWH(float l, float t, float w, float h, float tlRad, float trRad, float brRad, float blRad) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RoundedRect makeNinePatchXYWH(float l, float t, float w, float h, float lRad, float tRad, float rRad, float bRad) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RoundedRect makeComplexXYWH(float l, float t, float w, float h, float[] radii) {
        return new RoundedRect(l, t, l + w, t + h, radii);
    }

    public static RoundedRect makeOvalXYWH(float l, float t, float w, float h) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { w / 2f, h / 2f } );
    }

    public static RoundedRect makePillXYWH(float l, float t, float w, float h) {
        return new RoundedRect(l, t, l + w, t + h, new float[] { Math.min(w, h) / 2f } );
    }    

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || RoundedRect.class != object.getClass()) return false;
        RoundedRect that = (RoundedRect) object;
        return java.lang.Float.compare(that.left, left) == 0 &&
                java.lang.Float.compare(that.top, top) == 0 &&
                java.lang.Float.compare(that.right, right) == 0 &&
                java.lang.Float.compare(that.bottom, bottom) == 0 &&
                java.util.Arrays.equals(radii, that.radii);
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(left, top, right, bottom);
        result = 31 * result + java.util.Arrays.hashCode(radii);
        return result;
    }

    @Override
    public java.lang.String toString() {
        return "RoundedRect{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", radii=" + java.util.Arrays.toString(radii) +
                '}';
    }
}