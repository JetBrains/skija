package org.jetbrains.skija;

import lombok.*;
import lombok.Data;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

@Data @With @AllArgsConstructor
public class SurfaceProps {
    static { Library.load(); }
    
    public boolean _deviceIndependentFonts;
    public PixelGeometry _pixelGeometry;

    public SurfaceProps() {
        this(false);
    }

    public SurfaceProps(boolean useDeviceIndependentFonts) {
        this(useDeviceIndependentFonts, PixelGeometry.values()[_nComputeDefaultGeometry()]);
        Stats.onNativeCall();
    }

    public SurfaceProps(PixelGeometry pixelGeometry) {
        this(false, pixelGeometry);
    }

    @ApiStatus.Internal public int getFlags() {
        return 0 | (_deviceIndependentFonts ? 1 : 0); 
    }

    @ApiStatus.Internal public static native int _nComputeDefaultGeometry();
}