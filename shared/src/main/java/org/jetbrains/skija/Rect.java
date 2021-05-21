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
        return _right - _left;
    }

    public float getHeight() {
        return _bottom - _top;
    }

    @NotNull @Contract("_, _, _, _ -> new")
    public static Rect makeLTRB(float l, float t, float r, float b) {
        if (l > r)
            throw new IllegalArgumentException("Rect::makeLTRB expected l <= r, got " + l + " > " + r);
        if (t > b)
            throw new IllegalArgumentException("Rect::makeLTRB expected t <= b, got " + t + " > " + b);
        return new Rect(l, t, r, b);
    }

    @NotNull @Contract("_, _ -> new")
    public static Rect makeWH(float w, float h) {
        if (w < 0)
            throw new IllegalArgumentException("Rect::makeWH expected w >= 0, got: " + w);
        if (h < 0)
            throw new IllegalArgumentException("Rect::makeWH expected h >= 0, got: " + h);
        return new Rect(0, 0, w, h);
    }

    @NotNull @Contract("_, _ -> new")
    public static Rect makeWH(@NotNull Point size) {
        assert size != null : "Rect::makeWH expected size != null";
        return makeWH(size._x, size._y);
    }

    @NotNull @Contract("_, _, _, _ -> new")
    public static Rect makeXYWH(float l, float t, float w, float h) {
        if (w < 0)
            throw new IllegalArgumentException("Rect::makeXYWH expected w >= 0, got: " + w);
        if (h < 0)
            throw new IllegalArgumentException("Rect::makeXYWH expected h >= 0, got: " + h);
        return new Rect(l, t, l + w, t + h);
    }

    @Nullable
    public Rect intersect(@NotNull Rect other) {
        assert other != null : "Rect::intersect expected other != null";
        if (_right <= other._left || other._right <= _left || _bottom <= other._top || other._bottom <= _top)
            return null;
        return new Rect(Math.max(_left, other._left), Math.max(_top, other._top), Math.min(_right, other._right), Math.min(_bottom, other._bottom));
    }

    @NotNull
    public Rect scale(float scale) {
        return scale(scale, scale);
    }

    @NotNull
    public Rect scale(float sx, float sy) {
        return new Rect(_left * sx, _top * sy, _right * sx, _bottom * sy);
    }

    @NotNull
    public Rect offset(float dx, float dy) {
        return new Rect(_left + dx, _top + dy, _right + dx, _bottom + dy);
    }

    @NotNull
    public Rect offset(@NotNull Point vec) {
        assert vec != null : "Rect::offset expected vec != null";
        return offset(vec._x, vec._y);
    }

    @NotNull @Contract("-> new")
    public IRect toIRect() {
        return new IRect((int) _left, (int) _top, (int) _right, (int) _bottom);
    }

    @NotNull
    public Rect inflate(float spread) {
        if (spread <= 0)
            return Rect.makeLTRB(_left - spread,
                                 _top - spread,
                                 Math.max(_left - spread, _right + spread),
                                 Math.max(_top - spread, _bottom + spread));
        else
            return RRect.makeLTRB(_left - spread,
                                  _top - spread,
                                  Math.max(_left - spread, _right + spread),
                                  Math.max(_top - spread, _bottom + spread),
                                  spread);
    }

    public boolean isEmpty() {
        return _right == _left || _top == _bottom;
    }
}