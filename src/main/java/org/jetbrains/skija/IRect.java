package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.*;

@Getter
@EqualsAndHashCode
@ToString
public class IRect {
    public final int _left;
    public final int _top;
    public final int _right;
    public final int _bottom;

    @ApiStatus.Internal
    public IRect(int l, int t, int r, int b) {
        _left = l;
        _top = t;
        _right = r;
        _bottom = b;
    }

    public int getWidth() {
        return _right - _left;
    }

    public int getHeight() {
        return _bottom - _top;
    }

    @Nullable
    public static IRect makeLTRB(int l, int t, int r, int b) {
        return l <= r && t <= b ? new IRect(l, t, r, b) : null;
    }

    @Nullable
    public static IRect makeXYWH(int l, int t, int w, int h) {
        return w >= 0 && h >= 0 ? new IRect(l, t, l + w, t + h) : null;
    }

    @Nullable
    public static IRect makeWH(int w, int h) {
        return w >= 0 && h >= 0 ? new IRect(0, 0, w, h) : null;
    }

    @Nullable
    public IRect intersect(IRect other) {
        if (_right <= other._left || other._right <= _left || _bottom <= other._top || other._bottom <= _top)
            return null;
        return new IRect(Math.max(_left, other._left), Math.max(_top, other._top), Math.min(_right, other._right), Math.min(_bottom, other._bottom));
    }

    public IRect translate(int dx, int dy) {
        return new IRect(_left + dx, _top + dy, _right + dx, _bottom + dy);
    }

    public Rect toRect() {
        return new Rect(_left, _top, _right, _bottom);
    }
}