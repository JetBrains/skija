package org.jetbrains.skija;

public enum FilterMode {
    /**
     * single sample point (nearest neighbor)
     */
    NEAREST,

    /**
     * interporate between 2x2 sample points (bilinear interpolation)
     */
    LINEAR
}
