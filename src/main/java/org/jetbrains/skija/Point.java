package org.jetbrains.skija;

public class Point {
    public final float x;
    public final float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || Point.class != object.getClass()) return false;
        Point that = (Point) object;
        return java.lang.Float.compare(that.x, x) == 0 &&
                java.lang.Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    @Override
    public java.lang.String toString() {
        return "Point{x=" + x + ", y=" + y + '}';
    }
}