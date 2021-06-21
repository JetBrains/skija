# 0.92.2 - June 21, 2021

Support for Java 9 modules (#110 #111 thx @comtel2000):

- org.jetbrains.skija.shared
- org.jetbrains.skija.windows
- org.jetbrains.skija.linux
- org.jetbrains.skija.macos.x64
- org.jetbrains.skija.macos.arm64

Code reorganization:

- removed maven
- flattened source dirs
  - native/src → platform/cc
  - shared/src/main/java/org/jetbrains/skija → shared/java
  - shared/src/test/java/org/jetbrains/skija → tests/java
  - added platform/java-{platform}

# 0.92.1 - June 7, 2021

Skia version m92-f46c37ba85 -> m92-f46c37ba85-2

Changed:

- [ BREAKING ] SVGDOM::setContainerSize() does not scale SVG anymore (https://bugs.chromium.org/p/skia/issues/detail?id=11144)

Added:

- SVGDOM::getRoot()
- SVGSVG (root SVG node)

# 0.92.0 - June 4, 2021

Skia version m91-b99622c05a -> m92-f46c37ba85

Changed:

- paragraph.Shadow::blurRadius -> paragraph.Shadow::blurSigma

Removed:

- PathEffect::computeFastBounds

# 0.91.8 - June 1, 2021

Fixed:

- Typeface.getFamilyNames return type (String[] -> FontFamilyName[]) #108
- Canvas.resetMatrix argument (Matrix33 -> None) #109, thx @AnzerWall

# 0.91.6 - May 21, 2021

Added:

- Canvas.drawRectShadow
- Canvas.drawRectShadowNoclip
- Rect.inflate
- Rect.isEmpty
- Managed.isClosed

Updated:

- org.jetbrains:annotations from 19.0.0 to 20.1.0

# 0.91.4 - May 11, 2021

Added:

- Surface::makeFromMTKView
- Optionally load dll/so/dylib from current dir

# 0.91.3 - May 4, 2021

Added:

- DirectContext::makeMetal
- BackendRenderTarget::makeMetal

# 0.91.2 - Apr 29, 2021

Fixed:

- `-Xcheck:jni` warnings #70

# 0.91.1 - Apr 28, 2021

Fixed:

- NPE in TextLine::getIntercepts

# 0.91.0 - Apr 28, 2021

Skia version m90-adbb69cd7f-2 -> m91-b99622c05a

Changed:

- All variants of Picture.makeShader now take extra FilterMode argument

# 0.90.16 - Apr 23, 2021

Added:

- Matrix33.rotate(deg, pivot)

# 0.90.14 - Apr 22, 2021

New FontRunIter for TextLine/TextBlob, groups grapheme clusters together correctly

# 0.90.9 - Apr 20, 2021

Lombok 1.18.18 -> 1.18.20 (support for Java 16)

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