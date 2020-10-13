package org.jetbrains.skija;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class Canvas extends Native {

    @ApiStatus.Internal
    public Canvas(long ptr) {
        super(ptr);
    }

    /**
     * <p>Constructs a canvas that draws into bitmap.
     * Sets default pixel geometry in constructed Surface.</p>
     *
     * <p>Bitmap is copied so that subsequently editing bitmap will not affect
     * constructed Canvas.</p>
     *
     * <p>May be deprecated in the future.</p>
     *
     * @param bitmap  width, height, ColorType, ColorAlphaType, and pixel
     *                storage of raster surface
     * @return        Canvas that can be used to draw into bitmap
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_copy_const_SkBitmap">https://fiddle.skia.org/c/@Canvas_copy_const_SkBitmap</a>
     */
    public Canvas(Bitmap bitmap) {
        this(bitmap, new SurfaceProps());
    }

    /**
     * <p>Constructs a canvas that draws into bitmap.
     * Use props to match the device characteristics, like LCD striping.</p>
     *
     * <p>Bitmap is copied so that subsequently editing bitmap will not affect
     * constructed Canvas.</p>
     *
     * @param bitmap  width, height, ColorType, ColorAlphaType, and pixel
     *                storage of raster surface
     * @param props   order and orientation of RGB striping; and whether to use
     *                device independent fonts
     * @return        Canvas that can be used to draw into bitmap
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_const_SkBitmap_const_SkSurfaceProps">https://fiddle.skia.org/c/@Canvas_const_SkBitmap_const_SkSurfaceProps</a>
     */
    public Canvas(Bitmap bitmap, SurfaceProps surfaceProps) {
        this(_nMakeFromBitmap(Native.getPtr(bitmap), surfaceProps.getFlags(), surfaceProps.getPixelGeometry().ordinal()));
        Stats.onNativeCall();
    }

    public Canvas drawPoint(float x, float y, Paint paint) {
        Stats.onNativeCall();
        _nDrawPoint(_ptr, x, y, Native.getPtr(paint));
        return this;
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>The shape of point drawn depends on paint
     * PaintStrokeCap. If paint is set to {@link PaintStrokeCap#ROUND}, each point draws a
     * circle of diameter Paint stroke width. If paint is set to {@link PaintStrokeCap#SQUARE}
     * or {@link PaintStrokeCap#BUTT}, each point draws a square of width and height
     * Paint stroke width.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawPoints(Point[] coords, Paint paint) {
        return drawPoints(Point.flattenArray(coords), paint);
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>The shape of point drawn depends on paint
     * PaintStrokeCap. If paint is set to {@link PaintStrokeCap#ROUND}, each point draws a
     * circle of diameter Paint stroke width. If paint is set to {@link PaintStrokeCap#SQUARE}
     * or {@link PaintStrokeCap#BUTT}, each point draws a square of width and height
     * Paint stroke width.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawPoints(float[] coords, Paint paint) {
        Stats.onNativeCall();
        _nDrawPoints(_ptr, 0 /* SkCanvas::PointMode::kPoints_PointMode */, coords, Native.getPtr(paint));
        return this;
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>Each pair of points draws a line segment.
     * One line is drawn for every two points; each point is used once. If count is odd,
     * the final point is ignored.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawLines(Point[] coords, Paint paint) {
        return drawLines(Point.flattenArray(coords), paint);
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>Each pair of points draws a line segment.
     * One line is drawn for every two points; each point is used once. If count is odd,
     * the final point is ignored.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawLines(float[] coords, Paint paint) {
        Stats.onNativeCall();
        _nDrawPoints(_ptr, 1 /* SkCanvas::PointMode::kLines_PointMode */, coords, Native.getPtr(paint));
        return this;
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>Each adjacent pair of points draws a line segment.
     * count minus one lines are drawn; the first and last point are used once.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawPolygon(Point[] coords, Paint paint) {
        return drawPolygon(Point.flattenArray(coords), paint);
    }

    /** 
     * <p>Draws pts using clip, Matrix and Paint paint.</p>
     *
     * <p>Each adjacent pair of points draws a line segment.
     * count minus one lines are drawn; the first and last point are used once.</p>
     *
     * <p>Each line segment respects paint PaintStrokeCap and Paint stroke width.
     * PaintMode is ignored, as if were set to {@link PaintMode#STROKE}.</p>
     *
     * <p>Always draws each element one at a time; is not affected by
     * PaintStrokeJoin, and unlike drawPath(), does not create a mask from all points
     * and lines before drawing.</p>
     *
     * @param coords array of points to draw
     * @param paint  stroke, blend, color, and so on, used to draw
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawPoints">https://fiddle.skia.org/c/@Canvas_drawPoints</a>
     */
    public Canvas drawPolygon(float[] coords, Paint paint) {
        Stats.onNativeCall();
        _nDrawPoints(_ptr, 2 /* SkCanvas::PointMode::kPolygon_PointMode */, coords, Native.getPtr(paint));
        return this;
    }

    public Canvas drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        Stats.onNativeCall();
        _nDrawLine(_ptr, x0, y0, x1, y1, Native.getPtr(paint));
        return this;
    }

    public Canvas drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        Stats.onNativeCall();
        _nDrawArc(_ptr, left, top, width, height, startAngle, sweepAngle, includeCenter, Native.getPtr(paint));
        return this;
    }

    public Canvas drawRect(Rect r, Paint paint) {
        Stats.onNativeCall();
        _nDrawRect(_ptr, r._left, r._top, r._right, r._bottom, Native.getPtr(paint));
        return this;
    }

    public Canvas drawOval(Rect r, Paint paint) {
        Stats.onNativeCall();
        _nDrawOval(_ptr, r._left, r._top, r._right, r._bottom, Native.getPtr(paint));
        return this;
    }

    public Canvas drawCircle(float x, float y, float radius, Paint paint) {
        Stats.onNativeCall();
        _nDrawOval(_ptr, x - radius, y - radius, x + radius, y + radius, Native.getPtr(paint));
        return this;
    }

    public Canvas drawRRect(RRect r, Paint paint) {
        Stats.onNativeCall();
        _nDrawRRect(_ptr, r._left, r._top, r._right, r._bottom, r._radii, Native.getPtr(paint));
        return this;
    }

    public Canvas drawDRRect(RRect outer, RRect inner, Paint paint) {
        Stats.onNativeCall();
        _nDrawDRRect(_ptr, outer._left, outer._top, outer._right, outer._bottom, outer._radii, inner._left, inner._top, inner._right, inner._bottom, inner._radii, Native.getPtr(paint));
        return this;
    }

    public Canvas drawPath(Path path, Paint paint) {
        Stats.onNativeCall();
        _nDrawPath(_ptr, Native.getPtr(path), Native.getPtr(paint));
        return this;
    }

    public Canvas drawImage(Image image, float left, float top) {
        return drawImage(image, left, top, null);
    }

    public Canvas drawImage(Image image, float left, float top, Paint paint) {
        Stats.onNativeCall();
        _nDrawImageIRect(_ptr, Native.getPtr(image), 0, 0, image.getWidth(), image.getHeight(), left, top, left + image.getWidth(), top + image.getHeight(), Native.getPtr(paint), true);
        return this;
    }

    public Canvas drawImageRect(Image image, Rect dst) {
        return drawImageRect(image, dst, null);
    }

    public Canvas drawImageRect(Image image, Rect dst, Paint paint) {
        Stats.onNativeCall();
        _nDrawImageIRect(_ptr, Native.getPtr(image), 0, 0, image.getWidth(), image.getHeight(), dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), true);
        return this;
    }

    public Canvas drawImageRect(Image image, Rect src, Rect dst, Paint paint) {
        return drawImageRect(image, src, dst, paint, true);
    }

    public Canvas drawImageRect(Image image, Rect src, Rect dst, Paint paint, boolean strict) {
        Stats.onNativeCall();
        _nDrawImageRect(_ptr, Native.getPtr(image), src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), strict);
        return this;
    }

    public Canvas drawImageIRect(Image image, IRect src, Rect dst) {
        return drawImageIRect(image, src, dst, null);
    }

    public Canvas drawImageIRect(Image image, IRect src, Rect dst, Paint paint) {
        return drawImageIRect(image, src, dst, paint, true);
    }

    public Canvas drawImageIRect(Image image, IRect src, Rect dst, Paint paint, boolean strict) {
        Stats.onNativeCall();
        _nDrawImageIRect(_ptr, Native.getPtr(image), src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), strict);
        return this;
    }

    public Canvas drawBitmap(Bitmap bitmap, float left, float top) {
        return drawBitmap(bitmap, left, top, null);
    }

    public Canvas drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {
        Stats.onNativeCall();
        _nDrawBitmapIRect(_ptr, Native.getPtr(bitmap), 0, 0, bitmap.getWidth(), bitmap.getHeight(), left, top, left + bitmap.getWidth(), top + bitmap.getHeight(), Native.getPtr(paint), true);
        return this;
    }

    public Canvas drawBitmapRect(Bitmap bitmap, Rect dst) {
        return drawBitmapRect(bitmap, dst, null);
    }

    public Canvas drawBitmapRect(Bitmap bitmap, Rect dst, Paint paint) {
        Stats.onNativeCall();
        _nDrawBitmapIRect(_ptr, Native.getPtr(bitmap), 0, 0, bitmap.getWidth(), bitmap.getHeight(), dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), true);
        return this;
    }

    public Canvas drawBitmapRect(Bitmap bitmap, Rect src, Rect dst, Paint paint) {
        return drawBitmapRect(bitmap, src, dst, paint, true);
    }

    public Canvas drawBitmapRect(Bitmap bitmap, Rect src, Rect dst, Paint paint, boolean strict) {
        Stats.onNativeCall();
        _nDrawBitmapRect(_ptr, Native.getPtr(bitmap), src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), strict);
        return this;
    }

    public Canvas drawBitmapIRect(Bitmap bitmap, IRect src, Rect dst) {
        return drawBitmapIRect(bitmap, src, dst, null);
    }

    public Canvas drawBitmapIRect(Bitmap bitmap, IRect src, Rect dst, Paint paint) {
        return drawBitmapIRect(bitmap, src, dst, paint, true);
    }

    public Canvas drawBitmapIRect(Bitmap bitmap, IRect src, Rect dst, Paint paint, boolean strict) {
        Stats.onNativeCall();
        _nDrawBitmapIRect(_ptr, Native.getPtr(bitmap), src._left, src._top, src._right, src._bottom, dst._left, dst._top, dst._right, dst._bottom, Native.getPtr(paint), strict);
        return this;
    }

    public Canvas drawRegion(Region r, Paint paint) {
        Stats.onNativeCall();
        _nDrawRegion(_ptr, Native.getPtr(r), Native.getPtr(paint));
        return this;
    }

    public Canvas drawString(String s, float x, float y, Font font, Paint paint) {
        Stats.onNativeCall();
        _nDrawString(_ptr, s, x, y, Native.getPtr(font), Native.getPtr(paint));
        return this;
    }

    public Canvas drawTextBlob(TextBlob blob, float x, float y, Font font, Paint paint) {
        Stats.onNativeCall();
        _nDrawTextBlob(_ptr, Native.getPtr(blob), x, y, Native.getPtr(font), Native.getPtr(paint));
        return this;
    }

    public Canvas drawPicture(Picture picture) {
        return drawPicture(picture, null, null);
    }

    public Canvas drawPicture(Picture picture, Matrix33 matrix, Paint paint) {
        Stats.onNativeCall();
        _nDrawPicture(_ptr, Native.getPtr(picture), matrix == null ? null : matrix._mat, Native.getPtr(paint));
        return this;
    }   

    /**
     * <p>Draws a triangle mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader, the shader is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangles(@NotNull Point[] positions, @Nullable int[] colors, @NotNull Paint paint) { 
        return drawTriangles(positions, colors, null, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangles(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @Nullable Paint paint) { 
        return drawTriangles(positions, colors, texCoords, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangles(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @NotNull BlendMode mode, @Nullable Paint paint) { 
        assert positions.length % 3 == 0 : "Expected positions.length % 3 == 0, got: " + positions.length;
        assert colors == null || colors.length == positions.length : "Expected colors.length == positions.length, got: " + colors.length + " != " + positions.length;
        assert texCoords == null || texCoords.length == positions.length : "Expected texCoords.length == positions.length, got: " + texCoords.length + " != " + positions.length;
        Stats.onNativeCall();
        _nDrawVertices(_ptr, 0 /* kTriangles_VertexMode */, Point.flattenArray(positions), colors, Point.flattenArray(texCoords), mode.ordinal(), Native.getPtr(paint));
        return this;
    }

    /**
     * <p>Draws a triangle strip mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader, the shader is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleStrip(@NotNull Point[] positions, @Nullable int[] colors, @NotNull Paint paint) { 
        return drawTriangleStrip(positions, colors, null, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle strip mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleStrip(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @NotNull Paint paint) { 
        return drawTriangleStrip(positions, colors, texCoords, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle strip mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleStrip(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @NotNull BlendMode mode, @NotNull Paint paint) { 
        assert colors == null || colors.length == positions.length : "Expected colors.length == positions.length, got: " + colors.length + " != " + positions.length;
        assert texCoords == null || texCoords.length == positions.length : "Expected texCoords.length == positions.length, got: " + texCoords.length + " != " + positions.length;
        Stats.onNativeCall();
        _nDrawVertices(_ptr, 1 /* kTriangleStrip_VertexMode */, Point.flattenArray(positions), colors, Point.flattenArray(texCoords), mode.ordinal(), Native.getPtr(paint));
        return this;
    }

    /**
     * <p>Draws a triangle fan mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader, the shader is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleFan(@NotNull Point[] positions, @Nullable int[] colors, @NotNull Paint paint) { 
        return drawTriangleFan(positions, colors, null, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle fan mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleFan(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @NotNull Paint paint) { 
        return drawTriangleFan(positions, colors, texCoords, BlendMode.MODULATE, paint);
    }

    /**
     * <p>Draws a triangle fan mesh, using clip and Matrix.</p>
     *
     * <p>If paint contains an Shader and vertices does not contain texCoords, the shader
     * is mapped using the vertices' positions.</p>
     *
     * <p>If vertices colors are defined in vertices, and Paint paint contains Shader,
     * BlendMode mode combines vertices colors with Shader.</p>
     *
     * @param positions  triangle mesh to draw
     * @param colors     color array, one for each corner; may be null
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners; may be null
     * @param mode       combines vertices colors with Shader, if both are present
     * @param paint      specifies the Shader, used as Vertices texture
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices">https://fiddle.skia.org/c/@Canvas_drawVertices</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawVertices_2">https://fiddle.skia.org/c/@Canvas_drawVertices_2</a>
     */
    public Canvas drawTriangleFan(@NotNull Point[] positions, @Nullable int[] colors, @Nullable Point[] texCoords, @NotNull BlendMode mode, @NotNull Paint paint) { 
        assert colors == null || colors.length == positions.length : "Expected colors.length == positions.length, got: " + colors.length + " != " + positions.length;
        assert texCoords == null || texCoords.length == positions.length : "Expected texCoords.length == positions.length, got: " + texCoords.length + " != " + positions.length;
        Stats.onNativeCall();
        _nDrawVertices(_ptr, 2 /* kTriangleFan_VertexMode */, Point.flattenArray(positions), colors, Point.flattenArray(texCoords), mode.ordinal(), Native.getPtr(paint));
        return this;
    }

    /** 
     * <p>Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.</p>
     *
     * <p>Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture.</p>
     *
     * <p>Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.</p>
     *
     * <p>Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.</p>
     *
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see <a href="https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445">https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445</a>
     */
    public Canvas drawPatch(@NotNull Point[] cubics, @NotNull int[] colors, @NotNull Paint paint) {
        return drawPatch(cubics, colors, null, paint);
    }

    /** 
     * <p>Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.</p>
     *
     * <p>Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture.</p>
     *
     * <p>Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.</p>
     *
     * <p>Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.</p>
     * 
     * <p>If paint contains Shader, Point array texCoords maps Shader as texture to
     * corners in top-left, top-right, bottom-right, bottom-left order. If texCoords is
     * nullptr, Shader is mapped using positions (derived from cubics).</p>
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners;
     *                   may be null
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see <a href="https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445">https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445</a>
     */
    public Canvas drawPatch(@NotNull Point[] cubics, @NotNull int[] colors, @Nullable Point[] texCoords, @NotNull Paint paint) {
        return drawPatch(cubics, colors, texCoords, BlendMode.MODULATE, paint);
    }

    /** 
     * <p>Draws a Coons patch: the interpolation of four cubics with shared corners,
     * associating a color, and optionally a texture SkPoint, with each corner.</p>
     *
     * <p>Coons patch uses clip and Matrix, paint Shader, ColorFilter,
     * alpha, ImageFilter, and BlendMode. If Shader is provided it is treated
     * as Coons patch texture; BlendMode mode combines color colors and Shader if
     * both are provided.</p>
     *
     * <p>Point array cubics specifies four Path cubic starting at the top-left corner,
     * in clockwise order, sharing every fourth point. The last Path cubic ends at the
     * first point.</p>
     *
     * <p>Color array color associates colors with corners in top-left, top-right,
     * bottom-right, bottom-left order.</p>
     * 
     * <p>If paint contains Shader, Point array texCoords maps Shader as texture to
     * corners in top-left, top-right, bottom-right, bottom-left order. If texCoords is
     * nullptr, Shader is mapped using positions (derived from cubics).</p>
     *
     * @param cubics     Path cubic array, sharing common points
     * @param colors     color array, one for each corner
     * @param texCoords  Point array of texture coordinates, mapping Shader to corners;
     *                   may be null
     * @param mode       BlendMode for colors, and for Shader if paint has one
     * @param paint      Shader, ColorFilter, BlendMode, used to draw
     * @return           this
     *
     * @see <a href="https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445">https://fiddle.skia.org/c/4cf70f8d194867d053d7e177e5088445</a>
     */
    public Canvas drawPatch(@NotNull Point[] cubics, @NotNull int[] colors, @Nullable Point[] texCoords, @NotNull BlendMode mode, @NotNull Paint paint) {
        assert cubics.length == 12 : "Expected cubics.length == 12, got: " + cubics.length;
        assert colors.length == 4 : "Expected colors.length == 4, got: " + colors.length;
        assert texCoords == null || texCoords.length == 4 : "Expected texCoords.length == 4, got: " + texCoords.length;
        Stats.onNativeCall();
        _nDrawPatch(_ptr, Point.flattenArray(cubics), colors, Point.flattenArray(texCoords), mode.ordinal(), Native.getPtr(paint));
        return this;
    }

    /**
     * <p>Draws Drawable drawable using clip and matrix.</p>
     *
     * <p>If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.</p>
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @return          this
     */
    public Canvas drawDrawable(@NotNull Drawable drawable) {
        return drawDrawable(drawable, null);
    }

    /**
     * <p>Draws Drawable drawable using clip and matrix, offset by (x, y).</p>
     *
     * <p>If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.</p>
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @param x         offset into Canvas writable pixels on x-axis
     * @param y         offset into Canvas writable pixels on y-axis
     * @return          this
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawDrawable_2">https://fiddle.skia.org/c/@Canvas_drawDrawable_2</a>
     */
    public Canvas drawDrawable(@NotNull Drawable drawable, float x, float y) {
        return drawDrawable(drawable, Matrix33.makeTranslate(x, y));
    }

    /**
     * <p>Draws Drawable drawable using clip and matrix, concatenated with
     * optional matrix.</p>
     *
     * <p>If Canvas has an asynchronous implementation, as is the case
     * when it is recording into Picture, then drawable will be referenced,
     * so that Drawable::draw() can be called when the operation is finalized. To force
     * immediate drawing, call Drawable::draw() instead.</p>
     *
     * @param drawable  custom struct encapsulating drawing commands
     * @param matrix    transformation applied to drawing; may be null
     * @return          this
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_drawDrawable">https://fiddle.skia.org/c/@Canvas_drawDrawable</a>
     */
    public Canvas drawDrawable(@NotNull Drawable drawable, @Nullable Matrix33 matrix) {
        Stats.onNativeCall();
        _nDrawDrawable(_ptr, Native.getPtr(drawable), matrix == null ? null : matrix._mat);
        return this;
    }

    public Canvas clear(int color) {
        Stats.onNativeCall();
        _nClear(_ptr, color);
        return this;
    }

    public Canvas drawPaint(Paint paint) {
        Stats.onNativeCall();
        _nDrawPaint(_ptr, Native.getPtr(paint));
        return this;
    }

    /**
     * Replaces Matrix with matrix.
     * Unlike concat(), any prior matrix state is overwritten.
     *
     * @param matrix  matrix to copy, replacing existing Matrix
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_setMatrix">https://fiddle.skia.org/c/@Canvas_setMatrix</a>
     */
    public Canvas setMatrix(@NotNull Matrix33 matrix) {
        Stats.onNativeCall();
        _nSetMatrix(_ptr, matrix._mat);
        return this;
    }

    /**
     * Sets SkMatrix to the identity matrix.
     * Any prior matrix state is overwritten.
     *
     * @see <a href="https://fiddle.skia.org/c/@Canvas_resetMatrix">https://fiddle.skia.org/c/@Canvas_resetMatrix</a>
     */
    public Canvas resetMatrix(@NotNull Matrix33 matrix) {
        Stats.onNativeCall();
        _nResetMatrix(_ptr);
        return this;
    }

    public Canvas clipRect(Rect r, ClipMode mode, boolean antiAlias) {
        Stats.onNativeCall();
        _nClipRect(_ptr, r._left, r._top, r._right, r._bottom, mode.ordinal(), antiAlias);
        return this;
    }

    public Canvas clipRect(Rect r, ClipMode mode) {
        return clipRect(r, mode, false);
    }

    public Canvas clipRect(Rect r, boolean antiAlias) {
        return clipRect(r, ClipMode.INTERSECT, antiAlias);
    }

    public Canvas clipRect(Rect r) {
        return clipRect(r, ClipMode.INTERSECT, false);
    }

    public Canvas clipRRect(RRect r, ClipMode mode, boolean antiAlias) {
        Stats.onNativeCall();
        _nClipRRect(_ptr, r._left, r._top, r._right, r._bottom, r._radii, mode.ordinal(), antiAlias);
        return this;
    }

    public Canvas clipRRect(RRect r, ClipMode mode) {
        return clipRRect(r, mode, false);
    }

    public Canvas clipRRect(RRect r, boolean antiAlias) {
        return clipRRect(r, ClipMode.INTERSECT, antiAlias);
    }

    public Canvas clipRRect(RRect r) {
        return clipRRect(r, ClipMode.INTERSECT, false);
    }

    public Canvas clipPath(Path p, ClipMode mode, boolean antiAlias) {
        Stats.onNativeCall();
        _nClipPath(_ptr, Native.getPtr(p), mode.ordinal(), antiAlias);
        return this;
    }

    public Canvas clipPath(Path p, ClipMode mode) {
        return clipPath(p, mode, false);
    }

    public Canvas clipPath(Path p, boolean antiAlias) {
        return clipPath(p, ClipMode.INTERSECT, antiAlias);
    }

    public Canvas clipPath(Path p) {
        return clipPath(p, ClipMode.INTERSECT, false);
    }

    public Canvas clipRegion(Region r, ClipMode mode) {
        Stats.onNativeCall();
        _nClipRegion(_ptr, Native.getPtr(r), mode.ordinal());
        return this;
    }

    public Canvas clipRegion(Region r) {
        return clipRegion(r, ClipMode.INTERSECT);
    }

    public Canvas translate(float dx, float dy) {
        return concat(Matrix33.makeTranslate(dx, dy));
    }

    public Canvas scale(float sx, float sy) {
        return concat(Matrix33.makeScale(sx, sy));
    }

    public Canvas rotate(float deg) {
        return concat(Matrix33.makeRotate(deg));
    }

    public Canvas skew(float sx, float sy) {
        return concat(Matrix33.makeSkew(sx, sy));
    }

    public Canvas concat(Matrix33 matrix) {
        Stats.onNativeCall();
        _nConcat(_ptr, matrix.getMat());
        return this;
    }

    public Canvas concat(Matrix44 matrix) {
        Stats.onNativeCall();
        _nConcat44(_ptr, matrix.getMat());
        return this;
    }

    /** 
     * <p>Copies Rect of pixels from Canvas into bitmap. Matrix and clip are
     * ignored.</p>
     * 
     * <p>Source Rect corners are (srcX, srcY) and (imageInfo().width(), imageInfo().height()).
     * Destination Rect corners are (0, 0) and (bitmap.width(), bitmap.height()).
     * Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to bitmap.colorType() and bitmap.alphaType() if required.</p>
     * 
     * <p>Pixels are readable when BaseDevice is raster, or backed by a GPU.
     * Pixels are not readable when Canvas is returned by Document::beginPage,
     * returned by PictureRecorder::beginRecording, or Canvas is the base of a utility
     * class like DebugCanvas.</p>
     * 
     * <p>Caller must allocate pixel storage in bitmap if needed.</p>
     * 
     * <p>SkBitmap values are converted only if ColorType and AlphaType
     * do not match. Only pixels within both source and destination rectangles
     * are copied. Bitmap pixels outside Rect intersection are unchanged.</p>
     * 
     * <p>Pass negative values for srcX or srcY to offset pixels across or down bitmap.</p>
     * 
     * <p>Does not copy, and returns false if:
     * <ul><li>Source and destination rectangles do not intersect.</li>
     * <li>SkCanvas pixels could not be converted to bitmap.colorType() or bitmap.alphaType().</li>
     * <li>SkCanvas pixels are not readable; for instance, Canvas is document-based.</li>
     * <li>bitmap pixels could not be allocated.</li>
     * <li>bitmap.rowBytes() is too small to contain one row of pixels.</li></ul></p>
     * 
     * @param bitmap  storage for pixels copied from Canvas
     * @param srcX    offset into readable pixels on x-axis; may be negative
     * @param srcY    offset into readable pixels on y-axis; may be negative
     * @return        true if pixels were copied
     * 
     * @see <a href="https://fiddle.skia.org/c/@Canvas_readPixels_3">https://fiddle.skia.org/c/@Canvas_readPixels_3</a>
     */
    public boolean readPixels(Bitmap bitmap, int srcX, int srcY) {
        Stats.onNativeCall();
        return _nReadPixels(_ptr, Native.getPtr(bitmap), srcX, srcY);
    }

    /**
     * <p>Copies Rect from pixels to Canvas. Matrix and clip are ignored.
     * Source Rect corners are (0, 0) and (bitmap.width(), bitmap.height()).</p>
     * 
     * <p>Destination Rect corners are (x, y) and
     * (imageInfo().width(), imageInfo().height()).</p>
     * 
     * <p>Copies each readable pixel intersecting both rectangles, without scaling,
     * converting to getImageInfo().getColorType() and getImageInfo().getAlphaType() if required.</p>
     * 
     * <p>Pixels are writable when BaseDevice is raster, or backed by a GPU.
     * Pixels are not writable when Canvas is returned by Document::beginPage,
     * returned by PictureRecorder::beginRecording, or Canvas is the base of a utility
     * class like DebugCanvas.</p>
     * 
     * <p>Pixel values are converted only if ColorType and AlphaType
     * do not match. Only pixels within both source and destination rectangles
     * are copied. Canvas pixels outside Rect intersection are unchanged.</p>
     * 
     * <p>Pass negative values for x or y to offset pixels to the left or
     * above Canvas pixels.</p>
     * 
     * <p>Does not copy, and returns false if:
     * <ul><li>Source and destination rectangles do not intersect.</li>
     * <li>bitmap does not have allocated pixels.</li>
     * <li>bitmap pixels could not be converted to Canvas getImageInfo().getColorType() or
     * getImageInfo().getAlphaType().</li>
     * <li>Canvas pixels are not writable; for instance, Canvas is document based.</li>
     * <li>bitmap pixels are inaccessible; for instance, bitmap wraps a texture.</li></ul></p>
     * 
     * @param bitmap  contains pixels copied to Canvas
     * @param x       offset into Canvas writable pixels on x-axis; may be negative
     * @param y       offset into Canvas writable pixels on y-axis; may be negative
     * @return        true if pixels were written to Canvas
     * 
     * @see <a href="https://fiddle.skia.org/c/@Canvas_writePixels_2">https://fiddle.skia.org/c/@Canvas_writePixels_2</a>
     * @see <a href="https://fiddle.skia.org/c/@State_Stack_a">https://fiddle.skia.org/c/@State_Stack_a</a>
     * @see <a href="https://fiddle.skia.org/c/@State_Stack_b">https://fiddle.skia.org/c/@State_Stack_b</a>
    */
    public boolean writePixels(Bitmap bitmap, int x, int y) {
        Stats.onNativeCall();
        return _nWritePixels(_ptr, Native.getPtr(bitmap), x, y);
    }    

    public int save() {
        Stats.onNativeCall();
        return _nSave(_ptr);
    }

    public int saveLayer(float left, float top, float right, float bottom, Paint paint) {
        Stats.onNativeCall();
        return _nSaveLayerRect(_ptr, left, top, right, bottom, Native.getPtr(paint));
    }

    /**
     * <p>Saves Matrix and clip, and allocates a Bitmap for subsequent drawing.
     * Calling restore() discards changes to Matrix and clip, and draws the Bitmap.</p>
     * 
     * <p>Matrix may be changed by translate(), scale(), rotate(), skew(), concat(),
     * setMatrix(), and resetMatrix(). Clip may be changed by clipRect(), clipRRect(),
     * clipPath(), clipRegion().</p>
     * 
     * <p>Rect bounds suggests but does not define the Bitmap size. To clip drawing to
     * a specific rectangle, use clipRect().</p>
     * 
     * <p>Optional Paint paint applies alpha, ColorFilter, ImageFilter, and
     * BlendMode when restore() is called.</p>
     * 
     * <p>Call restoreToCount() with returned value to restore this and subsequent saves.</p>
     * 
     * @param bounds  hint to limit the size of the layer
     * @param paint   graphics state for layer; may be null
     * @return        depth of saved stack
     * 
     * @see <a href="https://fiddle.skia.org/c/@Canvas_saveLayer">https://fiddle.skia.org/c/@Canvas_saveLayer</a>
     * @see <a href="https://fiddle.skia.org/c/@Canvas_saveLayer_4">https://fiddle.skia.org/c/@Canvas_saveLayer_4</a>
    */
    public int saveLayer(@Nullable Rect bounds, @Nullable Paint paint) {
        Stats.onNativeCall();
        if (bounds == null)
            return _nSaveLayer(_ptr, Native.getPtr(paint));
        else
            return _nSaveLayerRect(_ptr, bounds._left, bounds._top, bounds._right, bounds._bottom, Native.getPtr(paint));
    }

    public int getSaveCount() {
        Stats.onNativeCall();
        return _nGetSaveCount(_ptr);
    }

    public Canvas restore() {
        Stats.onNativeCall();
        _nRestore(_ptr);
        return this;
    }

    public Canvas restoreToCount(int saveCount) {
        Stats.onNativeCall();
        _nRestoreToCount(_ptr, saveCount);
        return this;
    }

    public static native long _nMakeFromBitmap(long bitmapPtr, int flags, int pixelGeometry);
    public static native void _nDrawPoint(long ptr, float x, float y, long paintPtr);
    public static native void _nDrawPoints(long ptr, int mode, float[] coords, long paintPtr);
    public static native void _nDrawLine(long ptr, float x0, float y0, float x1, float y1, long paintPtr);
    public static native void _nDrawArc(long ptr, float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, long paintPtr);
    public static native void _nDrawRect(long ptr, float left, float top, float right, float bottom, long paintPtr);
    public static native void _nDrawOval(long ptr, float left, float top, float right, float bottom, long paintPtr);
    public static native void _nDrawRRect(long ptr, float left, float top, float right, float bottom, float[] radii, long paintPtr);
    public static native void _nDrawDRRect(long ptr, float ol, float ot, float or, float ob, float[] oradii, float il, float it, float ir, float ib, float[] iradii, long paintPtr);
    public static native void _nDrawPath(long ptr, long nativePath, long paintPtr);
    public static native void _nDrawImageRect(long ptr, long nativeImage, float sl, float st, float sr, float sb, float dl, float dt, float dr, float db, long paintPtr, boolean strict);
    public static native void _nDrawImageIRect(long ptr, long nativeImage, int sl, int st, int sr, int sb, float dl, float dt, float dr, float db, long paintPtr, boolean strict);
    public static native void _nDrawBitmapRect(long ptr, long bitmapPtr, float sl, float st, float sr, float sb, float dl, float dt, float dr, float db, long paintPtr, boolean strict);
    public static native void _nDrawBitmapIRect(long ptr, long bitmapPtr, int sl, int st, int sr, int sb, float dl, float dt, float dr, float db, long paintPtr, boolean strict);
    public static native void _nDrawRegion(long ptr, long nativeRegion, long paintPtr);
    public static native void _nDrawString(long ptr, String string, float x, float y, long font, long paint);
    public static native void _nDrawTextBlob(long ptr, long blob, float x, float y, long font, long paint);
    public static native void _nDrawPicture(long ptr, long picturePtr, float[] matrix, long paintPtr);
    public static native void _nDrawVertices(long ptr, int verticesMode, float[] cubics, int[] colors, float[] texCoords, int blendMode, long paintPtr);
    public static native void _nDrawPatch(long ptr, float[] cubics, int[] colors, float[] texCoords, int blendMode, long paintPtr);
    public static native void _nDrawDrawable(long ptr, long drawablePrt, float[] matrix);
    public static native void _nClear(long ptr, int color);
    public static native void _nDrawPaint(long ptr, long paintPtr);
    public static native void _nSetMatrix(long ptr, float[] matrix);
    public static native void _nResetMatrix(long ptr);
    public static native void _nClipRect(long ptr, float left, float top, float right, float bottom, int mode, boolean antiAlias);
    public static native void _nClipRRect(long ptr, float left, float top, float right, float bottom, float[] radii, int mode, boolean antiAlias);
    public static native void _nClipPath(long ptr, long nativePath, int mode, boolean antiAlias);
    public static native void _nClipRegion(long ptr, long nativeRegion, int mode);
    public static native void _nConcat(long ptr, float[] matrix);
    public static native void _nConcat44(long ptr, float[] matrix);
    public static native boolean _nReadPixels(long ptr, long bitmapPtr, int srcX, int srcY);
    public static native boolean _nWritePixels(long ptr, long bitmapPtr, int x, int y);
    public static native int  _nSave(long ptr);
    public static native int  _nSaveLayer(long ptr, long paintPtr);
    public static native int  _nSaveLayerRect(long ptr, float left, float top, float right, float bottom, long paintPtr);
    public static native int  _nGetSaveCount(long ptr);
    public static native void _nRestore(long ptr);
    public static native void _nRestoreToCount(long ptr, int saveCount);
}
