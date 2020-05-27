package org.jetbrains.skija;

public class FontCollection extends RefCounted {
    public FontCollection() {
        this(nInit());
        Native.onNativeCall();
    }
    
    public FontCollection setDefaultFontManager(FontManager fontManager) {
        Native.onNativeCall();
        nSetDefaultFontManager(nativeInstance, Native.pointer(fontManager));
        return this;
    }

    protected FontCollection(long nativeInstance) { super(nativeInstance); }
    private static native long nInit();
    private static native long nSetDefaultFontManager(long ptr, long fontManagerPtr);
}