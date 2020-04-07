package skija;

public class FontFeature {
    public final int tag;
    public final int value;
    public final int start;
    public final int end;

    public static final int GLOBAL_START = 0;
    public static final int GLOBAL_END = Integer.MAX_VALUE;

    public static int tag(String feature) {
        assert feature.length() == 4 : "Feature must be exactly 4 symbols, got: '" + feature + "'";
        return (feature.charAt(0) & 0xFF) << 24
             | (feature.charAt(1) & 0xFF) << 16
             | (feature.charAt(2) & 0xFF) << 8
             | (feature.charAt(3) & 0xFF);
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

    public String getFeature() { return untag(tag); }
}