package org.jetbrains.skija;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

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
public class Path extends Managed implements Iterable {
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

    /**
     * Verb instructs Path how to interpret one or more Point and optional conic weight;
     * manage contour, and terminate Path.
     */
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
     * Compares this path and o; Returns true if {@link FillType}, verb array, Point array, and weights
     * are equivalent.
     *
     * @param o  Path to compare
     * @return   true if this and Path are equivalent
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path op = (Path) o;
        if (nativeInstance == op.nativeInstance) return true;
        return nEquals(nativeInstance, op.nativeInstance);
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
    public RoundedRect isRoundedRect() {
        Native.onNativeCall();
        return nIsRoundedRect(nativeInstance);
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
        nRewind(nativeInstance);
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
     * <p>points may be null; then, max must be zero.
     * If max is greater than number of points, excess points storage is unaltered.</p>
     *
     * @param points  storage for Path Point array. May be null
     * @param max     maximum to copy; must be greater than or equal to zero
     * @return        Path Point array length
     * 
     * @see <a href="https://fiddle.skia.org/c/@Path_getPoints">https://fiddle.skia.org/c/@Path_getPoints</a>
     */
    public int getPoints(Point[] points, int max) {
        assert points == null ? max == 0 : true;
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
        assert verbs == null ? max == 0 : true;
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

    /** 
     * <p>Returns minimum and maximum axes values of Point array.</p>
     * 
     * <p>Returns (0, 0, 0, 0) if Path contains no points. Returned bounds width and height may
     * be larger or smaller than area affected when Path is drawn.</p>
     *
     * <p>Rect returned includes all Point added to Path, including Point associated with
     * {@link Verb#MOVE} that define empty contours.</p>
     *
     * @return  bounds of all Point in Point array
     */
    public Rect getBounds() {
        Native.onNativeCall();
        return nGetBounds(nativeInstance);
    }

    /**
     * <p>Updates internal bounds so that subsequent calls to {@link getBounds()} are instantaneous.
     * Unaltered copies of Path may also access cached bounds through {@link getBounds()}.</p>
     *
     * <p>For now, identical to calling {@link getBounds()} and ignoring the returned value.</p>
     *
     * <p>Call to prepare Path subsequently drawn from multiple threads,
     * to avoid a race condition where each draw separately computes the bounds.</p>
     *
     * @return  this
     */
    public Path updateBoundsCache() {
        Native.onNativeCall();
        nUpdateBoundsCache(nativeInstance);
        return this;
    }

    /**
     * <p>Returns minimum and maximum axes values of the lines and curves in Path.
     * Returns (0, 0, 0, 0) if Path contains no points.
     * Returned bounds width and height may be larger or smaller than area affected
     * when Path is drawn.</p>
     *
     * <p>Includes Point associated with {@link Verb#MOVE} that define empty
     * contours.</p>
     *
     * Behaves identically to {@link getBounds()} when Path contains
     * only lines. If Path contains curves, computed bounds includes
     * the maximum extent of the quad, conic, or cubic; is slower than {@link getBounds()};
     * and unlike {@link getBounds()}, does not cache the result.
     *
     * @return  tight bounds of curves in Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_computeTightBounds">https://fiddle.skia.org/c/@Path_computeTightBounds</a>
     */
    public Rect computeTightBounds() {
        Native.onNativeCall();
        return nComputeTightBounds(nativeInstance);
    }

    /**
     * <p>Returns true if rect is contained by Path.
     * May return false when rect is contained by Path.</p>
     *
     * <p>For now, only returns true if Path has one contour and is convex.
     * rect may share points and edges with Path and be contained.
     * Returns true if rect is empty, that is, it has zero width or height; and
     * the Point or line described by rect is contained by Path.</p>
     *
     * @param rect  Rect, line, or Point checked for containment
     * @return      true if rect is contained
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_conservativelyContainsRect">https://fiddle.skia.org/c/@Path_conservativelyContainsRect</a>
     */
    public boolean conservativelyContainsRect(Rect rect) {
        Native.onNativeCall();
        return nConservativelyContainsRect(nativeInstance, rect.left, rect.top, rect.right, rect.bottom);
    }

    /**
     * <p>Grows Path verb array and Point array to contain extraPtCount additional Point.
     * May improve performance and use less memory by
     * reducing the number and size of allocations when creating Path.</p>
     *
     * @param extraPtCount  number of additional Point to allocate
     * @return              this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_incReserve">https://fiddle.skia.org/c/@Path_incReserve</a>
     */
    public Path incReserve(int extraPtCount) {
        Native.onNativeCall();
        nIncReserve(nativeInstance, extraPtCount);
        return this;
    }

    /**
     * Shrinks Path verb array and Point array storage to discard unused capacity.
     * May reduce the heap overhead for Path known to be fully constructed.
     *
     * @return  this
     */
    public Path shrinkToFit() {
        Native.onNativeCall();
        nShrinkToFit(nativeInstance);
        return this;
    }

    /**
     * Adds beginning of contour at Point (x, y).
     *
     * @param x  x-axis value of contour start
     * @param y  y-axis value of contour start
     * @return   reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_moveTo">https://fiddle.skia.org/c/@Path_moveTo</a>
     */
    public Path moveTo(float x, float y) {
        Native.onNativeCall();
        nMoveTo(nativeInstance, x, y);
        return this;
    }

    /**
     * Adds beginning of contour at Point p.
     *
     * @param p  contour start
     * @return   this
     */
    public Path moveTo(Point p) {
        return moveTo(p.x, p.y);
    }

    /**
     * <p>Adds beginning of contour relative to last point.</p>
     *
     * <p>If Path is empty, starts contour at (dx, dy).
     * Otherwise, start contour at last point offset by (dx, dy).
     * Function name stands for "relative move to".</p>
     *
     * @param dx  offset from last point to contour start on x-axis
     * @param dy  offset from last point to contour start on y-axis
     * @return    reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_rMoveTo">https://fiddle.skia.org/c/@Path_rMoveTo</a>
     */
    public Path rMoveTo(float dx, float dy) {
        Native.onNativeCall();
        nRMoveTo(nativeInstance, dx, dy);
        return this;
    }

    /**
     * <p>Adds line from last point to (x, y). If Path is empty, or last Verb is
     * {@link Verb#CLOSE}, last point is set to (0, 0) before adding line.</p>
     *
     * <p>lineTo() appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed.
     * lineTo() then appends {@link Verb#LINE} to verb array and (x, y) to Point array.</p>
     *
     * @param x  end of added line on x-axis
     * @param y  end of added line on y-axis
     * @return   this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_lineTo">https://fiddle.skia.org/c/@Path_lineTo</a>
     */
    public Path lineTo(float x, float y) {
        Native.onNativeCall();
        nLineTo(nativeInstance, x, y);
        return this;
    }

    /**
     * <p>Adds line from last point to Point p. If Path is empty, or last {@link Verb} is
     * {@link Verb#CLOSE}, last point is set to (0, 0) before adding line.</p>
     *
     * <p>lineTo() first appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed.
     * lineTo() then appends {@link Verb#LINE} to verb array and Point p to Point array.</p>
     *
     * @param p  end Point of added line
     * @return   reference to Path
     */
    public Path lineTo(Point p) {
        return lineTo(p.x, p.y);
    }

    /**
     * <p>Adds line from last point to vector (dx, dy). If Path is empty, or last {@link Verb} is
     * {@link Verb#CLOSE}, last point is set to (0, 0) before adding line.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed;
     * then appends {@link Verb#LINE} to verb array and line end to Point array.</p>
     *
     * <p>Line end is last point plus vector (dx, dy).</p>
     * 
     * <p>Function name stands for "relative line to".</p>
     *
     * @param dx  offset from last point to line end on x-axis
     * @param dy  offset from last point to line end on y-axis
     * @return    reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_rLineTo">https://fiddle.skia.org/c/@Path_rLineTo</a>
     * @see <a href="https://fiddle.skia.org/c/@Quad_a">https://fiddle.skia.org/c/@Quad_a</a>
     * @see <a href="https://fiddle.skia.org/c/@Quad_b">https://fiddle.skia.org/c/@Quad_b</a>
     */
    public Path rLineTo(float dx, float dy) {
        Native.onNativeCall();
        nRLineTo(nativeInstance, dx, dy);
        return this;
    }

    /**
     * Adds quad from last point towards (x1, y1), to (x2, y2).
     * If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to (0, 0)
     * before adding quad.
     *
     * Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed;
     * then appends {@link Verb#QUAD} to verb array; and (x1, y1), (x2, y2)
     * to Point array.
     *
     * @param x1  control Point of quad on x-axis
     * @param y1  control Point of quad on y-axis
     * @param x2  end Point of quad on x-axis
     * @param y2  end Point of quad on y-axis
     * @return    reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_quadTo">https://fiddle.skia.org/c/@Path_quadTo</a>
     */
    public Path quadTo(float x1, float y1, float x2, float y2) {
        Native.onNativeCall();
        nQuadTo(nativeInstance, x1, y1, x2, y2);
        return this;
    }

    /**
     * <p>Adds quad from last point towards Point p1, to Point p2.</p>
     *
     * <p>If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to (0, 0)
     * before adding quad.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed;
     * then appends {@link Verb#QUAD} to verb array; and Point p1, p2
     * to Point array.</p>
     *
     * @param p1  control Point of added quad
     * @param p2  end Point of added quad
     * @return    reference to Path
     */
    public Path quadTo(Point p1, Point p2) {
        return quadTo(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * <p>Adds quad from last point towards vector (dx1, dy1), to vector (dx2, dy2).
     * If Path is empty, or last {@link Verb}
     * is {@link Verb#CLOSE}, last point is set to (0, 0) before adding quad.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array,
     * if needed; then appends {@link Verb#QUAD} to verb array; and appends quad
     * control and quad end to Point array.</p>
     *
     * <p>Quad control is last point plus vector (dx1, dy1).</p>
     *
     * <p>Quad end is last point plus vector (dx2, dy2).</p>
     *
     * <p>Function name stands for "relative quad to".</p>
     *
     * @param dx1  offset from last point to quad control on x-axis
     * @param dy1  offset from last point to quad control on y-axis
     * @param dx2  offset from last point to quad end on x-axis
     * @param dy2  offset from last point to quad end on y-axis
     * @return     reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Conic_Weight_a">https://fiddle.skia.org/c/@Conic_Weight_a</a>
     * @see <a href="https://fiddle.skia.org/c/@Conic_Weight_b">https://fiddle.skia.org/c/@Conic_Weight_b</a>
     * @see <a href="https://fiddle.skia.org/c/@Conic_Weight_c">https://fiddle.skia.org/c/@Conic_Weight_c</a>
     * @see <a href="https://fiddle.skia.org/c/@Path_rQuadTo">https://fiddle.skia.org/c/@Path_rQuadTo</a>
     */
    public Path rQuadTo(float dx1, float dy1, float dx2, float dy2) {
        Native.onNativeCall();
        nRQuadTo(nativeInstance, dx1, dy1, dx2, dy2);
        return this;
    }

    /** 
     * <p>Adds conic from last point towards (x1, y1), to (x2, y2), weighted by w.</p>
     *
     * <p>If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to (0, 0)
     * before adding conic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed.</p>
     *
     * <p>If w is finite and not one, appends {@link Verb#CONIC} to verb array;
     * and (x1, y1), (x2, y2) to Point array; and w to conic weights.</p>
     *
     * <p>If w is one, appends {@link Verb#QUAD} to verb array, and
     * (x1, y1), (x2, y2) to Point array.</p>
     *
     * <p>If w is not finite, appends {@link Verb#LINE} twice to verb array, and
     * (x1, y1), (x2, y2) to Point array.</p>
     *
     * @param x1  control Point of conic on x-axis
     * @param y1  control Point of conic on y-axis
     * @param x2  end Point of conic on x-axis
     * @param y2  end Point of conic on y-axis
     * @param w   weight of added conic
     * @return    reference to Path
     */
    public Path conicTo(float x1, float y1, float x2, float y2, float w) {
        Native.onNativeCall();
        nConicTo(nativeInstance, x1, y1, x2, y2, w);
        return this;
    }

    /** 
     * <p>Adds conic from last point towards Point p1, to Point p2, weighted by w.</p>
     *
     * <p>If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to (0, 0)
     * before adding conic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed.</p>
     *
     * <p>If w is finite and not one, appends {@link Verb#CONIC} to verb array;
     * and Point p1, p2 to Point array; and w to conic weights.</p>
     *
     * <p>If w is one, appends {@link Verb#QUAD} to verb array, and Point p1, p2
     * to Point array.</p>
     *
     * <p>If w is not finite, appends {@link Verb#LINE} twice to verb array, and
     * Point p1, p2 to Point array.</p>
     *
     * @param p1  control Point of added conic
     * @param p2  end Point of added conic
     * @param w   weight of added conic
     * @return    reference to Path
     */
    public Path conicTo(Point p1, Point p2, float w) {
        return conicTo(p1.x, p1.y, p2.x, p2.y, w);
    }

    /**
     * <p>Adds conic from last point towards vector (dx1, dy1), to vector (dx2, dy2),
     * weighted by w. If Path is empty, or last {@link Verb}
     * is {@link Verb#CLOSE}, last point is set to (0, 0) before adding conic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array,
     * if needed.</p>
     *
     * <p>If w is finite and not one, next appends {@link Verb#CONIC} to verb array,
     * and w is recorded as conic weight; otherwise, if w is one, appends
     * {@link Verb#QUAD} to verb array; or if w is not finite, appends {@link Verb#LINE}
     * twice to verb array.</p>
     *
     * <p>In all cases appends Point control and end to Point array.
     * control is last point plus vector (dx1, dy1).
     * end is last point plus vector (dx2, dy2).</p>
     *
     * <p>Function name stands for "relative conic to".</p>
     *
     * @param dx1  offset from last point to conic control on x-axis
     * @param dy1  offset from last point to conic control on y-axis
     * @param dx2  offset from last point to conic end on x-axis
     * @param dy2  offset from last point to conic end on y-axis
     * @param w    weight of added conic
     * @return     reference to Path
     */
    public Path rConicTo(float dx1, float dy1, float dx2, float dy2, float w) {
        Native.onNativeCall();
        nRConicTo(nativeInstance, dx1, dy1, dx2, dy2, w);
        return this;
    }

    /**
     * <p>Adds cubic from last point towards (x1, y1), then towards (x2, y2), ending at
     * (x3, y3). If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to
     * (0, 0) before adding cubic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed;
     * then appends {@link Verb#CUBIC} to verb array; and (x1, y1), (x2, y2), (x3, y3)
     * to Point array.</p>
     *
     * @param x1  first control Point of cubic on x-axis
     * @param y1  first control Point of cubic on y-axis
     * @param x2  second control Point of cubic on x-axis
     * @param y2  second control Point of cubic on y-axis
     * @param x3  end Point of cubic on x-axis
     * @param y3  end Point of cubic on y-axis
     * @return    reference to Path
     */
    public Path cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        Native.onNativeCall();
        nCubicTo(nativeInstance, x1, y1, x2, y2, x3, y3);
        return this;
    }

    /**
     * <p>Adds cubic from last point towards Point p1, then towards Point p2, ending at
     * Point p3. If Path is empty, or last {@link Verb} is {@link Verb#CLOSE}, last point is set to
     * (0, 0) before adding cubic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array, if needed;
     * then appends {@link Verb#CUBIC} to verb array; and Point p1, p2, p3
     * to Point array.</p>
     *
     * @param p1  first control Point of cubic
     * @param p2  second control Point of cubic
     * @param p3  end Point of cubic
     * @return    reference to Path
     */
    public Path cubicTo(Point p1, Point p2, Point p3) {
        return cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    /**
     * <p>Adds cubic from last point towards vector (dx1, dy1), then towards
     * vector (dx2, dy2), to vector (dx3, dy3).
     * If Path is empty, or last {@link Verb}
     * is {@link Verb#CLOSE}, last point is set to (0, 0) before adding cubic.</p>
     *
     * <p>Appends {@link Verb#MOVE} to verb array and (0, 0) to Point array,
     * if needed; then appends {@link Verb#CUBIC} to verb array; and appends cubic
     * control and cubic end to Point array.</p>
     *
     * <p>Cubic control is last point plus vector (dx1, dy1).</p>
     *
     * <p>Cubic end is last point plus vector (dx2, dy2).</p>
     *
     * <p>Function name stands for "relative cubic to".</p>
     *
     * @param dx1  offset from last point to first cubic control on x-axis
     * @param dy1  offset from last point to first cubic control on y-axis
     * @param dx2  offset from last point to second cubic control on x-axis
     * @param dy2  offset from last point to second cubic control on y-axis
     * @param dx3  offset from last point to cubic end on x-axis
     * @param dy3  offset from last point to cubic end on y-axis
     * @return    reference to Path
     */
    public Path rCubicTo(float dx1, float dy1, float dx2, float dy2, float dx3, float dy3) {
        Native.onNativeCall();
        nRCubicTo(nativeInstance, dx1, dy1, dx2, dy2, dx3, dy3);
        return this;
    }

    /**
     * <p>Appends arc to Path. Arc added is part of ellipse
     * bounded by oval, from startAngle through sweepAngle. Both startAngle and
     * sweepAngle are measured in degrees, where zero degrees is aligned with the
     * positive x-axis, and positive sweeps extends arc clockwise.</p>
     *
     * <p>arcTo() adds line connecting Path last Point to initial arc Point if forceMoveTo
     * is false and Path is not empty. Otherwise, added contour begins with first point
     * of arc. Angles greater than -360 and less than 360 are treated modulo 360.</p>
     *
     * @param oval         bounds of ellipse containing arc
     * @param startAngle   starting angle of arc in degrees
     * @param sweepAngle   sweep, in degrees. Positive is clockwise; treated modulo 360
     * @param forceMoveTo  true to start a new contour with arc
     * @return             reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_arcTo">https://fiddle.skia.org/c/@Path_arcTo</a>
     */
    public Path arcTo(Rect oval, float startAngle, float sweepAngle, boolean forceMoveTo) {
        Native.onNativeCall(); 
        nArcTo(nativeInstance, oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, forceMoveTo);
        return this;
    }

    /** 
     * <p>Appends arc to Path, after appending line if needed. Arc is implemented by conic
     * weighted to describe part of circle. Arc is contained by tangent from
     * last Path point to (x1, y1), and tangent from (x1, y1) to (x2, y2). Arc
     * is part of circle sized to radius, positioned so it touches both tangent lines.</p>
     *
     * <p>If last Path Point does not start Arc, tangentArcTo appends connecting Line to Path.
     * The length of Vector from (x1, y1) to (x2, y2) does not affect Arc.</p>
     *
     * <p>Arc sweep is always less than 180 degrees. If radius is zero, or if
     * tangents are nearly parallel, tangentArcTo appends Line from last Path Point to (x1, y1).</p>
     *
     * <p>tangentArcTo appends at most one Line and one conic.</p>
     *
     * <p>tangentArcTo implements the functionality of PostScript arct and HTML Canvas tangentArcTo.</p>
     *
     * @param x1      x-axis value common to pair of tangents
     * @param y1      y-axis value common to pair of tangents
     * @param x2      x-axis value end of second tangent
     * @param y2      y-axis value end of second tangent
     * @param radius  distance from arc to circle center
     * @return        reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_arcTo_2_a">https://fiddle.skia.org/c/@Path_arcTo_2_a</a>
     * @see <a href="https://fiddle.skia.org/c/@Path_arcTo_2_b">https://fiddle.skia.org/c/@Path_arcTo_2_b</a>
     * @see <a href="https://fiddle.skia.org/c/@Path_arcTo_2_c">https://fiddle.skia.org/c/@Path_arcTo_2_c</a>
     */
    public Path tangentArcTo(float x1, float y1, float x2, float y2, float radius) {
        Native.onNativeCall();
        nTangentArcTo(nativeInstance, x1, y1, x2, y2, radius);
        return this;
    }
        
    /**
     * <p>Appends arc to Path, after appending line if needed. Arc is implemented by conic
     * weighted to describe part of circle. Arc is contained by tangent from
     * last Path point to p1, and tangent from p1 to p2. Arc
     * is part of circle sized to radius, positioned so it touches both tangent lines.</p>
     *
     * <p>If last Path Point does not start arc, tangentArcTo() appends connecting line to Path.
     * The length of vector from p1 to p2 does not affect arc.</p>
     *
     * <p>Arc sweep is always less than 180 degrees. If radius is zero, or if
     * tangents are nearly parallel, tangentArcTo() appends line from last Path Point to p1.</p>
     *
     * <p>tangentArcTo() appends at most one line and one conic.</p>
     * 
     * <p>tangentArcTo() implements the functionality of PostScript arct and HTML Canvas tangentArcTo.</p>
     *
     * @param p1      Point common to pair of tangents
     * @param p2      end of second tangent
     * @param radius  distance from arc to circle center
     * @return        reference to Path
     */
    public Path tangentArcTo(Point p1, Point p2, float radius) {
        return tangentArcTo(p1.x, p1.y, p2.x, p2.y, radius);
    }

    /** <p>Appends arc to Path. Arc is implemented by one or more conics weighted to
     * describe part of oval with radii (rx, ry) rotated by xAxisRotate degrees. Arc
     * curves from last Path Point to (x, y), choosing one of four possible routes:
     * clockwise or counterclockwise, and smaller or larger.</p>
     *
     * <p>Arc sweep is always less than 360 degrees. ellipticalArcTo() appends line to (x, y) if
     * either radii are zero, or if last Path Point equals (x, y). ellipticalArcTo() scales radii
     * (rx, ry) to fit last Path Point and (x, y) if both are greater than zero but
     * too small.</p>
     *
     * <p>ellipticalArcTo() appends up to four conic curves.</p>
     *
     * <p>ellipticalArcTo() implements the functionality of SVG arc, although SVG sweep-flag value
     * is opposite the integer value of sweep; SVG sweep-flag uses 1 for clockwise,
     * while {@link Direction#CLOCKWISE} cast to int is zero.</p>
     *
     * @param rx           radius on x-axis before x-axis rotation
     * @param ry           radius on y-axis before x-axis rotation
     * @param xAxisRotate  x-axis rotation in degrees; positive values are clockwise
     * @param size         chooses smaller or larger arc
     * @param direction    chooses clockwise or counterclockwise arc
     * @param x            end of arc
     * @param y            end of arc
     * @return             reference to Path
     */
    public Path ellipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float x, float y) {
        Native.onNativeCall(); 
        nEllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), x, y);
        return this;
    }

    /**
     * <p>Appends arc to Path. Arc is implemented by one or more conic weighted to describe
     * part of oval with radii (r.fX, r.fY) rotated by xAxisRotate degrees. Arc curves
     * from last Path Point to (xy.fX, xy.fY), choosing one of four possible routes:
     * clockwise or counterclockwise, and smaller or larger.</p>
     *
     * <p>Arc sweep is always less than 360 degrees. ellipticalArcTo() appends line to xy if either
     * radii are zero, or if last Path Point equals (xy.fX, xy.fY). ellipticalArcTo() scales radii r to
     * fit last Path Point and xy if both are greater than zero but too small to describe
     * an arc.</p>
     *
     * <p>ellipticalArcTo() appends up to four conic curves.</p>
     *
     * <p>ellipticalArcTo() implements the functionality of SVG arc, although SVG sweep-flag value is
     * opposite the integer value of sweep; SVG sweep-flag uses 1 for clockwise, while
     * {@link Direction#CLOCKWISE} cast to int is zero.</p>
     *
     * @param r            radii on axes before x-axis rotation
     * @param xAxisRotate  x-axis rotation in degrees; positive values are clockwise
     * @param size         chooses smaller or larger arc
     * @param direction    chooses clockwise or counterclockwise arc
     * @param xy           end of arc
     * @return             reference to Path
     */
    public Path ellipticalArcTo(Point r, float xAxisRotate, ArcSize size, Direction direction, Point xy) {
        return ellipticalArcTo(r.x, r.y, xAxisRotate, size, direction, xy.x, xy.y);
    }

    /**
     * <p>Appends arc to Path, relative to last Path Point. Arc is implemented by one or
     * more conic, weighted to describe part of oval with radii (rx, ry) rotated by
     * xAxisRotate degrees. Arc curves from last Path Point to relative end Point:
     * (dx, dy), choosing one of four possible routes: clockwise or
     * counterclockwise, and smaller or larger. If Path is empty, the start arc Point
     * is (0, 0).</p>
     *
     * <p>Arc sweep is always less than 360 degrees. rEllipticalArcTo() appends line to end Point
     * if either radii are zero, or if last Path Point equals end Point.
     * rEllipticalArcTo() scales radii (rx, ry) to fit last Path Point and end Point if both are
     * greater than zero but too small to describe an arc.</p>
     *
     * <p>rEllipticalArcTo() appends up to four conic curves.</p>
     *
     * <p>rEllipticalArcTo() implements the functionality of svg arc, although SVG "sweep-flag" value is
     * opposite the integer value of sweep; SVG "sweep-flag" uses 1 for clockwise, while
     * {@link Direction#CLOCKWISE} cast to int is zero.</p>
     *
     * @param rx           radius before x-axis rotation
     * @param ry           radius before x-axis rotation
     * @param xAxisRotate  x-axis rotation in degrees; positive values are clockwise
     * @param size         chooses smaller or larger arc
     * @param direction    chooses clockwise or counterclockwise arc
     * @param dx           x-axis offset end of arc from last Path Point
     * @param dy           y-axis offset end of arc from last Path Point
     * @return             reference to Path
     */
    public Path rEllipticalArcTo(float rx, float ry, float xAxisRotate, ArcSize size, Direction direction, float dx, float dy) {
        Native.onNativeCall(); 
        nREllipticalArcTo(nativeInstance, rx, ry, xAxisRotate, size.ordinal(), direction.ordinal(), dx, dy);
        return this;
    }

    /**
     * <p>Appends {@link Verb#CLOSE} to Path. A closed contour connects the first and last Point
     * with line, forming a continuous loop. Open and closed contour draw the same
     * with {@link Paint.Style#FILL}. With {@link Paint.Style#STROKE}, open contour draws
     * {@link Paint.Cap} at contour start and end; closed contour draws
     * {@link Paint.Join} at contour start and end.</p>
     *
     * <p>closePath() has no effect if Path is empty or last Path {@link Verb} is {@link Verb#CLOSE}.</p>
     *
     * @return  reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_close">https://fiddle.skia.org/c/@Path_close</a>
     */
    public Path closePath() {
        Native.onNativeCall();
        nClosePath(nativeInstance);
        return this;
    }

    /**
     * <p>Approximates conic with quad array. Conic is constructed from start Point p0,
     * control Point p1, end Point p2, and weight w.</p>
     * 
     * <p>Quad array is stored in pts; this storage is supplied by caller.</p>
     *
     * <p>Maximum quad count is 2 to the pow2.</p>
     *
     * <p>Every third point in array shares last Point of previous quad and first Point of
     * next quad. Maximum pts storage size is given by: {@code (1 + 2 * (1 << pow2)).</p>}</p>
     *
     * <p>Returns quad count used the approximation, which may be smaller
     * than the number requested.</p>
     *
     * <p>conic weight determines the amount of influence conic control point has on the curve.</p>
     *
     * <p>w less than one represents an elliptical section. w greater than one represents
     * a hyperbolic section. w equal to one represents a parabolic section.</p>
     *
     * <p>Two quad curves are sufficient to approximate an elliptical conic with a sweep
     * of up to 90 degrees; in this case, set pow2 to one.</p>
     *
     * @param p0    conic start Point
     * @param p1    conic control Point
     * @param p2    conic end Point
     * @param w     conic weight
     * @param pow2  quad count, as power of two, normally 0 to 5 (1 to 32 quad curves)
     * @return      number of quad curves written to pts
     */
    public static Point[] convertConicToQuads(Point p0, Point p1, Point p2, float w, int pow2) {
        Native.onNativeCall();
        return nConvertConicToQuads(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y, w, pow2);
    }

    /**
     * <p>Returns Rect if Path is equivalent to Rect when filled.</p>
     *
     * rect may be smaller than the Path bounds. Path bounds may include {@link Verb#MOVE} points
     * that do not alter the area drawn by the returned rect.
     *
     * @return  bounds if Path contains Rect, null otherwise
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_isRect">https://fiddle.skia.org/c/@Path_isRect</a>
     */
    public Rect isRect() {
        Native.onNativeCall();
        return nIsRect(nativeInstance);
    }

    /**
     * Adds Rect to Path, appending {@link Verb#MOVE}, three {@link Verb#LINE}, and {@link Verb#CLOSE},
     * starting with top-left corner of Rect; followed by top-right, bottom-right,
     * and bottom-left.
     *
     * @param rect  Rect to add as a closed contour
     * @return      reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRect">https://fiddle.skia.org/c/@Path_addRect</a>
     */
    public Path addRect(Rect rect) {
        return addRect(rect, Direction.CLOCKWISE, 0);
    }

    /**
     * Adds Rect to Path, appending {@link Verb#MOVE}, three {@link Verb#LINE}, and {@link Verb#CLOSE},
     * starting with top-left corner of Rect; followed by top-right, bottom-right,
     * and bottom-left if dir is {@link Direction#CLOCKWISE}; or followed by bottom-left,
     * bottom-right, and top-right if dir is {@link Direction#COUNTER_CLOCKWISE}.
     *
     * @param rect  Rect to add as a closed contour
     * @param dir   Direction to wind added contour
     * @return      reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRect">https://fiddle.skia.org/c/@Path_addRect</a>
     */
    public Path addRect(Rect rect, Direction dir) {
        return addRect(rect, dir, 0);
    }

    /**
     * Adds Rect to Path, appending {@link Verb#MOVE}, three {@link Verb#LINE}, and {@link Verb#CLOSE}.
     * If dir is {@link Direction#CLOCKWISE}, Rect corners are added clockwise; if dir is
     * {@link Direction#COUNTER_CLOCKWISE}, Rect corners are added counterclockwise.
     * start determines the first corner added.
     *
     * @param rect   Rect to add as a closed contour
     * @param dir    Direction to wind added contour
     * @param start  initial corner of Rect to add. 0 for top left, 1 for top right, 2 for lower right, 3 for lower left
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRect_2">https://fiddle.skia.org/c/@Path_addRect_2</a>
     */
    public Path addRect(Rect rect, Direction dir, int start) {
        Native.onNativeCall();
        nAddRect(nativeInstance, rect.left, rect.top, rect.right, rect.bottom, dir.ordinal(), start);
        return this;
    }

    /**
     * <p>Adds oval to path, appending {@link Verb#MOVE}, four {@link Verb#CONIC}, and {@link Verb#CLOSE}.</p>
     *
     * <p>Oval is upright ellipse bounded by Rect oval with radii equal to half oval width
     * and half oval height. Oval begins at (oval.fRight, oval.centerY()) and continues
     * clockwise.</p>
     *
     * @param oval  bounds of ellipse added
     * @return      reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addOval">https://fiddle.skia.org/c/@Path_addOval</a>
     */

    public Path addOval(Rect oval) {
        return addOval(oval, Direction.CLOCKWISE, 1);
    }

    /**
     * <p>Adds oval to path, appending {@link Verb#MOVE}, four {@link Verb#CONIC}, and {@link Verb#CLOSE}.</p>
     *
     * <p>Oval is upright ellipse bounded by Rect oval with radii equal to half oval width
     * and half oval height. Oval begins at (oval.fRight, oval.centerY()) and continues
     * clockwise if dir is {@link Direction#CLOCKWISE}, counterclockwise if dir is {@link Direction#COUNTER_CLOCKWISE}.</p>
     *
     * @param oval  bounds of ellipse added
     * @param dir   Direction to wind ellipse
     * @return      reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addOval">https://fiddle.skia.org/c/@Path_addOval</a>
     */
    public Path addOval(Rect oval, Direction dir) {
        return addOval(oval, dir, 1);
    }

    /**
     * Adds oval to Path, appending {@link Verb#MOVE}, four {@link Verb#CONIC}, and {@link Verb#CLOSE}.
     * Oval is upright ellipse bounded by Rect oval with radii equal to half oval width
     * and half oval height. Oval begins at start and continues
     * clockwise if dir is {@link Direction#CLOCKWISE}, counterclockwise if dir is {@link Direction#COUNTER_CLOCKWISE}.
     *
     * @param oval   bounds of ellipse added
     * @param dir    Direction to wind ellipse
     * @param start  index of initial point of ellipse. 0 for top, 1 for right, 2 for bottom, 3 for left
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addOval_2">https://fiddle.skia.org/c/@Path_addOval_2</a>
     */
    public Path addOval(Rect oval, Direction dir, int start) {
        Native.onNativeCall();
        nAddOval(nativeInstance, oval.left, oval.top, oval.right, oval.bottom, dir.ordinal(), start);
        return this;
    }

    /** 
     * <p>Adds circle centered at (x, y) of size radius to Path, appending {@link Verb#MOVE},
     * four {@link Verb#CONIC}, and {@link Verb#CLOSE}. Circle begins at: (x + radius, y)</p>
     *
     * <p>Has no effect if radius is zero or negative.</p>
     *
     * @param x       center of circle
     * @param y       center of circle
     * @param radius  distance from center to edge
     * @return        reference to Path
     */
    public Path addCircle(float x, float y, float radius) {
        return addCircle(x, y, radius, Direction.CLOCKWISE);
    }

    /** 
     * <p>Adds circle centered at (x, y) of size radius to Path, appending {@link Verb#MOVE},
     * four {@link Verb#CONIC}, and {@link Verb#CLOSE}. Circle begins at: (x + radius, y), continuing
     * clockwise if dir is {@link Direction#CLOCKWISE}, and counterclockwise if dir is {@link Direction#COUNTER_CLOCKWISE}.</p>
     *
     * <p>Has no effect if radius is zero or negative.</p>
     *
     * @param x       center of circle
     * @param y       center of circle
     * @param radius  distance from center to edge
     * @param dir     Direction to wind circle
     * @return        reference to Path
     */
    public Path addCircle(float x, float y, float radius, Direction dir) {
        Native.onNativeCall();
        nAddCircle(nativeInstance, x, y, radius, dir.ordinal());
        return this;
    }

    /**
     * <p>Appends arc to Path, as the start of new contour. Arc added is part of ellipse
     * bounded by oval, from startAngle through sweepAngle. Both startAngle and
     * sweepAngle are measured in degrees, where zero degrees is aligned with the
     * positive x-axis, and positive sweeps extends arc clockwise.</p>
     *
     * <p>If sweepAngle &le; -360, or sweepAngle &ge; 360; and startAngle modulo 90 is nearly
     * zero, append oval instead of arc. Otherwise, sweepAngle values are treated
     * modulo 360, and arc may or may not draw depending on numeric rounding.</p>
     *
     * @param oval        bounds of ellipse containing arc
     * @param startAngle  starting angle of arc in degrees
     * @param sweepAngle  sweep, in degrees. Positive is clockwise; treated modulo 360
     * @return            reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addArc">https://fiddle.skia.org/c/@Path_addArc</a>
     */
    public Path addArc(Rect oval, float startAngle, float sweepAngle) {
        Native.onNativeCall();
        nAddArc(nativeInstance, oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle);
        return this;
    }

    /**
     * <p>Adds rrect to Path, creating a new closed contour. RRect starts at top-left of the lower-left corner and
     * winds clockwise.</p>
     *
     * <p>After appending, Path may be empty, or may contain: Rect, Oval, or RoundedRect.</p>
     *
     * @param rrect  bounds and radii of rounded rectangle
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRRect">https://fiddle.skia.org/c/@Path_addRRect</a>
     */
    public Path addRoundedRect(RoundedRect rrect) {
        return addRoundedRect(rrect, Direction.CLOCKWISE, 6);
    }

    /**
     * <p>Adds rrect to Path, creating a new closed contour. If
     * dir is {@link Direction#CLOCKWISE}, rrect starts at top-left of the lower-left corner and
     * winds clockwise. If dir is {@link Direction#COUNTER_CLOCKWISE}, rrect starts at the bottom-left
     * of the upper-left corner and winds counterclockwise.</p>
     *
     * <p>After appending, Path may be empty, or may contain: Rect, Oval, or RoundedRect.</p>
     *
     * @param rrect  bounds and radii of rounded rectangle
     * @param dir    Direction to wind RoundedRect
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRRect">https://fiddle.skia.org/c/@Path_addRRect</a>
     */
    public Path addRoundedRect(RoundedRect rrect, Direction dir) {
        return addRoundedRect(rrect, dir, dir == Direction.CLOCKWISE ? 6 : 7);
    }

    /**
     * <p>Adds rrect to Path, creating a new closed contour. If dir is {@link Direction#CLOCKWISE}, rrect
     * winds clockwise; if dir is {@link Direction#COUNTER_CLOCKWISE}, rrect winds counterclockwise.
     * start determines the first point of rrect to add.</p>
     *
     * @param rrect  bounds and radii of rounded rectangle
     * @param dir    Direction to wind RoundedRect
     * @param start  index of initial point of RoundedRect. 0 for top-right end of the arc at top left,
     *               1 for top-left end of the arc at top right, 2 for bottom-right end of top right arc, etc.
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addRRect_2">https://fiddle.skia.org/c/@Path_addRRect_2</a>
     */
    public Path addRoundedRect(RoundedRect rrect, Direction dir, int start) {
        Native.onNativeCall();
        nAddRoundedRect(nativeInstance, rrect.left, rrect.top, rrect.right, rrect.bottom, rrect.radii, dir.ordinal(), start);
        return this;
    }

    /**
     * <p>Adds contour created from line array, adding (pts.length - 1) line segments.
     * Contour added starts at pts[0], then adds a line for every additional Point
     * in pts array. If close is true, appends {@link Verb#CLOSE} to Path, connecting
     * pts[pts.length - 1] and pts[0].</p>
     *
     * <p>If pts is empty, append {@link Verb#MOVE} to path.</p>
     *
     * @param pts    array of line sharing end and start Point
     * @param close  true to add line connecting contour end and start
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addPoly">https://fiddle.skia.org/c/@Path_addPoly</a>
     */
    public Path addPoly(Point[] pts, boolean close) {
        float[] flat = new float[pts.length * 2];
        for (int i = 0; i < pts.length; ++i) {
            flat[i * 2] = pts[i].x;
            flat[i * 2 + 1] = pts[i].y;
        }
        return addPoly(flat, close);
    }

    /**
     * <p>Adds contour created from line array, adding (pts.length / 2 - 1) line segments.
     * Contour added starts at (pts[0], pts[1]), then adds a line for every additional pair of floats
     * in pts array. If close is true, appends {@link Verb#CLOSE} to Path, connecting
     * (pts[count - 2], pts[count - 1]) and (pts[0], pts[1]).</p>
     *
     * <p>If pts is empty, append {@link Verb#MOVE} to path.</p>
     *
     * @param pts    flat array of line sharing end and start Point
     * @param close  true to add line connecting contour end and start
     * @return       reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_addPoly">https://fiddle.skia.org/c/@Path_addPoly</a>
     */
    public Path addPoly(float[] pts, boolean close) {
        assert pts.length % 2 == 0 : "Expected even amount of pts, got " + pts.length;
        Native.onNativeCall();
        nAddPoly(nativeInstance, pts, close);
        return this;
    }

    /**
     * <p>Appends src to Path.</p>
     *
     * <p>src verb array, Point array, and conic weights are
     * added unaltered.</p>
     *
     * @param src  Path verbs, Point, and conic weights to add
     * @return     reference to Path
     */
    public Path addPath(Path src) {
        return addPath(src, false);
    }

    /**
     * <p>Appends src to Path.</p>
     *
     * <p>If extend is false, src verb array, Point array, and conic weights are
     * added unaltered. If extend is true, add line before appending
     * verbs, Point, and conic weights.</p>
     *
     * @param src     Path verbs, Point, and conic weights to add
     * @param extend  if should add a line before appending verbs
     * @return        reference to Path
     */
    public Path addPath(Path src, boolean extend) {
        Native.onNativeCall();
        nAddPath(nativeInstance, Native.pointer(src), extend);
        return this;
    }

    /**
     * <p>Appends src to Path, offset by (dx, dy).</p>
     *
     * <p>Src verb array, Point array, and conic weights are
     * added unaltered.</p>
     *
     * @param src     Path verbs, Point, and conic weights to add
     * @param dx      offset added to src Point array x-axis coordinates
     * @param dy      offset added to src Point array y-axis coordinates
     * @return        reference to Path
     */
    public Path addPath(Path src, float dx, float dy) {
        return addPath(src, dx, dy, false);
    }

    /**
     * <p>Appends src to Path, offset by (dx, dy).</p>
     *
     * <p>If extend is false, src verb array, Point array, and conic weights are
     * added unaltered. If extend is true, add line before appending
     * verbs, Point, and conic weights.</p>
     *
     * @param src     Path verbs, Point, and conic weights to add
     * @param dx      offset added to src Point array x-axis coordinates
     * @param dy      offset added to src Point array y-axis coordinates
     * @param extend  if should add a line before appending verbs
     * @return        reference to Path
     */
    public Path addPath(Path src, float dx, float dy, boolean extend) {
        Native.onNativeCall();
        nAddPathOffset(nativeInstance, Native.pointer(src), dx, dy, extend);
        return this;
    }

    /**
     * <p>Appends src to Path, transformed by matrix. Transformed curves may have different
     * verbs, Point, and conic weights.</p>
     *
     * <p>Src verb array, Point array, and conic weights are
     * added unaltered.</p>
     *
     * @param src     Path verbs, Point, and conic weights to add
     * @param matrix  transform applied to src
     * @return        reference to Path
     */
    public Path addPath(Path src, float[] matrix) {
        return addPath(src, matrix, false);
    }

    /**
     * <p>Appends src to Path, transformed by matrix. Transformed curves may have different
     * verbs, Point, and conic weights.</p>
     *
     * <p>If extend is false, src verb array, Point array, and conic weights are
     * added unaltered. If extend is true, add line before appending
     * verbs, Point, and conic weights.</p>
     *
     * @param src     Path verbs, Point, and conic weights to add
     * @param matrix  transform applied to src
     * @param extend  if should add a line before appending verbs
     * @return        reference to Path
     */
    public Path addPath(Path src, float[] matrix, boolean extend) {
        Native.onNativeCall();
        nAddPathTransform(nativeInstance, Native.pointer(src), matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8], extend);
        return this;
    }

    /** 
     * Appends src to Path, from back to front.
     * Reversed src always appends a new contour to Path.
     *
     * @param src  Path verbs, Point, and conic weights to add
     * @return     reference to Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_reverseAddPath">https://fiddle.skia.org/c/@Path_reverseAddPath</a>
     */
    public Path reverseAddPath(Path src) {
        Native.onNativeCall();
        nReverseAddPath(nativeInstance, Native.pointer(src));
        return this;
    }

    /**
     * Offsets Point array by (dx, dy). Path is replaced by offset data.
     *
     * @param dx  offset added to Point array x-axis coordinates
     * @param dy  offset added to Point array y-axis coordinates
     * @return    this
     */
    public Path offset(float dx, float dy) {
        return offset(dx, dy, null);
    }

    /** 
     * Offsets Point array by (dx, dy). Offset Path replaces dst.
     * If dst is null, Path is replaced by offset data.
     *
     * @param dx   offset added to Point array x-axis coordinates
     * @param dy   offset added to Point array y-axis coordinates
     * @param dst  overwritten, translated copy of Path; may be null
     * @return     this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_offset">https://fiddle.skia.org/c/@Path_offset</a>
     */
    public Path offset(float dx, float dy, Path dst) {
        Native.onNativeCall();
        nOffset(nativeInstance, dx, dy, Native.pointer(dst));
        return this;
    }

    /**
     * Transforms verb array, Point array, and weight by matrix.
     * transform may change verbs and increase their number.
     * Path is replaced by transformed data.
     *
     * @return  this
     *
     * @param matrix  float[9] to apply to Path
     */
    public Path transform(float[] matrix) {
        return transform(matrix, null, true);
    }

    /**
     * Transforms verb array, Point array, and weight by matrix.
     * transform may change verbs and increase their number.
     * Path is replaced by transformed data.
     *
     * @param matrix                float[9] to apply to Path
     * @param applyPerspectiveClip  whether to apply perspective clipping
     * @return                      this
     */
    public Path transform(float[] matrix, boolean applyPerspectiveClip) {
        return transform(matrix, null, applyPerspectiveClip);
    }

    /**
     * Transforms verb array, Point array, and weight by matrix.
     * transform may change verbs and increase their number.
     * Transformed Path replaces dst; if dst is null, original data
     * is replaced.
     *
     * @param matrix  float[9] to apply to Path
     * @param dst     overwritten, transformed copy of Path; may be null
     * @return        this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_transform">https://fiddle.skia.org/c/@Path_transform</a>
     */
    public Path transform(float[] matrix, Path dst) {
        return transform(matrix, dst, true);
    }

    /**
     * Transforms verb array, Point array, and weight by matrix.
     * transform may change verbs and increase their number.
     * Transformed Path replaces dst; if dst is null, original data
     * is replaced.
     *
     * @param matrix                float[9] to apply to Path
     * @param dst                   overwritten, transformed copy of Path; may be null
     * @param applyPerspectiveClip  whether to apply perspective clipping
     * @return                      this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_transform">https://fiddle.skia.org/c/@Path_transform</a>
     */
    public Path transform(float[] matrix, Path dst, boolean applyPerspectiveClip) {
        Native.onNativeCall();
        nTransform(nativeInstance, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8], Native.pointer(dst), applyPerspectiveClip);
        return this;
    }

    /**
     * Returns last point on Path in lastPt. Returns null if Point array is empty.
     *
     * @param lastPt  storage for final Point in Point array; may be null
     * @return        point if Point array contains one or more Point, null otherwise
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_getLastPt">https://fiddle.skia.org/c/@Path_getLastPt</a>
     */
    public Point getLastPt() {
        Native.onNativeCall();
        return nGetLastPt(nativeInstance);
    }

    /**
     * Sets last point to (x, y). If Point array is empty, append {@link Verb#MOVE} to
     * verb array and append (x, y) to Point array.
     *
     * @param x  set x-axis value of last point
     * @param y  set y-axis value of last point
     * @return   this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_setLastPt">https://fiddle.skia.org/c/@Path_setLastPt</a>
     */
    public Path setLastPt(float x, float y) {
        Native.onNativeCall();
        nSetLastPt(nativeInstance, x, y);
        return this;
    }

    /**
     * Sets the last point on the path. If Point array is empty, append {@link Verb#MOVE} to
     * verb array and append p to Point array.
     *
     * @param p  set value of last point
     * @return   this
     */
    public Path setLastPt(Point p) {
        return setLastPt(p.x, p.y);
    }

    public static final int SEGMENT_MASK_LINE  = 1 << 0;
    public static final int SEGMENT_MASK_QUAD  = 1 << 1;
    public static final int SEGMENT_MASK_CONIC = 1 << 2;
    public static final int SEGMENT_MASK_CUBIC = 1 << 3;

    /**
     * <p>Returns a mask, where each set bit corresponds to a SegmentMask constant
     * if Path contains one or more verbs of that type.</p>
     *
     * <p>Returns zero if Path contains no lines, or curves: quads, conics, or cubics.</p>
     *
     * <p>getSegmentMasks() returns a cached result; it is very fast.</p>
     *
     * @return  SegmentMask bits or zero
     *
     * @see {@link SEGMENT_MASK_LINE}
     * @see {@link SEGMENT_MASK_QUAD}
     * @see {@link SEGMENT_MASK_CONIC}
     * @see {@link SEGMENT_MASK_CUBIC}
     */
    public int getSegmentMasks() {
        Native.onNativeCall();
        return nGetSegmentMasks(nativeInstance);
    }

    public static class Segment {
        public Verb    verb;
        public Point   p0;
        public Point   p1;
        public Point   p2;
        public Point   p3;
        public float   conicWeight;
        public boolean isCloseLine;
        public boolean isClosedContour;

        public Segment(int verbOrdinal) {
            verb = Verb.values()[verbOrdinal];
        }

        @Override
        public String toString() {
            return "Segment{" +
                    "verb=" + verb +
                    (verb != Verb.DONE ? ", p0=" + p0 : "") +
                    (verb == Verb.LINE || verb == Verb.QUAD || verb == Verb.CONIC || verb == Verb.CUBIC ? ", p1=" + p1 : "") +
                    (verb == Verb.QUAD || verb == Verb.CONIC || verb == Verb.CUBIC ? ", p2=" + p2 : "") +
                    (verb == Verb.CUBIC ? ", p3=" + p3 : "") +
                    (verb == Verb.CONIC ? ", conicWeight=" + conicWeight : "") +
                    (verb == Verb.LINE  ? ", isCloseLine=" + isCloseLine : "") +
                    (verb != Verb.DONE ? ", isClosedContour=" + isClosedContour : "") +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Segment segment = (Segment) o;
            return verb == segment.verb &&
                   (verb != Verb.DONE ? Objects.equals(p0, segment.p0) : true) &&
                   (verb == Verb.LINE || verb == Verb.QUAD || verb == Verb.CONIC || verb == Verb.CUBIC ? Objects.equals(p1, segment.p1) : true) &&
                   (verb == Verb.QUAD || verb == Verb.CONIC || verb == Verb.CUBIC ? Objects.equals(p2, segment.p2) : true) &&
                   (verb == Verb.CUBIC ? Objects.equals(p3, segment.p3) : true) &&
                   (verb == Verb.CONIC ? Float.compare(segment.conicWeight, conicWeight) == 0 : true) &&
                   (verb == Verb.LINE  ? isCloseLine == segment.isCloseLine : true) &&
                   (verb != Verb.DONE ? isClosedContour == segment.isClosedContour : true);
        }

        @Override
        public int hashCode() {
            switch (verb) {
                case DONE:
                    return Objects.hash(verb);
                case MOVE:
                    return Objects.hash(verb, p0, isClosedContour);
                case LINE:
                    return Objects.hash(verb, p0, p1, isCloseLine, isClosedContour);
                case QUAD:
                    return Objects.hash(verb, p0, p1, p2, isClosedContour);
                case CONIC:
                    return Objects.hash(verb, p0, p1, p2, conicWeight, isClosedContour);
                case CUBIC:    
                    return Objects.hash(verb, p0, p1, p2, p3, isClosedContour);
                default:
                    throw new RuntimeException("Unreachable");
            }
        }
    }

    public static class Iter extends Managed implements Iterator<Segment> {
        protected Path path;
        protected Segment nextSegment;

        @Override
        public Segment next() {
            if (nextSegment.verb == Verb.DONE)
                throw new NoSuchElementException();
            Segment res = nextSegment;
            nextSegment = nNext(nativeInstance);
            return res;
        }

        @Override
        public boolean hasNext() {
            return nextSegment.verb != Verb.DONE;
        }

        protected Iter(Path path, long ptr) {
            super(ptr, nGetNativeFinalizer());
            this.path = path;
            Native.onNativeCall();
        }

        public static Iter make(Path path, boolean forceClose) {
            long ptr = nInit(Native.pointer(path), forceClose);
            Iter i = new Iter(path, ptr);
            i.nextSegment = nNext(ptr);
            return i;
        }

        private static final  long nativeFinalizer = nGetNativeFinalizer();
        private static native long nInit(long pathPtr, boolean forceClose);
        private static native long nGetNativeFinalizer();
        private static native Segment nNext(long ptr);
    } 

    @Override
    public Iter iterator() {
        return iterator(false);
    }

    public Iter iterator(boolean forceClose) {
        return Iter.make(this, forceClose);
    }

    /**
     * Returns true if the point (x, y) is contained by Path, taking into
     * account {@link FillType}.
     *
     * @param x  x-axis value of containment test
     * @param y  y-axis value of containment test
     * @return   true if Point is in Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_contains">https://fiddle.skia.org/c/@Path_contains</a>
     */
    public boolean contains(float x, float y) {
        Native.onNativeCall();
        return nContains(nativeInstance, x, y);
    }

    /**
     * Returns true if the point is contained by Path, taking into
     * account {@link FillType}.
     *
     * @param p  point of containment test
     * @return   true if Point is in Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_contains">https://fiddle.skia.org/c/@Path_contains</a>
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Writes text representation of Path to standard output. The representation may be
     * directly compiled as C++ code. Floating point values are written
     * with limited precision; it may not be possible to reconstruct original Path
     * from output.
     *
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_dump_2">https://fiddle.skia.org/c/@Path_dump_2</a>
     */
    public Path dump() {
        Native.onNativeCall();
        nDump(nativeInstance);
        return this;
    }

    /**
     * <p>Writes text representation of Path to standard output. The representation may be
     * directly compiled as C++ code. Floating point values are written
     * in hexadecimal to preserve their exact bit pattern. The output reconstructs the
     * original Path.</p>
     *
     * <p>Use instead of {@link dump()} when submitting</p>
     *
     * @return  this
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_dumpHex">https://fiddle.skia.org/c/@Path_dumpHex</a>
     */
    public Path dumpHex() {
        Native.onNativeCall();
        nDumpHex(nativeInstance);
        return this;
    }

    /**
     * <p>Writes Path to byte buffer.</p>
     *
     * <p>Writes {@link FillType}, verb array, Point array, conic weight, and
     * additionally writes computed information like {@link ConvexityType} and bounds.</p>
     *
     * <p>Use only be used in concert with {@link readFromMemory(byte[])};
     * the format used for Path in memory is not guaranteed.</p>
     *
     * @return  serialized Path; length always a multiple of 4
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_writeToMemory">https://fiddle.skia.org/c/@Path_writeToMemory</a>
     */
    public byte[] writeToMemory() {
        Native.onNativeCall();
        return nWriteToMemory(nativeInstance);
    }

    /**
     * <p>Initializes Path from byte buffer. Returns null if the buffer is
     * data is inconsistent, or the length is too small.</p>
     *
     * <p>Reads {@link FillType}, verb array, Point array, conic weight, and
     * additionally reads computed information like {@link ConvexityType} and bounds.</p>
     *
     * <p>Used only in concert with {@link writeToMemory()};
     * the format used for Path in memory is not guaranteed.</p>
     *
     * @param data  storage for Path
     * @return      reconstructed Path
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_readFromMemory">https://fiddle.skia.org/c/@Path_readFromMemory</a>
     */
    public static Path readFromMemory(byte[] data) {
        Native.onNativeCall();
        return new Path(nReadFromMemory(data));
    }

    /** 
     * <p>Returns a non-zero, globally unique value. A different value is returned
     * if verb array, Point array, or conic weight changes.</p>
     *
     * <p>Setting {@link FillType} does not change generation identifier.</p>
     *
     * <p>Each time the path is modified, a different generation identifier will be returned.
     * {@link FillType} does affect generation identifier on Android framework.</p>
     *
     * @return  non-zero, globally unique value
     *
     * @see <a href="https://fiddle.skia.org/c/@Path_getGenerationID">https://fiddle.skia.org/c/@Path_getGenerationID</a>
     * @see Skia bug 1762
     */
    public int nGetGenerationID() {
        Native.onNativeCall();
        return nGetGenerationID(nativeInstance);
    }    

    /**
     * Returns if Path data is consistent. Corrupt Path data is detected if
     * internal values are out of range or internal storage does not match
     * array dimensions.
     *
     * @return  true if Path data is consistent
     */
    public boolean isValid() {
        Native.onNativeCall();
        return nIsValid(nativeInstance);
    }

    protected Path(long ptr) { super(ptr, nativeFinalizer); Native.onNativeCall(); }
    private static final  long    nativeFinalizer = nGetNativeFinalizer();
    private static native long    nInit();
    private static native long    nGetNativeFinalizer();
    private static native boolean nEquals(long aPtr, long bPtr);
    private static native boolean nIsInterpolatable(long ptr, long comparePtr);
    private static native long    nInterpolate(long ptr, long endingPtr, float weight);
    private static native void    nSetFillType(long ptr, int fillType);
    private static native int     nGetConvexityType(long ptr);
    private static native int     nGetConvexityTypeOrUnknown(long ptr);
    private static native void    nSetConvexityType(long ptr, int convexity);
    private static native Rect    nIsOval(long ptr);
    private static native RoundedRect nIsRoundedRect(long ptr);
    private static native void    nReset(long ptr);
    private static native void    nRewind(long ptr);
    private static native boolean nIsEmpty(long ptr);
    private static native boolean nIsLastContourClosed(long ptr);
    private static native boolean nIsFinite(long ptr);
    private static native void    nSetIsVolatile(long ptr, boolean isVolatile);
    private static native boolean nIsLineDegenerate(float x0, float y0, float x1, float y1, boolean exact);
    private static native boolean nIsQuadDegenerate(float x0, float y0, float x1, float y1, float x2, float y2, boolean exact);
    private static native boolean nIsCubicDegenerate(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean exact);
    private static native Point[] nIsLine(long ptr);
    private static native int     nCountPoints(long ptr);
    private static native Point   nGetPoint(long ptr, int index);
    private static native int     nGetPoints(long ptr, Point[] points, int max);
    private static native int     nCountVerbs(long ptr);
    private static native int     nGetVerbs(long ptr, byte[] verbs, int max);
    private static native long    nApproximateBytesUsed(long ptr);
    private static native void    nSwap(long ptr, long otherPtr);

    private static native Rect    nGetBounds(long ptr);
    private static native void    nUpdateBoundsCache(long ptr);
    private static native Rect    nComputeTightBounds(long ptr);
    private static native boolean nConservativelyContainsRect(long ptr, float l, float t, float r, float b);
    private static native void    nIncReserve(long ptr, int extraPtCount);
    private static native void    nShrinkToFit(long ptr);
    private static native void    nMoveTo(long ptr, float x, float y);
    private static native void    nRMoveTo(long ptr, float dx, float dy);
    private static native void    nLineTo(long ptr, float x, float y);
    private static native void    nRLineTo(long ptr, float dx, float dy);
    private static native void    nQuadTo(long ptr, float x1, float y1, float x2, float y2);
    private static native void    nRQuadTo(long ptr, float dx1, float dy1, float dx2, float dy2);
    private static native void    nConicTo(long ptr, float x1, float y1, float x2, float y2, float w);
    private static native void    nRConicTo(long ptr, float dx1, float dy1, float dx2, float dy2, float w);
    private static native void    nCubicTo(long ptr, float x1, float y1, float x2, float y2, float x3, float y3);
    private static native void    nRCubicTo(long ptr, float dx1, float dy1, float dx2, float dy2, float dx3, float dy3);
    private static native void    nArcTo(long ptr, float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo);
    private static native void    nTangentArcTo(long ptr, float x1, float y1, float x2, float y2, float radius);
    private static native void    nEllipticalArcTo(long ptr, float rx, float ry, float xAxisRotate, int size, int direction, float x, float y);
    private static native void    nREllipticalArcTo(long ptr, float rx, float ry, float xAxisRotate, int size, int direction, float dx, float dy);
    private static native void    nClosePath(long ptr);
    private static native Point[] nConvertConicToQuads(float x0, float y0, float x1, float y1, float x2, float y2, float w, int pow2);
    private static native Rect    nIsRect(long ptr);
    private static native void    nAddRect(long ptr, float l, float t, float r, float b, int dir, int start);
    private static native void    nAddOval(long ptr, float l, float t, float r, float b, int dir, int start);
    private static native void    nAddCircle(long ptr, float x, float y, float r, int dir);
    private static native void    nAddArc(long ptr, float l, float t, float r, float b, float startAngle, float sweepAngle);
    private static native void    nAddRoundedRect(long ptr, float l, float t, float r, float b, float[] radii, int dir, int start);
    private static native void    nAddPoly(long ptr, float[] coords, boolean close);
    private static native void    nAddPath(long ptr, long srcPtr, boolean extend);
    private static native void    nAddPathOffset(long ptr, long srcPtr, float dx, float dy, boolean extend);
    private static native void    nAddPathTransform(long ptr, long srcPtr,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2,
        boolean extend);
    private static native void    nReverseAddPath(long ptr, long srcPtr);
    private static native void    nOffset(long ptr, float dx, float dy, long dst);
    private static native void    nTransform(long ptr,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2,
        long dst, boolean applyPerspectiveClip);
    private static native Point   nGetLastPt(long ptr);
    private static native void    nSetLastPt(long ptr, float x, float y);
    private static native int     nGetSegmentMasks(long ptr);
    private static native boolean nContains(long ptr, float x, float y);
    private static native void    nDump(long ptr);
    private static native void    nDumpHex(long ptr);
    private static native byte[]  nWriteToMemory(long ptr);
    private static native long    nReadFromMemory(byte[] data);
    private static native int     nGetGenerationID(long ptr);
    private static native boolean nIsValid(long ptr);
}