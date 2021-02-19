package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class SVGCanvas {
    static { Library.staticLoad(); }
    
    /**
     *  Returns a new canvas that will generate SVG commands from its draw calls, and send
     *  them to the provided stream. Ownership of the stream is not transfered, and it must
     *  remain valid for the lifetime of the returned canvas.
     *
     *  The canvas may buffer some drawing calls, so the output is not guaranteed to be valid
     *  or complete until the canvas instance is deleted.
     *
     * @param bounds              defines an initial SVG viewport (viewBox attribute on the root SVG element).
     * @param out                 stream SVG commands will be written to
     * @return                    new Canvas
     */
    public static Canvas make(Rect bounds, WStream out) {
        return make(bounds, out, false, true);
    }

    /**
     *  Returns a new canvas that will generate SVG commands from its draw calls, and send
     *  them to the provided stream. Ownership of the stream is not transfered, and it must
     *  remain valid for the lifetime of the returned canvas.
     *
     *  The canvas may buffer some drawing calls, so the output is not guaranteed to be valid
     *  or complete until the canvas instance is deleted.
     *
     * @param bounds              defines an initial SVG viewport (viewBox attribute on the root SVG element).
     * @param out                 stream SVG commands will be written to
     * @param convertTextToPaths  emit text as &lt;path&gt;s
     * @param prettyXML           add newlines and tabs in output
     * @return                    new Canvas
     */
    @NotNull @Contract("_, _, _, _ -> new")
    public static Canvas make(@NotNull Rect bounds, @NotNull WStream out, boolean convertTextToPaths, boolean prettyXML) {
        Stats.onNativeCall();
        long ptr = _nMake(bounds._left, bounds._top, bounds._right, bounds._bottom, Native.getPtr(out), 0 | (convertTextToPaths ? 1 : 0) | (prettyXML ? 0 : 2));
        return new Canvas(ptr, true, out);
    }

    @ApiStatus.Internal public static native long _nMake(float left, float top, float right, float bottom, long wstreamPtr, int flags);
}
