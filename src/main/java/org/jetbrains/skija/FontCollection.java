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

    public SkTypeface defaultFallback(int unicode, FontStyle style, String locale) {
        Native.onNativeCall();
        return new SkTypeface(nDefaultFallbackChar(nativeInstance, unicode, style.getWeight(), style.getWidth(), style.getSlant().ordinal(), locale));
    }

    public SkTypeface defaultFallback() {
        Native.onNativeCall();
        return new SkTypeface(nDefaultFallback(nativeInstance));
    }

    public FontCollection setEnableFallback(boolean value) {
        Native.onNativeCall();
        nSetEnableFallback(nativeInstance, value);
        return this;
    }

    protected FontCollection(long nativeInstance) { super(nativeInstance); }
    private static native long nInit();
    private static native long nGetFontManagersCount(long ptr);
    private static native long nSetAssetFontManager(long ptr, long fontManagerPtr);
    private static native long nSetDynamicFontManager(long ptr, long fontManagerPtr);
    private static native long nSetTestFontManager(long ptr, long fontManagerPtr);
    private static native long nSetDefaultFontManager(long ptr, long fontManagerPtr, String defaultFamilyName);
    private static native long nGetFallbackManager(long ptr);
    private static native long nDefaultFallbackChar(long ptr, int unicode, int weight, int width, int slant, String locale);
    private static native long nDefaultFallback(long ptr);
    private static native long nSetEnableFallback(long ptr, boolean value);
}