package org.jetbrains.skija.paragraph;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class FontCollection extends RefCnt {
    static { Library.staticLoad(); }
    
    public FontCollection() {
        this(_nMake());
        Stats.onNativeCall();
    }

    public long getFontManagersCount() {
        try {
            Stats.onNativeCall();
            return _nGetFontManagersCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }
    
    public FontCollection setAssetFontManager(FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            _nSetAssetFontManager(_ptr, Native.getPtr(fontMgr));
            return this;
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    public FontCollection setDynamicFontManager(FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            _nSetDynamicFontManager(_ptr, Native.getPtr(fontMgr));
            return this;
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    public FontCollection setTestFontManager(FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            _nSetTestFontManager(_ptr, Native.getPtr(fontMgr));
            return this;
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    public FontCollection setDefaultFontManager(FontMgr fontMgr) {
        return setDefaultFontManager(fontMgr, null);
    }

    public FontCollection setDefaultFontManager(FontMgr fontMgr, String defaultFamilyName) {
        try {
            Stats.onNativeCall();
            _nSetDefaultFontManager(_ptr, Native.getPtr(fontMgr), defaultFamilyName);
            return this;
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }
    
    public FontMgr getFallbackManager() {
        try {
            Stats.onNativeCall();
            long ptr = _nGetFallbackManager(_ptr);
            return ptr == 0 ? null : new FontMgr(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Typeface[] findTypefaces(String[] familyNames, FontStyle style) {
        try {
            Stats.onNativeCall();
            long[] ptrs = _nFindTypefaces(_ptr, familyNames, style._value);
            Typeface[] res = new Typeface[ptrs.length];
            for (int i = 0; i < ptrs.length; ++i)
                res[i] = new Typeface(ptrs[i]);
            return res;
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Typeface defaultFallback(int unicode, FontStyle style, String locale) {
        try {
            Stats.onNativeCall();
            long ptr = _nDefaultFallbackChar(_ptr, unicode, style._value, locale);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public Typeface defaultFallback() {
        try {
            Stats.onNativeCall();
            long ptr = _nDefaultFallback(_ptr);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public FontCollection setEnableFallback(boolean value) {
        Stats.onNativeCall();
        _nSetEnableFallback(_ptr, value);
        return this;
    }

    public ParagraphCache getParagraphCache() {
        try {
            Stats.onNativeCall();
            return new ParagraphCache(this, _nGetParagraphCache(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
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