package org.jetbrains.skija.paragraph;

import java.util.Objects;

public class LineMetrics {
    private final long    startIndex;
    private final long    endIndex;
    private final long    endExcludingWhitespaces;
    private final long    endIncludingNewline;
    private final boolean hardBreak;
    private final double  ascent;
    private final double  descent;
    private final double  unscaledAscent;
    private final double  height;
    private final double  width;
    private final double  left;
    private final double  baseline;
    private final long    lineNumber;

    /**
     * The index in the text buffer the line begins.
     */
    public long getStartIndex() {
        return startIndex;
    }

    /**
     * The index in the text buffer the line ends.
     */
    public long getEndIndex() {
        return endIndex;
    }

    public long getEndExcludingWhitespaces() {
        return endExcludingWhitespaces;
    }

    public long getEndIncludingNewline() {
        return endIncludingNewline;
    }

    public boolean isHardBreak() {
        return hardBreak;
    }

    /**
     * The final computed ascent for the line. This can be impacted by the strut, height,
     * scaling, as well as outlying runs that are very tall. The top edge is
     * {@code getBaseline() - getAscent()} and the bottom edge is {@code getBaseline() + getDescent()}.
     * Ascent and descent are provided as positive numbers. These values are the cumulative metrics for the entire line.
     */
    public double getAscent() {
        return ascent;
    }

    /**
     * The final computed descent for the line. This can be impacted by the strut, height,
     * scaling, as well as outlying runs that are very tall. The top edge is
     * {@code getBaseline() - getAscent()} and the bottom edge is {@code getBaseline() + getDescent()}.
     * Ascent and descent are provided as positive numbers. These values are the cumulative metrics for the entire line.
     */
    public double getDescent() {
        return descent;
    }

    public double getUnscaledAscent() {
        return unscaledAscent;
    }

    /**
     * Total height of the paragraph including the current line.
     */
    public double getHeight() {
        return height;
    }

    /**
     * The height of the current line, equals to {@code Math.round(getAscent() + getDescent())}.
     */
    public double getLineHeight() {
        return ascent + descent;
    }

    /**
     * Width of the line.
     */
    public double getWidth() {
        return width;
    }

    /**
     * The left edge of the line.
     */
    public double getLeft() {
        return left;
    }

    /**
     * The right edge of the line, equals to {@code getLeft() + getWidth()}
     */
    public double getRight() {
        return left + width;
    }

    /**
     * The y position of the baseline for this line from the top of the paragraph.
     */
    public double getBaseline() {
        return baseline;
    }

    /**
     * Zero indexed line number
     */
    public long getLineNumber() {
        return lineNumber;
    }

    public LineMetrics(long startIndex, long endIndex, long endExcludingWhitespaces, long endIncludingNewline,
                       boolean hardBreak, double ascent, double descent, double unscaledAscent, double height, double width,
                       double left, double baseline, long lineNumber) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.endExcludingWhitespaces = endExcludingWhitespaces;
        this.endIncludingNewline = endIncludingNewline;
        this.hardBreak = hardBreak;
        this.ascent = ascent;
        this.descent = descent;
        this.unscaledAscent = unscaledAscent;
        this.height = height;
        this.width = width;
        this.left = left;
        this.baseline = baseline;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineMetrics that = (LineMetrics) o;
        return startIndex == that.startIndex &&
                endIndex == that.endIndex &&
                endExcludingWhitespaces == that.endExcludingWhitespaces &&
                endIncludingNewline == that.endIncludingNewline &&
                hardBreak == that.hardBreak &&
                Double.compare(that.ascent, ascent) == 0 &&
                Double.compare(that.descent, descent) == 0 &&
                Double.compare(that.unscaledAscent, unscaledAscent) == 0 &&
                Double.compare(that.height, height) == 0 &&
                Double.compare(that.width, width) == 0 &&
                Double.compare(that.left, left) == 0 &&
                Double.compare(that.baseline, baseline) == 0 &&
                lineNumber == that.lineNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, endIndex, endExcludingWhitespaces, endIncludingNewline, hardBreak, ascent, descent, unscaledAscent, height, width, left, baseline, lineNumber);
    }

    @Override
    public String toString() {
        return "LineMetrics{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", endExcludingWhitespaces=" + endExcludingWhitespaces +
                ", endIncludingNewline=" + endIncludingNewline +
                ", hardBreak=" + hardBreak +
                ", ascent=" + ascent +
                ", descent=" + descent +
                ", unscaledAscent=" + unscaledAscent +
                ", height=" + height +
                ", width=" + width +
                ", left=" + left +
                ", baseline=" + baseline +
                ", lineNumber=" + lineNumber +
                '}';
    }
}
