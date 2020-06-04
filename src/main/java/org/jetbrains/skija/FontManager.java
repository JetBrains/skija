package org.jetbrains.skija;

public class FontManager extends RefCounted {
    public int countFamilies() {
        Native.onNativeCall();
        return nCountFamilies(nativeInstance);
    }

    public String getFamilyName(int index) {
        Native.onNativeCall();
        return nGetFamilyName(nativeInstance, index);
    }

    public FontStyleSet createStyleSet(int index) {
        Native.onNativeCall();
        return new FontStyleSet(nCreateStyleSet(nativeInstance, index));
    }

    public FontStyleSet matchFamily(String familyName) {
        Native.onNativeCall();
        return new FontStyleSet(nMatchFamily(nativeInstance, familyName));
    }

    public SkTypeface matchFamilyStyle(String familyName, FontStyle style) {
        Native.onNativeCall();
        return new SkTypeface(nMatchFamilyStyle(nativeInstance, familyName, style.value));
    }

    public SkTypeface matchFamilyStyleCharacter(String familyName, FontStyle style, String[] bcp47, int character) {
        Native.onNativeCall();
        return new SkTypeface(nMatchFamilyStyleCharacter(nativeInstance, familyName, style.value, bcp47, character));
    }

    public SkTypeface matchFaceStyle(SkTypeface typeface, FontStyle style) {
        Native.onNativeCall();
        return new SkTypeface(nMatchFaceStyle(nativeInstance, Native.pointer(typeface), style.value));
    }

    public SkTypeface makeFromData(Data data) {
        return makeFromData(data, 0);
    }

    public SkTypeface makeFromData(Data data, int ttcIndex) {
        Native.onNativeCall();
        return new SkTypeface(nMakeFromData(nativeInstance, Native.pointer(data), ttcIndex));
    }

    private static class DefaultHolder {
        static { Native.onNativeCall(); }
        public static final FontManager INSTANCE = new FontManager(nDefault(), false);
    }

    public static FontManager getDefault() { return DefaultHolder.INSTANCE; }

    protected FontManager(long nativeInstance) { super(nativeInstance); }
    protected FontManager(long nativeInstance, boolean allowClose) { super(nativeInstance, allowClose); }

    private static native int nCountFamilies(long ptr);
    private static native String nGetFamilyName(long ptr, int index);
    private static native long nCreateStyleSet(long ptr, int index);
    private static native long nMatchFamily(long ptr, String familyName);
    private static native long nMatchFamilyStyle(long ptr, String familyName, int fontStyle);
    private static native long nMatchFamilyStyleCharacter(long ptr, String familyName, int fontStyle, String[] bcp47, int character);
    private static native long nMatchFaceStyle(long ptr, long typefacePtr, int fontStyle);
    private static native long nMakeFromData(long ptr, long dataPtr, int ttcIndex);
    private static native long nDefault();
}