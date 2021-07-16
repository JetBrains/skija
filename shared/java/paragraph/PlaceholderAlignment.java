package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;

/**
 * Where to vertically align the placeholder relative to the surrounding text.
 */
public enum PlaceholderAlignment {
  /** 
   * Match the baseline of the placeholder with the baseline.
   */
  BASELINE,

  /** 
   * Align the bottom edge of the placeholder with the baseline such that the
   * placeholder sits on top of the baseline.
   */
  ABOVE_BASELINE,

  /** 
   * Align the top edge of the placeholder with the baseline specified in
   * such that the placeholder hangs below the baseline.
   */
  BELOW_BASELINE,

  /** 
   * Align the top edge of the placeholder with the top edge of the font.
   * When the placeholder is very tall, the extra space will hang from
   * the top and extend through the bottom of the line.
   */
  TOP,

  /** 
   * Align the bottom edge of the placeholder with the top edge of the font.
   * When the placeholder is very tall, the extra space will rise from
   * the bottom and extend through the top of the line.
   */
  BOTTOM,

  /** 
   * Align the middle of the placeholder with the middle of the text. When the
   * placeholder is very tall, the extra space will grow equally from
   * the top and bottom of the line.
   */
  MIDDLE;

  @ApiStatus.Internal public static final PlaceholderAlignment[] _values = values();
}