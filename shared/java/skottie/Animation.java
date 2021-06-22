package org.jetbrains.skija.skottie;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.skija.sksg.InvalidationController;

public class Animation extends Managed {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public Animation(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public static Animation makeFromString(@NotNull String data) {
        assert data != null : "Can’t Animation::makeFromString with data == null";
        Stats.onNativeCall();
        long ptr = _nMakeFromString(data);
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Animation from string=\"" + data.toString() + "\"");
        return new Animation(ptr);
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public static Animation makeFromFile(@NotNull String path) {
        assert path != null : "Can’t Animation::makeFromFile with path == null";
        Stats.onNativeCall();
        long ptr = _nMakeFromFile(path);
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Animation from path=\"" + path + "\"");
        return new Animation(ptr);
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public static Animation makeFromData(@NotNull Data data) {
        assert data != null : "Can’t Animation::makeFromData with data == null";
        Stats.onNativeCall();
        long ptr = _nMakeFromData(Native.getPtr(data));
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Animation from data.");
        return new Animation(ptr);
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas  destination canvas
     * @return        this
     */
    @NotNull @Contract("!null -> this; null -> fail")
    public Animation render(@NotNull Canvas canvas) {
        return render(canvas, Rect.makeXYWH(0, 0, getWidth(), getHeight()));
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas  destination canvas
     * @param offset  destination offset
     * @return        this
     */
    @NotNull @Contract("_, _, _ -> this")
    public Animation render(@NotNull Canvas canvas, @NotNull Point offset) {
        assert offset != null : "Can’t Animation::render with offset == null";
        return render(canvas, offset._x, offset._y);
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas  destination canvas
     * @param left    destination offset left
     * @param top     destination offset top
     * @return        this
     */
    @NotNull @Contract("_, _, _ -> this")
    public Animation render(@NotNull Canvas canvas, float left, float top) {
        return render(canvas, Rect.makeXYWH(left, top, getWidth(), getHeight()));
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas       destination canvas
     * @param dst          destination rect
     * @param renderFlags  render flags
     * @return             this
     */
    @NotNull @Contract("_, _, _ -> this")
    public Animation render(@NotNull Canvas canvas, @NotNull Rect dst, RenderFlag... renderFlags) {
        try {
            assert canvas != null : "Can’t Animation::render with canvas == null";
            assert dst != null : "Can’t Animation::render with dst == null";
            Stats.onNativeCall();
            int flags = 0;
            for (RenderFlag flag: renderFlags)
                flags |= flag._flag;
            _nRender(_ptr, Native.getPtr(canvas), dst._left, dst._top, dst._right, dst._bottom, flags);
            return this;
        } finally {
            Reference.reachabilityFence(canvas);
        }
    }

    /**
     * <p>Updates the animation state for |t|.</p>
     *
     * @param t   normalized [0..1] frame selector (0 → first frame, 1 → final frame)
     * @return    this
     */
    @NotNull @Contract("_ -> this")
    public Animation seek(float t) {
        return seek(t, null);
    }

    /**
     * <p>Updates the animation state for |t|.</p>
     *
     * @param t   normalized [0..1] frame selector (0 → first frame, 1 → final frame)
     * @param ic  invalidation controller (dirty region tracking)
     * @return    this
     */
    @NotNull @Contract("_, _ -> this")
    public Animation seek(float t, @Nullable InvalidationController ic) {
        try {
            Stats.onNativeCall();
            _nSeek(_ptr, t, Native.getPtr(ic));
            return this;
        } finally {
            Reference.reachabilityFence(ic);
        }
    }

    /**
     * <p>Update the animation state to match |t|, specified as a frame index i.e.
     * relative to {@link getDuration()} * {@link getFPS()}.</p>
     *
     * <p>Fractional values are allowed and meaningful - e.g.
     * 0.0 → first frame 1.0 → second frame 0.5 → halfway between first and second frame</p>
     *
     * @param t   frame index
     * @return    this
     */
    @NotNull @Contract("_ -> this")
    public Animation seekFrame(float t) {
        return seekFrame(t, null);
    }

    /**
     * <p>Update the animation state to match |t|, specified as a frame index i.e.
     * relative to {@link getDuration()} * {@link getFPS()}.</p>
     *
     * <p>Fractional values are allowed and meaningful - e.g.
     * 0.0 → first frame 1.0 → second frame 0.5 → halfway between first and second frame</p>
     *
     * @param t   frame index
     * @param ic  invalidation controller (dirty region tracking)
     * @return    this
     */
    @NotNull @Contract("_, _ -> this")
    public Animation seekFrame(float t, @Nullable InvalidationController ic) {
        try {
            Stats.onNativeCall();
            _nSeekFrame(_ptr, t, Native.getPtr(ic));
            return this;
        } finally {
            Reference.reachabilityFence(ic);
        }
    }

    /**
     * <p>Update the animation state to match t, specifed in frame time i.e.
     * relative to {@link getDuration()}.</p>
     *
     * @param t   frame time
     * @return    this
     */
    @NotNull @Contract("_ -> this")
    public Animation seekFrameTime(float t) {
        return seekFrameTime(t, null);
    }

    /**
     * <p>Update the animation state to match t, specifed in frame time i.e.
     * relative to {@link getDuration()}.</p>
     *
     * @param t   frame time
     * @param ic  invalidation controller (dirty region tracking)
     * @return    this
     */
    @NotNull @Contract("_, _ -> this")
    public Animation seekFrameTime(float t, @Nullable InvalidationController ic) {
        try {
            Stats.onNativeCall();
            _nSeekFrameTime(_ptr, t, Native.getPtr(ic));
            return this;
        } finally {
            Reference.reachabilityFence(ic);
        }
    }

    /**
     * @return  the animation duration in seconds
     */
    public float getDuration() {
        try {
            Stats.onNativeCall();
            return _nGetDuration(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  the animation frame rate (frames / second)
     */
    public float getFPS() {
        try {
            Stats.onNativeCall();
            return _nGetFPS(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  Animation in point, in frame index units
     */
    public float getInPoint() {
        try {
            Stats.onNativeCall();
            return _nGetInPoint(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  Animation out point, in frame index units
     */
    public float getOutPoint() {
        try {
            Stats.onNativeCall();
            return _nGetOutPoint(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public String getVersion() {
        try {
            Stats.onNativeCall();
            return _nGetVersion(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal @Nullable public Point _size = null;

    @NotNull
    public Point getSize() {
        if (_size == null) {
            _size = _nGetSize(_ptr);
        }
        return _size;
    }

    public float getWidth() {
        return getSize()._x;
    }

    public float getHeight() {
        return getSize()._y;
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMakeFromString(String data);
    @ApiStatus.Internal public static native long _nMakeFromFile(String path);
    @ApiStatus.Internal public static native long _nMakeFromData(long dataPtr);
    @ApiStatus.Internal public static native void _nRender(long ptr, long canvasPtr, float left, float top, float right, float bottom, int flags);
    @ApiStatus.Internal public static native void _nSeek(long ptr, float t, long icPtr);
    @ApiStatus.Internal public static native void _nSeekFrame(long ptr, float t, long icPtr);
    @ApiStatus.Internal public static native void _nSeekFrameTime(long ptr, float t, long icPtr);
    @ApiStatus.Internal public static native float _nGetDuration(long ptr);
    @ApiStatus.Internal public static native float _nGetFPS(long ptr);
    @ApiStatus.Internal public static native float _nGetInPoint(long ptr);
    @ApiStatus.Internal public static native float _nGetOutPoint(long ptr);
    @ApiStatus.Internal public static native String _nGetVersion(long ptr);
    @ApiStatus.Internal public static native Point _nGetSize(long ptr);

   
}