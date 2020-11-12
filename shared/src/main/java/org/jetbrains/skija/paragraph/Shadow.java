package org.jetbrains.skija.paragraph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import org.jetbrains.skija.*;

@AllArgsConstructor @Data @With
public class Shadow {
    public final int    _color;
    public final float  _offsetX;
    public final float  _offsetY;
    public final double _blurRadius;

    public Shadow(int color, Point offset, double blurRadius) {
        this(color, offset.getX(), offset.getY(), blurRadius);
    }

    public Point getOffset() {
        return new Point(_offsetX, _offsetY);
    }
}
