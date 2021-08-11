package org.jetbrains.skija.shaper;

import java.lang.ref.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

/**
 * Shapes text using HarfBuzz and places the shaped text into a
 * client-managed buffer.
 */
public class Shaper extends Managed {
    static { Library.staticLoad(); }
    
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
        try {
            Stats.onNativeCall();
            return new Shaper(_nMakeShaperDrivenWrapper(Native.getPtr(fontMgr)));
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    @NotNull @Contract("-> new")
    public static Shaper makeShapeThenWrap() {
        return makeShapeThenWrap(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper makeShapeThenWrap(@Nullable FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            return new Shaper(_nMakeShapeThenWrap(Native.getPtr(fontMgr)));
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    @NotNull @Contract("-> new")
    public static Shaper makeShapeDontWrapOrReorder() {
        return makeShapeDontWrapOrReorder(null);
    }

    @NotNull @Contract("_ -> new")
    public static Shaper makeShapeDontWrapOrReorder(@Nullable FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            return new Shaper(_nMakeShapeDontWrapOrReorder(Native.getPtr(fontMgr)));
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
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
        try {
            Stats.onNativeCall();
            return new Shaper(_nMake(Native.getPtr(fontMgr)));
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    @Nullable @Contract("_, _ -> new")
    public TextBlob shape(String text, Font font) {
        return shape(text, font, ShapingOptions.DEFAULT, Float.POSITIVE_INFINITY, Point.ZERO);
    }

    @Nullable @Contract("_, _, _ -> new")
    public TextBlob shape(String text, Font font, float width) {
        return shape(text, font, ShapingOptions.DEFAULT, width, Point.ZERO);
    }

    @Nullable @Contract("_, _, _, _ -> new")
    public TextBlob shape(String text, Font font, float width, @NotNull Point offset) {
        return shape(text, font, ShapingOptions.DEFAULT, width, offset);
    }

    @Nullable @Contract("_, _, _, _, _ -> new")
    public TextBlob shape(String text, Font font, @NotNull ShapingOptions opts, float width, @NotNull Point offset) {
        try {
            assert opts != null : "Can’t Shaper::shape with opts == null";
            Stats.onNativeCall();
            long ptr = _nShapeBlob(_ptr, text, Native.getPtr(font), opts, width, offset._x, offset._y);
            return 0 == ptr ? null : new TextBlob(ptr);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(font);
        }
    }

    @NotNull @Contract("_, _, _, _, _ -> this")
    public Shaper shape(String text,
                        Font font,
                        @NotNull ShapingOptions opts,
                        float width,
                        RunHandler runHandler)
    {
        try (ManagedString textUtf8 = new ManagedString(text);
             FontMgrRunIterator fontIter = new FontMgrRunIterator(textUtf8, false, font, opts);
             IcuBidiRunIterator bidiIter = new IcuBidiRunIterator(textUtf8, false, opts._leftToRight ? java.text.Bidi.DIRECTION_LEFT_TO_RIGHT : java.text.Bidi.DIRECTION_RIGHT_TO_LEFT);
             HbIcuScriptRunIterator scriptIter = new HbIcuScriptRunIterator(textUtf8, false);)
        {
            Iterator<LanguageRun> langIter = new TrivialLanguageRunIterator(text, Locale.getDefault().toLanguageTag());
            return shape(textUtf8, fontIter, bidiIter, scriptIter, langIter, opts, width, runHandler);
        }
    }

    @NotNull @Contract("_, _, _, _, _, _, _ -> this")
    public Shaper shape(@NotNull String text,
                        @NotNull Iterator<FontRun> fontIter,
                        @NotNull Iterator<BidiRun> bidiIter,
                        @NotNull Iterator<ScriptRun> scriptIter,
                        @NotNull Iterator<LanguageRun> langIter,
                        @NotNull ShapingOptions opts,
                        float width,
                        @NotNull RunHandler runHandler)
    {
        try (ManagedString textUtf8 = new ManagedString(text);) {
            return shape(textUtf8, fontIter, bidiIter, scriptIter, langIter, opts, width, runHandler);
        }
    }

    @NotNull @Contract("_, _, _, _, _, _, _ -> this")
    public Shaper shape(@NotNull ManagedString textUtf8,
                        @NotNull Iterator<FontRun> fontIter,
                        @NotNull Iterator<BidiRun> bidiIter,
                        @NotNull Iterator<ScriptRun> scriptIter,
                        @NotNull Iterator<LanguageRun> langIter,
                        @NotNull ShapingOptions opts,
                        float width,
                        @NotNull RunHandler runHandler)
    {
        assert fontIter != null : "FontRunIterator == null";
        assert bidiIter != null : "BidiRunIterator == null";
        assert scriptIter != null : "ScriptRunIterator == null";
        assert langIter != null : "LanguageRunIterator == null";
        assert opts != null : "Can’t Shaper::shape with opts == null";
        Stats.onNativeCall();
        _nShape(_ptr, Native.getPtr(textUtf8), fontIter, bidiIter, scriptIter, langIter, opts, width, runHandler);
        return this;
    }

    @NotNull @Contract("_, _, _ -> new")
    public TextLine shapeLine(String text, Font font, @NotNull ShapingOptions opts) {
        try {
            assert opts != null : "Can’t Shaper::shapeLine with opts == null";
            Stats.onNativeCall();
            return new TextLine(_nShapeLine(_ptr, text, Native.getPtr(font), opts));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(font);
        }
    }

    @NotNull @Contract("_, _, _ -> new")
    public TextLine shapeLine(String text, Font font) {
        return shapeLine(text, font, ShapingOptions.DEFAULT);
    }

    @ApiStatus.Internal
    public Shaper(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public static native long _nGetFinalizer();
    public static native long _nMakePrimitive();
    public static native long _nMakeShaperDrivenWrapper(long fontMgrPtr);
    public static native long _nMakeShapeThenWrap(long fontMgrPtr);
    public static native long _nMakeShapeDontWrapOrReorder(long fontMgrPtr);
    public static native long _nMakeCoreText();
    public static native long _nMake(long fontMgrPtr);
    public static native long _nShapeBlob(long ptr, String text, long fontPtr, ShapingOptions opts, float width, float offsetX, float offsetY);
    public static native long _nShapeLine(long ptr, String text, long fontPtr, ShapingOptions opts);
    public static native void _nShape(long ptr, long textPtr, Iterator<FontRun> fontIter, Iterator<BidiRun> bidiIter, Iterator<ScriptRun> scriptIter, Iterator<LanguageRun> langIter, ShapingOptions opts, float width, RunHandler runHandler);
}
