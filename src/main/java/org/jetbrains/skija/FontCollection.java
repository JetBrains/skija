package org.jetbrains.skija;

public class FontCollection extends RefCounted {
    public FontCollection() {
        this(nInit());
        Native.onNativeCall();
    }

    public long getFontManagersCount() {
        Native.onNativeCall();
        return nGetFontManagersCount(nativeInstance);
    }
    
    public FontCollection setAssetFontManager(FontManager fontManager) {
        Native.onNativeCall();
        nSetAssetFontManager(nativeInstance, Native.pointer(fontManager));
        return this;
    }

    public FontCollection setDynamicFontManager(FontManager fontManager) {
        Native.onNativeCall();
        nSetDynamicFontManager(nativeInstance, Native.pointer(fontManager));
        return this;
    }

    public FontCollection setTestFontManager(FontManager fontManager) {
        Native.onNativeCall();
        nSetTestFontManager(nativeInstance, Native.pointer(fontManager));
        return this;
    }

    public FontCollection setDefaultFontManager(FontManager fontManager) {
        return setDefaultFontManager(fontManager, null);
    }

    public FontCollection setDefaultFontManager(FontManager fontManager, String defaultFamilyName) {
        Native.onNativeCall();
        nSetDefaultFontManager(nativeInstance, Native.pointer(fontManager), defaultFamilyName);
        return this;
    }

    public FontManager getFallbackManager() {
        Native.onNativeCall();
        return new FontManager(nGetFallbackManager(nativeInstance));
    }

    public Typeface[] findTypefaces(String[] familyNames, FontStyle style) {
        Native.onNativeCall();
        long[] ptrs = nFindTypefaces(nativeInstance, familyNames, style.value);
        Typeface[] res = new Typeface[ptrs.length];
        for (int i = 0; i < ptrs.length; ++i) {
            res[i] = new Typeface(ptrs[i]);
        }
        return res;
    }

    public Typeface defaultFallback(int unicode, FontStyle style, String locale) {
        Native.onNativeCall();
        return new Typeface(nDefaultFallbackChar(nativeInstance, unicode, style.value, locale));
    }

    public Typeface defaultFallback() {
        Native.onNativeCall();
        return new Typeface(nDefaultFallback(nativeInstance));
    }

    public FontCollection setEnableFallback(boolean value) {
        Native.onNativeCall();
        nSetEnableFallback(nativeInstance, value);
        return this;
    }

    protected FontCollection(long nativeInstance) { super(nativeInstance); }
    private static native long   nInit();
    private static native long   nGetFontManagersCount(long ptr);
    private static native long   nSetAssetFontManager(long ptr, long fontManagerPtr);
    private static native long   nSetDynamicFontManager(long ptr, long fontManagerPtr);
    private static native long   nSetTestFontManager(long ptr, long fontManagerPtr);
    private static native long   nSetDefaultFontManager(long ptr, long fontManagerPtr, String defaultFamilyName);
    private static native long   nGetFallbackManager(long ptr);
    private static native long[] nFindTypefaces(long ptr, String[] familyNames, int fontStyle);
    private static native long   nDefaultFallbackChar(long ptr, int unicode, int fontStyle, String locale);
    private static native long   nDefaultFallback(long ptr);
    private static native long   nSetEnableFallback(long ptr, boolean value);
}