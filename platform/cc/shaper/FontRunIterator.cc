#include "SkShaper.h"
#include "FontRunIterator.hh"
#include "src/utils/SkUTF.h"
#include "unicode/uchar.h"
#include <iostream>

// Adapted from SkShaper.cpp

/** Replaces invalid utf-8 sequences with REPLACEMENT CHARACTER U+FFFD. */
static inline SkUnichar utf8_next(const char** ptr, const char* end) {
    SkUnichar val = SkUTF::NextUTF8(ptr, end);
    return val < 0 ? 0xFFFD : val;
}

bool can_handle_cluster(SkTypeface* typeface, const char* clusterStart, const char* clusterEnd) {
    const char *ptr = clusterStart;
    while (ptr < clusterEnd) {
        SkUnichar u = SkUTF::NextUTF8(&ptr, clusterEnd);
        u = u < 0 ? 0xFFFD : u;
        if (0 == typeface->unicharToGlyph(u))
            return false;
    }
    return true;
}

void FontRunIterator::consume() {
    const char* clusterStart = fCurrent;
    const char* clusterEnd = fBegin + ubrk_following(fGraphemeIter.get(), clusterStart - fBegin);
    UErrorCode status = U_ZERO_ERROR;

    // If the starting typeface can handle this character, use it.
    if (can_handle_cluster(fFont.getTypeface(), clusterStart, clusterEnd)) {
        fCurrentFont = &fFont;
    // If the current fallback can handle this character, use it.
    } else if (fFallbackFont.getTypeface() && can_handle_cluster(fFallbackFont.getTypeface(), clusterStart, clusterEnd)) {
        fCurrentFont = &fFallbackFont;
    // If not, try to find a fallback typeface
    } else {
        const char* language = fLanguage ? fLanguage->currentLanguage() : nullptr;
        int languageCount = fLanguage ? 1 : 0;
        const char *ptr = clusterStart;
        fCurrentFont = &fFont;
        while (ptr < clusterEnd) {
            SkUnichar u = SkUTF::NextUTF8(&ptr, clusterEnd);
            u = u < 0 ? 0xFFFD : u;
            sk_sp<SkTypeface> candidate(fFallbackMgr->matchFamilyStyleCharacter(fRequestName, fRequestStyle, &language, languageCount, u));
            if (candidate && can_handle_cluster(candidate.get(), clusterStart, clusterEnd)) {
                fFallbackFont.setTypeface(std::move(candidate));
                fCurrentFont = &fFallbackFont;
                break;
            }
        }
    }
    
    while (clusterStart < fEnd) {
        clusterStart = clusterEnd;
        clusterEnd = fBegin + ubrk_following(fGraphemeIter.get(), clusterStart - fBegin);

        const char* ptr = clusterStart;
        SkUnichar u = utf8_next(&ptr, clusterEnd);

        // Do not switch font on control, whitespace or punct
        if (ptr == clusterEnd
            && fCurrentFont->getTypeface()->unicharToGlyph(u) != 0
            && (u_iscntrl(u)
                || (fApproximateSpaces && u_isWhitespace(u))
                || (fApproximatePunctuation && u_ispunct(u))))
            continue;

        // End run if not using initial typeface and initial typeface has this character.
        if (fCurrentFont->getTypeface() != fFont.getTypeface() && can_handle_cluster(fFont.getTypeface(), clusterStart, clusterEnd)) {
            fCurrent = clusterStart;
            return;
        }

        // End run if current typeface does not have this character and some other font does.
        if (!can_handle_cluster(fCurrentFont->getTypeface(), clusterStart, clusterEnd)) {
            const char* language = fLanguage ? fLanguage->currentLanguage() : nullptr;
            int languageCount = fLanguage ? 1 : 0;
            const char *ptr = clusterStart;
            while (ptr < clusterEnd) {
                SkUnichar u = SkUTF::NextUTF8(&ptr, clusterEnd);
                u = u < 0 ? 0xFFFD : u;
                sk_sp<SkTypeface> candidate(fFallbackMgr->matchFamilyStyleCharacter(fRequestName, fRequestStyle, &language, languageCount, u));
                if (candidate && can_handle_cluster(candidate.get(), clusterStart, clusterEnd)) {
                    fCurrent = clusterStart;
                    return;
                }
            }
        }
    }
    fCurrent = clusterStart;
}
