package org.jetbrains.skija;

import lombok.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class ShadowUtils {
    static { Library.staticLoad(); }
    
    /**
     * Draw an offset spot shadow and outlining ambient shadow for the given path using a disc
     * light. The shadow may be cached, depending on the path type and canvas matrix. If the
     * matrix is perspective or the path is volatile, it will not be cached.
     *
     * @param canvas               The canvas on which to draw the shadows.
     * @param path                 The occluder used to generate the shadows.
     * @param zPlaneParams         Values for the plane function which returns the Z offset of the
     *                             occluder from the canvas based on local x and y values (the current matrix is not applied).
     * @param lightPos             The 3D position of the light relative to the canvas plane. This is
     *                             independent of the canvas's current matrix.
     * @param lightRadius          The radius of the disc light.
     * @param ambientColor         The color of the ambient shadow.
     * @param spotColor            The color of the spot shadow.
     * @param transparentOccluder  The occluding object is not opaque. Knowing that the occluder is opaque allows
     *                             us to cull shadow geometry behind it and improve performance.
     * @param geometricOnly        Don't try to use analytic shadows.
     */
    public static void drawShadow(@NotNull Canvas canvas,
                                  @NotNull Path path,
                                  @NotNull Point3 zPlaneParams,
                                  @NotNull Point3 lightPos,
                                  float lightRadius,
                                  int ambientColor,
                                  int spotColor,
                                  boolean transparentOccluder,
                                  boolean geometricOnly)
    {
        Stats.onNativeCall();
        int flags = 0;
        if (transparentOccluder)
            flags |= 1;
        if (geometricOnly)
            flags |= 2;
        _nDrawShadow(Native.getPtr(canvas), Native.getPtr(path), zPlaneParams._x, zPlaneParams._y, zPlaneParams._z,
            lightPos._x, lightPos._y, lightPos._z, lightRadius, ambientColor, spotColor, flags);
    }

    /**
     * Helper routine to compute ambient color value for one-pass tonal alpha.
     *
     * @param ambientColor   Original ambient color
     * @param spotColor      Original spot color
     * @return               Modified ambient color
     */
    public static int computeTonalAmbientColor(int ambientColor, int spotColor) {
        Stats.onNativeCall();
        return _nComputeTonalAmbientColor(ambientColor, spotColor);
    }

    /**
     * Helper routine to compute spot color value for one-pass tonal alpha.
     *
     * @param ambientColor   Original ambient color
     * @param spotColor      Original spot color
     * @return               Modified spot color
     */
    public static int computeTonalSpotColor(int ambientColor, int spotColor) {
        Stats.onNativeCall();
        return _nComputeTonalSpotColor(ambientColor, spotColor);
    }

    @ApiStatus.Internal public static native void _nDrawShadow(long canvasPtr, long pathPtr, float zPlaneX, float zPlaneY, float zPlaneZ,
        float lightPosX, float lightPosY, float lightPosZ, float lightRadius, int ambientColor, int spotColor, int flags);
    @ApiStatus.Internal public static native int _nComputeTonalAmbientColor(int ambientColor, int spotColor);
    @ApiStatus.Internal public static native int _nComputeTonalSpotColor(int ambientColor, int spotColor);
}
