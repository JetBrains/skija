package org.jetbrains.skija.paragraph;

import lombok.Data;

@Data 
public class PlaceholderStyle {
    public final float _width;
    public final float _height;
    public final PlaceholderAlignment _alignment;
    public final BaselineMode _baselineMode;

    /**
     * <p>Distance from the top edge of the rect to the baseline position. This
     * baseline will be aligned against the alphabetic baseline of the surrounding
     * text.</p>
     *
     * <p>Positive values drop the baseline lower (positions the rect higher) and
     * small or negative values will cause the rect to be positioned underneath
     * the line. When baseline == height, the bottom edge of the rect will rest on
     * the alphabetic baseline.</p>
     */
    public final float _baseline;
}