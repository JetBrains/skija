# 0.90.6 - Apr 16, 2021

Added:

- TextLine.getIntercepts()

# 0.90.3 - Mar 26, 2021

Skia version m90-adbb69cd7f -> m90-adbb69cd7f-2

Changed:

- Fixed exception during reporting double close in Managed #102
- Fixed for locale-dependent SVGCanvas serialization https://skbug.com/11794

# 0.90.2 - Mar 25, 2021

Changed:

- Fixed Skottie linking on Linux

# 0.90.0 - March 19, 2021

Skia version m89-109bfc9052 -> m90-adbb69cd7f

Added:

- Canvas.drawImageRect(..., SamplingMode, ...)
- Canvas.drawImageNine()

Changed:

- SamplingMode.FilterMipmap -> FilterMipmap
- SamplingMode.CubicResampler -> CubicResampler
- ImageFilter.makeXfermode() -> ImageFilter.makeBlend()
- ImageFilter.makeImage(..., FilterQuality -> SamplingMode)
- ImageFilter.makeMatrixTransform(..., FilterQuality -> SamplingMode)

Removed:

- Canvas.drawImageIRect()
- Canvas.drawBitmap()
- Canvas.drawBitmapRect()
- Canvas.drawBitmapIRect()

(See https://bugs.chromium.org/p/skia/issues/detail?id=11764#c1)

# 0.89.39 - March 18, 2021

Lombok 1.18.16 -> 1.18.18

# 0.89.38 - March 17, 2021

Added:

- BreakIterator
- U16String

# 0.89.37 - March 11, 2021

Added:

- Codec
- AnimationFrameInfo
- AnimationDisposalMethod