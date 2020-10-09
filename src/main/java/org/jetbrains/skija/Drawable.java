package org.jetbrains.skija;

import java.io.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.annotations.*;

public abstract class Drawable extends RefCnt {
    @ApiStatus.Internal
    public Rect _bounds = null;

    public Drawable() {
        super(_nMake());
        Stats.onNativeCall();
        
        Stats.onNativeCall();
        _nInit(_ptr);
    }

    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas) {
        return draw(canvas, null);
    }

    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas, float x, float y) {
        return draw(canvas, Matrix33.makeTranslate(x, y));
    }

    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas, @Nullable Matrix33 matrix) {
        Stats.onNativeCall();
        _nDraw(_ptr, Native.getPtr(canvas), matrix == null ? null : matrix._mat);
        return this;
    }

    @ApiStatus.NonExtendable
    public Picture makePictureSnapshot() {
        Stats.onNativeCall();
        return new Picture(_nMakePictureSnapshot(_ptr));
    }

    public int getGenerationId() {
        Stats.onNativeCall();
        return _nGetGenerationId(_ptr);
    }

    @ApiStatus.NonExtendable
    public @NotNull Rect getBounds() {
        if (_bounds == null)
            _bounds = onGetBounds();
        return _bounds;
    }

    @ApiStatus.NonExtendable
    public Drawable notifyDrawingChanged() {
        Stats.onNativeCall();
        _nNotifyDrawingChanged(_ptr);
        _bounds = null;
        return this;
    }

    @ApiStatus.OverrideOnly
    public abstract void onDraw(Canvas canvas);

    @ApiStatus.OverrideOnly
    public abstract @NotNull Rect onGetBounds();

    @ApiStatus.Internal
    public void _onDraw(long canvasPtr) {
        onDraw(new Canvas(canvasPtr));
    }

    @ApiStatus.Internal public static native long _nMake();
    @ApiStatus.Internal public        native void _nInit(long ptr);
    @ApiStatus.Internal public static native void _nDraw(long ptr, long canvasPtr, float[] matrix);
    @ApiStatus.Internal public static native long _nMakePictureSnapshot(long ptr);
    @ApiStatus.Internal public static native int  _nGetGenerationId(long ptr);
    @ApiStatus.Internal public static native void _nNotifyDrawingChanged(long ptr);
}


