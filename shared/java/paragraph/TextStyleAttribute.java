package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

public enum TextStyleAttribute {
    NONE,
    ALL_ATTRIBUTES,
    FONT,
    FOREGROUND,
    BACKGROUND,
    SHADOW,
    DECORATIONS,
    LETTER_SPACING,
    WORD_SPACING,
    FONT_EXACT;

    @ApiStatus.Internal public static final TextStyleAttribute[] _values = values();
}
