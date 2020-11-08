package org.jetbrains.skija.paragraph;

import org.jetbrains.annotations.*;
import org.jetbrains.skija.*;
import org.jetbrains.skija.impl.*;

public class TextStyle extends Managed {
    static { Library.staticLoad(); }
    
    @ApiStatus.Internal
    public TextStyle(long ptr) {
        super(ptr, _finalizerPtr);
    }

    public TextStyle() {
        this(_nMake());
        Stats.onNativeCall();
    }

    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        Stats.onNativeCall();
        return _nEquals(_ptr, Native.getPtr(other));
    }

    public boolean equals(TextStyleAttribute attribute, TextStyle other) {
        Stats.onNativeCall();
        return _nAttributeEquals(_ptr, attribute.ordinal(), Native.getPtr(other));
    }

    public int getColor() {
        Stats.onNativeCall();
        return _nGetColor(_ptr);
    }

    public TextStyle setColor(int color) {
        Stats.onNativeCall();
        _nSetColor(_ptr, color);
        return this;
    }

    @Nullable
    public Paint getForeground() {
        Stats.onNativeCall();
        long ptr = _nGetForeground(_ptr);
        return ptr == 0 ? null : new Paint(ptr, true);
    }

    public TextStyle setForeground(@Nullable Paint paint) {
        Stats.onNativeCall();
        _nSetForeground(_ptr, Native.getPtr(paint));
        return this;
    }

    @Nullable
    public Paint getBackground() {
        Stats.onNativeCall();
        long ptr = _nGetBackground(_ptr);
        return ptr == 0 ? null : new Paint(ptr, true);
    }

    public TextStyle setBackground(@Nullable Paint paint) {
        Stats.onNativeCall();
        _nSetBackground(_ptr, Native.getPtr(paint));
        return this;
    }

    public DecorationStyle getDecorationStyle() {
        Stats.onNativeCall();
        return _nGetDecorationStyle(_ptr);
    }

    public TextStyle setDecorationStyle(DecorationStyle d) {
        Stats.onNativeCall();
        _nSetDecorationStyle(_ptr, d._underline, d._overline, d._lineThrough, d._gaps, d._color, d._lineStyle.ordinal(), d._thicknessMultiplier);
        return this;
    }

    public FontStyle getFontStyle() {
        Stats.onNativeCall();
        return new FontStyle(_nGetFontStyle(_ptr));
    }

    public TextStyle setFontStyle(FontStyle s) {
        Stats.onNativeCall();
        _nSetFontStyle(_ptr, s._value);
        return this;
    }

    public Shadow[] getShadows() {
        Stats.onNativeCall();
        return _nGetShadows(_ptr);
    }

    public TextStyle addShadow(Shadow s) {
        Stats.onNativeCall();
        _nAddShadow(_ptr, s._color, s._offsetX, s._offsetY, s._blurRadius);
        return this;
    }

    public TextStyle addShadows(Shadow[] shadows) {
        for (Shadow s: shadows)
            addShadow(s);
        return this;
    }

    public TextStyle clearShadows() {
        Stats.onNativeCall();
        _nClearShadows(_ptr);
        return this;
    }

    public FontFeature[] getFontFeatures() {
        Stats.onNativeCall();
        return _nGetFontFeatures(_ptr);
    }

    public TextStyle addFontFeature(FontFeature f) {
        Stats.onNativeCall();
        _nAddFontFeature(_ptr, f.getTag(), f._value);
        return this;
    }

    public TextStyle addFontFeatures(FontFeature[] FontFeatures) {
        for (FontFeature s: FontFeatures)
            addFontFeature(s);
        return this;
    }

    public TextStyle clearFontFeatures() {
        Stats.onNativeCall();
        _nClearFontFeatures(_ptr);
        return this;
    }

    public float getFontSize() {
        Stats.onNativeCall();
        return _nGetFontSize(_ptr);
    }

    public TextStyle setFontSize(float size) {
        Stats.onNativeCall();
        _nSetFontSize(_ptr, size);
        return this;
    }

    public String[] getFontFamilies() {
        Stats.onNativeCall();
        return _nGetFontFamilies(_ptr);
    }

    public TextStyle setFontFamily(String family) {
        return setFontFamilies(new String[] { family });
    }

    public TextStyle setFontFamilies(String[] families) {
        Stats.onNativeCall();
        _nSetFontFamilies(_ptr, families);
        return this;
    }

    @Nullable
    public Float getHeight() {
        Stats.onNativeCall();
        return _nGetHeight(_ptr);
    }

    public TextStyle setHeight(@Nullable Float height) {
        Stats.onNativeCall();
        if (height == null)
            _nSetHeight(_ptr, false, 0);
        else
            _nSetHeight(_ptr, true, height);
        return this;
    }

    public float getLetterSpacing() {
        Stats.onNativeCall();
        return _nGetLetterSpacing(_ptr);
    }

    public TextStyle setLetterSpacing(float letterSpacing) {
        Stats.onNativeCall();
        _nSetLetterSpacing(_ptr, letterSpacing);
        return this;
    }

    public float getWordSpacing() {
        Stats.onNativeCall();
        return _nGetWordSpacing(_ptr);
    }

    public TextStyle setWordSpacing(float wordSpacing) {
        Stats.onNativeCall();
        _nSetWordSpacing(_ptr, wordSpacing);
        return this;
    }

    public Typeface getTypeface() {
        Stats.onNativeCall();
        long ptr = _nGetTypeface(_ptr);
        return ptr == 0 ? null : new Typeface(ptr);
    }

    public TextStyle setTypeface(Typeface typeface) {
        Stats.onNativeCall();
        _nSetTypeface(_ptr, Native.getPtr(typeface));
        return this;
    }

    public String getLocale() {
        Stats.onNativeCall();
        return _nGetLocale(_ptr);
    }

    public TextStyle setLocale(String locale) {
        Stats.onNativeCall();
        _nSetLocale(_ptr, locale);
        return this;
    }

    public BaselineMode getBaselineMode() {
        Stats.onNativeCall();
        return BaselineMode.values()[_nGetBaselineMode(_ptr)];
    }

    public TextStyle setBaselineMode(BaselineMode baseline) {
        Stats.onNativeCall();
        _nSetBaselineMode(_ptr, baseline.ordinal());
        return this;
    }

    public FontMetrics getFontMetrics() {
        Stats.onNativeCall();
        return _nGetFontMetrics(_ptr);
    }    

    public boolean isPlaceholder() {
        Stats.onNativeCall();
        return _nIsPlaceholder(_ptr);
    }

    public TextStyle setPlaceholder() {
        Stats.onNativeCall();
        _nSetPlaceholder(_ptr);
        return this;
    }

    public static native long  _nMake();
    public static native long  _nGetFinalizer();
    public static final  long  _finalizerPtr = _nGetFinalizer();
    public static native boolean _nEquals(long ptr, long otherPtr);
    public static native boolean _nAttributeEquals(long ptr, int attribute, long otherPtr);
    public static native int   _nGetColor(long ptr);
    public static native void  _nSetColor(long ptr,int color);
    public static native long  _nGetForeground(long ptr);
    public static native void  _nSetForeground(long ptr, long paintPtr);
    public static native long  _nGetBackground(long ptr);
    public static native void  _nSetBackground(long ptr, long paintPtr);
    public static native DecorationStyle _nGetDecorationStyle(long ptr);
    public static native void  _nSetDecorationStyle(long ptr, boolean underline, boolean overline, boolean lineThrough, boolean gaps, int color, int style, float thicknessMultiplier);
    public static native int   _nGetFontStyle(long ptr);
    public static native void  _nSetFontStyle(long ptr, int fontStyle);
    public static native Shadow[] _nGetShadows(long ptr);
    public static native void  _nAddShadow(long ptr, int color, float offsetX, float offsetY, double blurRadius);
    public static native void  _nClearShadows(long ptr);
    public static native FontFeature[] _nGetFontFeatures(long ptr);
    public static native void  _nAddFontFeature(long ptr, String name, int value);
    public static native void  _nClearFontFeatures(long ptr);
    public static native float _nGetFontSize(long ptr);
    public static native void  _nSetFontSize(long ptr, float size);
    public static native String[] _nGetFontFamilies(long ptr);
    public static native void  _nSetFontFamilies(long ptr, String[] families);
    public static native Float _nGetHeight(long ptr);
    public static native void  _nSetHeight(long ptr, boolean override, float height);
    public static native float _nGetLetterSpacing(long ptr);
    public static native void  _nSetLetterSpacing(long ptr, float letterSpacing);
    public static native float _nGetWordSpacing(long ptr);
    public static native void  _nSetWordSpacing(long ptr, float wordSpacing);
    public static native long  _nGetTypeface(long ptr);
    public static native void  _nSetTypeface(long ptr, long typefacePtr);
    public static native String _nGetLocale(long ptr);
    public static native void  _nSetLocale(long ptr, String locale);
    public static native int   _nGetBaselineMode(long ptr);
    public static native void  _nSetBaselineMode(long ptr, int mode);
    public static native FontMetrics _nGetFontMetrics(long ptr);
    public static native boolean _nIsPlaceholder(long ptr);
    public static native void  _nSetPlaceholder(long ptr);
}