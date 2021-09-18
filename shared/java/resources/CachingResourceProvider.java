package org.jetbrains.skija.resources;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class CachingResourceProvider extends ResourceProvider {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public CachingResourceProvider(long ptr) {
        super(ptr);
    }

    @NotNull @Contract("_ -> new")
    public static CachingResourceProvider make(@NotNull ResourceProvider resourceProvider) {
        assert resourceProvider != null : "Can’t CachingResourceProvider::make with resourceProvider == null";
        Stats.onNativeCall();
        return new CachingResourceProvider(_nMake(Native.getPtr(resourceProvider)));
    }

    @ApiStatus.Internal public static native long _nMake(long resourceProviderPtr);
}