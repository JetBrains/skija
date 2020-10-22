package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class FontCollection extends RefCnt {
    static { Library.load(); }
    
    public FontCollection() {
        this(_nMake());
        Stats.onNativeCall();
    }

    public long getFontManagersCount() {
        Stats.onNativeCall();
        return _nGetFontManagersCount(_ptr);
    }
    
    public FontCollection setAssetFontManager(FontMgr fontMgr) {
        Stats.onNativeCall();
        _nSetAssetFontManager(_ptr, Native.getPtr(fontMgr));
        return this;
    }

    public FontCollection setDynamicFontManager(FontMgr fontMgr) {
        Stats.onNativeCall();
        _nSetDynamicFontManager(_ptr, Native.getPtr(fontMgr));
        return this;
    }

    public FontCollection setTestFontManager(FontMgr fontMgr) {
        Stats.onNativeCall();
        _nSetTestFontManager(_ptr, Native.getPtr(fontMgr));
        return this;
    }

    public FontCollection setDefaultFontManager(FontMgr fontMgr) {
        return setDefaultFontManager(fontMgr, null);
    }

    public FontCollection setDefaultFontManager(FontMgr fontMgr, String defaultFamilyName) {
        Stats.onNativeCall();
        _nSetDefaultFontManager(_ptr, Native.getPtr(fontMgr), defaultFamilyName);
        return this;
    }
    
    public FontMgr getFallbackManager() {
        Stats.onNativeCall();
        long ptr = _nGetFallbackManager(_ptr);
        return ptr == 0 ? null : new FontMgr(ptr);
    }

    public Typeface[] findTypefaces(String[] familyNames, FontStyle style) {
        Stats.onNativeCall();
        long[] ptrs = _nFindTypefaces(_ptr, familyNames, style._value);
        Typeface[] res = new Typeface[ptrs.length];
        for (int i = 0; i < ptrs.length; ++i)
            res[i] = new Typeface(ptrs[i]);
        return res;
    }

    public Typeface defaultFallback(int unicode, FontStyle style, String locale) {
        Stats.onNativeCall();
        long ptr = _nDefaultFallbackChar(_ptr, unicode, style._value, locale);
        return ptr == 0 ? null : new Typeface(ptr);
    }

    public Typeface defaultFallback() {
        Stats.onNativeCall();
        long ptr = _nDefaultFallback(_ptr);
        return ptr == 0 ? null : new Typeface(ptr);
    }

    public FontCollection setEnableFallback(boolean value) {
        Stats.onNativeCall();
        _nSetEnableFallback(_ptr, value);
        return this;
    }

    public ParagraphCache getParagraphCache() {
        Stats.onNativeCall();
        return new ParagraphCache(this, _nGetParagraphCache(_ptr));
    }

    @ApiStatus.Internal
    public FontCollection(long ptr) {
        super(ptr);
    }

    public static native long   _nMake();
    public static native long   _nGetFontManagersCount(long ptr);
    public static native long   _nSetAssetFontManager(long ptr, long fontManagerPtr);
    public static native long   _nSetDynamicFontManager(long ptr, long fontManagerPtr);
    public static native long   _nSetTestFontManager(long ptr, long fontManagerPtr);
    public static native long   _nSetDefaultFontManager(long ptr, long fontManagerPtr, String defaultFamilyName);
    public static native long   _nGetFallbackManager(long ptr);
    public static native long[] _nFindTypefaces(long ptr, String[] familyNames, int fontStyle);
    public static native long   _nDefaultFallbackChar(long ptr, int unicode, int fontStyle, String locale);
    public static native long   _nDefaultFallback(long ptr);
    public static native long   _nSetEnableFallback(long ptr, boolean value);
    public static native long   _nGetParagraphCache(long ptr);
}