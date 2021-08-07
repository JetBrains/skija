package org.jetbrains.skija;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.skija.impl.*;

public class RuntimeEffect extends RefCnt {
    static {
        Library.staticLoad();
    }

    public Shader makeShader(@Nullable Data uniforms, @Nullable Shader[] children, @Nullable Matrix33 localMatrix,
            boolean isOpaque) {
        Stats.onNativeCall();
        float[] arr = localMatrix == null ? null : localMatrix._mat;
        int childCount = children == null ? 0 : children.length;
        long[] childrenPtrs = new long[childCount];
        for (int i = 0; i < childCount; i++) {
            childrenPtrs[i] = Native.getPtr(children[i]);
        }
        return new Shader(_nMakeShader(_ptr, Native.getPtr(uniforms), childrenPtrs, arr, isOpaque));
    }

    public RuntimeEffect makeColorFilter(String sksl) {
        Stats.onNativeCall();
        return new RuntimeEffect(_nMakeColorFilter(_ptr, sksl));
    }

    @ApiStatus.Internal
    public RuntimeEffect(long ptr) {
        super(ptr);
    }

    public static native long _nMakeShader(long runtimeEffectPtr, long uniformPtr, long[] childrenPtrs,
            float[] localMatrix, boolean isOpaque);

    public static native long _nMakeColorFilter(long runtimeEffectPtr, String sksl);
}