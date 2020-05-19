package org.jetbrains.skija;

/**
 * <p>Path contain geometry. Path may be empty, or contain one or more verbs that
 * outline a figure. Path always starts with a move verb to a Cartesian coordinate,
 * and may be followed by additional verbs that add lines or curves.</p>
 * 
 * <p>Adding a close verb makes the geometry into a continuous loop, a closed contour.
 * Path may contain any number of contours, each beginning with a move verb.</p>
 *
 * <p>Path contours may contain only a move verb, or may also contain lines,
 * quadratic beziers, conics, and cubic beziers. Path contours may be open or
 * closed.</p>
 *
 * <p>When used to draw a filled area, Path describes whether the fill is inside or
 * outside the geometry. Path also describes the winding rule used to fill
 * overlapping contours.</p>
 *
 * <p>Internally, Path lazily computes metrics likes bounds and convexity. Call
 * {@link #updateBoundsCache()} to make Path thread safe.</p>
 */
public class Path extends Managed {
    public enum FillType { 
        /** Specifies that "inside" is computed by a non-zero sum of signed edge crossings. */
        WINDING,

        /** Specifies that "inside" is computed by an odd number of edge crossings. */
        EVEN_ODD,

        /** Same as {@link #WINDING}, but draws outside of the path, rather than inside. */
        INVERSE_WINDING,

        /** Same as {@link EVEN_ODD}, but draws outside of the path, rather than inside. */
        INVERSE_EVEN_ODD;

        /**
         * Returns if FillType describes area outside Path geometry. The inverse fill area
         * extends indefinitely.
         *
         * @return  true if FillType is {@link #INVERSE_WINDING} or {@link #INVERSE_EVEN_ODD}
         */
        public boolean isInverse() { return this == INVERSE_WINDING || this == INVERSE_EVEN_ODD; }

        /**
         * Returns the inverse fill type. The inverse of FillType describes the area
         * unmodified by the original FillType.
         *
         * @return  inverse FillType
         */
        public FillType inverse() {
            switch (this) {
                case WINDING:
                    return INVERSE_WINDING;
                case EVEN_ODD:
                    return INVERSE_EVEN_ODD;
                case INVERSE_WINDING:
                    return WINDING;
                case INVERSE_EVEN_ODD:
                    return EVEN_ODD;
                default:
                    throw new RuntimeException("Unreachable");
            }
        }
    }

    public enum Direction {
        /** Clockwise direction for adding closed contours. */
        CLOCKWISE,

        /** Counter-clockwise direction for adding closed contours. */
        COUNTER_CLOCKWISE
    }

    public enum ArcSize {
        /** Smaller of arc pair. */
        SMALL,

        /** Larger of arc pair. */
        LARGE
    }

    public enum ConvexityType {
        UNKNOWN,
        CONVEX,
        CONCAVE
    }

    public enum Verb {
        /** iter.next returns 1 point */
        MOVE,

        /** iter.next returns 2 points */
        LINE,

        /** iter.next returns 3 points */
        QUAD,

        /** iter.next returns 3 points + iter.conicWeight() */
        CONIC,

        /** iter.next returns 4 points */
        CUBIC,

        /** iter.next returns 1 point (contour's moveTo pt) */
        CLOSE,

        /** iter.next returns 0 points */
        DONE
    }

    /**
     * Constructs an empty Path. By default, Path has no verbs, no {@link Point}, and no weights.
     * FillType is set to {@link FillType#WINDING}.
     */
    public Path() {
        this(nInit());
    }

    /**
     * <p>Returns true if Path contain equal verbs and equal weights.
     * If Path contain one or more conics, the weights must match.</p>
     *
     * <p>{@link conicTo(float, float, float, float, float)} may add different verbs
     * depending on conic weight, so it is not trivial to interpolate a pair of Path
     * containing conics with different conic weight values.</p>
     *
     * @param compare  Path to compare
     * @return         true if Path verb array and weights are equivalent
     * 
     * @see <a href="https://fiddle.skia.org/c/@Path_isInterpolatable">https://fiddle.skia.org/c/@Path_isInterpolatable</a>
     */
    public boolean isInterpolatable(Path compare) {
        Native.onNativeCall();
        return nIsInterpolatable(nativeInstance, Native.pointer(compare));
    }

    /** 
     * <p>Interpolates between Path with {@link Point} array of equal size.
     * Copy verb array and weights to out, and set out Point array to a weighted
     * average of this Point array and ending Point array, using the formula:
     *
     * <p>{@code (Path Point * weight) + ending Point * (1 - weight)}
     *
     * <p>weight is most useful when between zero (ending Point array) and
     * one (this Point_Array); will work with values outside of this
     * range.</p>
     *
     * <p>interpolate() returns null if Point array is not
     * the same size as ending Point array. Call {@link isInterpolatable(Path)} to check Path
     * compatibility prior to calling interpolate().</p>
     *
     * @param ending  Point array averaged with this Point array
     * @param weight  contribution of this Point array, and
     *                one minus contribution of ending Point array
     * @return        interpolated Path if Path contain same number of Point, null otherwise
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_interpolate">https://fiddle.skia.org/c/@Path_interpolate</a>
     */
    public Path interpolate(Path ending, float weight) {
        Native.onNativeCall();
        long ptr = nInterpolate(nativeInstance, Native.pointer(ending), weight);   
        if (ptr == 0)
            throw new IllegalArgumentException("Point array is not the same size as ending Point array");
        return new Path(ptr);
    }

    protected FillType fillType = FillType.WINDING;

    public FillType getFillType() {
        return fillType;
    }

    public Path setFillType(FillType fillType) {
        this.fillType = fillType;
        Native.onNativeCall();
        nSetFillType(nativeInstance, fillType.ordinal());
        return this;
    }

    /**
     * Returns the convexity type, computing if needed. Never returns {@link ConvexityType#UNKNOWN}.
     * 
     * @return  path's convexity type ({@link ConvexityType#CONVEX} or {@link ConvexityType#CONCAVE})
     */
    public ConvexityType getConvexityType() {
        Native.onNativeCall();
        return ConvexityType.values()[nGetConvexityType(nativeInstance)];
    }

    /**
     * If the path's convexity is already known, return it, else return {@link ConvexityType#UNKNOWN}.
     * If you always want to know the convexity, even if that means having to compute it,
     * call {@link getConvexityType()}.
     *
     * @return  known convexity, or {@link ConvexityType#UNKNOWN}
     */
    public ConvexityType getConvexityTypeOrUnknown() {
        Native.onNativeCall();
        return ConvexityType.values()[nGetConvexityTypeOrUnknown(nativeInstance)];
    }

    /**
     * <p>Stores a convexity type for this path.</p>
     *
     * <p>This is what will be returned if
     * {@link getConvexityTypeOrUnknown()} is called. If you pass {@link ConvexityType#UNKNOWN},
     * then if {@link getConvexityType()} is called, the real convexity will be computed.</p>
     *
     * @param   convexity value to set
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_setConvexity">https://fiddle.skia.org/c/@Path_setConvexity</a>
     */
    public Path setConvexityType(ConvexityType convexity) {
        Native.onNativeCall();
        nSetConvexityType(nativeInstance, convexity.ordinal());
        return this;
    }

    /**
     * Returns true if the path is convex. If necessary, it will first compute the convexity.
     *
     * @return  true or false
     */
    public boolean isConvex() {
        return getConvexityType() == ConvexityType.CONVEX;
    }

    /**
     * Returns oval bounds if this path is recognized as an oval or circle.
     * 
     * @return  bounds is recognized as an oval or circle, null otherwise
     * 
     * @see <a href="https://fiddle.skia.org/c/@Path_isOval">https://fiddle.skia.org/c/@Path_isOval</a>
     */
    public Rect isOval() {
        Native.onNativeCall();
        return nIsOval(nativeInstance);
    }

    /**
     * Returns {@link RoundedRect} if this path is recognized as an oval, circle or RoundedRect.
     * 
     * @return  bounds is recognized as an oval, circle or RoundedRect, null otherwise
     * 
     * @see <a href="https://fiddle.skia.org/c/@Path_isRRect">https://fiddle.skia.org/c/@Path_isRRect</a>
     */
    public RoundedRect isRRect() {
        Native.onNativeCall();
        return nIsRRect(nativeInstance);
    }

    /**
     * <p>Sets Path to its initial state.</p>
     * 
     * <p>Removes verb array, Point array, and weights, and sets FillType to {@link FillType#WINDING}.
     * Internal storage associated with Path is released.</p>
     *
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_reset">https://fiddle.skia.org/c/@Path_reset</a>
     */
    public Path reset() {
        Native.onNativeCall();
        nReset(nativeInstance);
        return this;
    }

    /**
     * <p>Sets Path to its initial state, preserving internal storage.
     * Removes verb array, Point array, and weights, and sets FillType to kWinding.
     * Internal storage associated with Path is retained.</p>
     *
     * <p>Use {@link rewind()} instead of {@link reset()} if Path storage will be reused and performance
     * is critical.</p>
     *
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_rewind">https://fiddle.skia.org/c/@Path_rewind</a>
     */
    public Path rewind() {
        Native.onNativeCall();
        nReset(nativeInstance);
        return this;
    }

    /**
     * <p>Returns if Path is empty.</p>
     * 
     * <p>Empty Path may have FillType but has no {@link Point}, {@link Verb}, or conic weight.
     * {@link Path()} constructs empty Path; {@link reset()} and {@link rewind()} make Path empty.</p>
     *
     * @return  true if the path contains no Verb array
     */
    public boolean isEmpty() {
        Native.onNativeCall();
        return nIsEmpty(nativeInstance);
    }

    /**
     * <p>Returns if contour is closed.</p>
     * 
     * <p>Contour is closed if Path Verb array was last modified by {@link closePath()}. When stroked,
     * closed contour draws {@link Paint.Join} instead of {@link Paint.Cap} at first and last Point.</p>
     *
     * @return  true if the last contour ends with a {@link Verb#CLOSE}
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_isLastContourClosed">https://fiddle.skia.org/c/@Path_isLastContourClosed</a>
     */
    public boolean isLastContourClosed() {
        Native.onNativeCall();
        return nIsLastContourClosed(nativeInstance);
    }

    /**
     * Returns true for finite Point array values between negative Float.MIN_VALUE and
     * positive Float.MAX_VALUE. Returns false for any Point array value of
     * Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, or Float.NaN.
     *
     * @return  true if all Point values are finite
     */
    public boolean isFinite() {
        Native.onNativeCall();
        return nIsFinite(nativeInstance);
    }

    protected boolean isVolatile = false;

    /**
     * Returns true if the path is volatile; it will not be altered or discarded
     * by the caller after it is drawn. Path by default have volatile set false, allowing
     * {@link Surface} to attach a cache of data which speeds repeated drawing. If true, {@link Surface}
     * may not speed repeated drawing.
     *
     * @return  true if caller will alter Path after drawing
    */
    public boolean isVolatile() {
        return isVolatile;
    }

    /**
     * <p>Specifies whether Path is volatile; whether it will be altered or discarded
     * by the caller after it is drawn. Path by default have volatile set false, allowing
     * SkBaseDevice to attach a cache of data which speeds repeated drawing.</p>
     *
     * <p>Mark temporary paths, discarded or modified after use, as volatile
     * to inform SkBaseDevice that the path need not be cached.</p>
     *
     * <p>Mark animating Path volatile to improve performance.
     * Mark unchanging Path non-volatile to improve repeated rendering.</p>
     *
     * <p>raster surface Path draws are affected by volatile for some shadows.
     * GPU surface Path draws are affected by volatile for some shadows and concave geometries.</p>
     *
     * @param isVolatile  true if caller will alter Path after drawing
     * @return            this
     */
    public Path setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
        Native.onNativeCall();
        nSetIsVolatile(nativeInstance, isVolatile);
        return this;
    }

    /**
     * <p>Tests if line between Point pair is degenerate.</p>
     * 
     * <p>Line with no length or that moves a very short distance is degenerate; it is
     * treated as a point.</p>
     *
     * <p>exact changes the equality test. If true, returns true only if p1 equals p2.
     * If false, returns true if p1 equals or nearly equals p2.</p>
     *
     * @param p1     line start point
     * @param p2     line end point
     * @param exact  if false, allow nearly equals
     * @return       true if line is degenerate; its length is effectively zero
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_IsLineDegenerate">https://fiddle.skia.org/c/@Path_IsLineDegenerate</a>
     */
    public static boolean isLineDegenerate(Point p1, Point p2, boolean exact) {
        Native.onNativeCall();
        return nIsLineDegenerate(p1.x, p1.y, p2.x, p2.y, exact);
    }

    /**
     * <p>Tests if quad is degenerate.</p>
     *
     * <p>Quad with no length or that moves a very short distance is degenerate; it is
     * treated as a point.</p>
     *
     * @param p1     quad start point
     * @param p2     quad control point
     * @param p3     quad end point
     * @param exact  if true, returns true only if p1, p2, and p3 are equal;
     *               if false, returns true if p1, p2, and p3 are equal or nearly equal
     * @return       true if quad is degenerate; its length is effectively zero
     */
    public static boolean isQuadDegenerate(Point p1, Point p2, Point p3, boolean exact) {
        Native.onNativeCall();
        return nIsQuadDegenerate(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, exact);
    }

    /**
     * <p>Tests if cubic is degenerate.</p>
     * 
     * <p>Cubic with no length or that moves a very short distance is degenerate; it is
     * treated as a point.</p>
     *
     * @param p1     cubic start point
     * @param p2     cubic control point 1
     * @param p3     cubic control point 2
     * @param p4     cubic end point
     * @param exact  if true, returns true only if p1, p2, p3, and p4 are equal;
     *               if false, returns true if p1, p2, p3, and p4 are equal or nearly equal
     * @return       true if cubic is degenerate; its length is effectively zero
    */
    public static boolean isCubicDegenerate(Point p1, Point p2, Point p3, Point p4, boolean exact) {
        Native.onNativeCall();
        return nIsCubicDegenerate(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y, exact);
    }

    /**
     * Returns array of two points if Path contains only one line;
     * Verb array has two entries: {@link Verb#MOVE}, {@link Verb#LINE}.
     * Returns null if Path is not one line.
     *
     * @return  Point[2] if Path contains exactly one line, null otherwise
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_isLine">https://fiddle.skia.org/c/@Path_isLine</a>
     */
    public Point[] isLine() {
        Native.onNativeCall();
        return nIsLine(nativeInstance);
    }

    /**
     * Returns the number of points in Path.
     * Point count is initially zero.
     *
     * @return  Path Point array length
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_countPoints">https://fiddle.skia.org/c/@Path_countPoints</a>
     */
    public int countPoints() {
        Native.onNativeCall();
        return nCountPoints(nativeInstance);
    }

    /**
     * <p>Returns Point at index in Point array. Valid range for index is
     * 0 to countPoints() - 1.</p>
     *
     * <p>Returns (0, 0) if index is out of range.</p>
     *
     * @param index  Point array element selector
     * @return       Point array value or (0, 0)
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_getPoint">https://fiddle.skia.org/c/@Path_getPoint</a>
     */
    public Point getPoint(int index) {
        Native.onNativeCall();
        return nGetPoint(nativeInstance, index);
    }

    /**
     * <p>Returns number of points in Path. Up to max points are copied.</p>
     * 
     * <p>points may be nullptr; then, max must be zero.
     * If max is greater than number of points, excess points storage is unaltered.</p>
     *
     * @param points  storage for Path Point array. May be nullptr
     * @param max     maximum to copy; must be greater than or equal to zero
     * @return        Path Point array length
     * 
     * @see <a href="https://fiddle.skia.org/c/@Path_getPoints">https://fiddle.skia.org/c/@Path_getPoints</a>
     */
    public int getPoints(Point[] points, int max) {
        Native.onNativeCall();
        return nGetPoints(nativeInstance, points, max);
    }

    /**
     * Returns the number of verbs: {@link Verb#MOVE}, {@link Verb#LINE}, {@link Verb#QUAD}, {@link Verb#CONIC},
     * {@link Verb#CUBIC}, and {@link Verb#CLOSE}; added to Path.
     *
     * @return  length of verb array
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_countVerbs">https://fiddle.skia.org/c/@Path_countVerbs</a>
     */
    public int countVerbs() {
        Native.onNativeCall();
        return nCountVerbs(nativeInstance);
    }

    /**
     * Returns the number of verbs in the path. Up to max verbs are copied.
     *
     * @param verbs  storage for verbs, may be null
     * @param max    maximum number to copy into verbs
     * @return       the actual number of verbs in the path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_getVerbs">https://fiddle.skia.org/c/@Path_getVerbs</a>
     */
    public int getVerbs(Verb[] verbs, int max) {
        Native.onNativeCall();
        byte[] out = verbs == null ? null : new byte[max];
        int count = nGetVerbs(nativeInstance, out, max);
        if (verbs != null)
            for (int i = 0; i < Math.min(count, max); ++i)
                verbs[i] = Verb.values()[out[i]];
        return count;
    }

    /**
     * Returns the approximate byte size of the Path in memory.
     *
     * @return  approximate size
     */
    public long approximateBytesUsed() {
        Native.onNativeCall();
        return nApproximateBytesUsed(nativeInstance);
    }

    /**
     * <p>Exchanges the verb array, Point array, weights, and FillType with other.
     * Cached state is also exchanged. swap() internally exchanges pointers, so
     * it is lightweight and does not allocate memory.</p>
     *
     * @param   other  Path exchanged by value
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_swap">https://fiddle.skia.org/c/@Path_swap</a>
     */
    public Path swap(Path other) {
        Native.onNativeCall();
        nSwap(nativeInstance, Native.pointer(other));
        return this;
    }

    public Rect getBounds() { Native.onNativeCall(); float[] b = nGetBounds(nativeInstance); return Rect.makeLTRB(b[0], b[1], b[2], b[3]); }
    public Rect computeTightBounds() { Native.onNativeCall(); float[] b = nComputeTightBounds(nativeInstance); return Rect.makeLTRB(b[0], b[1], b[2], b[3]); }
    public Path moveTo(float x, float y) { Native.onNativeCall(); nMoveTo(nativeInstance, x, y); return this; }
    public Path rMoveTo(float dx, float dy) { Native.onNativeCall(); nRMoveTo(nativeInstance, dx, dy); return this; }
    public Path lineTo(float x, float y) { Native.onNativeCall(); nLineTo(nativeInstance, x, y); return this; }
    public Path rLineTo(float dx, float dy) { Native.onNativeCall(); nRLineTo(nativeInstance, dx, dy); return this;}
    public Path closePath() { Native.onNativeCall(); nClosePath(nativeInstance); return this; }
    public Path quadTo(float x1, float y1, float x2, float y2) { Native.onNativeCall(); nQuadTo(nativeInstance, x1, y1, x2, y2); return this; }
    public Path rQuadTo(float dx1, float dy1, float dx2, float dy2) { Native.onNativeCall(); nRQuadTo(nativeInstance, dx1, dy1, dx2, dy2); return this; }
    public Path conicTo(float x1, float y1, float x2, float y2, float w) { Native.onNativeCall(); nConicTo(nativeInstance, x1, y1, x2, y2, w); return this; }
    public Path rConicTo(float dx1, float dy1, float dx2, float dy2, float w) { Native.onNativeCall(); nRConicTo(nativeInstance, dx1, dy1, dx2, dy2, w); return this; }
    public Path cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) { Native.onNativeCall(); nCubicTo(nativeInstance, x1, y1, x2, y2, x3, y3); return this; }
    public Path rCubicTo(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) { Native.onNativeCall(); nRCubicTo(nativeInstance, dx1, dy1, dx2, dy2, dx3, dy3); return this; }
    public Path arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) {
        Native.onNativeCall(); 
        nArcTo(nativeInstance, left, top, right, bottom, startAngle, sweepAngle, forceMoveTo);
        return this;
    }
    public Path tangentArcTo(float x1, float y1, float x2, float y2, float radius) { Native.onNativeCall(); nTangentArcTo(nativeInstance, x1, y1, x2, y2, radius); return this; }
    public Path ellipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float x, float y) {
        Native.onNativeCall(); 
        nEllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), x, y);
        return this;
    }

    public Path rEllipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float dx, float dy) {
        Native.onNativeCall(); 
        nREllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), dx, dy);
        return this;
    }

    public Path addPoly(float[] coords, boolean close) { Native.onNativeCall(); nAddPoly(nativeInstance, coords, close); return this; }

    protected Path(long ptr) { super(ptr, nativeFinalizer); Native.onNativeCall(); }
    private static final  long        nativeFinalizer = nGetNativeFinalizer();
    private static native long        nInit();
    private static native long        nGetNativeFinalizer();
    private static native boolean     nIsInterpolatable(long ptr, long comparePtr);
    private static native long        nInterpolate(long ptr, long endingPtr, float weight);
    private static native void        nSetFillType(long ptr, int fillType);
    private static native int         nGetConvexityType(long ptr);
    private static native int         nGetConvexityTypeOrUnknown(long ptr);
    private static native void        nSetConvexityType(long ptr, int convexity);
    private static native Rect        nIsOval(long ptr);
    private static native RoundedRect nIsRRect(long ptr);
    private static native void        nReset(long ptr);
    private static native void        nRewind(long ptr);
    private static native boolean     nIsEmpty(long ptr);
    private static native boolean     nIsLastContourClosed(long ptr);
    private static native boolean     nIsFinite(long ptr);
    private static native void        nSetIsVolatile(long ptr, boolean isVolatile);
    private static native boolean     nIsLineDegenerate(float x0, float y0, float x1, float y1, boolean exact);
    private static native boolean     nIsQuadDegenerate(float x0, float y0, float x1, float y1, float x2, float y2, boolean exact);
    private static native boolean     nIsCubicDegenerate(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean exact);
    private static native Point[]     nIsLine(long ptr);
    private static native int         nCountPoints(long ptr);
    private static native Point       nGetPoint(long ptr, int index);
    private static native int         nGetPoints(long ptr, Point[] points, int max);
    private static native int         nCountVerbs(long ptr);
    private static native int         nGetVerbs(long ptr, byte[] verbs, int max);
    private static native long        nApproximateBytesUsed(long ptr);
    private static native void        nSwap(long ptr, long otherPtr);

    private static native float[] nGetBounds(long ptr);
    private static native float[] nComputeTightBounds(long ptr);
    private static native void nMoveTo(long ptr, float x, float y);
    private static native void nRMoveTo(long ptr, float dx, float dy);
    private static native void nLineTo(long ptr, float x, float y);
    private static native void nRLineTo(long ptr, float dx, float dy);
    private static native void nQuadTo(long ptr, float x1, float y1, float x2, float y2);
    private static native void nRQuadTo(long ptr, float dx1, float dy1, float dx2, float dy2);
    private static native void nConicTo(long ptr, float x1, float y1, float x2, float y2, float w);
    private static native void nRConicTo(long ptr, float dx1, float dy1, float dx2, float dy2, float w);
    private static native void nCubicTo(long ptr, float x1, float y1, float x2, float y2, float x3, float y3);
    private static native void nRCubicTo(long ptr, float dx1, float dy1, float dx2, float dy2, float dx3, float dy3);
    private static native void nArcTo(long ptr, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo);
    private static native void nTangentArcTo(long ptr, float x1, float y1, float x2, float y2, float radius);
    private static native void nEllipticalArcTo(long ptr, float rx, float ry, float xAxisRotate, int size, int direction, float x, float y);
    private static native void nREllipticalArcTo(long ptr, float rx, float ry, float xAxisRotate, int size, int direction, float dx, float dy);
    private static native void nAddPoly(long ptr, float[] coords, boolean close);
    private static native void nClosePath(long ptr);
}