package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

/**
 * Describes how pixel bits encode color. A pixel may be an alpha mask, a
 * grayscale, RGB, or ARGB.
 */
public enum ColorType {
    /**
     * Uninitialized
     */
    UNKNOWN,      

    /**
     * Pixel with alpha in 8-bit byte
     */
    ALPHA_8,      

    /**
     * Pixel with 5 bits red, 6 bits green, 5 bits blue, in 16-bit word
     */
    RGB_565,      

    /**
     * Pixel with 4 bits for alpha, red, green, blue; in 16-bit word
     */
    ARGB_4444,    

    /**
     * Pixel with 8 bits for red, green, blue, alpha; in 32-bit word
     */
    RGBA_8888,    

    /**
     * Pixel with 8 bits each for red, green, blue; in 32-bit word
     */
    RGB_888X,     

    /**
     * Pixel with 8 bits for blue, green, red, alpha; in 32-bit word
     */
    BGRA_8888,    

    /**
     * 10 bits for red, green, blue; 2 bits for alpha; in 32-bit word
     */
    RGBA_1010102, 

    /**
     * 10 bits for blue, green, red; 2 bits for alpha; in 32-bit word
     */
    BGRA_1010102, 

    /**
     * Pixel with 10 bits each for red, green, blue; in 32-bit word
     */
    RGB_101010X,  

    /**
     * Pixel with 10 bits each for blue, green, red; in 32-bit word
     */
    BGR_101010X,  

    /**
     * Pixel with grayscale level in 8-bit byte
     */
    GRAY_8,       

    /**
     * Pixel with half floats in [0,1] for red, green, blue, alpha; in 64-bit word
     */
    RGBA_F16NORM, 

    /**
     * Pixel with half floats for red, green, blue, alpha; in 64-bit word
     */
    RGBA_F16,     

    /**
     * Pixel using C float for red, green, blue, alpha; in 128-bit word
     */
    RGBA_F32,     


    // The following 6 colortypes are just for reading from - not for rendering to
    
    /**
     * Pixel with a uint8_t for red and green
     */
    R8G8_UNORM,        

    /**
     * Pixel with a half float for alpha
     */
    A16_FLOAT,         

    /**
     * Pixel with a half float for red and green
     */
    R16G16_FLOAT,      

    /**
     * Pixel with a little endian uint16_t for alpha
     */
    A16_UNORM,         

    /**
     * Pixel with a little endian uint16_t for red and green
     */
    R16G16_UNORM,      

    /**
     * Pixel with a little endian uint16_t for red, green, blue, and alpha
     */
    R16G16B16A16_UNORM;

    @ApiStatus.Internal public static final ColorType[] _values = values();

    /**
     * Native ARGB 32-bit encoding
     */
    public static ColorType N32 = BGRA_8888;

    /**
     * Returns the number of bytes required to store a pixel, including unused padding.
     * Returns zero for {@link #UNKNOWN}.
     *
     * @return  bytes per pixel
     */  
    public int getBytesPerPixel() {
        switch (this) {
            case UNKNOWN:            return 0;
            case ALPHA_8:            return 1;
            case RGB_565:            return 2;
            case ARGB_4444:          return 2;
            case RGBA_8888:          return 4;
            case BGRA_8888:          return 4;
            case RGB_888X:           return 4;
            case RGBA_1010102:       return 4;
            case RGB_101010X:        return 4;
            case BGRA_1010102:       return 4;
            case BGR_101010X:        return 4;
            case GRAY_8:             return 1;
            case RGBA_F16NORM:       return 8;
            case RGBA_F16:           return 8;
            case RGBA_F32:           return 16;
            case R8G8_UNORM:         return 2;
            case A16_UNORM:          return 2;
            case R16G16_UNORM:       return 4;
            case A16_FLOAT:          return 2;
            case R16G16_FLOAT:       return 4;
            case R16G16B16A16_UNORM: return 8;
        }
        throw new RuntimeException("Unreachable");
    }

    public int getShiftPerPixel() {
        switch (this) {
            case UNKNOWN:            return 0;
            case ALPHA_8:            return 0;
            case RGB_565:            return 1;
            case ARGB_4444:          return 1;
            case RGBA_8888:          return 2;
            case RGB_888X:           return 2;
            case BGRA_8888:          return 2;
            case RGBA_1010102:       return 2;
            case RGB_101010X:        return 2;
            case BGRA_1010102:       return 2;
            case BGR_101010X:        return 2;
            case GRAY_8:             return 0;
            case RGBA_F16NORM:       return 3;
            case RGBA_F16:           return 3;
            case RGBA_F32:           return 4;
            case R8G8_UNORM:         return 1;
            case A16_UNORM:          return 1;
            case R16G16_UNORM:       return 2;
            case A16_FLOAT:          return 1;
            case R16G16_FLOAT:       return 2;
            case R16G16B16A16_UNORM: return 3;
        }
        throw new RuntimeException("Unreachable");
    }

    /**
     * Returns true if ColorType always decodes alpha to 1.0, making the pixel
     * fully opaque. If true, ColorType does not reserve bits to encode alpha.
     *
     * @return  true if alpha is always set to 1.0
     */
    public boolean isAlwaysOpaque() {
        Stats.onNativeCall();
        return _nIsAlwaysOpaque(ordinal());
    }

    /**
     * <p>Returns a valid ColorAlphaType for colorType. If there is more than one valid canonical
     * ColorAlphaType, set to alphaType, if valid.</p>
     *
     * <p>Returns null only if alphaType is {@link ColorAlphaType#UNKNOWN}, color type is not
     * {@link #UNKNOWN}, and ColorType is not always opaque.</p>
     *
     * @return  ColorAlphaType if can be associated with colorType
     */
    @Nullable
    public ColorAlphaType validateAlphaType(ColorAlphaType alphaType) {
        switch (this) {
            case UNKNOWN:
                alphaType = ColorAlphaType.UNKNOWN;
                break;
            case ALPHA_8:         // fall-through
            case A16_UNORM:       // fall-through
            case A16_FLOAT:
                if (ColorAlphaType.UNPREMUL == alphaType)
                    alphaType = ColorAlphaType.PREMUL;
                // fall-through
            case ARGB_4444:
            case RGBA_8888:
            case BGRA_8888:
            case RGBA_1010102:
            case BGRA_1010102:
            case RGBA_F16NORM:
            case RGBA_F16:
            case RGBA_F32:
            case R16G16B16A16_UNORM:
                if (ColorAlphaType.UNKNOWN == alphaType)
                    return null;
                break;
            case GRAY_8:
            case R8G8_UNORM:
            case R16G16_UNORM:
            case R16G16_FLOAT:
            case RGB_565:
            case RGB_888X:
            case RGB_101010X:
            case BGR_101010X:
                alphaType = ColorAlphaType.OPAQUE;
                break;
        }
        return alphaType;
    }

    public long computeOffset(int x, int y, long rowBytes) {
        if (this == UNKNOWN)
            return 0;
        return y * rowBytes + (x << getShiftPerPixel());
    }

    public float getR(byte color) {
        switch (this) {
            case GRAY_8:
                return Byte.toUnsignedInt(color) / 255f;
            default:
                throw new IllegalArgumentException("getR(byte) is not supported on ColorType." + this);
        }
    }

    public float getR(short color) {
        switch (this) {
            case RGB_565:
                return ((color >> 11) & 0b11111) / 31f;
            case ARGB_4444:
                return ((color >> 8) & 0xF) / 15f;
            default:
                throw new IllegalArgumentException("getR(short) is not supported on ColorType." + this);
        }
    }

    public float getR(int color) {
        switch (this) {
            case RGBA_8888:
                return ((color >> 24) & 0xFF) / 255f;
            case RGB_888X:
                return ((color >> 24) & 0xFF) / 255f;
            case BGRA_8888:
                return ((color >> 8) & 0xFF) / 255f;
            case RGBA_1010102:
                return ((color >> 22) & 0b1111111111) / 1023f;
            case RGB_101010X:
                return ((color >> 22) & 0b1111111111) / 1023f;
            case BGRA_1010102:
                return ((color >> 2) & 0b1111111111) / 1023f;
            case BGR_101010X:
                return ((color >> 2) & 0b1111111111) / 1023f;
            default:
                throw new IllegalArgumentException("getR(int) is not supported on ColorType." + this);
        }
    }

    public float getG(byte color) {
        switch (this) {
            case GRAY_8:
                return Byte.toUnsignedInt(color) / 255f;
            default:
                throw new IllegalArgumentException("getG(byte) is not supported on ColorType." + this);
        }
    }

    public float getG(short color) {
        switch (this) {
            case RGB_565:
                return ((color >> 5) & 0b111111) / 63f;
            case ARGB_4444:
                return ((color >> 4) & 0xF) / 15f;
            default:
                throw new IllegalArgumentException("getG(short) is not supported on ColorType." + this);
        }
    }

    public float getG(int color) {
        switch (this) {
            case RGBA_8888:
                return ((color >> 16) & 0xFF)  / 255f;
            case RGB_888X:
                return ((color >> 16) & 0xFF) / 255f;
            case BGRA_8888:
                return ((color >> 16) & 0xFF) / 255f;
            case RGBA_1010102:
                return ((color >> 12) & 0b1111111111) / 1023f;
            case RGB_101010X:
                return ((color >> 12) & 0b1111111111) / 1023f;
            case BGRA_1010102:
                return ((color >> 12) & 0b1111111111) / 1023f;
            case BGR_101010X:
                return ((color >> 12) & 0b1111111111) / 1023f;
            default:
                throw new IllegalArgumentException("getG(int) is not supported on ColorType." + this);
        }
    }

    public float getB(byte color) {
        switch (this) {
            case GRAY_8:
                return Byte.toUnsignedInt(color) / 255f;
            default:
                throw new IllegalArgumentException("getB(byte) is not supported on ColorType." + this);
        }
    }

    public float getB(short color) {
        switch (this) {
            case RGB_565:
                return (color & 0b11111) / 31f;
            case ARGB_4444:
                return (color & 0xF) / 15f;
            default:
                throw new IllegalArgumentException("getB(short) is not supported on ColorType." + this);
        }
    }

    public float getB(int color) {
        switch (this) {
            case RGBA_8888:
                return ((color >> 8) & 0xFF) / 255f;
            case RGB_888X:
                return ((color >> 8) & 0xFF) / 255f;
            case BGRA_8888:
                return ((color >> 24) & 0xFF) / 255f;
            case RGBA_1010102:
                return ((color >> 2) & 0b1111111111) / 1023f;
            case RGB_101010X:
                return ((color >> 2) & 0b1111111111) / 1023f;
            case BGRA_1010102:
                return ((color >> 22) & 0b1111111111) / 1023f;
            case BGR_101010X:
                return ((color >> 22) & 0b1111111111) / 1023f;
            default:
                throw new IllegalArgumentException("getB(int) is not supported on ColorType." + this);
        }
    }

    public float getA(byte color) {
        switch (this) {
            case ALPHA_8:
                return Byte.toUnsignedInt(color) / 255f;
            default:
                throw new IllegalArgumentException("getA(byte) is not supported on ColorType." + this);
        }
    }

    public float getA(short color) {
        switch (this) {
            case ARGB_4444:
                return ((color >> 12) & 0xF) / 15f;
            default:
                throw new IllegalArgumentException("getA(short) is not supported on ColorType." + this);
        }
    }

    public float getA(int color) {
        switch (this) {
            case RGBA_8888:
                return (color & 0xFF) / 255f;
            case BGRA_8888:
                return (color & 0xFF) / 255f;
            case RGBA_1010102:
                return (color & 0b11) / 3f;
            case BGRA_1010102:
                return (color & 0b11) / 3f;
            default:
                throw new IllegalArgumentException("getA(int) is not supported on ColorType." + this);
        }
    }

    @ApiStatus.Internal public static native boolean _nIsAlwaysOpaque(int value);
}