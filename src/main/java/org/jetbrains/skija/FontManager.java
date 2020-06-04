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
        long ptr = nCreateStyleSet(nativeInstance, index);
        return ptr == 0 ? null : new FontStyleSet(ptr);
    }

    /**
     * The caller must call {@link #close()} on the returned object.
     * Never returns null; will return an empty set if the name is not found.
     *
     * Passing null as the parameter will return the default system family.
     * Note that most systems don't have a default system family, so passing null will often
     * result in the empty set.
     *
     * It is possible that this will return a style set not accessible from
     * {@link #createStyleSet(int)} due to hidden or auto-activated fonts.
     */
    public FontStyleSet matchFamily(String familyName) {
        Native.onNativeCall();
        return new FontStyleSet(nMatchFamily(nativeInstance, familyName));
    }

    /**
     * Find the closest matching typeface to the specified familyName and style
     * and return a ref to it. The caller must call {@link #close()} on the returned
     * object. Will return null if no 'good' match is found.
     *
     * Passing null as the parameter for `familyName` will return the
     * default system font.
     *
     * It is possible that this will return a style set not accessible from
     * {@link #createStyleSet(int)} or {@link #matchFamily(String)} due to hidden or
     * auto-activated fonts.
     */
    public SkTypeface matchFamilyStyle(String familyName, FontStyle style) {
        Native.onNativeCall();
        long ptr = nMatchFamilyStyle(nativeInstance, familyName, style.value);
        return ptr == 0 ? null : new SkTypeface(ptr);
    }

    /**
     * Use the system fallback to find a typeface for the given character.
     * Note that bcp47 is a combination of ISO 639, 15924, and 3166-1 codes,
     * so it is fine to just pass a ISO 639 here.
     *
     * Will return null if no family can be found for the character
     * in the system fallback.
     *
     * Passing `null` as the parameter for `familyName` will return the
     * default system font.
     *
     * bcp47[0] is the least significant fallback, bcp47[bcp47.length-1] is the
     * most significant. If no specified bcp47 codes match, any font with the
     * requested character will be matched.
     */
    public SkTypeface matchFamilyStyleCharacter(String familyName, FontStyle style, String[] bcp47, int character) {
        Native.onNativeCall();
        long ptr = nMatchFamilyStyleCharacter(nativeInstance, familyName, style.value, bcp47, character);
        return ptr == 0 ? null : new SkTypeface(ptr);
    }

    public SkTypeface matchFaceStyle(SkTypeface typeface, FontStyle style) {
        Native.onNativeCall();
        long ptr = nMatchFaceStyle(nativeInstance, Native.pointer(typeface), style.value);
        return ptr == 0 ? null : new SkTypeface(ptr);
    }

    /**
     * Create a typeface for the specified data and TTC index (pass 0 for none)
     * or null if the data is not recognized. The caller must call {@link #close()} on
     * the returned object if it is not null.
     */
    public SkTypeface makeFromData(Data data) {
        return makeFromData(data, 0);
    }

    public SkTypeface makeFromData(Data data, int ttcIndex) {
        Native.onNativeCall();
        long ptr = nMakeFromData(nativeInstance, Native.pointer(data), ttcIndex);
        return ptr == 0 ? null : new SkTypeface(ptr);
    }

    private static class DefaultHolder {
        static { Native.onNativeCall(); }
        public static final FontManager INSTANCE = new FontManager(nDefault(), false);
    }

    /**
     * Return the default fontmgr.
     */
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