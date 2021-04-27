package org.jetbrains.skija;

import java.lang.ref.*;
import java.util.function.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Picture extends RefCnt {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public Picture(long ptr) {
        super(ptr);
    }

    /**
     * Recreates Picture that was serialized into data. Returns constructed Picture
     * if successful; otherwise, returns null. Fails if data does not permit
     * constructing valid Picture.
     */
    @Nullable
    public static Picture makeFromData(Data data) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeFromData(Native.getPtr(data));
            return ptr == 0 ? null : new Picture(ptr);
        } finally {
            Reference.reachabilityFence(data);
        }
    }

    /**
     * <p>Replays the drawing commands on the specified canvas. In the case that the
     * commands are recorded, each command in the Picture is sent separately to canvas.</p>
     *
     * <p>To add a single command to draw Picture to recording canvas, call
     * {@link Canvas#drawPicture} instead.</p>
     *
     * @param canvas  receiver of drawing commands
     * @return        this
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_playback">https://fiddle.skia.org/c/@Picture_playback</a>
     */
    public Picture playback(Canvas canvas) {
        return playback(canvas, null);
    }

    /**
     * <p>Replays the drawing commands on the specified canvas. In the case that the
     * commands are recorded, each command in the Picture is sent separately to canvas.</p>
     *
     * <p>To add a single command to draw Picture to recording canvas, call
     * {@link Canvas#drawPicture} instead.</p>
     *
     * @param canvas  receiver of drawing commands
     * @param abort   return true to interrupt the playback
     * @return        this
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_playback">https://fiddle.skia.org/c/@Picture_playback</a>
     */
    public Picture playback(Canvas canvas, @Nullable BooleanSupplier abort) {
        try {
            Stats.onNativeCall();
            _nPlayback(_ptr, Native.getPtr(canvas), abort);
            return this;
        } finally {
            Reference.reachabilityFence(canvas);
        }
    }

    /**
     * <p>Returns cull Rect for this picture, passed in when Picture was created.
     * Returned Rect does not specify clipping Rect for Picture; cull is hint
     * of Picture bounds.</p>
     *
     * <p>Picture is free to discard recorded drawing commands that fall outside cull.</p>
     *
     * @return  bounds passed when Picture was created
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_cullRect">https://fiddle.skia.org/c/@Picture_cullRect</a>
     */
    public Rect getCullRect() {
        try {
            Stats.onNativeCall();
            return _nGetCullRect(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns a non-zero value unique among Picture in Skia process.
     *
     * @return  identifier for Picture
     */
    public int getUniqueId() {
        try {
            Stats.onNativeCall();
            return _nGetUniqueId(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  storage containing Data describing Picture.
     *
     * @see  <a href="https://fiddle.skia.org/c/@Picture_serialize">https://fiddle.skia.org/c/@Picture_serialize</a>
     */
    public Data serializeToData() {
        try {
            Stats.onNativeCall();
            return new Data(_nSerializeToData(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns a placeholder Picture. Result does not draw, and contains only
     * cull Rect, a hint of its bounds. Result is immutable; it cannot be changed
     * later. Result identifier is unique.</p>
     *
     * <p>Returned placeholder can be intercepted during playback to insert other
     * commands into Canvas draw stream.</p>
     *
     * @param cull  placeholder dimensions
     * @return      placeholder with unique identifier
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_MakePlaceholder">https://fiddle.skia.org/c/@Picture_MakePlaceholder</a>
     */
    public static Picture makePlaceholder(@NotNull Rect cull) {
        Stats.onNativeCall();
        return new Picture(_nMakePlaceholder(cull._left, cull._top, cull._right, cull._bottom));
    }

    /**
     * Returns the approximate number of operations in SkPicture. Returned value
     * may be greater or less than the number of SkCanvas calls
     * recorded: some calls may be recorded as more than one operation, other
     * calls may be optimized away.
     *
     * @return  approximate operation count
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_approximateOpCount">https://fiddle.skia.org/c/@Picture_approximateOpCount</a>
     */
    public int getApproximateOpCount() {
        try {
            Stats.onNativeCall();
            return _nGetApproximateOpCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Returns the approximate byte size of Picture. Does not include large objects
     * referenced by Picture.
     *
     * @return  approximate size
     *
     * @see <a href="https://fiddle.skia.org/c/@Picture_approximateBytesUsed">https://fiddle.skia.org/c/@Picture_approximateBytesUsed</a>
     */
    public long getApproximateBytesUsed() {
        try {
            Stats.onNativeCall();
            return _nGetApproximateBytesUsed(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Return a new shader that will draw with this picture. The tile rect is considered
     * equal to the picture bounds.
     *
     * @param tmx   The tiling mode to use when sampling in the x-direction.
     * @param tmy   The tiling mode to use when sampling in the y-direction.
     * @param mode  How to filter the tiles
     * @return      Returns a new shader object. Note: this function never returns null.
     */
    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx,
                             @NotNull FilterTileMode tmy,
                             @NotNull FilterMode mode) {
        return makeShader(tmx, tmy, mode, null, null);
    }

    /**
     * Return a new shader that will draw with this picture. The tile rect is considered
     * equal to the picture bounds.
     *
     * @param tmx          The tiling mode to use when sampling in the x-direction.
     * @param tmy          The tiling mode to use when sampling in the y-direction.
     * @param mode         How to filter the tiles
     * @param localMatrix  Optional matrix used when sampling
     * @return             Returns a new shader object. Note: this function never returns null.
     */
    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx,
                             @NotNull FilterTileMode tmy,
                             @NotNull FilterMode mode,
                             @Nullable Matrix33 localMatrix) {
        return makeShader(tmx, tmy, mode, localMatrix, null);
    }

    /**
     * Return a new shader that will draw with this picture.
     *
     * @param tmx          The tiling mode to use when sampling in the x-direction.
     * @param tmy          The tiling mode to use when sampling in the y-direction.
     * @param mode         How to filter the tiles
     * @param localMatrix  Optional matrix used when sampling
     * @param tileRect     The tile rectangle in picture coordinates: this represents the subset
     *                     (or superset) of the picture used when building a tile. It is not
     *                     affected by localMatrix and does not imply scaling (only translation
     *                     and cropping). If null, the tile rect is considered equal to the picture
     *                     bounds.
     * @return             Returns a new shader object. Note: this function never returns null.
     */
    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx,
                             @NotNull FilterTileMode tmy,
                             @NotNull FilterMode mode,
                             @Nullable Matrix33 localMatrix,
                             @Nullable Rect tileRect)
    {
        try {
            Stats.onNativeCall();
            float[] arr = localMatrix == null ? null : localMatrix._mat;
            return new Shader(_nMakeShader(_ptr, tmx.ordinal(), tmy.ordinal(), mode.ordinal(), arr, tileRect));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native long _nMakeFromData(long dataPtr /*, SkDeserialProcs */);
    @ApiStatus.Internal public static native void _nPlayback(long ptr, long canvasPtr, @Nullable BooleanSupplier abort);
    @ApiStatus.Internal public static native Rect _nGetCullRect(long ptr);
    @ApiStatus.Internal public static native int  _nGetUniqueId(long ptr);
    @ApiStatus.Internal public static native long _nSerializeToData(long ptr /*, SkSerialProcs */);
    @ApiStatus.Internal public static native long _nMakePlaceholder(float left, float top, float right, float bottom);
    @ApiStatus.Internal public static native int  _nGetApproximateOpCount(long ptr);
    @ApiStatus.Internal public static native long _nGetApproximateBytesUsed(long ptr);
    @ApiStatus.Internal public static native long _nMakeShader(long ptr, int tmx, int tmy, int filterMode, float[] localMatrix, Rect tileRect);
}