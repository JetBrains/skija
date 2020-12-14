package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Surface extends RefCnt {
    static {
        Library.staticLoad();
    }

    /**
     * <p>Wraps a GPU-backed buffer into {@link Surface}.</p>
     *
     * <p>Caller must ensure backendRenderTarget is valid for the lifetime of returned {@link Surface}.</p>
     *
     * <p>{@link Surface} is returned if all parameters are valid. backendRenderTarget is valid if its pixel
     * configuration agrees with colorSpace and context;
     * for instance, if backendRenderTarget has an sRGB configuration, then context must support sRGB,
     * and colorSpace must be present. Further, backendRenderTarget width and height must not exceed
     * context capabilities, and the context must be able to support back-end render targets.</p>
     *
     * @param context     GPU context
     * @param rt          texture residing on GPU
     * @param origin      surfaceOrigin pins either the top-left or the bottom-left corner to the origin.
     * @param colorFormat color format
     * @param colorSpace  range of colors; may be null
     * @return Surface if all parameters are valid; otherwise, null
     * @see <a href="https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture">https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture</a>
     */
    @Nullable
    public static Surface makeFromBackendRenderTarget(DirectContext context, BackendRenderTarget rt, SurfaceOrigin origin, SurfaceColorFormat colorFormat, @Nullable ColorSpace colorSpace) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeFromBackendRenderTarget(Native.getPtr(context), Native.getPtr(rt), origin.ordinal(), colorFormat.ordinal(), Native.getPtr(colorSpace));
            return ptr == 0 ? null : new Surface(ptr);
        } finally {
            Reference.reachabilityFence(context);
            Reference.reachabilityFence(rt);
            Reference.reachabilityFence(colorSpace);
        }
    }

    /**
     * <p>Allocates raster {@link Surface}.</p>
     *
     * <p>Canvas returned by Surface draws directly into pixels. Allocates and zeroes pixel memory.
     * Pixel memory size is height times width times four. Pixel memory is deleted when Surface is deleted.</p>
     *
     * <p>Internally, sets ImageInfo to width, height, native color type, and ColorAlphaType.PREMUL.</p>
     *
     * <p>Surface is returned if width and height are greater than zero.</p>
     *
     * <p>Use to create Surface that matches PMColor, the native pixel arrangement on the platform.
     * Surface drawn to output device skips converting its pixel format.</p>
     *
     * @param width  pixel column count; must be greater than zero
     * @param height pixel row count; must be greater than zero
     * @return Surface if all parameters are valid; otherwise, null
     * @see <a href="https://fiddle.skia.org/c/@Surface_MakeRasterN32Premul">https://fiddle.skia.org/c/@Surface_MakeRasterN32Premul</a>
     */
    @Nullable
    public static Surface makeRasterN32Premul(int width, int height) {
        Stats.onNativeCall();
        long ptr = _nMakeRasterN32Premul(width, height);
        return ptr == 0 ? null : new Surface(ptr);
    }

    /**
     * <p>Returns pixel count in each row; may be zero or greater.</p>
     *
     * @return number of pixel columns
     * @see <a href="https://fiddle.skia.org/c/@Surface_width">https://fiddle.skia.org/c/@Surface_width</a>
     */
    public int getWidth() {
        try {
            Stats.onNativeCall();
            return _nGetWidth(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns pixel row count; may be zero or greater.</p>
     *
     * @return number of pixel rows
     * @see <a href="https://fiddle.skia.org/c/@Surface_height">https://fiddle.skia.org/c/@Surface_height</a>
     */
    public int getHeight() {
        try {
            Stats.onNativeCall();
            return _nGetHeight(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns an ImageInfo describing the surface.</p>
     *
     * @return ImageInfo describing the surface.
     */
    @NotNull
    public ImageInfo getImageInfo() {
        try {
            Stats.onNativeCall();
            return _nGetImageInfo(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns unique value identifying the content of Surface.</p>
     *
     * <p>Returned value changes each time the content changes.
     * Content is changed by drawing, or by calling notifyContentWillChange().</p>
     *
     * @return unique content identifier
     */
    public int getGenerationId() {
        try {
            Stats.onNativeCall();
            return _nGenerationId(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Notifies that Surface contents will be changed by code outside of Skia.</p>
     *
     * <p>Subsequent calls to generationID() return a different value.</p>
     *
     * @see <a href="https://fiddle.skia.org/c/@Surface_notifyContentWillChange">https://fiddle.skia.org/c/@Surface_notifyContentWillChange</a>
     */
    public void notifyContentWillChange(ContentChangeMode mode) {
        try {
            Stats.onNativeCall();
            _nNotifyContentWillChange(_ptr, mode.ordinal());
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns the recording context being used by the Surface.</p>
     *
     * @return the recording context, if available; null otherwise
     */
    @Nullable
    public DirectContext getRecordingContext() {
        try {
            Stats.onNativeCall();
            long ptr = _nGetRecordingContext(_ptr);
            return ptr == 0 ? null : new DirectContext(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns Canvas that draws into Surface.</p>
     *
     * <p>Subsequent calls return the same Canvas.
     * Canvas returned is managed and owned by Surface, and is deleted when Surface is deleted.</p>
     *
     * @return Canvas for Surface
     */
    @NotNull
    public Canvas getCanvas() {
        try {
            Stats.onNativeCall();
            long ptr = _nGetCanvas(_ptr);
            return ptr == 0 ? null : new Canvas(ptr, false, this);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns a compatible Surface, or null.</p>
     *
     * <p>Returned Surface contains the same raster, GPU, or null properties as the original.
     * Returned Surface does not share the same pixels.</p>
     *
     * <p>Returns null if imageInfo width or height are zero, or if imageInfo is incompatible with Surface.</p>
     *
     * @param imageInfo contains width, height, AlphaType, ColorType, ColorSpace
     * @return compatible SkSurface or null
     * @see <a href="https://fiddle.skia.org/c/@Surface_makeSurface">https://fiddle.skia.org/c/@Surface_makeSurface</a>
     */
    @Nullable
    public Surface makeSurface(ImageInfo imageInfo) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeSurfaceI(_ptr,
                    imageInfo._width,
                    imageInfo._height,
                    imageInfo._colorInfo._colorType.ordinal(),
                    imageInfo._colorInfo._alphaType.ordinal(),
                    Native.getPtr(imageInfo._colorInfo._colorSpace));
            return new Surface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Calls makeSurface(ImageInfo) with the same ImageInfo as this surface,
     * but with the specified width and height.</p>
     *
     * <p>Returned Surface contains the same raster, GPU, or null properties as the original.
     * Returned Surface does not share the same pixels.</p>
     *
     * <p>Returns null if imageInfo width or height are zero, or if imageInfo is incompatible with Surface.</p>
     *
     * @param width  pixel column count; must be greater than zero
     * @param height pixel row count; must be greater than zero
     * @return compatible SkSurface or null
     */
    @Nullable
    public Surface makeSurface(int width, int height) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeSurface(_ptr, width, height);
            return new Surface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Returns Image capturing Surface contents.</p>
     *
     * <p>Subsequent drawing to Surface contents are not captured.
     * Image allocation is accounted for if Surface was created with SkBudgeted::kYes.</p>
     *
     * @return Image initialized with Surface contents
     * @see <a href="https://fiddle.skia.org/c/@Surface_makeImageSnapshot">https://fiddle.skia.org/c/@Surface_makeImageSnapshot</a>
     */
    public Image makeImageSnapshot() {
        try {
            Stats.onNativeCall();
            return new Image(_nMakeImageSnapshot(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Like the no-parameter version, this returns an image of the current surface contents.</p>
     *
     * <p>This variant takes a rectangle specifying the subset of the surface that is of interest.
     * These bounds will be sanitized before being used.</p>
     *
     * <ul>
     *  <li>If bounds extends beyond the surface, it will be trimmed to just the intersection of it and the surface.</li>
     *  <li>If bounds does not intersect the surface, then this returns null.</li>
     *  <li>If bounds == the surface, then this is the same as calling the no-parameter variant.</li>
     * </ul>
     *
     * @return Image initialized with Surface contents or null
     * @see <a href="https://fiddle.skia.org/c/@Surface_makeImageSnapshot_2">https://fiddle.skia.org/c/@Surface_makeImageSnapshot_2</a>
     */
    @Nullable
    public Image makeImageSnapshot(IRect area) {
        try {
            Stats.onNativeCall();
            return new Image(_nMakeImageSnapshotR(_ptr, area._left, area._top, area._right, area._bottom));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Draws Surface contents to canvas, with its top-left corner at (x, y).</p>
     *
     * <p>If Paint paint is not null, apply ColorFilter, alpha, ImageFilter, and BlendMode.</p>
     *
     * @param canvas Canvas drawn into
     * @param x      horizontal offset in Canvas
     * @param y      vertical offset in Canvas
     * @param paint  Paint containing BlendMode, ColorFilter, ImageFilter, and so on; or null
     * @see <a href="https://fiddle.skia.org/c/@Surface_draw">https://fiddle.skia.org/c/@Surface_draw</a>
     */
    public void draw(Canvas canvas, int x, int y, Paint paint) {
        try {
            Stats.onNativeCall();
            _nDraw(_ptr, Native.getPtr(canvas), x, y, Native.getPtr(paint));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(canvas);
            Reference.reachabilityFence(paint);
        }
    }

    /**
     * <p>Copies Rect of pixels from Surface into bitmap.</p>
     *
     * <p>Source Rect corners are (srcX, srcY) and Surface (width(), height()).
     * Destination Rect corners are (0, 0) and (bitmap.width(), bitmap.height()).
     * Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to bitmap.colorType() and bitmap.alphaType() if required.</p>
     *
     * <p>Pixels are readable when Surface is raster, or backed by a GPU.</p>
     *
     * <p>The destination pixel storage must be allocated by the caller.</p>
     *
     * <p>Pixel values are converted only if ColorType and AlphaType do not match.
     * Only pixels within both source and destination rectangles are copied.
     * dst contents outside Rect intersection are unchanged.</p>
     *
     * <p>Pass negative values for srcX or srcY to offset pixels across or down destination.</p>
     *
     * <p>Does not copy, and returns false if:</p>
     * 
     * <ul>
     *  <li>Source and destination rectangles do not intersect.</li>
     *  <li>Surface pixels could not be converted to dst.colorType() or dst.alphaType().</li>
     *  <li>dst pixels could not be allocated.</li>
     *  <li>dst.rowBytes() is too small to contain one row of pixels.</li>
     * </ul>
     *
     * @param bitmap  storage for pixels copied from SkSurface
     * @param srcX    offset into readable pixels on x-axis; may be negative
     * @param srcY    offset into readable pixels on y-axis; may be negative
     * @return        true if pixels were copied
     * @see <a href="https://fiddle.skia.org/c/@Surface_readPixels_3">https://fiddle.skia.org/c/@Surface_readPixels_3</a>
     */
    public boolean readPixels(Bitmap bitmap, int srcX, int srcY) {
        try {
            Stats.onNativeCall();
            return _nReadPixels(_ptr, Native.getPtr(bitmap), srcX, srcY);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(bitmap);
        }
    }

    /**
     * <p>Copies Rect of pixels from the src Bitmap to the Surface.</p>
     *
     * <p>Source Rect corners are (0, 0) and (src.width(), src.height()).
     * Destination Rect corners are (dstX, dstY) and (dstX + Surface width(), dstY + Surface height()).</p>
     *
     * <p>Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to Surface colorType() and Surface alphaType() if required.</p>
     *
     * @param bitmap storage for pixels to copy to Surface
     * @param x      x-axis position relative to Surface to begin copy; may be negative
     * @param y      y-axis position relative to Surface to begin copy; may be negative
     * @see <a href="https://fiddle.skia.org/c/@Surface_writePixels_2">https://fiddle.skia.org/c/@Surface_writePixels_2</a>
     */
    public void writePixels(Bitmap bitmap, int x, int y) {
        try {
            Stats.onNativeCall();
            _nWritePixels(_ptr, Native.getPtr(bitmap), x, y);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(bitmap);
        }
    }

    /**
     * <p>Call to ensure all reads/writes of the surface have been issued to the underlying 3D API.</p>
     *
     * <p>Skia will correctly order its own draws and pixel operations.
     * This must to be used to ensure correct ordering when the surface backing store is accessed
     * outside Skia (e.g. direct use of the 3D API or a windowing system).
     * DirectContext has additional flush and submit methods that apply to all surfaces and images created from
     * a DirectContext.
     */
    public void flushAndSubmit() {
        try {
            Stats.onNativeCall();
            _nFlushAndSubmit(_ptr, false);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Call to ensure all reads/writes of the surface have been issued to the underlying 3D API.</p>
     *
     * <p>Skia will correctly order its own draws and pixel operations.
     * This must to be used to ensure correct ordering when the surface backing store is accessed
     * outside Skia (e.g. direct use of the 3D API or a windowing system).
     * DirectContext has additional flush and submit methods that apply to all surfaces and images created from
     * a DirectContext.
     *
     * @param syncCpu a flag determining if cpu should be synced
     */
    public void flushAndSubmit(boolean syncCpu) {
        try {
            Stats.onNativeCall();
            _nFlushAndSubmit(_ptr, syncCpu);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public void flush() {
        try {
            Stats.onNativeCall();
            _nFlush(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>May return true if the caller is the only owner.</p>
     *
     * <p>Ensures that all previous owner's actions are complete.</p>
     */
    public boolean isUnique() {
        try {
            Stats.onNativeCall();
            return _nUnique(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public Surface(long ptr) {
        super(ptr);
    }

    public static native long _nMakeFromBackendRenderTarget(long pContext, long pBackendRenderTarget, int surfaceOrigin, int colorType, long colorSpacePtr);
    public static native long _nMakeRasterN32Premul(int width, int height);
    public static native int _nGetWidth(long ptr);
    public static native int _nGetHeight(long ptr);
    public static native ImageInfo _nGetImageInfo(long ptr);
    public static native int _nGenerationId(long ptr);
    public static native void _nNotifyContentWillChange(long ptr, int mode);
    public static native long _nGetRecordingContext(long ptr);
    public static native long _nGetCanvas(long ptr);
    public static native long _nMakeSurfaceI(long ptr, int width, int height, int colorType, int alphaType, long colorSpacePtr);
    public static native long _nMakeSurface(long ptr, int width, int height);
    public static native long _nMakeImageSnapshot(long ptr);
    public static native long _nMakeImageSnapshotR(long ptr, int left, int top, int right, int bottom);
    public static native void _nDraw(long ptr, long canvasPtr, float x, float y, long paintPtr);
    public static native boolean _nReadPixels(long ptr, long bitmapPtr, int srcX, int srcY);
    public static native void _nWritePixels(long ptr, long bitmapPtr, int x, int y);
    public static native void _nFlushAndSubmit(long ptr, boolean syncCpu);
    public static native void _nFlush(long ptr);
    public static native boolean _nUnique(long ptr);
}