package org.jetbrains.skija.pdf;

import org.jetbrains.skija.Canvas;
import org.jetbrains.skija.impl.Native;
import org.jetbrains.skija.impl.Stats;

public class Document extends Native {

    public Document(long ptr) {
        super(ptr);
    }

    public static Document createPDF() {
        Stats.onNativeCall();
        long ptr = _nMake();
        return new Document(ptr);
    }

    public static class _FinalizerHolder {
        static {
            Stats.onNativeCall();
        }

        //public static final long PTR = _nGetFinalizer();
    }

    public Canvas beginPage(float width, float height) {
        Stats.onNativeCall();
        return new Canvas(_nBeginPage(_ptr, width, height, 0f, 0f, width, height));
    }

    public void endPage() {
        Stats.onNativeCall();
        _nEndPage(_ptr);
    }

    public void write(String path) {
        Stats.onNativeCall();
        _nWrite(_ptr, path);
    }

    public void close() {
        Stats.onNativeCall();
        _nClose(_ptr);
    }

    public static native long _nMake();

    public static native long _nGetFinalizer();

    public static native long _nBeginPage(long ptr, float width, float height, float left, float top, float right, float bottom);

    public static native void _nEndPage(long ptr);

    public static native void _nWrite(long ptr, String path);

    public static native void _nClose(long ptr);
}
