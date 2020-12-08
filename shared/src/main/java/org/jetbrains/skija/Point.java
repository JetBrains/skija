package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

@Data
public class Point {
    public static final Point ZERO = new Point(0, 0);

    public final float _x;
    public final float _y;

    @Contract("null -> null; !null -> new")
    public static @Nullable float[] flattenArray(@Nullable Point[] pts) {
        if (pts == null) return null;
        float[] arr = new float[pts.length * 2];
        for (int i = 0; i < pts.length; ++i) {
            arr[i * 2]     = pts[i]._x;
            arr[i * 2 + 1] = pts[i]._y;
        }
        return arr;
    }

    @Contract("null -> null; !null -> new")
    public static @Nullable Point[] fromArray(@Nullable float[] pts) {
        if (pts == null) return null;
        assert pts.length % 2 == 0 : "Expected " + pts.length + " % 2 == 0";
        Point[] arr = new Point[pts.length / 2];
        for (int i = 0; i < pts.length / 2; ++i)
            arr[i] = new Point(pts[i * 2], pts[i * 2 + 1]);
        return arr;
    }

    public Point offset(float dx, float dy) {
        return new Point(_x + dx, _y + dy);
    }

    public boolean isEmpty() {
        return _x <= 0 || _y <= 0;
    }
}