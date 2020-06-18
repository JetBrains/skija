package org.jetbrains.skija;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class FontAxisInfo {
    public static final int _FLAG_HIDDEN = 1;

    public final int    _axisIndex;
    @Getter(AccessLevel.NONE)
    public final int    _tag;
    public final String _name;
    @Getter(AccessLevel.NONE)
    public final int    _flags;
    public final float  _minValue;
    public final float  _defaultValue;
    public final float  _maxValue;

    public boolean isHidden() {
        return (_flags & _FLAG_HIDDEN) != 0;
    }

    public String getTag() {
        return FontFeature.untag(_tag);
    }
}