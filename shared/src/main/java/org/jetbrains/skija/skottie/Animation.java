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

    @NotNull
    public static Animation make(@NotNull ManagedString data) {
        Stats.onNativeCall();
        long ptr = _nMake(Native.getPtr(data));
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Animation from string=\"" + data.toString() + "\"");
        return new Animation(ptr);
    }

    @NotNull
    public static Animation makeFromFile(String path) {
        Stats.onNativeCall();
        long ptr = _nMakeFromFile(path);
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Animation from path=\"" + path + "\"");
        return new Animation(ptr);
    }

    @NotNull
    public static Animation makeFromData(@NotNull Data data) {
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
     * @param canvas destination canvas
     */
    public void render(Canvas canvas) {
        try {
            Stats.onNativeCall();
            _nRender(_ptr, Native.getPtr(canvas));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(canvas);
        }
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas destination canvas
     * @param left   destination rect left
     * @param top    destination rect top
     * @param right  destination rect right
     * @param bottom destination rect bottm
     */
    public void render(Canvas canvas, float left, float top, float right, float bottom) {
        render(canvas, left, top, right, bottom, new RenderFlag[0]);
    }

    /**
     * <p>Draws the current animation frame</p>
     *
     * <p>It is undefined behavior to call render() on a newly created Animation
     * before specifying an initial frame via one of the seek() variants.</p>
     *
     * @param canvas      destination canvas
     * @param left        destination rect left
     * @param top         destination rect top
     * @param right       destination rect right
     * @param bottom      destination rect bottm
     * @param renderFlags render flags
     */
    public void render(Canvas canvas, float left, float top, float right, float bottom, RenderFlag... renderFlags) {
        try {
            Stats.onNativeCall();
            int flags = 0;
            for (var flag : renderFlags)
                flags |= flag._flag;
            _nRenderRect(_ptr, Native.getPtr(canvas), left, top, right, bottom, flags);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(canvas);
        }
    }

    /**
     * <p>Updates the animation state for |t|.</p>
     *
     * @param t                      normalized [0..1] frame selector (0 -> first frame, 1 -> final frame)
     * @param invalidationController invalidation controller (dirty region tracking)
     */
    public void seek(float t, InvalidationController invalidationController) {
        try {
            Stats.onNativeCall();
            _nSeek(_ptr, t, Native.getPtr(invalidationController));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(invalidationController);
        }
    }

    /**
     * <p>Update the animation state to match |t|, specified as a frame index i.e.
     * relative to {@link getDuration()} * {@link getFps()}.</p>
     *
     * <p>Fractional values are allowed and meaningful - e.g.
     * 0.0 -> first frame 1.0 -> second frame 0.5 -> halfway between first and second frame</p>
     *
     * @param t                      frame index
     * @param invalidationController invalidation controller (dirty region tracking)
     */
    public void seekFrame(double t, InvalidationController invalidationController) {
        try {
            Stats.onNativeCall();
            _nSeekFrame(_ptr, t, Native.getPtr(invalidationController));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(invalidationController);
        }
    }

    /**
     * <p>Update the animation state to match t, specifed in frame time i.e.
     * relative to {@link getDuration()}.</p>
     *
     * @param t                      frame time
     * @param invalidationController invalidation controller (dirty region tracking)
     */
    public void seekFrameTime(double t, InvalidationController invalidationController) {
        try {
            Stats.onNativeCall();
            _nSeekFrameTime(_ptr, t, Native.getPtr(invalidationController));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(invalidationController);
        }
    }

    /**
     * <p>Returns the animation duration in seconds.</p>
     */
    public double getDuration() {
        try {
            Stats.onNativeCall();
            return _nGetDuration(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns the animation frame rate (frames / second).</p>
     */
    public double getFps() {
        try {
            Stats.onNativeCall();
            return _nGetFps(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Animation in point, in frame index units.</p>
     */
    public double getInPoint() {
        try {
            Stats.onNativeCall();
            return _nGetInPoint(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Animation out point, in frame index units.</p>
     */
    public double getOutPoint() {
        try {
            Stats.onNativeCall();
            return _nGetOutPoint(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public String getVersion() {
        try {
            Stats.onNativeCall();
            return _nGetVersion(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public Point getSize() {
        try {
            return _nGetSize(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public static native long _nGetFinalizer();
    public static native long _nMake(long textPtr);
    public static native long _nMakeFromFile(String path);
    public static native long _nMakeFromData(long dataPtr);
    public static native void _nRender(long ptr, long canvasPtr);
    public static native void _nRenderRect(long ptr, long canvasPtr, float left, float top, float right, float bottom, int flags);
    public static native void _nSeek(long ptr, float t, long invalidationControllerPtr);
    public static native void _nSeekFrame(long ptr, double t, long invalidationControllerPtr);
    public static native void _nSeekFrameTime(long ptr, double t, long invalidationControllerPtr);
    public static native double _nGetDuration(long ptr);
    public static native double _nGetFps(long ptr);
    public static native double _nGetInPoint(long ptr);
    public static native double _nGetOutPoint(long ptr);
    public static native String _nGetVersion(long ptr);
    public static native Point _nGetSize(long ptr);

    public static class Builder extends Managed {
        static { Library.staticLoad(); }

        @ApiStatus.Internal
        public Builder(long ptr) {
            super(ptr, _FinalizerHolder.PTR);
        }

        @ApiStatus.Internal
        public static class _FinalizerHolder {
            public static final long PTR = _nGetFinalizer();
        }

        public Builder() {
            this(new AnimationBuilderFlag[0]);
        }

        public Builder(AnimationBuilderFlag... builderFlags) {
            this(_nCreateInstance(computeFlagsValue(builderFlags)));
            Stats.onNativeCall();
        }

        private static int computeFlagsValue(AnimationBuilderFlag... builderFlags) {
            int flags = 0;
            for (var flag : builderFlags)
                flags |= flag._flag;
            return flags;
        }

        /**
         * <p>Specify a font manager for loading animation fonts.</p>
         */
        public Builder setFontManager(FontMgr fontMgr) {
            try {
                Stats.onNativeCall();
                _nSetFontManager(_ptr, Native.getPtr(fontMgr));
                return this;
            } finally {
                Reference.reachabilityFence(fontMgr);
            }
        }

        /**
         * <p>Register a {@link #Logger} with this builder.</p>
         */
        public Builder setLogger(Logger logger) {
            try {
                Stats.onNativeCall();
                _nSetLogger(_ptr, Native.getPtr(logger));
                return this;
            } finally {
                Reference.reachabilityFence(logger);
            }
        }

        @NotNull
        public Animation make(@NotNull ManagedString data) {
            Stats.onNativeCall();
            long ptr = _nMake(Native.getPtr(data));
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from string=\"" + data.toString() + "\"");
            return new Animation(ptr);
        }

        @NotNull
        public Animation makeFromFile(String path) {
            Stats.onNativeCall();
            long ptr = _nMakeFromFile(path);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from path=\"" + path + "\"");
            return new Animation(ptr);
        }

        @NotNull
        public Animation makeFromData(@NotNull Data data) {
            Stats.onNativeCall();
            long ptr = _nMakeFromData(Native.getPtr(data));
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from data.");
            return new Animation(ptr);
        }

        public static native long _nGetFinalizer();
        public static native long _nCreateInstance(int flags);
        public static native void _nSetFontManager(long ptr, long fontMgrPtr);
        public static native void _nSetLogger(long ptr, long loggerPtr);
        public static native long _nMake(long textPtr);
        public static native long _nMakeFromFile(String path);
        public static native long _nMakeFromData(long dataPtr);
    }
}