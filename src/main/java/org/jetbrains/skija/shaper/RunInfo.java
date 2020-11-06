package org.jetbrains.skija.shaper;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

@lombok.Data
public class RunInfo implements AutoCloseable {
    public final Font  _font;
    public final int   _bidiLevel;
    public final float _advanceX;
    public final float _advanceY;
    public final long  _glyphCount;
    /** WARN does not work in Shaper.makeCoreText https://bugs.chromium.org/p/skia/issues/detail?id=10899 */
    public final int  _rangeBegin;
    /** WARN does not work in Shaper.makeCoreText https://bugs.chromium.org/p/skia/issues/detail?id=10899 */
    public final int  _rangeSize;

    public RunInfo(long fontPtr, int biDiLevel, float advanceX, float advanceY, long glyphCount, int rangeBegin, int rangeSize) {
        _font = new Font(fontPtr);
        _bidiLevel = biDiLevel;
        _advanceX = advanceX;
        _advanceY = advanceY;
        _glyphCount = glyphCount;
        _rangeBegin = rangeBegin;
        _rangeSize = rangeSize;
    }

    public Point getAdvance() {
        return new Point(_advanceX, _advanceY);
    }

    /** WARN does not work in Shaper.makeCoreText https://bugs.chromium.org/p/skia/issues/detail?id=10899 */
    public int getRangeEnd() {
        return _rangeBegin + _rangeSize;
    }

    @Override
    public void close() {
        _font.close();
    }
}
