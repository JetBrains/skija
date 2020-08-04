package org.jetbrains.skija;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.*;

@Getter
@EqualsAndHashCode
@ToString
public class Rect {
    public final float _left;
    public final float _top;
    public final float _right;
    public final float _bottom;

    @ApiStatus.Internal
    public Rect(float l, float t, float r, float b) {
        this._left = l;
        this._top = t;
        this._right = r;
        this._bottom = b;
    }

    public float getWidth() {
        return Math.abs(_right - _left);
    }

    public float getHeight() {
        return Math.abs(_bottom - _top);
    }

    public static Rect makeLTRB(float l, float t, float r, float b) {
        return new Rect(l, t, r, b);
    }

    public static Rect makeWH(float w, float h) {
        return new Rect(0, 0, w, h);
    }

    public static Rect makeXYWH(float l, float t, float w, float h) {
        return new Rect(l, t, l + w, t + h);
    }

    @Nullable
    public Rect intersect(Rect other) {
        if (_right <= other._left || other._right <= _left || _bottom <= other._top || other._bottom <= _top)
            return null;
        return new Rect(Math.max(_left, other._left), Math.max(_top, other._top), Math.min(_right, other._right), Math.min(_bottom, other._bottom));
    }

    public Rect translate(float dx, float dy) {
        return new Rect(_left + dx, _top + dy, _right + dx, _bottom + dy);
    }

    public IRect toIRect() {
        return new IRect((int) _left, (int) _top, (int) _right, (int) _bottom);
    }
}