package org.jetbrains.skija;

import java.lang.ref.*;
import org.jetbrains.annotations.*;
import org.jetbrains.skija.impl.*;

public class FontMgr extends RefCnt {
    static { Library.staticLoad(); }
    
    public int getFamiliesCount() {
        try {
            Stats.onNativeCall();
            return _nGetFamiliesCount(_ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public String getFamilyName(int index) {
        try {
            Stats.onNativeCall();
            return _nGetFamilyName(_ptr, index);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    public FontStyleSet makeStyleSet(int index) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeStyleSet(_ptr, index);
            return ptr == 0 ? null : new FontStyleSet(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * The caller must call {@link #close()} on the returned object.
     * Never returns null; will return an empty set if the name is not found.
     *
     * Passing null as the parameter will return the default system family.
     * Note that most systems don't have a default system family, so passing null will often
     * result in the empty set.
     *
     * It is possible that this will return a style set not accessible from
     * {@link #makeStyleSet(int)} due to hidden or auto-activated fonts.
     */
    public FontStyleSet matchFamily(String familyName) {
        try {
            Stats.onNativeCall();
            return new FontStyleSet(_nMatchFamily(_ptr, familyName));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    /**
     * Find the closest matching typeface to the specified familyName and style
     * and return a ref to it. The caller must call {@link #close()} on the returned
     * object. Will return null if no 'good' match is found.
     *
     * Passing null as the parameter for `familyName` will return the
     * default system font.
     *
     * It is possible that this will return a style set not accessible from
     * {@link #makeStyleSet(int)} or {@link #matchFamily(String)} due to hidden or
     * auto-activated fonts.
     */
    @Nullable
    public Typeface matchFamilyStyle(String familyName, FontStyle style) {
        try {
            Stats.onNativeCall();
            long ptr = _nMatchFamilyStyle(_ptr, familyName, style._value);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @Nullable
    public Typeface matchFamiliesStyle(String[] families, FontStyle style) {
        for (String family: families) {
            Typeface typeface = matchFamilyStyle(family, style);
            if (typeface != null)
                return typeface;
        }
        return null;
    }

    /**
     * Use the system fallback to find a typeface for the given character.
     * Note that bcp47 is a combination of ISO 639, 15924, and 3166-1 codes,
     * so it is fine to just pass a ISO 639 here.
     *
     * Will return null if no family can be found for the character
     * in the system fallback.
     *
     * Passing `null` as the parameter for `familyName` will return the
     * default system font.
     *
     * bcp47[0] is the least significant fallback, bcp47[bcp47.length-1] is the
     * most significant. If no specified bcp47 codes match, any font with the
     * requested character will be matched.
     */
    public Typeface matchFamilyStyleCharacter(@Nullable String familyName, FontStyle style, @Nullable String[] bcp47, int character) {
        try {
            Stats.onNativeCall();
            long ptr = _nMatchFamilyStyleCharacter(_ptr, familyName, style._value, bcp47, character);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    @Nullable
    public Typeface matchFamiliesStyleCharacter(String[] families, FontStyle style, @Nullable String[] bcp47, int character) {
        for (String family: families) {
            Typeface typeface = matchFamilyStyleCharacter(family, style, bcp47, character);
            if (typeface != null)
                return typeface;
        }
        return null;
    }

    /**
     * Create a typeface for the specified data and TTC index (pass 0 for none)
     * or null if the data is not recognized. The caller must call {@link #close()} on
     * the returned object if it is not null.
     */
    public Typeface makeFromData(Data data) {
        return makeFromData(data, 0);
    }

    public Typeface makeFromData(Data data, int ttcIndex) {
        try {
            Stats.onNativeCall();
            long ptr = _nMakeFromData(_ptr, Native.getPtr(data), ttcIndex);
            return ptr == 0 ? null : new Typeface(ptr);
        } finally {
            Reference.reachabilityFence(this);
            Reference.reachabilityFence(data);
        }
    }

    public static class _DefaultHolder {
        static { Stats.onNativeCall(); }
        public static final FontMgr INSTANCE = new FontMgr(_nDefault(), false);
    }

    /**
     * Return the default fontmgr.
     */
    public static FontMgr getDefault() {
        return _DefaultHolder.INSTANCE;
    }

    @ApiStatus.Internal
    public FontMgr(long ptr) {
        super(ptr);
    }

    @ApiStatus.Internal
    public FontMgr(long ptr, boolean allowClose) {
        super(ptr, allowClose);
    }

    public static native int _nGetFamiliesCount(long ptr);
    public static native String _nGetFamilyName(long ptr, int index);
    public static native long _nMakeStyleSet(long ptr, int index);
    public static native long _nMatchFamily(long ptr, String familyName);
    public static native long _nMatchFamilyStyle(long ptr, String familyName, int fontStyle);
    public static native long _nMatchFamilyStyleCharacter(long ptr, String familyName, int fontStyle, String[] bcp47, int character);
    public static native long _nMakeFromData(long ptr, long dataPtr, int ttcIndex);
    public static native long _nDefault();
}