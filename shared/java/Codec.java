package org.jetbrains.skija;

import java.lang.ref.*;
import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Codec extends Managed implements IHasImageInfo {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public ImageInfo _imageInfo = null;

    @ApiStatus.Internal
    public Codec(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    /**
     * If this data represents an encoded image that we know how to decode,
     * return an Codec that can decode it. Otherwise throws IllegalArgumentException.
     */
    public static Codec makeFromData(Data data) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeFromData(Native.getPtr(data));
            if (ptr == 0)
                throw new IllegalArgumentException("Unsupported format");
            return new Codec(ptr);
        } finally {
            Reference.reachabilityFence(data);
        }
    }

    @Override @NotNull
    public ImageInfo getImageInfo() {
        try {
            if (_imageInfo == null) {
                Stats.onNativeCall();
                _imageInfo = _nGetImageInfo(_ptr);
            }
            return _imageInfo;
        } finally {
            Reference.reachabilityFence(this);
        }        
    }

    @NotNull @Contract("-> new")
    public IPoint getSize() {
        try {
            Stats.onNativeCall();
            return IPoint._makeFromLong(_nGetSize(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }        
    }

    @NotNull
    public EncodedOrigin getEncodedOrigin() {
        try {
            Stats.onNativeCall();
            return EncodedOrigin._values[_nGetEncodedOrigin(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }        
    }

    @NotNull
    public EncodedImageFormat getEncodedImageFormat() {
        try {
            Stats.onNativeCall();
            return EncodedImageFormat._values[_nGetEncodedImageFormat(_ptr)];
        } finally {
            Reference.reachabilityFence(this);
        }        
    }

    /**
     * <p>Decodes an image into a bitmap.</p>
     * 
     * @return  decoded bitmap
     */
    @NotNull @Contract("_ -> new")
    public Bitmap readPixels() {
        Bitmap bitmap = new Bitmap();
        bitmap.allocPixels(getImageInfo());
        readPixels(bitmap);
        return bitmap;
    }

    /**
     * <p>Decodes an image into a bitmap.</p>
     *
     * <p>Repeated calls to this function should give the same results,
     * allowing the PixelRef to be immutable.</p>
     *
     * <p>Bitmap specifies the description of the format (config, size)
     * expected by the caller.  This can simply be identical
     * to the info returned by getImageInfo().</p>
     * 
     * <p>This contract also allows the caller to specify
     * different output-configs, which the implementation can
     * decide to support or not.</p>
     * 
     * <p>A size that does not match getImageInfo() implies a request
     * to scale. If the generator cannot perform this scale,
     * it will throw an exception.</p>
     * 
     * <p>If the info contains a non-null ColorSpace, the codec
     * will perform the appropriate color space transformation.</p>
     * 
     * <p>If the caller passes in the ColorSpace that maps to the
     * ICC profile reported by getICCProfile(), the color space
     * transformation is a no-op.</p>
     * 
     * <p>If the caller passes a null SkColorSpace, no color space
     * transformation will be done.</p>
     * 
     * @param bitmap      the description of the format (config, size) expected by the caller
     * @return            this
     */
    @NotNull @Contract("_ -> this")
    public Codec readPixels(Bitmap bitmap) {
        try {
            Stats.onNativeCall();
            _validateResult(_nReadPixels(_ptr, Native.getPtr(bitmap), 0, -1));
            return this;
        } finally {
            Reference.reachabilityFence(bitmap);
        }        
    }

    /**
     * <p>Decodes a frame in a multi-frame image into a bitmap.</p>
     *
     * <p>Repeated calls to this function should give the same results,
     * allowing the PixelRef to be immutable.</p>
     *
     * <p>Bitmap specifies the description of the format (config, size)
     * expected by the caller.  This can simply be identical
     * to the info returned by getImageInfo().</p>
     * 
     * <p>This contract also allows the caller to specify
     * different output-configs, which the implementation can
     * decide to support or not.</p>
     * 
     * <p>A size that does not match getImageInfo() implies a request
     * to scale. If the generator cannot perform this scale,
     * it will throw an exception.</p>
     * 
     * <p>If the info contains a non-null ColorSpace, the codec
     * will perform the appropriate color space transformation.</p>
     * 
     * <p>If the caller passes in the ColorSpace that maps to the
     * ICC profile reported by getICCProfile(), the color space
     * transformation is a no-op.</p>
     * 
     * <p>If the caller passes a null SkColorSpace, no color space
     * transformation will be done.</p>
     * 
     * @param bitmap      the description of the format (config, size) expected by the caller
     * @param frame       index of the frame in multi-frame image to decode
     * @return            this
     */
    @NotNull @Contract("_ -> this")
    public Codec readPixels(Bitmap bitmap, int frame) {
        try {
            Stats.onNativeCall();
            _validateResult(_nReadPixels(_ptr, Native.getPtr(bitmap), frame, -1));
            return this;
        } finally {
            Reference.reachabilityFence(bitmap);
        }        
    }

    /**
     * <p>Decodes a frame in a multi-frame image into a bitmap.</p>
     *
     * <p>Repeated calls to this function should give the same results,
     * allowing the PixelRef to be immutable.</p>
     *
     * <p>Bitmap specifies the description of the format (config, size)
     * expected by the caller.  This can simply be identical
     * to the info returned by getImageInfo().</p>
     * 
     * <p>This contract also allows the caller to specify
     * different output-configs, which the implementation can
     * decide to support or not.</p>
     * 
     * <p>A size that does not match getImageInfo() implies a request
     * to scale. If the generator cannot perform this scale,
     * it will throw an exception.</p>
     * 
     * <p>If the info contains a non-null ColorSpace, the codec
     * will perform the appropriate color space transformation.</p>
     * 
     * <p>If the caller passes in the ColorSpace that maps to the
     * ICC profile reported by getICCProfile(), the color space
     * transformation is a no-op.</p>
     * 
     * <p>If the caller passes a null SkColorSpace, no color space
     * transformation will be done.</p>
     * 
     * @param bitmap      the description of the format (config, size) expected by the caller
     * @param frame       index of the frame in multi-frame image to decode
     * @param priorFrame  index of the frame already in bitmap, might be used to optimize retrieving current frame
     * @return            this
     */
    @NotNull @Contract("_ -> this")
    public Codec readPixels(Bitmap bitmap, int frame, int priorFrame) {
        try {
            Stats.onNativeCall();
            _validateResult(_nReadPixels(_ptr, Native.getPtr(bitmap), frame, priorFrame));
            return this;
        } finally {
            Reference.reachabilityFence(bitmap);
        }        
    }

    /**
     * <p>Return the number of frames in the image.</p>
     *
     * <p>May require reading through the stream.</p>
     */
    public int getFrameCount() {
        try {
            Stats.onNativeCall();
            return _nGetFrameCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }    

    /**
     * <p>Return info about a single frame.</p>
     *
     * <p>Only supported by multi-frame images. Does not read through the stream,
     * so it should be called after getFrameCount() to parse any frames that
     * have not already been parsed.</p>
     */
    public AnimationFrameInfo getFrameInfo(int frame) {
        try {
            Stats.onNativeCall();
            return _nGetFrameInfo(_ptr, frame);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Return info about all the frames in the image.</p>
     *
     * <p>May require reading through the stream to determine info about the
     * frames (including the count).</p>
     *
     * <p>As such, future decoding calls may require a rewind.</p>
     *
     * <p>For still (non-animated) image codecs, this will return an empty array.</p>
     */
    public AnimationFrameInfo[] getFramesInfo() {
        try {
            Stats.onNativeCall();
            return _nGetFramesInfo(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * <p>Return the number of times to repeat, if this image is animated. This number does not
     * include the first play through of each frame. For example, a repetition count of 4 means
     * that each frame is played 5 times and then the animation stops.</p>
     *
     * <p>It can return -1, a negative number, meaning that the animation
     * should loop forever.</p>
     *
     * <p>May require reading the stream to find the repetition count.</p>
     *
     * <p>As such, future decoding calls may require a rewind.</p>
     *
     * <p>For still (non-animated) image codecs, this will return 0.</p>
     */
    public int getRepetitionCount() {
        try {
            Stats.onNativeCall();
            return _nGetRepetitionCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public static void _validateResult(int result) {
        switch (result) {
            case 1: // kIncompleteInput
                throw new IllegalArgumentException("Incomplete input: A partial image was generated.");

            case 2: // kErrorInInput
                throw new IllegalArgumentException("Error in input");

            case 3: // kInvalidConversion
                throw new IllegalArgumentException("Invalid conversion: The generator cannot convert to match the request, ignoring dimensions");

            case 4: // kInvalidScale
                throw new IllegalArgumentException("Invalid scale: The generator cannot scale to requested size");

            case 5: // kInvalidParameters
                throw new IllegalArgumentException("Invalid parameter: Parameters (besides info) are invalid. e.g. NULL pixels, rowBytes too small, etc");

            case 6: // kInvalidInput
                throw new IllegalArgumentException("Invalid input: The input did not contain a valid image");

            case 7: // kCouldNotRewind
                throw new UnsupportedOperationException("Could not rewind: Fulfilling this request requires rewinding the input, which is not supported for this input");

            case 8: // kInternalError
                throw new RuntimeException("Internal error");

            case 9: // kUnimplemented
                throw new UnsupportedOperationException("Unimplemented: This method is not implemented by this codec");
        }
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMakeFromData(long dataPtr);
    @ApiStatus.Internal public static native ImageInfo _nGetImageInfo(long ptr);
    @ApiStatus.Internal public static native long _nGetSize(long ptr);
    @ApiStatus.Internal public static native int _nGetEncodedOrigin(long ptr);
    @ApiStatus.Internal public static native int _nGetEncodedImageFormat(long ptr);
    @ApiStatus.Internal public static native int _nReadPixels(long ptr, long bitmapPtr, int frame, int priorFrame);
    @ApiStatus.Internal public static native int _nGetFrameCount(long ptr);
    @ApiStatus.Internal public static native AnimationFrameInfo _nGetFrameInfo(long ptr, int frame);
    @ApiStatus.Internal public static native AnimationFrameInfo[] _nGetFramesInfo(long ptr);
    @ApiStatus.Internal public static native int _nGetRepetitionCount(long ptr);
}
