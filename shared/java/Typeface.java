package org.jetbrains.skija;

import java.lang.ref.*;
import java.util.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class Typeface extends RefCnt {
    static { Library.staticLoad(); }
    
    /**
     * @return  the typeface’s intrinsic style attributes
     */
    public FontStyle getFontStyle() {
        try {
            Stats.onNativeCall();
            return new FontStyle(_nGetFontStyle(_ptr));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  true if {@link #getFontStyle()} has the bold bit set
     */
    public boolean isBold() {
        return getFontStyle().getWeight() >= FontWeight.SEMI_BOLD;
    }

    /**
     * @return  true if {@link #getFontStyle()} has the italic bit set
     */
    public boolean isItalic() {
        return getFontStyle().getSlant() != FontSlant.UPRIGHT;
    }

    /** 
     * This is a style bit, advance widths may vary even if this returns true.
     * @return  true if the typeface claims to be fixed-pitch
     */
    public boolean isFixedPitch() {
        try {
            Stats.onNativeCall();
            return _nIsFixedPitch(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * It is possible the number of axes can be retrieved but actual position cannot.
     * @return  the variation coordinates describing the position of this typeface in design variation space, null if there’s no variations
     */
    @Nullable
    public FontVariation[] getVariations() {
        try {
            Stats.onNativeCall();
            return _nGetVariations(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

        /**
     * It is possible the number of axes can be retrieved but actual position cannot.
     * @return  the variation coordinates describing the position of this typeface in design variation space, null if there’s no variations
     */
    @Nullable
    public FontVariationAxis[] getVariationAxes() {
        try {
            Stats.onNativeCall();
            return _nGetVariationAxes(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  a 32bit value for this typeface, unique for the underlying font data. Never 0
     */
    public int getUniqueId() {
        try {
            Stats.onNativeCall();
            return _nGetUniqueId(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  true if the two typefaces reference the same underlying font, treating null as the default font
     */
    @ApiStatus.Internal @Override
    public boolean _nativeEquals(Native other) {
        try {
            return _nEquals(_ptr, Native.getPtr(other));
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(other);
        }
    }

    /**
     * @return  the default normal typeface, which is never null
     */
    @NotNull
    public static Typeface makeDefault() {
        Stats.onNativeCall();
        return new Typeface(_nMakeDefault());
    }

    /**
     * Creates a new reference to the typeface that most closely matches the
     * requested name and style. This method allows extended font
     * face specifiers as in the {@link FontStyle} type. Will never return null.
     * @param name   May be null. The name of the font family
     * @param style  The style of the typeface
     * @return       reference to the closest-matching typeface
     */
    @NotNull
    public static Typeface makeFromName(String name, FontStyle style) {
        Stats.onNativeCall();
        return new Typeface(_nMakeFromName(name, style._value));
    }

    /**
     * @return  a new typeface given a file
     * @throws IllegalArgumentException  If the file does not exist, or is not a valid font file
     */
    @NotNull
    public static Typeface makeFromFile(String path) {
        return makeFromFile(path, 0);
    }

    /**
     * @return  a new typeface given a file
     * @throws IllegalArgumentException  If the file does not exist, or is not a valid font file
     */
    @NotNull
    public static Typeface makeFromFile(String path, int index) {
        Stats.onNativeCall();
        long ptr = _nMakeFromFile(path, index);
        if (ptr == 0)
            throw new IllegalArgumentException("Failed to create Typeface from path=\"" + path + "\" index=" + index);
        return new Typeface(ptr);
    }

    /**
     * @return  a new typeface given a Data
     * @throws IllegalArgumentException  If the data is null, or is not a valid font file
     */
    @NotNull
    public static Typeface makeFromData(Data data) {
        return makeFromData(data, 0);
    }

    /**
     * @return  a new typeface given a Data
     * @throws IllegalArgumentException  If the data is null, or is not a valid font file
     */
    @NotNull
    public static Typeface makeFromData(Data data, int index) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeFromData(Native.getPtr(data), index);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to create Typeface from data " + data);
            return new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(data);
        }
    }

    /**
     * Return a new typeface based on this typeface but parameterized as specified in the
     * variation. If the variation does not supply an argument for a parameter
     * in the font then the value from this typeface will be used as the value for that argument.
     * @return  same typeface if variation already matches, new typeface otherwise
     * @throws IllegalArgumentException  on failure
     */
    public Typeface makeClone(FontVariation variation) {
        return makeClone(new FontVariation[] { variation }, 0);
    }

    /**
     * Return a new typeface based on this typeface but parameterized as specified in the
     * variations. If the variations does not supply an argument for a parameter
     * in the font then the value from this typeface will be used as the value for that argument.
     * @return  same typeface if all variation already match, new typeface otherwise
     * @throws IllegalArgumentException  on failure
     */
    public Typeface makeClone(FontVariation[] variations) {
        return makeClone(variations, 0);
    }

    /**
     * Return a new typeface based on this typeface but parameterized as specified in the
     * variations. If the variations does not supply an argument for a parameter
     * in the font then the value from this typeface will be used as the value for that argument.
     * @return  same typeface if all variation already match, new typeface otherwise
     * @throws IllegalArgumentException  on failure
     */
    public Typeface makeClone(FontVariation[] variations, int collectionIndex) {
        try {
            if (variations.length == 0)
                return this;
            Stats.onNativeCall();
            long ptr = _nMakeClone(_ptr, variations, collectionIndex);
            if (ptr == 0)
                throw new IllegalArgumentException("Failed to clone Typeface " + this + " with " + Arrays.toString(variations));
            return new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     *  Given a string, returns corresponding glyph ids.
     *
     *  @return  the corresponding glyph ids for each character.
     */
    public short[] getStringGlyphs(String s) {
        return getUTF32Glyphs(s.codePoints().toArray());
    }

    /**
     *  Given an array of UTF32 character codes, return their corresponding glyph IDs.
     *
     *  @return  the corresponding glyph IDs for each character.
     */
    public short[] getUTF32Glyphs(int[] uni) {
        try {
            Stats.onNativeCall();
            return _nGetUTF32Glyphs(_ptr, uni);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     *  This is a short-cut for calling {@link #getUTF32Glyphs(int[])}.
     *  @return  the glyph that corresponds to the specified unicode code-point (in UTF32 encoding). If the unichar is not supported, returns 0
     */
    public short getUTF32Glyph(int unichar) {
        try {
            Stats.onNativeCall();
            return _nGetUTF32Glyph(_ptr, unichar);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  the number of glyphs in the typeface
     */
    public int getGlyphsCount() {
        try {
            Stats.onNativeCall();
            return _nGetGlyphsCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /** 
     * @return  the number of tables in the font
     */
    public int getTablesCount() {
        try {
            Stats.onNativeCall();
            return _nGetTablesCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  the list of table tags in the font
     */
    public String[] getTableTags() {
        try {
            Stats.onNativeCall();
            return Arrays.stream(_nGetTableTags(_ptr)).mapToObj(FourByteTag::toString).toArray(String[]::new);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Given a table tag, return the size of its contents, or 0 if not present
     */
    public long getTableSize(String tag) {
        try {
            Stats.onNativeCall();
            return _nGetTableSize(_ptr, FourByteTag.fromString(tag));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Return an immutable copy of the requested font table, or null if that table was
     * not found.
     *
     * @param tag  The table tag whose contents are to be copied
     * @return     an immutable copy of the table's data, or null
     */
    @Nullable
    public Data getTableData(String tag) {
        try {
            Stats.onNativeCall();
            long ptr = _nGetTableData(_ptr, FourByteTag.fromString(tag));
            return ptr == 0 ? null : new Data(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  the units-per-em value for this typeface, or zero if there is an error
     */
    public int getUnitsPerEm() {
        try {
            Stats.onNativeCall();
            return _nGetUnitsPerEm(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Given a run of glyphs, return the associated horizontal adjustments.
     * Adjustments are in "design units", which are integers relative to the
     * typeface's units per em (see {@link #getUnitsPerEm()}).
     *
     * Some typefaces are known to never support kerning. Calling this with null,
     * if it returns null then it will always return null (no kerning) for all
     * possible glyph runs. If it returns int[0], then it *may* return non-null
     * adjustments for some glyph runs.
     * 
     * @return  adjustment array (one less than glyphs), or null if no kerning should be applied
     */
    @Nullable
    public int[] getKerningPairAdjustments(short[] glyphs) {
        try {
            Stats.onNativeCall();
            return _nGetKerningPairAdjustments(_ptr, glyphs);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  all of the family names specified by the font
     */
    public FontFamilyName[] getFamilyNames() {
        try {
            Stats.onNativeCall();
            return _nGetFamilyNames(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * @return  the family name for this typeface. The language of the name is whatever the host platform chooses
     */
    public String getFamilyName() {
        try {
            Stats.onNativeCall();
            return _nGetFamilyName(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Return a rectangle (scaled to 1-pt) that represents the union of the bounds of all
     * of the glyphs, but each one positioned at (0,). This may be conservatively large, and
     * will not take into account any hinting or other size-specific adjustments.
     */
    public Rect getBounds() {
        try {
            Stats.onNativeCall();
            return _nGetBounds(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @ApiStatus.Internal
    public Typeface(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal public static native int      _nGetFontStyle(long ptr);
    @ApiStatus.Internal public static native boolean  _nIsFixedPitch(long ptr);
    @ApiStatus.Internal public static native FontVariation[] _nGetVariations(long ptr);
    @ApiStatus.Internal public static native FontVariationAxis[] _nGetVariationAxes(long ptr);
    @ApiStatus.Internal public static native int      _nGetUniqueId(long ptr);
    @ApiStatus.Internal public static native boolean  _nEquals(long ptr, long otherPtr);
    @ApiStatus.Internal public static native long     _nMakeDefault();
    @ApiStatus.Internal public static native long     _nMakeFromName(String name, int fontStyle);
    @ApiStatus.Internal public static native long     _nMakeFromFile(String path, int index);
    @ApiStatus.Internal public static native long     _nMakeFromData(long dataPtr, int index);
    @ApiStatus.Internal public static native long     _nMakeClone(long ptr, FontVariation[] variations, int collectionIndex);
    @ApiStatus.Internal public static native short[]  _nGetUTF32Glyphs(long ptr, int[] uni);
    @ApiStatus.Internal public static native short    _nGetUTF32Glyph(long ptr, int unichar);
    @ApiStatus.Internal public static native int      _nGetGlyphsCount(long ptr);
    @ApiStatus.Internal public static native int      _nGetTablesCount(long ptr);
    @ApiStatus.Internal public static native int[]    _nGetTableTags(long ptr);
    @ApiStatus.Internal public static native long     _nGetTableSize(long ptr, int tag);
    @ApiStatus.Internal public static native long     _nGetTableData(long ptr, int tag);
    @ApiStatus.Internal public static native int      _nGetUnitsPerEm(long ptr);
    @ApiStatus.Internal public static native int[]    _nGetKerningPairAdjustments(long ptr, short[] glyphs);
    @ApiStatus.Internal public static native FontFamilyName[] _nGetFamilyNames(long ptr);
    @ApiStatus.Internal public static native String   _nGetFamilyName(long ptr);
    @ApiStatus.Internal public static native Rect     _nGetBounds(long ptr);
}