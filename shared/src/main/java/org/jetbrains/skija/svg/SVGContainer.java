package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public abstract class SVGContainer extends SVGTransformableNode {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public SVGContainer(long ptr) {
        super(ptr);
    }
}