package skija;

public class RectInscribed {
    public final float left;
    public final float top;
    public final float right;
    public final float bottom;
    public final float[] radii;

    private RectInscribed(float l, float t, float r, float b, float[] radii) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
        this.radii = radii;
    }

    public float getWidth() { return Math.abs(right - left); }
    public float getHeight() { return Math.abs(bottom - top); }

    public static RectInscribed rectLTRB(float l, float t, float r, float b) {
        return new RectInscribed(l, t, r, b, new float[0]);
    }

    public static RectInscribed ovalLTRB(float l, float t, float r, float b) {
        return new RectInscribed(l, t, r, b, new float[] { Math.abs(r - l) / 2f, Math.abs(b - t) / 2f });
    }

    public static RectInscribed roundedRectLTRB(float l, float t, float r, float b, float radii) {
        return new RectInscribed(l, t, r, b, new float[] { radii } );
    }

    public static RectInscribed roundedRectLTRB(float l, float t, float r, float b, float tlRad, float trRad, float brRad, float blRad) {
        return new RectInscribed(l, t, r, b, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RectInscribed ninePatchLTRB(float l, float t, float r, float b, float lRad, float tRad, float rRad, float bRad) {
        return new RectInscribed(l, t, r, b, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RectInscribed complexLTRB(float l, float t, float r, float b, float[] radii) {
        return new RectInscribed(l, t, r, b, radii);
    }


    public static RectInscribed rectXYWH(float l, float t, float w, float h) {
        return new RectInscribed(l, t, l + w, t + h, new float[0]);
    }

    public static RectInscribed ovalXYWH(float l, float t, float w, float h) {
        return new RectInscribed(l, t, l + w, t + h, new float[] { w / 2f, h / 2f });
    }

    public static RectInscribed roundedRectXYWH(float l, float t, float w, float h, float radii) {
        return new RectInscribed(l, t, l + w, t + h, new float[] { radii } );
    }

    public static RectInscribed roundedRectXYWH(float l, float t, float w, float h, float tlRad, float trRad, float brRad, float blRad) {
        return new RectInscribed(l, t, l + w, t + h, new float[] { tlRad, trRad, brRad, blRad });
    }

    public static RectInscribed ninePatchXYWH(float l, float t, float w, float h, float lRad, float tRad, float rRad, float bRad) {
        return new RectInscribed(l, t, l + w, t + h, new float[] { lRad, tRad, rRad, tRad, rRad, bRad, lRad, bRad });
    }

    public static RectInscribed complexXYWH(float l, float t, float w, float h, float[] radii) {
        return new RectInscribed(l, t, l + w, t + h, radii);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RectInscribed that = (RectInscribed) object;
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
        return "RectInscribed{" +
                "left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", radii=" + java.util.Arrays.toString(radii) +
                '}';
    }
}