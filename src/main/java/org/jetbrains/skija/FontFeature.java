package org.jetbrains.skija;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;

import java.util.Objects;

@AllArgsConstructor
@EqualsAndHashCode
public class FontFeature {
    public final int _tag;
    @Getter
    public final int _value;
    @Getter
    public final int _start;
    @Getter
    public final int _end;

    public static final int GLOBAL_START = 0;
    public static final int GLOBAL_END = Integer.MAX_VALUE;
    public static final FontFeature[] EMPTY = new FontFeature[0];

    public static int tag(String name) {
        assert name.length() == 4 : "Name must be exactly 4 symbols, got: '" + name + "'";
        return (name.charAt(0) & 0xFF) << 24
             | (name.charAt(1) & 0xFF) << 16
             | (name.charAt(2) & 0xFF) << 8
             | (name.charAt(3) & 0xFF);
    }

    public static String untag(int tag) {
        return new String(new byte[] { (byte) (tag >> 24 & 0xFF),
                                       (byte) (tag >> 16 & 0xFF),
                                       (byte) (tag >> 8 & 0xFF),
                                       (byte) (tag & 0xFF) });
    }

    public FontFeature(String feature, int value, int start, int end) {
        this(tag(feature), value, start, end);
    }

    public FontFeature(String feature, int value) {
        this(tag(feature), value, GLOBAL_START, GLOBAL_END);
    }

    public FontFeature(String feature, boolean value) {
        this(tag(feature), value ? 1 : 0, GLOBAL_START, GLOBAL_END);
    }

    public FontFeature(String feature) {
        this(tag(feature), 1, GLOBAL_START, GLOBAL_END);
    }

    public String getTag() {
        return untag(_tag);
    }

    @Override
    public String toString() {
        String range = "";
        if (_start > 0 || _end < Integer.MAX_VALUE) {
            range = "[" + (_start > 0 ? _start : "") + ":" + (_end < Integer.MAX_VALUE ? _end : "") + "]";
        }
        String valuePrefix = "";
        String valueSuffix = "";
        if (_value == 0)
            valuePrefix = "-";
        else if (_value == 1)
            valuePrefix = "+";
        else
            valueSuffix = "=" + _value;
        return "FontFeature(" + valuePrefix + getTag() + range + valueSuffix + ")";
    }
}