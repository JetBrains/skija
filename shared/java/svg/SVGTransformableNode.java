package org.jetbrains.skija.svg;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public abstract class SVGTransformableNode extends SVGNode {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public SVGTransformableNode(long ptr) {
        super(ptr);
    }
}