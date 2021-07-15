package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Surface extends RefCnt {
    static {
        Library.staticLoad();
    }

    @ApiStatus.Internal public final DirectContext _context;
    @ApiStatus.Internal public final BackendRenderTarget _renderTarget;

    @NotNull @Contract("_ -> new")
    public static Surface makeRasterDirect(@NotNull Pixmap pixmap) {
        return makeRasterDirect(pixmap, null);
    }

    /**
     * <p>Allocates raster Surface. Canvas returned by Surface draws directly into pixels.</p>
     *
     * <p>Surface is returned if all parameters are valid. Valid parameters include:</p>
     *
     * <ul><li>info dimensions are greater than zero;</li>
     * <li>info contains ColorType and AlphaType supported by raster surface;</li>
     * <li>pixelsPtr is not 0;</li>
     * <li>rowBytes is large enough to contain info width pixels of ColorType.</li></ul>
     *
     * <p>Pixel buffer size should be info height times computed rowBytes.</p>
     *
     * <p>Pixels are not initialized.</p>
     *
     * <p>To access pixels after drawing, peekPixels() or readPixels().</p>
     *
     * @param imageInfo     width, height, ColorType, AlphaType, ColorSpace,
     *                      of raster surface; width and height must be greater than zero
     * @param pixelsPtr     pointer to destination pixels buffer
     * @param rowBytes      memory address of destination native pixels buffer
     * @return              created Surface
     */
    @NotNull @Contract("_, _, _ -> new")
    public static Surface makeRasterDirect(@NotNull ImageInfo imageInfo,
                                           long pixelsPtr,
                                           long rowBytes) {
        return makeRasterDirect(imageInfo, pixelsPtr, rowBytes, null);
    }

    @NotNull @Contract("_, _ -> new")
    public static Surface makeRasterDirect(@NotNull Pixmap pixmap,
                                           @Nullable SurfaceProps surfaceProps) {
        try {
            assert pixmap != null : "Can’t makeRasterDirect with pixmap == null";
            Stats.onNativeCall();
            long ptr = _nMakeRasterDirectWithPixmap(
                Native.getPtr(pixmap), surfaceProps
            );
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeRasterDirect(%s, %s)", pixmap, surfaceProps));
            return new Surface(ptr);
        } finally {
            Reference.reachabilityFence(pixmap);
        }
    }

    /**
     * <p>Allocates raster Surface. Canvas returned by Surface draws directly into pixels.</p>
     *
     * <p>Surface is returned if all parameters are valid. Valid parameters include:</p>
     *
     * <ul><li>info dimensions are greater than zero;</li>
     * <li>info contains ColorType and AlphaType supported by raster surface;</li>
     * <li>pixelsPtr is not 0;</li>
     * <li>rowBytes is large enough to contain info width pixels of ColorType.</li></ul>
     *
     * <p>Pixel buffer size should be info height times computed rowBytes.</p>
     *
     * <p>Pixels are not initialized.</p>
     *
     * <p>To access pixels after drawing, peekPixels() or readPixels().</p>
     *
     * @param imageInfo     width, height, ColorType, AlphaType, ColorSpace,
     *                      of raster surface; width and height must be greater than zero
     * @param pixelsPtr     pointer to destination pixels buffer
     * @param rowBytes      memory address of destination native pixels buffer
     * @param surfaceProps  LCD striping orientation and setting for device independent fonts;
     *                      may be null
     * @return              created Surface
     */
    @NotNull @Contract("_, _, _, _ -> new")
    public static Surface makeRasterDirect(@NotNull ImageInfo imageInfo,
                                           long pixelsPtr,
                                           long rowBytes,
                                           @Nullable SurfaceProps surfaceProps) {
        try {
            assert imageInfo != null : "Can’t makeRasterDirect with imageInfo == null";
            Stats.onNativeCall();
            long ptr = _nMakeRasterDirect(
                imageInfo._width,
                imageInfo._height,
                imageInfo._colorInfo._colorType.ordinal(),
                imageInfo._colorInfo._alphaType.ordinal(),
                Native.getPtr(imageInfo._colorInfo._colorSpace),
                pixelsPtr,
                rowBytes,
                surfaceProps);
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeRasterDirect(%s, %d, %d, %s)", imageInfo, pixelsPtr, rowBytes, surfaceProps));
            return new Surface(ptr);
        } finally {
            Reference.reachabilityFence(imageInfo._colorInfo._colorSpace);
        }
    }

    /**
     * <p>Allocates raster Surface. Canvas returned by Surface draws directly into pixels.
     * Allocates and zeroes pixel memory. Pixel memory size is imageInfo.height() times imageInfo.minRowBytes().
     * Pixel memory is deleted when Surface is deleted.</p>
     *
     * <p>Surface is returned if all parameters are valid. Valid parameters include:</p>
     *
     * <ul><li>info dimensions are greater than zero;</li>
     * <li>info contains ColorType and AlphaType supported by raster surface;</li></ul>
     *
     * @param imageInfo     width, height, ColorType, AlphaType, ColorSpace,
     *                      of raster surface; width and height must be greater than zero
     * @return              new Surface
     */
    @NotNull @Contract("_, _, _ -> new")
    public static Surface makeRaster(@NotNull ImageInfo imageInfo) {
        return makeRaster(imageInfo, 0, null);
    }

    /**
     * <p>Allocates raster Surface. Canvas returned by Surface draws directly into pixels.
     * Allocates and zeroes pixel memory. Pixel memory size is imageInfo.height() times
     * rowBytes, or times imageInfo.minRowBytes() if rowBytes is zero.
     * Pixel memory is deleted when Surface is deleted.</p>
     *
     * <p>Surface is returned if all parameters are valid. Valid parameters include:</p>
     *
     * <ul><li>info dimensions are greater than zero;</li>
     * <li>info contains ColorType and AlphaType supported by raster surface;</li>
     * <li>rowBytes is large enough to contain info width pixels of ColorType, or is zero.</li></ul>
     *
     * <p>If rowBytes is zero, a suitable value will be chosen internally.</p>
     *
     * @param imageInfo     width, height, ColorType, AlphaType, ColorSpace,
     *                      of raster surface; width and height must be greater than zero
     * @param rowBytes      interval from one Surface row to the next; may be zero
     * @return              new Surface
     */
    @NotNull @Contract("_, _, _ -> new")
    public static Surface makeRaster(@NotNull ImageInfo imageInfo,
                                     long rowBytes) {
        return makeRaster(imageInfo, rowBytes, null);
    }

    /**
     * <p>Allocates raster Surface. Canvas returned by Surface draws directly into pixels.
     * Allocates and zeroes pixel memory. Pixel memory size is imageInfo.height() times
     * rowBytes, or times imageInfo.minRowBytes() if rowBytes is zero.
     * Pixel memory is deleted when Surface is deleted.</p>
     *
     * <p>Surface is returned if all parameters are valid. Valid parameters include:</p>
     *
     * <ul><li>info dimensions are greater than zero;</li>
     * <li>info contains ColorType and AlphaType supported by raster surface;</li>
     * <li>rowBytes is large enough to contain info width pixels of ColorType, or is zero.</li></ul>
     *
     * <p>If rowBytes is zero, a suitable value will be chosen internally.</p>
     *
     * @param imageInfo     width, height, ColorType, AlphaType, ColorSpace,
     *                      of raster surface; width and height must be greater than zero
     * @param rowBytes      interval from one Surface row to the next; may be zero
     * @param surfaceProps  LCD striping orientation and setting for device independent fonts;
     *                      may be null
     * @return              new Surface
     */
    @NotNull @Contract("_, _, _ -> new")
    public static Surface makeRaster(@NotNull ImageInfo imageInfo,
                                     long rowBytes,
                                     @Nullable SurfaceProps surfaceProps) {
        try {
            assert imageInfo != null : "Can’t makeRaster with imageInfo == null";
            Stats.onNativeCall();
            long ptr = _nMakeRaster(
                imageInfo._width,
                imageInfo._height,
                imageInfo._colorInfo._colorType.ordinal(),
                imageInfo._colorInfo._alphaType.ordinal(),
                Native.getPtr(imageInfo._colorInfo._colorSpace),
                rowBytes,
                surfaceProps);
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeRaster(%s, %d, %s)", imageInfo, rowBytes, surfaceProps));
            return new Surface(ptr);
        } finally {
            Reference.reachabilityFence(imageInfo._colorInfo._colorSpace);
        }
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
     * @param context       GPU context
     * @param rt            texture residing on GPU
     * @param origin        surfaceOrigin pins either the top-left or the bottom-left corner to the origin.
     * @param colorFormat   color format
     * @param colorSpace    range of colors; may be null
     * @return              Surface if all parameters are valid; otherwise, null
     * @see <a href="https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture">https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture</a>
     */
    @NotNull
    public static Surface makeFromBackendRenderTarget(@NotNull DirectContext context,
                                                      @NotNull BackendRenderTarget rt,
                                                      @NotNull SurfaceOrigin origin,
                                                      @NotNull SurfaceColorFormat colorFormat,
                                                      @Nullable ColorSpace colorSpace) {
        return makeFromBackendRenderTarget(context, rt, origin, colorFormat, colorSpace, null);
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
     * @param context       GPU context
     * @param rt            texture residing on GPU
     * @param origin        surfaceOrigin pins either the top-left or the bottom-left corner to the origin.
     * @param colorFormat   color format
     * @param colorSpace    range of colors; may be null
     * @param surfaceProps  LCD striping orientation and setting for device independent fonts; may be null
     * @return              Surface if all parameters are valid; otherwise, null
     * @see <a href="https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture">https://fiddle.skia.org/c/@Surface_MakeFromBackendTexture</a>
     */
    @NotNull
    public static Surface makeFromBackendRenderTarget(@NotNull DirectContext context,
                                                      @NotNull BackendRenderTarget rt,
                                                      @NotNull SurfaceOrigin origin,
                                                      @NotNull SurfaceColorFormat colorFormat,
                                                      @Nullable ColorSpace colorSpace,
                                                      @Nullable SurfaceProps surfaceProps) {
        try {
            assert context != null : "Can’t makeFromBackendRenderTarget with context == null";
            assert rt != null : "Can’t makeFromBackendRenderTarget with rt == null";
            assert origin != null : "Can’t makeFromBackendRenderTarget with origin == null";
            assert colorFormat != null : "Can’t makeFromBackendRenderTarget with colorFormat == null";
            Stats.onNativeCall();
            long ptr = _nMakeFromBackendRenderTarget(Native.getPtr(context), Native.getPtr(rt), origin.ordinal(), colorFormat.ordinal(), Native.getPtr(colorSpace), surfaceProps);
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeFromBackendRenderTarget(%s, %s, %s, %s, %s)", context, rt, origin, colorFormat, colorSpace));
            return new Surface(ptr, context, rt);
        } finally {
            Reference.reachabilityFence(context);
            Reference.reachabilityFence(rt);
            Reference.reachabilityFence(colorSpace);
        }
    }

    @NotNull
    public static Surface makeFromMTKView(@NotNull DirectContext context,
                                          long mtkViewPtr,
                                          @NotNull SurfaceOrigin origin,
                                          int sampleCount,
                                          @NotNull SurfaceColorFormat colorFormat,
                                          @Nullable ColorSpace colorSpace,
                                          @Nullable SurfaceProps surfaceProps) {
        try {
            assert context != null : "Can’t makeFromBackendRenderTarget with context == null";
            assert origin != null : "Can’t makeFromBackendRenderTarget with origin == null";
            assert colorFormat != null : "Can’t makeFromBackendRenderTarget with colorFormat == null";
            Stats.onNativeCall();
            long ptr = _nMakeFromMTKView(Native.getPtr(context), mtkViewPtr, origin.ordinal(), sampleCount, colorFormat.ordinal(), Native.getPtr(colorSpace), surfaceProps);
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeFromMTKView(%s, %s, %s, %s, %s, %s)", context, mtkViewPtr, origin, colorFormat, colorSpace, surfaceProps));
            return new Surface(ptr, context);
        } finally {
            Reference.reachabilityFence(context);
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
    @NotNull
    public static Surface makeRasterN32Premul(int width, int height) {
        Stats.onNativeCall();
        long ptr = _nMakeRasterN32Premul(width, height);
        if (ptr == 0)
            throw new IllegalArgumentException(String.format("Failed Surface.makeRasterN32Premul(%d, %d)", width, height));
        return new Surface(ptr);
    }

    /**
     * <p>Returns Surface on GPU indicated by context. Allocates memory for
     * pixels, based on the width, height, and ColorType in ImageInfo.
     * describes the pixel format in ColorType, and transparency in
     * AlphaType, and color matching in ColorSpace.</p>
     *
     * @param context               GPU context
     * @param budgeted              selects whether allocation for pixels is tracked by context
     * @param imageInfo             width, height, ColorType, AlphaType, ColorSpace;
     *                              width, or height, or both, may be zero
     * @return                      new SkSurface
     */
    @NotNull @Contract("_, _, _ -> new")
    public static Surface makeRenderTarget(@NotNull DirectContext context,
                                           boolean budgeted,
                                           @NotNull ImageInfo imageInfo) {
        return makeRenderTarget(context, budgeted, imageInfo, 0, SurfaceOrigin.BOTTOM_LEFT, null, false);
    }

    /**
     * <p>Returns Surface on GPU indicated by context. Allocates memory for
     * pixels, based on the width, height, and ColorType in ImageInfo.
     * describes the pixel format in ColorType, and transparency in
     * AlphaType, and color matching in ColorSpace.</p>
     *
     * <p>sampleCount requests the number of samples per pixel.
     * Pass zero to disable multi-sample anti-aliasing.  The request is rounded
     * up to the next supported count, or rounded down if it is larger than the
     * maximum supported count.</p>
     *
     * @param context               GPU context
     * @param budgeted              selects whether allocation for pixels is tracked by context
     * @param imageInfo             width, height, ColorType, AlphaType, ColorSpace;
     *                              width, or height, or both, may be zero
     * @param sampleCount           samples per pixel, or 0 to disable full scene anti-aliasing
     * @param surfaceProps          LCD striping orientation and setting for device independent
     *                               fonts; may be null
     * @return                      new SkSurface
     */
    @NotNull @Contract("_, _, _, _, _ -> new")
    public static Surface makeRenderTarget(@NotNull DirectContext context,
                                           boolean budgeted,
                                           @NotNull ImageInfo imageInfo,
                                           int sampleCount,
                                           @Nullable SurfaceProps surfaceProps) {
        return makeRenderTarget(context, budgeted, imageInfo, sampleCount, SurfaceOrigin.BOTTOM_LEFT, surfaceProps, false);
    }

    /**
     * <p>Returns Surface on GPU indicated by context. Allocates memory for
     * pixels, based on the width, height, and ColorType in ImageInfo.
     * describes the pixel format in ColorType, and transparency in
     * AlphaType, and color matching in ColorSpace.</p>
     *
     * <p>sampleCount requests the number of samples per pixel.
     * Pass zero to disable multi-sample anti-aliasing.  The request is rounded
     * up to the next supported count, or rounded down if it is larger than the
     * maximum supported count.</p>
     *
     * @param context               GPU context
     * @param budgeted              selects whether allocation for pixels is tracked by context
     * @param imageInfo             width, height, ColorType, AlphaType, ColorSpace;
     *                              width, or height, or both, may be zero
     * @param sampleCount           samples per pixel, or 0 to disable full scene anti-aliasing
     * @param origin                pins either the top-left or the bottom-left corner to the origin.
     * @param surfaceProps          LCD striping orientation and setting for device independent
     *                               fonts; may be null
     * @return                      new SkSurface
     */
    @NotNull @Contract("_, _, _, _, _, _ -> new")
    public static Surface makeRenderTarget(@NotNull DirectContext context,
                                           boolean budgeted,
                                           @NotNull ImageInfo imageInfo,
                                           int sampleCount,
                                           @NotNull SurfaceOrigin origin,
                                           @Nullable SurfaceProps surfaceProps) {
        return makeRenderTarget(context, budgeted, imageInfo, sampleCount, origin, surfaceProps, false);
    }

    /**
     * <p>Returns Surface on GPU indicated by context. Allocates memory for
     * pixels, based on the width, height, and ColorType in ImageInfo.
     * describes the pixel format in ColorType, and transparency in
     * AlphaType, and color matching in ColorSpace.</p>
     *
     * <p>sampleCount requests the number of samples per pixel.
     * Pass zero to disable multi-sample anti-aliasing.  The request is rounded
     * up to the next supported count, or rounded down if it is larger than the
     * maximum supported count.</p>
     *
     * <p>shouldCreateWithMips hints that Image returned by {@link #makeImageSnapshot()} is mip map.</p>
     *
     * @param context               GPU context
     * @param budgeted              selects whether allocation for pixels is tracked by context
     * @param imageInfo             width, height, ColorType, AlphaType, ColorSpace;
     *                              width, or height, or both, may be zero
     * @param sampleCount           samples per pixel, or 0 to disable full scene anti-aliasing
     * @param origin                pins either the top-left or the bottom-left corner to the origin.
     * @param surfaceProps          LCD striping orientation and setting for device independent
     *                               fonts; may be null
     * @param shouldCreateWithMips  hint that SkSurface will host mip map images
     * @return                      new SkSurface
     */
    @NotNull @Contract("_, _, _, _, _, _, _ -> new")
    public static Surface makeRenderTarget(@NotNull DirectContext context,
                                           boolean budgeted,
                                           @NotNull ImageInfo imageInfo,
                                           int sampleCount,
                                           @NotNull SurfaceOrigin origin,
                                           @Nullable SurfaceProps surfaceProps,
                                           boolean shouldCreateWithMips) {
        try {
            assert context != null : "Can’t makeFromBackendRenderTarget with context == null";
            assert imageInfo != null : "Can’t makeFromBackendRenderTarget with imageInfo == null";
            assert origin != null : "Can’t makeFromBackendRenderTarget with origin == null";
            Stats.onNativeCall();
            long ptr = _nMakeRenderTarget(
                Native.getPtr(context),
                budgeted,
                imageInfo._width,
                imageInfo._height,
                imageInfo._colorInfo._colorType.ordinal(),
                imageInfo._colorInfo._alphaType.ordinal(),
                Native.getPtr(imageInfo._colorInfo._colorSpace),
                sampleCount,
                origin.ordinal(),
                surfaceProps,
                shouldCreateWithMips);
            if (ptr == 0)
                throw new IllegalArgumentException(String.format("Failed Surface.makeRenderTarget(%s, %b, %s, %d, %s, %s, %b)", context, budgeted, imageInfo, sampleCount, origin, surfaceProps, shouldCreateWithMips));
            return new Surface(ptr, context);
        } finally {
            Reference.reachabilityFence(context);
            Reference.reachabilityFence(imageInfo._colorInfo._colorSpace);
        }
    }

    /**
     * Returns Surface without backing pixels. Drawing to Canvas returned from Surface
     * has no effect. Calling makeImageSnapshot() on returned Surface returns null.
     *
     * @param width   one or greater
     * @param height  one or greater
     * @return        Surface if width and height are positive
     *
     * @see <a href="https://fiddle.skia.org/c/@Surface_MakeNull">https://fiddle.skia.org/c/@Surface_MakeNull</a>
     */
    @NotNull @Contract("_, _ -> new")
    public static Surface makeNull(int width, int height) {
        Stats.onNativeCall();
        long ptr = _nMakeNull(width, height);
        if (ptr == 0)
            throw new IllegalArgumentException(String.format("Failed Surface.makeNull(%d, %d)", width, height));
        return new Surface(ptr);
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

    public boolean peekPixels(@NotNull Pixmap pixmap) {
        try {
            Stats.onNativeCall();
            return _nPeekPixels(_ptr, Native.getPtr(pixmap));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(pixmap);
        }
    }

    public boolean readPixels(Pixmap pixmap, int srcX, int srcY) {
        try {
            Stats.onNativeCall();
            return _nReadPixelsToPixmap(_ptr, Native.getPtr(pixmap), srcX, srcY);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(pixmap);
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

    public void writePixels(Pixmap pixmap, int x, int y) {
        try {
            Stats.onNativeCall();
            _nWritePixelsFromPixmap(_ptr, Native.getPtr(pixmap), x, y);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(pixmap);
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
        _context = null;
        _renderTarget = null;
    }

    @ApiStatus.Internal
    public Surface(long ptr, DirectContext context) {
        super(ptr);
        _context = context;
        _renderTarget = null;
    }

    @ApiStatus.Internal
    public Surface(long ptr, DirectContext context, BackendRenderTarget renderTarget) {
        super(ptr);
        _context = context;
        _renderTarget = renderTarget;
    }

    public static native long _nMakeRasterDirect(int width, int height, int colorType, int alphaType, long colorSpacePtr, long pixelsPtr, long rowBytes, SurfaceProps surfaceProps);
    public static native long _nMakeRasterDirectWithPixmap(long pixmapPtr, SurfaceProps surfaceProps);
    public static native long _nMakeRaster(int width, int height, int colorType, int alphaType, long colorSpacePtr, long rowBytes, SurfaceProps surfaceProps);
    public static native long _nMakeRasterN32Premul(int width, int height);
    public static native long _nMakeFromBackendRenderTarget(long pContext, long pBackendRenderTarget, int surfaceOrigin, int colorType, long colorSpacePtr, SurfaceProps surfaceProps);
    public static native long _nMakeFromMTKView(long contextPtr, long mtkViewPtr, int surfaceOrigin, int sampleCount, int colorType, long colorSpacePtr, SurfaceProps surfaceProps);
    public static native long _nMakeRenderTarget(long contextPtr, boolean budgeted, int width, int height, int colorType, int alphaType, long colorSpacePtr, int sampleCount, int surfaceOrigin, SurfaceProps surfaceProps, boolean shouldCreateWithMips);
    public static native long _nMakeNull(int width, int height);
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
    public static native boolean _nPeekPixels(long ptr, long pixmapPtr);
    public static native boolean _nReadPixelsToPixmap(long ptr, long pixmapPtr, int srcX, int srcY);
    public static native boolean _nReadPixels(long ptr, long bitmapPtr, int srcX, int srcY);
    public static native void _nWritePixelsFromPixmap(long ptr, long pixmapPtr, int x, int y);
    public static native void _nWritePixels(long ptr, long bitmapPtr, int x, int y);
    public static native void _nFlushAndSubmit(long ptr, boolean syncCpu);
    public static native void _nFlush(long ptr);
    public static native boolean _nUnique(long ptr);
}