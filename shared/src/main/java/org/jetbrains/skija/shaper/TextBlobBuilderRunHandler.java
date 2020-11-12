package org.jetbrains.skija.shaper;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class TextBlobBuilderRunHandler<T> extends Managed implements RunHandler {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public final ManagedString _text;

    @ApiStatus.Internal
    public TextBlobBuilderRunHandler(ManagedString text, boolean manageText, float offsetX, float offsetY) {
        super(_nMake(Native.getPtr(text), offsetX, offsetY), _FinalizerHolder.PTR);
        _text = manageText ? text : null;
    }

    public TextBlobBuilderRunHandler(String text) {
        this(new ManagedString(text), true, 0, 0);
    }

    public TextBlobBuilderRunHandler(String text, Point offset) {
        this(new ManagedString(text), true, offset._x, offset._y);
    }

    @Override
    public void close() {
        super.close();
        if (_text != null)
            _text.close();
    }

    @Override
    public void beginLine() {
        throw new UnsupportedOperationException("beginLine");
    }

    @Override
    public void runInfo(RunInfo info) {
        throw new UnsupportedOperationException("runInfo");
    }

    @Override
    public void commitRunInfo() {
        throw new UnsupportedOperationException("commitRunInfo");
    }

    @Override
    public Point runOffset(RunInfo info) {
        throw new UnsupportedOperationException("runOffset");
    }

    @Override
    public void commitRun(RunInfo info, short[] glyphs, Point[] positions, int[] clusters) {
        throw new UnsupportedOperationException("commitRun");
    }

    @Override
    public void commitLine() {
        throw new UnsupportedOperationException("commitLine");
    }

    public TextBlob makeBlob() {
        Stats.onNativeCall();
        long ptr = _nMakeBlob(_ptr);
        if (0 == ptr)
            throw new RuntimeException("Failed to shape: " + _text);
        return new TextBlob(ptr);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMake(long textPtr, float offsetX, float offsetY);
    @ApiStatus.Internal public static native long _nMakeBlob(long ptr);
}