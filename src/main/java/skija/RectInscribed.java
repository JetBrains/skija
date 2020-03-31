package skija;

public class RectInscribed {
    public final float left;
    public final float top;
    public final float width;
    public final float height;
    public final float[] radii;

    private RectInscribed(float left, float top, float width, float height, float[] radii) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.radii = radii;
    }

    public static RectInscribed rect(float left, float top, float width, float height) {
        return new RectInscribed(left, top, width, height, new float[0]);
    }

    public static RectInscribed oval(float left, float top, float width, float height) {
        return new RectInscribed(left, top, width, height, new float[] { width / 2f, height / 2f });
    }

    public static RectInscribed roundedRect(float left, float top, float width, float height, float radii) {
        return new RectInscribed(left, top, width, height, new float[] { radii } );
    }

    public static RectInscribed roundedRect(float left, float top, float width, float height, float topLeftRad, float topRightRad, float bottomRightRad, float bottomLeftRad) {
        return new RectInscribed(left, top, width, height, new float[] { topLeftRad, topRightRad, bottomRightRad, bottomLeftRad });
    }

    public static RectInscribed ninePatch(float left, float top, float width, float height, float leftRad, float topRad, float rightRad, float bottomRad) {
        return new RectInscribed(left, top, width, height, new float[] { leftRad, topRad, rightRad, topRad, rightRad, bottomRad, leftRad, bottomRad });
    }

    public static RectInscribed complex(float left, float top, float width, float height, float[] radii) {
        return new RectInscribed(left, top, width, height, radii);
    }
}