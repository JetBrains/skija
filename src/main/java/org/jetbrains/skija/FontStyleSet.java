package org.jetbrains.skija;

public class FontStyleSet extends RefCnt {
    public static FontStyleSet createEmpty() {
        Stats.onNativeCall();
        return new FontStyleSet(nCreateEmpty());
    }

    public int count() {
        Stats.onNativeCall();
        return nCount(_ptr);
    }

    public FontStyle getStyle(int index) {
        Stats.onNativeCall();
        return new FontStyle(nGetStyle(_ptr, index));
    }    

    public String getStyleName(int index) {
        Stats.onNativeCall();
        return nGetStyleName(_ptr, index);
    }        

    public Typeface createTypeface(int index) {
        Stats.onNativeCall();
        long ptr = nCreateTypeface(_ptr, index);
        return ptr == 0 ? null : new Typeface(ptr);
    }    

    public Typeface matchStyle(FontStyle style) {
        Stats.onNativeCall();
        long ptr = nMatchStyle(_ptr, style.value);
        return ptr == 0 ? null : new Typeface(ptr);
    }    

    protected FontStyleSet(long nativeInstance) { super(nativeInstance); }
    private static native long nCreateEmpty();
    private static native int nCount(long ptr);
    private static native int nGetStyle(long ptr, int index);
    private static native String nGetStyleName(long ptr, int index);
    private static native long nCreateTypeface(long ptr, int index);
    private static native long nMatchStyle(long ptr, int style);
}