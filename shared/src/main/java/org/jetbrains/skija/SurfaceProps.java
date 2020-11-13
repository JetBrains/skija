package org.jetbrains.skija;

import lombok.*;
import lombok.Data;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

@Data @With @AllArgsConstructor
public class SurfaceProps {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal 
    public boolean _deviceIndependentFonts;
    
    @ApiStatus.Internal 
    public PixelGeometry _pixelGeometry;

    public SurfaceProps() {
        this(false, PixelGeometry.UNKNOWN);
    }

    public SurfaceProps(PixelGeometry geo) {
        this(false, geo);
    }

    @ApiStatus.Internal public int getFlags() {
        return 0 | (_deviceIndependentFonts ? 1 : 0); 
    }
}