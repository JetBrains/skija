package org.jetbrains.skija;

import java.lang.ref.*;
import java.io.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.annotations.*;

/**
 * <p>Base class for objects that draw into Canvas.</p>
 *
 * <p>The object has a generation id, which is guaranteed to be unique across all drawables. To
 * allow for clients of the drawable that may want to cache the results, the drawable must
 * change its generation id whenever its internal state changes such that it will draw differently.</p>
 */
public abstract class Drawable extends RefCnt {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public Rect _bounds = null;

    public Drawable() {
        super(_nMake());
        Stats.onNativeCall();        
        Stats.onNativeCall();
        _nInit(_ptr);
    }

    /**
     * Draws into the specified content. The drawing sequence will be balanced upon return
     * (i.e. the saveLevel() on the canvas will match what it was when draw() was called,
     * and the current matrix and clip settings will not be changed.
     */
    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas) {
        return draw(canvas, null);
    }

    /**
     * Draws into the specified content. The drawing sequence will be balanced upon return
     * (i.e. the saveLevel() on the canvas will match what it was when draw() was called,
     * and the current matrix and clip settings will not be changed.
     */
    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas, float x, float y) {
        return draw(canvas, Matrix33.makeTranslate(x, y));
    }

    /**
     * Draws into the specified content. The drawing sequence will be balanced upon return
     * (i.e. the saveLevel() on the canvas will match what it was when draw() was called,
     * and the current matrix and clip settings will not be changed.
     */
    @ApiStatus.NonExtendable
    public Drawable draw(Canvas canvas, @Nullable Matrix33 matrix) {
        try {
            Stats.onNativeCall();
            _nDraw(_ptr, Native.getPtr(canvas), matrix == null ? null : matrix._mat);
            return this;
        } finally {
            Reference.reachabilityFence(canvas);
        }
    }

    @ApiStatus.NonExtendable
    public Picture makePictureSnapshot() {
        try {
            Stats.onNativeCall();
            return new Picture(_nMakePictureSnapshot(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Return a unique value for this instance. If two calls to this return the same value,
     * it is presumed that calling the draw() method will render the same thing as well.</p>
     *
     * <p>Subclasses that change their state should call notifyDrawingChanged() to ensure that
     * a new value will be returned the next time it is called.</p>
     */
    public int getGenerationId() {
        try {
            Stats.onNativeCall();
            return _nGetGenerationId(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Return the (conservative) bounds of what the drawable will draw. If the drawable can
     * change what it draws (e.g. animation or in response to some external change), then this
     * must return a bounds that is always valid for all possible states.
     */
    @ApiStatus.NonExtendable
    public @NotNull Rect getBounds() {
        if (_bounds == null)
            _bounds = onGetBounds();
        return _bounds;
    }

    /**
     * Calling this invalidates the previous generation ID, and causes a new one to be computed
     * the next time getGenerationId() is called. Typically this is called by the object itself,
     * in response to its internal state changing.
     */
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
        onDraw(new Canvas(canvasPtr, false, this));
    }

    @ApiStatus.Internal public static native long _nMake();
    @ApiStatus.Internal public        native void _nInit(long ptr);
    @ApiStatus.Internal public static native void _nDraw(long ptr, long canvasPtr, float[] matrix);
    @ApiStatus.Internal public static native long _nMakePictureSnapshot(long ptr);
    @ApiStatus.Internal public static native int  _nGetGenerationId(long ptr);
    @ApiStatus.Internal public static native void _nNotifyDrawingChanged(long ptr);
}


