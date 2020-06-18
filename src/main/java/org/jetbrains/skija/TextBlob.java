package org.jetbrains.skija;

import org.jetbrains.skija.impl.Internal;
import org.jetbrains.skija.impl.Managed;
import org.jetbrains.skija.impl.Stats;

public class TextBlob extends Managed {
    @Internal
    public TextBlob(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public Rect getBounds() {
        Stats.onNativeCall();
        return _nBounds(_ptr);
    }

    public static final long _finalizerPtr = _nGetFinalizer();
    public static native long _nMake();
    public static native long _nGetFinalizer();
    public static native Rect _nBounds(long ptr);
}