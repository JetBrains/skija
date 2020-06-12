package org.jetbrains.skija.paragraph;

import org.jetbrains.skija.Managed;
import org.jetbrains.skija.Native;
import org.jetbrains.skija.Stats;

public class ParagraphBuilder extends Managed {
    public ParagraphBuilder(ParagraphStyle style, FontCollection fc) {
        super(nInit(Native.getPtr(style), Native.getPtr(fc)), nativeFinalizer);
        Stats.onNativeCall();
    }

    public ParagraphBuilder pushStyle(TextStyle style) {
        Stats.onNativeCall();
        nPushStyle(_ptr, Native.getPtr(style));
        return this;
    }

    public ParagraphBuilder popStyle() {
        Stats.onNativeCall();
        nPopStyle(_ptr);
        return this;
    }

    public ParagraphBuilder addText(String text) {
        Stats.onNativeCall();
        nAddText(_ptr, text);
        return this;
    }

    public Paragraph build() {
        Stats.onNativeCall();
        return new Paragraph(nBuild(_ptr));
    }

    private static final long nativeFinalizer = nGetNativeFinalizer();
    private static native long nInit(long paragraphStylePtr, long fontCollectionPtr);
    private static native long nGetNativeFinalizer();
    private static native void nPushStyle(long ptr, long textStylePtr);
    private static native void nPopStyle(long ptr);
    private static native void nAddText(long ptr, String text);
    private static native long nBuild(long ptr);
}