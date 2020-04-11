package skija;

import java.util.Objects;

public class FontFeature {
    public final int tag;
    public final int value;
    public final int start;
    public final int end;

    public static final int GLOBAL_START = 0;
    public static final int GLOBAL_END = Integer.MAX_VALUE;

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

    public FontFeature(int tag, int value, int start, int end) {
        this.tag = tag;
        this.value = value;
        this.start = start;
        this.end = end;
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

    public String getTagName() { return untag(tag); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontFeature that = (FontFeature) o;
        return tag == that.tag &&
                value == that.value &&
                start == that.start &&
                end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, value, start, end);
    }

    @Override
    public String toString() {
        String range = "";
        if (start > 0 || end < Integer.MAX_VALUE) {
            range = "[" + (start > 0 ? start : "") + ":" + (end < Integer.MAX_VALUE ? end : "") + "]";
        }
        String valuePrefix = "";
        String valueSuffix = "";
        if (value == 0)
            valuePrefix = "-";
        else if (value == 1)
            valuePrefix = "+";
        else
            valueSuffix = "=" + value;
        return valuePrefix + getTagName() + range + valueSuffix;
    }
}