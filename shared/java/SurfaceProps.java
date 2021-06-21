package org.jetbrains.skija;

import lombok.*;
import lombok.Data;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

@Data @With @AllArgsConstructor
public class SurfaceProps {
    @ApiStatus.Internal 
    public final boolean _deviceIndependentFonts;
    
    @ApiStatus.Internal 
    public final PixelGeometry _pixelGeometry;

    public SurfaceProps() {
        this(false, PixelGeometry.UNKNOWN);
    }

    public SurfaceProps(PixelGeometry geo) {
        this(false, geo);
    }

    @ApiStatus.Internal
    public int _getFlags() {
        return 0 | (_deviceIndependentFonts ? 1 : 0); 
    }

    @ApiStatus.Internal
    public int _getPixelGeometryOrdinal() {
        return _pixelGeometry.ordinal();
    }
}