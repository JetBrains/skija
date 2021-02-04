package org.jetbrains.skija.examples.scenes;

import lombok.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.shaper.*;

@AllArgsConstructor
@lombok.Data
public class DebugTextRun {
    public RunInfo _info;
    public Font    _font;
    public Rect    _bounds;
    public short[] _glyphs;
    public Point[] _positions;
    public int[]   _clusters;
}
