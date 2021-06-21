package org.jetbrains.skija.paragraph;

import lombok.Data;

@Data
public class LineMetrics {
    /**
     * The index in the text buffer the line begins.
     */
    public final long    _startIndex;

    /**
     * The index in the text buffer the line ends.
     */
    public final long    _endIndex;
    public final long    _endExcludingWhitespaces;
    public final long    _endIncludingNewline;
    public final boolean _hardBreak;

    /**
     * The final computed ascent for the line. This can be impacted by the strut, height,
     * scaling, as well as outlying runs that are very tall. The top edge is
     * {@code getBaseline() - getAscent()} and the bottom edge is {@code getBaseline() + getDescent()}.
     * Ascent and descent are provided as positive numbers. These values are the cumulative metrics for the entire line.
     */
    public final double  _ascent;

    /**
     * The final computed descent for the line. This can be impacted by the strut, height,
     * scaling, as well as outlying runs that are very tall. The top edge is
     * {@code getBaseline() - getAscent()} and the bottom edge is {@code getBaseline() + getDescent()}.
     * Ascent and descent are provided as positive numbers. These values are the cumulative metrics for the entire line.
     */
    public final double  _descent;
    public final double  _unscaledAscent;

    /**
     * Total height of the paragraph including the current line.
     */
    public final double  _height;

    /**
     * Width of the line.
     */
    public final double  _width;

    /**
     * The left edge of the line.
     */
    public final double  _left;

    /**
     * The y position of the baseline for this line from the top of the paragraph.
     */
    public final double  _baseline;

    /**
     * Zero indexed line number
     */
    public final long    _lineNumber;

    /**
     * The height of the current line, equals to {@code Math.round(getAscent() + getDescent())}.
     */
    public double getLineHeight() {
        return _ascent + _descent;
    }

    /**
     * The right edge of the line, equals to {@code getLeft() + getWidth()}
     */
    public double getRight() {
        return _left + _width;
    }
}
