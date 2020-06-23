package org.jetbrains.skija.paragraph;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Data;
import lombok.With;

import org.jetbrains.annotations.*;

@AllArgsConstructor @Data @With
public class Decoration {
    public static final Decoration NONE = new Decoration(false, false, false, DecorationMode.GAPS, 0xFF000000, DecorationStyle.SOLID, 1f);

    @Getter(AccessLevel.NONE)
    public final boolean _underline;
    @Getter(AccessLevel.NONE)
    public final boolean _overline;
    @Getter(AccessLevel.NONE)
    public final boolean _lineThrough;
    public final DecorationMode _mode;
    public final int _color;
    public final DecorationStyle _style;
    public final float _thicknessMultiplier;

    @ApiStatus.Internal
    public Decoration(boolean underline, boolean overline, boolean lineThrough, int mode, int color, int style, float thicknessMultiplier) {
        this(underline, overline, lineThrough, DecorationMode.values()[mode], color, DecorationStyle.values()[style], thicknessMultiplier);
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
}