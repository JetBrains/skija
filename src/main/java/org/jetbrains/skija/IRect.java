package org.jetbrains.skija;

public class IRect {
    public final int left;
    public final int top;
    public final int right;
    public final int bottom;

    protected IRect(int l, int t, int r, int b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }

    public int getWidth() { return Math.abs(right - left); }
    public int getHeight() { return Math.abs(bottom - top); }

    public static IRect makeLTRB(int l, int t, int r, int b) {
        return new IRect(l, t, r, b);
    }

    public static IRect makeXYWH(int l, int t, int w, int h) {
        return new IRect(l, t, l + w, t + h);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || IRect.class != object.getClass()) return false;
        IRect that = (IRect) object;
        return java.lang.Integer.compare(that.left, left) == 0 &&
                java.lang.Integer.compare(that.top, top) == 0 &&
                java.lang.Integer.compare(that.right, right) == 0 &&
                java.lang.Integer.compare(that.bottom, bottom) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(left, top, right, bottom);
    }

    @Override
    public java.lang.String toString() {
        return "IRect{left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + '}';
    }


}