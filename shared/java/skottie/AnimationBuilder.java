package org.jetbrains.skija.skottie;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;
import org.jetbrains.skija.resources.*;

public class AnimationBuilder extends Managed {
    static { Library.staticLoad(); }

    @ApiStatus.Internal
    public AnimationBuilder(long ptr) {
        super(ptr, _FinalizerHolder.PTR);
    }

    @ApiStatus.Internal
    public static class _FinalizerHolder {
        public static final long PTR = _nGetFinalizer();
    }

    public AnimationBuilder() {
        this(new AnimationBuilderFlag[0]);
    }

    public AnimationBuilder(AnimationBuilderFlag... builderFlags) {
        this(_nMake(_flagsToInt(builderFlags)));
        Stats.onNativeCall();
    }

    @ApiStatus.Internal
    public static int _flagsToInt(AnimationBuilderFlag... builderFlags) {
        int flags = 0;
        for (AnimationBuilderFlag flag: builderFlags)
            flags |= flag._flag;
        return flags;
    }

    /**
     * <p>Specify a font manager for loading animation fonts.</p>
     */
    @NotNull @Contract("_ -> this")
    public AnimationBuilder setFontManager(@Nullable FontMgr fontMgr) {
        try {
            Stats.onNativeCall();
            _nSetFontManager(_ptr, Native.getPtr(fontMgr));
            return this;
        } finally {
            Reference.reachabilityFence(fontMgr);
        }
    }

    /**
     * <p>Register a {@link Logger} with this builder.</p>
     */
    @NotNull @Contract("_ -> this")
    public AnimationBuilder setLogger(@Nullable Logger logger) {
        try {
            Stats.onNativeCall();
            _nSetLogger(_ptr, Native.getPtr(logger));
            return this;
        } finally {
            Reference.reachabilityFence(logger);
        }
    }

    @NotNull @Contract("_ -> this")
    public AnimationBuilder setResourceProvider(@Nullable ResourceProvider resourceProvider) {
        try {
            Stats.onNativeCall();
            _nSetResourceProvider(_ptr, Native.getPtr(resourceProvider));
            return this;
        } finally {
            Reference.reachabilityFence(resourceProvider);
        }
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public Animation buildFromString(@NotNull String data) {
        try {
            assert data != null : "Can’t buildFromString with data == null";
            Stats.onNativeCall();
            long ptr = _nBuildFromString(_ptr, data);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from string: \"" + data + "\"");
            return new Animation(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public Animation buildFromFile(@NotNull String path) {
        try {
            assert path != null : "Can’t buildFromFile with path == null";
            Stats.onNativeCall();
            long ptr = _nBuildFromFile(_ptr, path);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from path: " + path);
            return new Animation(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @NotNull @Contract("!null -> new; null -> fail")
    public Animation buildFromData(@NotNull Data data) {
        try {
            assert data != null : "Can’t buildFromData with data == null";
            Stats.onNativeCall();
            long ptr = _nBuildFromData(_ptr, Native.getPtr(data));
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Animation from data");
            return new Animation(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal public static native long _nGetFinalizer();
    @ApiStatus.Internal public static native long _nMake(int flags);
    @ApiStatus.Internal public static native void _nSetFontManager(long ptr, long fontMgrPtr);
    @ApiStatus.Internal public static native void _nSetLogger(long ptr, long loggerPtr);
    @ApiStatus.Internal public static native void _nSetResourceProvider(long ptr, long resourceProviderPtr);
    @ApiStatus.Internal public static native long _nBuildFromString(long ptr, String data);
    @ApiStatus.Internal public static native long _nBuildFromFile(long ptr, String path);
    @ApiStatus.Internal public static native long _nBuildFromData(long ptr, long dataPtr);
}