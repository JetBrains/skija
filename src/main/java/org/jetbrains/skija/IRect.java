package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.skija.impl.Internal;

@Getter
@EqualsAndHashCode
@ToString
public class IRect {
    public final int _left;
    public final int _top;
    public final int _right;
    public final int _bottom;

    @Internal
    public IRect(int l, int t, int r, int b) {
        _left = l;
        _top = t;
        _right = r;
        _bottom = b;
    }

    public int getWidth() {
        return Math.abs(_right - _left);
    }

    public int getHeight() {
        return Math.abs(_bottom - _top);
    }

    public static IRect makeLTRB(int l, int t, int r, int b) {
        return new IRect(l, t, r, b);
    }

    public static IRect makeXYWH(int l, int t, int w, int h) {
        return new IRect(l, t, l + w, t + h);
    }

    public static IRect makeWH(int w, int h) {
        return new IRect(0, 0, w, h);
    }
}