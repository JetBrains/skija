package org.jetbrains.skija.paragraph;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Data;
import lombok.With;

import org.jetbrains.annotations.*;

@AllArgsConstructor @Data @With
public class DecorationStyle {
    public static final DecorationStyle NONE = new DecorationStyle(false, false, false, true, 0xFF000000, DecorationLineStyle.SOLID, 1f);

    @Getter(AccessLevel.NONE)
    public final boolean _underline;
    @Getter(AccessLevel.NONE)
    public final boolean _overline;
    @Getter(AccessLevel.NONE)
    public final boolean _lineThrough;
    @Getter(AccessLevel.NONE)
    public final boolean _gaps;
    public final int _color;
    public final DecorationLineStyle _lineStyle;
    public final float _thicknessMultiplier;

    @ApiStatus.Internal
    public DecorationStyle(boolean underline, boolean overline, boolean lineThrough, boolean gaps, int color, int lineStyle, float thicknessMultiplier) {
        this(underline, overline, lineThrough, gaps, color, DecorationLineStyle._values[lineStyle], thicknessMultiplier);
    }

    public boolean hasUnderline() {
        return _underline;
    }

    public boolean hasOverline() {
        return _overline;
    }

    public boolean hasLineThrough() {
        return _lineThrough;
    }

    public boolean hasGaps() {
        return _gaps;
    }
}