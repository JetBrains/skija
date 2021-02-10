package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Image extends RefCnt {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal public int _width = -1;
    @ApiStatus.Internal public int _height = -1;

    public static Image makeFromEncoded(byte[] bytes) {
        Stats.onNativeCall();
        return new Image(_nMakeFromEncoded(bytes));
    }

    public int getWidth() {
        if (_width == -1) _getDimensions();
        return _width;
    }

    public int getHeight() {
        if (_height == -1) _getDimensions();
        return _height;
    }

    /** 
     * <p>Encodes Image pixels, returning result as Data. Returns existing encoded data
     * if present; otherwise, Image is encoded with {@link EncodedImageFormat#PNG}.</p>
     *
     * <p>Returns null if existing encoded data is missing or invalid, and encoding fails.</p>
     *
     * @return  encoded Image, or null
     *
     * @see <a href="https://fiddle.skia.org/c/@Image_encodeToData_2">https://fiddle.skia.org/c/@Image_encodeToData_2</a>
     */
    @Nullable
    public Data encodeToData() {
        return encodeToData(EncodedImageFormat.PNG, 100);
    }

    @Nullable
    public Data encodeToData(EncodedImageFormat format) {
        return encodeToData(format, 100);
    }

    /**
     * Encodes Image pixels, returning result as Data.
     * 
     * Returns null if encoding fails, or if format is not supported.
     * 
     * On a macOS, encodedImageFormat can additionally be one of: 
     * {@link EncodedImageFormat#ICO}, {@link EncodedImageFormat#BMP} or {@link EncodedImageFormat#GIF}.
     * 
     * quality is a platform and format specific metric trading off size and encoding
     * error. When used, quality equaling 100 encodes with the least error. quality may
     * be ignored by the encoder.
     * 
     * @param format   one of: {@link EncodedImageFormat#JPEG}, {@link EncodedImageFormat#PNG}, {@link EncodedImageFormat#WEBP}
     * @param quality  encoder specific metric with 100 equaling best
     * @return         encoded Image, or null
     * 
     * @see <a href="https://fiddle.skia.org/c/@Image_encodeToData">https://fiddle.skia.org/c/@Image_encodeToData</a>
     */
    @Nullable
    public Data encodeToData(EncodedImageFormat format, int quality) {
        try {
            Stats.onNativeCall();
            long ptr = _nEncodeToData(_ptr, format.ordinal(), quality);
            return ptr == 0 ? null : new Data(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public void _getDimensions() {
        try {
            Stats.onNativeCall();
            long res = _nGetDimensions(_ptr);
            _height = (int) (res & 0xFFFFFFFF);
            _width = (int) (res >>> 32);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull
    public Shader makeShader() {
        return makeShader(FilterTileMode.CLAMP, FilterTileMode.CLAMP, SamplingMode.DEFAULT, null);
    }

    @NotNull
    public Shader makeShader(@Nullable Matrix33 localMatrix) {
        return makeShader(FilterTileMode.CLAMP, FilterTileMode.CLAMP, SamplingMode.DEFAULT, localMatrix);
    }

    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tm) {
        return makeShader(tm, tm, SamplingMode.DEFAULT, null);
    }

    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx, @NotNull FilterTileMode tmy) {
        return makeShader(tmx, tmy, SamplingMode.DEFAULT, null);
    }

    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx,
                             @NotNull FilterTileMode tmy,
                             @Nullable Matrix33 localMatrix) {
        return makeShader(tmx, tmy, SamplingMode.DEFAULT, localMatrix);
    }

    @NotNull
    public Shader makeShader(@NotNull FilterTileMode tmx,
                             @NotNull FilterTileMode tmy,
                             @NotNull SamplingMode sampling,
                             @Nullable Matrix33 localMatrix) {
        try {
            assert tmx != null : "Can’t Bitmap.makeShader with tmx == null";
            assert tmy != null : "Can’t Bitmap.makeShader with tmy == null";
            assert sampling != null : "Can’t Bitmap.makeShader with sampling == null";
            Stats.onNativeCall();
            long ptr;
            if (sampling instanceof SamplingMode.FilterMipmap) {
                var s = (SamplingMode.FilterMipmap) sampling;
                ptr = _nMakeShader(_ptr, tmx.ordinal(), tmy.ordinal(), s._filterMode.ordinal(), s._mipmapMode.ordinal(), localMatrix == null ? null : localMatrix._mat);
            } else if (sampling instanceof SamplingMode.CubicResampler) {
                var s = (SamplingMode.CubicResampler) sampling;
                ptr = _nMakeShaderCubic(_ptr, tmx.ordinal(), tmy.ordinal(), s._B, s._C, localMatrix == null ? null : localMatrix._mat);
            } else {
                throw new IllegalArgumentException("Unknown sampling class: " + sampling);
            }
            return new Shader(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public Image(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal public static native long _nMakeFromEncoded(byte[] bytes);
    @ApiStatus.Internal public static native long _nGetDimensions(long ptr);
    @ApiStatus.Internal public static native long _nEncodeToData(long ptr, int format, int quality);
    @ApiStatus.Internal public static native long    _nMakeShader(long ptr, int tmx, int tmy, int filterMode, int mipmapMode, float[] localMatrix);
    @ApiStatus.Internal public static native long    _nMakeShaderCubic(long ptr, int tmx, int tmy, float B, float C, float[] localMatrix);
}