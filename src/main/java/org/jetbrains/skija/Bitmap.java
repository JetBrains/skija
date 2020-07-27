package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Bitmap extends Managed {
    @ApiStatus.Internal
    public Bitmap(long ptr) {
        super(ptr, _finalizerPtr);
    }

    /**
     * Creates an empty Bitmap without pixels, with {@link ColorType#UNKNOWN},
     * {@link ColorAlphaType#UNKNOWN}, and with a width and height of zero. 
     * PixelRef origin is set to (0, 0). Bitmap is not volatile.
     *
     * Use {@link #setInfo(ImageInfo, long)} to associate ColorType, ColorAlphaType, width, and height after Bitmap has been created.
     * @return  empty Bitmap
     * 
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_empty_constructor">https://fiddle.skia.org/c/@Bitmap_empty_constructor</a>
     */
    public Bitmap() {
        this(_nMake());
        Stats.onNativeCall();
    }

    /**
     * Copies settings from src to returned Bitmap. Shares pixels if src has pixels
     * allocated, so both bitmaps reference the same pixels.
     *
     * @return  copy of src
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_copy_const_SkBitmap">https://fiddle.skia.org/c/@Bitmap_copy_const_SkBitmap</a>
     */
    public Bitmap makeClone() {
        Stats.onNativeCall();
        return new Bitmap(_nMakeClone(_ptr));
    }

    /**
     * Swaps the fields of the two bitmaps.
     *
     * @param other  Bitmap exchanged with original
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_swap">https://fiddle.skia.org/c/@Bitmap_swap</a>
     */
    public void swap(Bitmap other) {
        Stats.onNativeCall();
        _nSwap(_ptr, Native.getPtr(other));
    }

    public ImageInfo getImageInfo() {
        Stats.onNativeCall();
        return _nGetImageInfo(_ptr);
    }

    /**
     * Returns pixel count in each row. Should be equal or less than
     * getRowBytes() / getImageInfo().getBytesPerPixel().
     *
     * May be less than getPixelRef().getWidth(). Will not exceed getPixelRef().getWidth() less
     *
     * @return  pixel width in ImageInfo
     */
    public int getWidth() {
        return getImageInfo()._width;
    }

    /**
     * Returns pixel row count.
     *
     * Maybe be less than getPixelRef().getHeight(). Will not exceed getPixelRef().getHeight()
     *
     * @return  pixel height in ImageInfo
    */
    public int getHeight() {
        return getImageInfo()._height;
    }

    public ColorInfo getColorInfo() {
        return getImageInfo()._colorInfo;
    }    

    public ColorType getColorType() {
        return getImageInfo()._colorInfo._colorType;
    }    

    public ColorAlphaType getAlphaType() {
        return getImageInfo()._colorInfo._alphaType;
    }

    public ColorSpace getColorSpace() {
        return getImageInfo()._colorInfo._colorSpace;
    }

    /**
     * Returns number of bytes per pixel required by ColorType.
     * Returns zero if colorType is {@link ColorType#UNKNOWN}.
     *
     * @return  bytes in pixel
     */
    public int getBytesPerPixel() {
        return getImageInfo().getBytesPerPixel();
    }

    /**
     * Returns number of pixels that fit on row. Should be greater than or equal to
     * getWidth().
     *
     * @return  maximum pixels per row
     */
    public int getRowBytesAsPixels() {
        Stats.onNativeCall();
        return _nGetRowBytesAsPixels(_ptr);
    }

    /**
     * Returns bit shift converting row bytes to row pixels.
     * Returns zero for {@link ColorType#UNKNOWN}.
     * 
     * @return  one of: 0, 1, 2, 3; left shift to convert pixels to bytes
     */
    public int getShiftPerPixel() {
        return getImageInfo().getShiftPerPixel();
    }    

    /**
     * Returns true if either getWidth() or getHeight() are zero.
     *
     * Does not check if PixelRef is null; call {@link drawsNothing()} to check
     * getWidth(), getHeight(), and PixelRef.
     *
     * @return  true if dimensions do not enclose area
     */
    public boolean isEmpty() {
        return getImageInfo().isEmpty();
    }

    /**
     * Returns true if PixelRef is null.
     *
     * Does not check if width or height are zero; call {@link #drawsNothing()}
     * to check width, height, and PixelRef.
     * 
     * @return  true if no PixelRef is associated
     */
    public boolean isNull() {
        Stats.onNativeCall();
        return _nIsNull(_ptr);
    }

    /**
     * Returns true if width or height are zero, or if PixelRef is null.
     * If true, Bitmap has no effect when drawn or drawn into.
     *
     * @return  true if drawing has no effect
     */
    public boolean drawsNothing() {
        return isEmpty() || isNull();
    }

    /**
     * Returns row bytes, the interval from one pixel row to the next. Row bytes
     * is at least as large as: getWidth() * getBytesPerPixel().
     *
     * Returns zero if getColorType() is {@link ColorType#UNKNOWN}, or if row bytes
     * supplied to setInfo() is not large enough to hold a row of pixels.
     *
     * @return  byte length of pixel row
     */
    public long getRowBytes() {
        Stats.onNativeCall();
        return _nGetRowBytes(_ptr);
    }

    /**
     * <p>Sets alpha type, if argument is compatible with current color type.
     * Returns true unless argument is {@link ColorAlphaType#UNKNOWN} and current 
     * value is {@link ColorAlphaType#UNKNOWN}.</p>
     *
     * <p>Returns true if current color type is {@link ColorType#UNKNOWN}.
     * Argument is ignored, and alpha type remains {@link ColorAlphaType#UNKNOWN}.</p>
     *
     * <p>Returns true if current color type is {@link ColorType#RGB_565} or
     * {@link ColorType#GRAY_8}. Argument is ignored, and alpha type remains
     * {@link ColorAlphaType#OPAQUE}.</p>
     * 
     * <p>If current color type is {@link ColorType#ARGB_4444}, {@link ColorType#RGBA_8888},
     * {@link ColorType#BGRA_8888}, or {@link ColorType#RGBA_F16}: returns true unless
     * argument is {@link ColorAlphaType#UNKNOWN} and current alpha type is not
     * {@link ColorAlphaType#UNKNOWN}. If current alpha type is
     * {@link ColorAlphaType#UNKNOWN}, argument is ignored.</p>
     *
     * <p>If current color type is {@link ColorType#ALPHA_8}, returns true unless
     * argument is {@link ColorAlphaType#UNKNOWN} and current alpha type is not 
     * {@link ColorAlphaType#UNKNOWN}. If current alpha type is
     * {@link ColorAlphaType#UNKNOWN}, argument is ignored. If argument is
     * {@link ColorAlphaType#UNPREMUL}, it is treated as {@link ColorAlphaType#PREMUL}.</p>
     *
     * <p>This changes alpha type in PixelRef; all bitmaps sharing PixelRef
     * are affected.</p>
     *
     * @return  true if alpha type is set
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_setAlphaType">https://fiddle.skia.org/c/@Bitmap_setAlphaType</a>
     */
    public boolean setAlphaType(ColorAlphaType alphaType) {
        Stats.onNativeCall();
        return _nSetAlphaType(_ptr, alphaType.ordinal());
    }

    /**
     * Returns minimum memory required for pixel storage.
     * Does not include unused memory on last row when getRowBytesAsPixels() exceeds getWidth().
     * Returns zero if height() or width() is 0.
     * Returns getHeight() times getRowBytes() if getColorType() is {@link ColorType#UNKNOWN}.
     * 
     * @return  size in bytes of image buffer
     */
    public long computeByteSize() {
        Stats.onNativeCall();
        return _nComputeByteSize(_ptr);
    }

    /**
     * <p>Returns true if pixels can not change.</p>
     *
     * <p>Most immutable Bitmap checks trigger an assert only on debug builds.</p>
     * 
     * @return  true if pixels are immutable
     * 
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_isImmutable">https://fiddle.skia.org/c/@Bitmap_isImmutable</a>
     */
    public boolean isImmutable() {
        Stats.onNativeCall();
        return _nIsImmutable(_ptr);
    }

    /**
     * <p>Sets internal flag to mark Bitmap as immutable. Once set, pixels can not change.
     * Any other bitmap sharing the same PixelRef are also marked as immutable.
     * Once PixelRef is marked immutable, the setting cannot be cleared.</p>
     * 
     * <p>Writing to immutable Bitmap pixels triggers an assert on debug builds.</p>
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_setImmutable">https://fiddle.skia.org/c/@Bitmap_setImmutable</a>
     */
    public Bitmap setImmutable() {
        Stats.onNativeCall();
        _nSetImmutable(_ptr);
        return this;
    }

    /**
     * <p>Returns true if ColorAlphaType is set to hint that all pixels are opaque; their
     * alpha value is implicitly or explicitly 1.0. If true, and all pixels are
     * not opaque, Skia may draw incorrectly.</p>
     * 
     * <p>Does not check if SkColorType allows alpha, or if any pixel value has
     * transparency.</p>
     *
     * @return  true if ImageInfo ColorAlphaType is {@link ColorAlphaType#OPAQUE}
     */
    public boolean isOpaque() {
        return getImageInfo()._colorInfo.isOpaque();
    }

    /**
     * <p>Provides a hint to caller that pixels should not be cached. Only true if
     * setVolatile() has been called to mark as volatile.</p>
     *
     * <p>Volatile state is not shared by other bitmaps sharing the same PixelRef.</p>
     *
     * @return  true if marked volatile
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_isVolatile">https://fiddle.skia.org/c/@Bitmap_isVolatile</a>
    */
    public boolean isVolatile() {
        Stats.onNativeCall();
        return _nIsVolatile(_ptr);
    }

    /** 
     * Sets if pixels should be read from PixelRef on every access. Bitmap are not
     * volatile by default; a GPU back end may upload pixel values expecting them to be
     * accessed repeatedly. Marking temporary Bitmap as volatile provides a hint to
     * BaseDevice that the Bitmap pixels should not be cached. This can
     * improve performance by avoiding overhead and reducing resource
     * consumption on BaseDevice.
     *
     * @param isVolatile  true if backing pixels are temporary
     * @return            this
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_setIsVolatile">https://fiddle.skia.org/c/@Bitmap_setIsVolatile</a>
     */
    public Bitmap setVolatile(boolean isVolatile) {
        Stats.onNativeCall();
        _nSetVolatile(_ptr, isVolatile);
        return this;
    }

    /**
     * <p>Resets to its initial state; all fields are set to zero, as if Bitmap had
     * been initialized by Bitmap().</p>
     *
     * <p>Sets width, height, row bytes to zero; pixel address to nullptr; ColorType to
     * {@link ColorType#UNKNOWN}; and ColorAlphaType to {@link ColorAlphaType#UNKNOWN}.</p>
     *
     * <p>If PixelRef is allocated, its reference count is decreased by one, releasing
     * its memory if Bitmap is the sole owner.</p>
     * 
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_reset">https://fiddle.skia.org/c/@Bitmap_reset</a>
     */
    public Bitmap reset() {
        Stats.onNativeCall();
        _nReset(_ptr);
        return this;
    }

    /**
     * <p>Returns true if all pixels are opaque. ColorType determines how pixels
     * are encoded, and whether pixel describes alpha. Returns true for ColorType
     * without alpha in each pixel; for other ColorType, returns true if all
     * pixels have alpha values equivalent to 1.0 or greater.</p>
     *
     * <p>For {@link ColorType#RGB_565} or {@link ColorTyp#GRAY_8}: always
     * returns true. For {@link ColorType#ALPHA_8}, {@link ColorType#BGRA_8888},
     * {@link ColorType#RGBA_8888}: returns true if all pixel alpha values are 255.
     * For {@link ColorType#ARGB_4444}: returns true if all pixel alpha values are 15.
     * For {@libnk ColorType#RGBA_F16}: returns true if all pixel alpha values are 1.0 or
     * greater.</p>
     *
     * <p>Returns false for {@link ColorType#UNKNOWN}.
     *
     * @return    true if all pixels have opaque values or ColorType is opaque
     */
    public boolean computeIsOpaque() {
        Stats.onNativeCall();
        return _nComputeIsOpaque(_ptr);
    }

    /**
     * Returns IRect { 0, 0, width(), height() }.
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_getBounds_2">https://fiddle.skia.org/c/@Bitmap_getBounds_2</a>
     */
    public IRect getBounds() {
        return IRect.makeXYWH(0, 0, getWidth(), getHeight());
    }

    /** 
     * <p>Sets width, height, ColorAlphaType, ColorType, ColorSpace.
     * Frees pixels, and returns true if successful.</p>
     *
     * <p>imageInfo.getAlphaType() may be altered to a value permitted by imageInfo.getColorSpace().
     * If imageInfo.getColorType() is {@link ColorType#UNKNOWN}, imageInfo.getAlphaType() is
     * set to {@link ColorAlphaType#UNKNOWN}.
     * If imageInfo.colorType() is {@link ColorType#ALPHA_8} and imageInfo.getAlphaType() is
     * {@link ColorAlphaType#UNPREMUL}, imageInfo.getAlphaType() is replaced by {@link ColorAlphaType#PREMUL}.
     * If imageInfo.colorType() is {@link ColorType#RGB_565} or {@link ColorType#GRAY_8},
     * imageInfo.getAlphaType() is set to {@link ColorAlphaType#OPAQUE}.
     * If imageInfo.colorType() is {@link ColorType#ARGB_4444}, {@link ColorType#RGBA_8888},
     * {@link ColorType#BGRA_8888}, or {@link ColorType#RGBA_F16}: imageInfo.getAlphaType() remains
     * unchanged.</p>
     *
     * <p>Calls reset() and returns false if:
     * - rowBytes exceeds 31 bits
     * - imageInfo.getWidth() is negative
     * - imageInfo.getHeight() is negative
     * - rowBytes is positive and less than imageInfo.getWidth() times imageInfo.getBytesPerPixel()</p>
     *
     * @param imageInfo  contains width, height, AlphaType, ColorType, ColorSpace
     * @return           true if ImageInfo set successfully
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_setInfo">https://fiddle.skia.org/c/@Bitmap_setInfo</a>
     */
    public boolean setImageInfo(ImageInfo imageInfo) {
        return setImageInfo(imageInfo, 0);
    }

    /** 
     * <p>Sets width, height, ColorAlphaType, ColorType, ColorSpace, and optional
     * rowBytes. Frees pixels, and returns true if successful.</p>
     *
     * <p>imageInfo.getAlphaType() may be altered to a value permitted by imageInfo.getColorSpace().
     * If imageInfo.getColorType() is {@link ColorType#UNKNOWN}, imageInfo.getAlphaType() is
     * set to {@link ColorAlphaType#UNKNOWN}.
     * If imageInfo.colorType() is {@link ColorType#ALPHA_8} and imageInfo.getAlphaType() is
     * {@link ColorAlphaType#UNPREMUL}, imageInfo.getAlphaType() is replaced by {@link ColorAlphaType#PREMUL}.
     * If imageInfo.colorType() is {@link ColorType#RGB_565} or {@link ColorType#GRAY_8},
     * imageInfo.getAlphaType() is set to {@link ColorAlphaType#OPAQUE}.
     * If imageInfo.colorType() is {@link ColorType#ARGB_4444}, {@link ColorType#RGBA_8888},
     * {@link ColorType#BGRA_8888}, or {@link ColorType#RGBA_F16}: imageInfo.getAlphaType() remains
     * unchanged.</p>
     *
     * <p>rowBytes must equal or exceed imageInfo.getMinRowBytes(). If imageInfo.getColorSpace() is
     * {@link ColorType#UNKNOWN}, rowBytes is ignored and treated as zero; for all other
     * ColorSpace values, rowBytes of zero is treated as imageInfo.getMinRowBytes().</p>
     *
     * <p>Calls reset() and returns false if:
     * - rowBytes exceeds 31 bits
     * - imageInfo.getWidth() is negative
     * - imageInfo.getHeight() is negative
     * - rowBytes is positive and less than imageInfo.getWidth() times imageInfo.getBytesPerPixel()</p>
     *
     * @param imageInfo  contains width, height, AlphaType, ColorType, ColorSpace
     * @param rowBytes   imageInfo.getMinRowBytes() or larger; or zero
     * @return           true if ImageInfo set successfully
     *
     * @see <a href="https://fiddle.skia.org/c/@Bitmap_setInfo">https://fiddle.skia.org/c/@Bitmap_setInfo</a>
     */
    public boolean setImageInfo(ImageInfo imageInfo, long rowBytes) {
        Stats.onNativeCall();
        return _nSetImageInfo(_ptr,
            imageInfo._colorInfo._colorType.ordinal(),
            imageInfo._colorInfo._alphaType.ordinal(),
            Native.getPtr(imageInfo._colorInfo._colorSpace),
            imageInfo._width,
            imageInfo._height,
            rowBytes);
    }

    @ApiStatus.Internal public static final  long _finalizerPtr = _nGetFinalizer();
    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMake();
    @ApiStatus.Internal public static native long _nMakeClone(long ptr);
    @ApiStatus.Internal public static native void _nSwap(long ptr, long otherPtr);
    @ApiStatus.Internal public static native ImageInfo _nGetImageInfo(long ptr);
    
    @ApiStatus.Internal public static native int _nGetRowBytesAsPixels(long ptr);
    @ApiStatus.Internal public static native boolean _nIsNull(long ptr);
    @ApiStatus.Internal public static native long _nGetRowBytes(long ptr);

    @ApiStatus.Internal public static native boolean _nSetAlphaType(long ptr, int alphaType);
    @ApiStatus.Internal public static native long    _nComputeByteSize(long ptr);
    @ApiStatus.Internal public static native boolean _nIsImmutable(long ptr);
    @ApiStatus.Internal public static native void    _nSetImmutable(long ptr);
    @ApiStatus.Internal public static native boolean _nIsVolatile(long ptr);
    @ApiStatus.Internal public static native void    _nSetVolatile(long ptr, boolean value);
    @ApiStatus.Internal public static native void    _nReset(long ptr);
    @ApiStatus.Internal public static native boolean _nComputeIsOpaque(long ptr);
    @ApiStatus.Internal public static native boolean _nSetImageInfo(long ptr, int colorType, int alphaType, long colorSpacePtr, int width, int height, long rowBytes);
}