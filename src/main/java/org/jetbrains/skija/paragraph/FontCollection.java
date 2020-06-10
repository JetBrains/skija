package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.*;

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

    private static final java.util.function.LongFunction<Typeface> typefaceCtor;
    private static final java.util.function.LongFunction<FontManager> fontManagerCtor; 
    static {
        try {
            typefaceCtor = (ptr) -> {
                try {
                    return Typeface.class.getConstructor​(Long.TYPE).newInstance(ptr);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            fontManagerCtor = (ptr) -> {
                try {
                    return FontManager.class.getConstructor​(Long.TYPE).newInstance(ptr);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public FontManager getFallbackManager() {
        Native.onNativeCall();
        long ptr = nGetFallbackManager(nativeInstance);
        return fontManagerCtor.apply(ptr);
        // return new FontManager(ptr);
    }

    public Typeface[] findTypefaces(String[] familyNames, FontStyle style) {
        Native.onNativeCall();
        long[] ptrs = nFindTypefaces(nativeInstance, familyNames, style.value);
        Typeface[] res = new Typeface[ptrs.length];
        for (int i = 0; i < ptrs.length; ++i) {
            res[i] = typefaceCtor.apply(ptrs[i]);
        }
        return res;
    }

    public Typeface defaultFallback(int unicode, FontStyle style, String locale) {
        Native.onNativeCall();
        return typefaceCtor.apply(nDefaultFallbackChar(nativeInstance, unicode, style.value, locale));
    }

    public Typeface defaultFallback() {
        Native.onNativeCall();
        return typefaceCtor.apply(nDefaultFallback(nativeInstance));
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