package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.*;

public class FontCollection extends RefCnt {
    public FontCollection() {
        this(nInit());
        Stats.onNativeCall();
    }

    public long getFontManagersCount() {
        Stats.onNativeCall();
        return nGetFontManagersCount(_ptr);
    }
    
    public FontCollection setAssetFontManager(FontManager fontManager) {
        Stats.onNativeCall();
        nSetAssetFontManager(_ptr, Native.getPtr(fontManager));
        return this;
    }

    public FontCollection setDynamicFontManager(FontManager fontManager) {
        Stats.onNativeCall();
        nSetDynamicFontManager(_ptr, Native.getPtr(fontManager));
        return this;
    }

    public FontCollection setTestFontManager(FontManager fontManager) {
        Stats.onNativeCall();
        nSetTestFontManager(_ptr, Native.getPtr(fontManager));
        return this;
    }

    public FontCollection setDefaultFontManager(FontManager fontManager) {
        return setDefaultFontManager(fontManager, null);
    }

    public FontCollection setDefaultFontManager(FontManager fontManager, String defaultFamilyName) {
        Stats.onNativeCall();
        nSetDefaultFontManager(_ptr, Native.getPtr(fontManager), defaultFamilyName);
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
        Stats.onNativeCall();
        long ptr = nGetFallbackManager(_ptr);
        return fontManagerCtor.apply(ptr);
        // return new FontManager(ptr);
    }

    public Typeface[] findTypefaces(String[] familyNames, FontStyle style) {
        Stats.onNativeCall();
        long[] ptrs = nFindTypefaces(_ptr, familyNames, style.value);
        Typeface[] res = new Typeface[ptrs.length];
        for (int i = 0; i < ptrs.length; ++i) {
            res[i] = typefaceCtor.apply(ptrs[i]);
        }
        return res;
    }

    public Typeface defaultFallback(int unicode, FontStyle style, String locale) {
        Stats.onNativeCall();
        return typefaceCtor.apply(nDefaultFallbackChar(_ptr, unicode, style.value, locale));
    }

    public Typeface defaultFallback() {
        Stats.onNativeCall();
        return typefaceCtor.apply(nDefaultFallback(_ptr));
    }

    public FontCollection setEnableFallback(boolean value) {
        Stats.onNativeCall();
        nSetEnableFallback(_ptr, value);
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