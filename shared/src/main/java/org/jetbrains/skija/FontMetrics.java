package org.jetbrains.skija;

import lombok.Data;
import org.jetbrains.annotations.*;

@Data
public class FontMetrics {
    /**
     * greatest extent above origin of any glyph bounding box, typically negative; deprecated with variable fonts
     */
    public final float _top;

    /**
     * distance to reserve above baseline, typically negative
     */
    public final float _ascent;

    /**
     * distance to reserve below baseline, typically positive
     */
    public final float _descent;

    /**
     * greatest extent below origin of any glyph bounding box, typically positive; deprecated with variable fonts
     */
    public final float _bottom;

    /**
     * distance to add between lines, typically positive or zero
     */
    public final float _leading;

    /**
     * average character width, zero if unknown
     */
    public final float _avgCharWidth;

    /**
     * maximum character width, zero if unknown
     */
    public final float _maxCharWidth;

    /**
     * greatest extent to left of origin of any glyph bounding box, typically negative; deprecated with variable fonts
     */
    public final float _xMin;

    /**
     * greatest extent to right of origin of any glyph bounding box, typically positive; deprecated with variable fonts
     */
    public final float _xMax;

    /**
     * height of lower-case 'x', zero if unknown, typically negative
     */
    public final float _xHeight;

    /**
     * height of an upper-case letter, zero if unknown, typically negative
     */
    public final float _capHeight;

    /**
     * underline thickness
     */
    @Nullable
    public final Float _underlineThickness;

    /**
     * distance from baseline to top of stroke, typically positive
     */
    @Nullable
    public final Float _underlinePosition;

    /**
     * strikeout thickness
     */
    @Nullable
    public final Float _strikeoutThickness;

    /**
     * distance from baseline to bottom of stroke, typically negative
     */
    @Nullable
    public final Float _strikeoutPosition;

    public float getHeight() {
        return _descent - _ascent;
    }
}