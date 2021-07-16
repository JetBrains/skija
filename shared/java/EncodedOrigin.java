package org.jetbrains.skija;

import org.jetbrains.annotations.*;

public enum EncodedOrigin {
    @ApiStatus.Internal
    _UNUSED,

    /**
     * Default
     */
    TOP_LEFT,

    /**
     * Reflected across y-axis
     */
    TOP_RIGHT,

    /**
     * Rotated 180
     */
    BOTTOM_RIGHT,

    /**
     * Reflected across x-axis
     */
    BOTTOM_LEFT,

    /**
     * Reflected across x-axis, Rotated 90 CCW
     */
    LEFT_TOP,

    /**
     * Rotated 90 CW
     */
    RIGHT_TOP,

    /**
     * Reflected across x-axis, Rotated 90 CW
     */
    RIGHT_BOTTOM,

    /**
     * Rotated 90 CCW
     */
    LEFT_BOTTOM;

    @ApiStatus.Internal public static final EncodedOrigin[] _values = values();

    /**
     * Given an encoded origin and the width and height of the source data, returns a matrix
     * that transforms the source rectangle with upper left corner at [0, 0] and origin to a correctly
     * oriented destination rectangle of [0, 0, w, h].
     */
    public Matrix33 toMatrix(int w, int h) {
        switch (this) {
            case TOP_LEFT:
                return Matrix33.IDENTITY;

            case TOP_RIGHT:
                return new Matrix33(-1,  0, w,  0,  1, 0, 0, 0, 1);

            case BOTTOM_RIGHT:
                return new Matrix33(-1,  0, w,  0, -1, h, 0, 0, 1);

            case BOTTOM_LEFT:
                return new Matrix33( 1,  0, 0,  0, -1, h, 0, 0, 1);

            case LEFT_TOP:
                return new Matrix33( 0,  1, 0,  1,  0, 0, 0, 0, 1);

            case RIGHT_TOP:
                return new Matrix33( 0, -1, w,  1,  0, 0, 0, 0, 1);

            case RIGHT_BOTTOM:
                return new Matrix33( 0, -1, w, -1,  0, h, 0, 0, 1);

            case LEFT_BOTTOM:
                return new Matrix33( 0,  1, 0, -1,  0, h, 0, 0, 1);

            default:
                throw new IllegalArgumentException("Unsupported origin " + this);
        }
    }

    /**
     * Return true if the encoded origin includes a 90 degree rotation, in which case the width
     * and height of the source data are swapped relative to a correctly oriented destination.
     */
    public boolean swapsWidthHeight() {
        switch (this) {
            case LEFT_TOP:
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
            case LEFT_BOTTOM:
                return true;
            default:
                return false;
        }
    }
}
