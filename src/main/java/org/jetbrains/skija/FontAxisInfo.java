package org.jetbrains.skija;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

@Accessors(fluent=true)
@Data
public class FontAxisInfo {
    public static final int FLAG_HIDDEN = 1;

    private final int    axisIndex;
    private final int    tag;
    private final String name;
    @Getter(AccessLevel.NONE)
    private final int    flags;
    private final float  minValue;
    private final float  defaultValue;
    private final float  maxValue;

    @Tolerate
    public FontAxisInfo(int axisIndex, int tag, byte[] nameBytes, int flags, float minValue, float defaultValue, float maxValue) {
        this(axisIndex, tag, new String(nameBytes, StandardCharsets.UTF_8), flags, minValue, defaultValue, maxValue);
    }

    public boolean isHidden() { return (flags & FLAG_HIDDEN) != 0; }
    public String getTagName() { return FontFeature.untag(tag); }
}