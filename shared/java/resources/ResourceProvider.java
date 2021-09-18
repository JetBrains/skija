package org.jetbrains.skija.resources;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public abstract class ResourceProvider extends RefCnt {
    @ApiStatus.Internal
    public ResourceProvider(long ptr) {
        super(ptr);
    }
}