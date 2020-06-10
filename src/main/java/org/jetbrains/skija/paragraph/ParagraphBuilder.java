package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.Managed;
import org.jetbrains.skija.Native;

public class ParagraphBuilder extends Managed {
    public ParagraphBuilder(ParagraphStyle style, FontCollection fc) {
        super(nInit(Native.pointer(style), Native.pointer(fc)), nativeFinalizer);
        Native.onNativeCall();
    }

    public ParagraphBuilder pushStyle(TextStyle style) {
        Native.onNativeCall();
        nPushStyle(nativeInstance, Native.pointer(style));
        return this;
    }

    public ParagraphBuilder popStyle() {
        Native.onNativeCall();
        nPopStyle(nativeInstance);
        return this;
    }

    public ParagraphBuilder addText(String text) {
        Native.onNativeCall();
        nAddText(nativeInstance, text);
        return this;
    }

    public Paragraph build() {
        Native.onNativeCall();
        return new Paragraph(nBuild(nativeInstance));
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long paragraphStylePtr, long fontCollectionPtr);
    private static native long nGetNativeFinalizer();
    private static native void nPushStyle(long ptr, long textStylePtr);
    private static native void nPopStyle(long ptr);
    private static native void nAddText(long ptr, String text);
    private static native long nBuild(long ptr);
}