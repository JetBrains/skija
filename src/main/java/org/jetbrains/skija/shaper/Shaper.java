package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

/**
 * Shapes text using HarfBuzz and places the shaped text into a
 * client-managed buffer.
 */
public class Shaper extends Managed {
    static { Library.load(); }
    
    @NotNull @Contract("-> new")
    public static Shaper makePrimitive() {
        Stats.onNativeCall();
        return new Shaper(_nMakePrimitive());
    }

    @NotNull @Contract("-> new")
    public static Shaper makeShaperDrivenWrapper() {
        return makeShaperDrivenWrapper(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper makeShaperDrivenWrapper(@Nullable FontMgr fontMgr) {
        Stats.onNativeCall();
        return new Shaper(_nMakeShaperDrivenWrapper(Native.getPtr(fontMgr)));
    }

    @NotNull @Contract("-> new")
    public static Shaper makeShapeThenWrap() {
        return makeShapeThenWrap(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper makeShapeThenWrap(@Nullable FontMgr fontMgr) {
        Stats.onNativeCall();
        return new Shaper(_nMakeShapeThenWrap(Native.getPtr(fontMgr)));
    }

    @NotNull @Contract("-> new")
    public static Shaper makeShapeDontWrapOrReorder() {
        return makeShapeDontWrapOrReorder(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper makeShapeDontWrapOrReorder(@Nullable FontMgr fontMgr) {
        Stats.onNativeCall();
        return new Shaper(_nMakeShapeDontWrapOrReorder(Native.getPtr(fontMgr)));
    }

    /**
     * <p>Only works on macOS</p>
     * 
     * <p>WARN broken in m87 https://bugs.chromium.org/p/skia/issues/detail?id=10897</p>
     * 
     * @return  Shaper on macOS, throws UnsupportedOperationException elsewhere
     */
    @NotNull @Contract("-> new")
    public static Shaper makeCoreText() {
        Stats.onNativeCall();
        long ptr = _nMakeCoreText();
        if (ptr == 0)
            throw new UnsupportedOperationException("CoreText not available");
        return new Shaper(ptr);
    }

    @NotNull @Contract("-> new")
    public static Shaper make() {
        return make(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper make(@Nullable FontMgr fontMgr) {
        Stats.onNativeCall();
        return new Shaper(_nMake(Native.getPtr(fontMgr)));
    }

    @NotNull @Contract("_, _ -> new")
    public TextBlob shape(String text, Font font) {
        return shape(text, font, null, true, Float.POSITIVE_INFINITY, Point.ZERO);
    }

    @NotNull @Contract("_, _, _ -> new")
    public TextBlob shape(String text, Font font, float width) {
        return shape(text, font, null, true, width, Point.ZERO);
    }

    @NotNull @Contract("_, _, _, _ -> new")
    public TextBlob shape(String text, Font font, float width, @NotNull Point offset) {
        return shape(text, font, null, true, width, offset);
    }

    @NotNull @Contract("_, _, _, _, _ -> new")
    public TextBlob shape(String text, Font font, boolean leftToRight, float width, @NotNull Point offset) {
        return shape(text, font, null, leftToRight, width, offset);
    }

    @NotNull @Contract("_, _, _, _, _, _ -> new")
    public TextBlob shape(String text, Font font, @Nullable FontFeature[] features, boolean leftToRight, float width, @NotNull Point offset) {
        Stats.onNativeCall();
        long ptr = _nShapeToTextBlob(_ptr, text, Native.getPtr(font), features, leftToRight, width, offset._x, offset._y);
        if (0 == ptr)
            throw new RuntimeException("Failed to shape: " + text);
        return new TextBlob(ptr);
    }

    @NotNull @Contract("_, _, _, _, _, _, _ -> this")
    public Shaper shape(String text, Font font, @Nullable FontMgr fontMgr, @Nullable FontFeature[] features, boolean leftToRight, float width, RunHandler runHandler) {
        Stats.onNativeCall();
        _nShape(_ptr, text, Native.getPtr(font), Native.getPtr(fontMgr), features, leftToRight, width, runHandler);
        return this;
    }

    @NotNull @Contract("_, _, _, _, _, _, _ -> this")
    public Shaper shape(String text, @NotNull FontRunIterator fontIter, @NotNull BidiRunIterator bidiIter, @NotNull ScriptRunIterator scriptIter, @NotNull LanguageRunIterator langIter,
                        @Nullable FontFeature[] features,float width, RunHandler runHandler) {
        assert fontIter != null : "FontRunIterator == null";
        assert bidiIter != null : "BidiRunIterator == null";
        assert scriptIter != null : "ScriptRunIterator == null";
        assert langIter != null : "LanguageRunIterator == null";
        Stats.onNativeCall();
        _nShapeRunIters(_ptr, text, fontIter, bidiIter, scriptIter, langIter, features, width, runHandler);
        return this;
    }

    @ApiStatus.Internal
    public Shaper(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public static final  long _finalizerPtr = _nGetFinalizer();
    public static native long _nGetFinalizer();
    public static native long _nMakePrimitive();
    public static native long _nMakeShaperDrivenWrapper(long fontMgrPtr);
    public static native long _nMakeShapeThenWrap(long fontMgrPtr);
    public static native long _nMakeShapeDontWrapOrReorder(long fontMgrPtr);
    public static native long _nMakeCoreText();
    public static native long _nMake(long fontMgrPtr);

    public static native long _nShapeToTextBlob(long ptr, String text, long fontPtr, FontFeature[] features, boolean leftToRight, float width, float offsetX, float offsetY);
    public static native void _nShape(long ptr, String text, long fontPtr, long fontMgrPtr, FontFeature[] features, boolean leftToRight, float width, RunHandler runHandler);
    public static native void _nShapeRunIters(long ptr, String text, FontRunIterator fontIter, BidiRunIterator bidiIter, ScriptRunIterator scriptIter, LanguageRunIterator langIter,
                                              FontFeature[] features, float width, RunHandler runHandler);
}
