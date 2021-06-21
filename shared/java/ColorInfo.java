package org.jetbrains.skija;

import lombok.Data;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

/**
 * <p>Describes pixel and encoding. ImageInfo can be created from ColorInfo by
 * providing dimensions.</p>
 *
 * <p>It encodes how pixel bits describe alpha, transparency; color components red, blue,
 * and green; and ColorSpace, the range and linearity of colors.</p>
 */
@AllArgsConstructor @Data @With
public class ColorInfo {
    @NotNull
    public final ColorType _colorType;
    @NotNull
    public final ColorAlphaType _alphaType;
    @Nullable
    public final ColorSpace _colorSpace;

    /**
     * Creates an ColorInfo with {@link ColorType#UNKNOWN}, {@link ColorAlphaType#UNKNOWN},
     * and no ColorSpace.
     */
    public static final ColorInfo DEFAULT = new ColorInfo(ColorType.UNKNOWN, ColorAlphaType.UNKNOWN, null);

    public boolean isOpaque() {
        return _alphaType == ColorAlphaType.OPAQUE || _colorType.isAlwaysOpaque();
    }

    /**
     * Returns number of bytes per pixel required by ColorType.
     * Returns zero if getColorType() is {@link ColorType#UNKNOWN}.
     *
     * @return  bytes in pixel
     *
     * @see <a href="https://fiddle.skia.org/c/@ImageInfo_bytesPerPixel">https://fiddle.skia.org/c/@ImageInfo_bytesPerPixel</a>
     */
    public int getBytesPerPixel() {
        return _colorType.getBytesPerPixel();
    }

    /**
     * Returns bit shift converting row bytes to row pixels.
     * Returns zero for {@link ColorType#UNKNOWN}.
     *
     * @return  one of: 0, 1, 2, 3, 4; left shift to convert pixels to bytes
     *
     * @see <a href="https://fiddle.skia.org/c/@ImageInfo_shiftPerPixel">https://fiddle.skia.org/c/@ImageInfo_shiftPerPixel</a>
     */
    public int getShiftPerPixel() {
        return _colorType.getShiftPerPixel();
    }

    public boolean isGammaCloseToSRGB() {
        return _colorSpace != null && _colorSpace.isGammaCloseToSRGB();
    }
}