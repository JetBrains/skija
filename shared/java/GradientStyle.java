package org.jetbrains.skija;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.ApiStatus;

@AllArgsConstructor @Data @With
public class GradientStyle {
    @ApiStatus.Internal public static final int _INTERPOLATE_PREMUL = 1;
    public static GradientStyle DEFAULT = new GradientStyle(FilterTileMode.CLAMP, true, null);

    @ApiStatus.Internal public final FilterTileMode _tileMode;
    @ApiStatus.Internal public final boolean _premul;
    @ApiStatus.Internal public final Matrix33 _localMatrix;

    @ApiStatus.Internal
    public int _getFlags() {
        return 0 | (_premul ? _INTERPOLATE_PREMUL : 0);
    }

    @ApiStatus.Internal
    public float[] _getMatrixArray() {
        return _localMatrix == null ? null : _localMatrix.getMat();
    }
}
