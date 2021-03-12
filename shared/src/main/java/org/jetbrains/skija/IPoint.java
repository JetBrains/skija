package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

@Data
public class IPoint {
    public static final IPoint ZERO = new IPoint(0, 0);

    @ApiStatus.Internal
    public final int _x;

    @ApiStatus.Internal
    public final int _y;

    @NotNull
    public IPoint offset(int dx, int dy) {
        return new IPoint(_x + dx, _y + dy);
    }

    @NotNull
    public IPoint offset(@NotNull IPoint vec) {
        assert vec != null : "IPoint::offset expected other != null";
        return offset(vec._x, vec._y);
    }

    public boolean isEmpty() {
        return _x <= 0 || _y <= 0;
    }

    @ApiStatus.Internal
    public static IPoint _makeFromLong(long l) {
        return new IPoint((int) (l >>> 32), (int) (l & 0xFFFFFFFF));
    }
}