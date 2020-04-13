package org.jetbrains.skija;

public class Rect {
    public final float left;
    public final float top;
    public final float right;
    public final float bottom;

    protected Rect(float l, float t, float r, float b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }

    public float getWidth() { return Math.abs(right - left); }
    public float getHeight() { return Math.abs(bottom - top); }

    public static Rect makeLTRB(float l, float t, float r, float b) {
        return new Rect(l, t, r, b);
    }

    public static Rect makeXYWH(float l, float t, float w, float h) {
        return new Rect(l, t, l + w, t + h);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Rect.class != object.getClass()) return false;
        Rect that = (Rect) object;
        return java.lang.Float.compare(that.left, left) == 0 &&
                java.lang.Float.compare(that.top, top) == 0 &&
                java.lang.Float.compare(that.right, right) == 0 &&
                java.lang.Float.compare(that.bottom, bottom) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(left, top, right, bottom);
    }

    @Override
    public java.lang.String toString() {
        return "Rect{left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + '}';
    }
}