#include "SkShaper.h"
#include "FontRunIterator.hh"
#include "src/utils/SkUTF.h"
#include "unicode/uchar.h"

// Adapted from SkShaper.cpp

/** Replaces invalid utf-8 sequences with REPLACEMENT CHARACTER U+FFFD. */
static inline SkUnichar utf8_next(const char** ptr, const char* end) {
    SkUnichar val = SkUTF::NextUTF8(ptr, end);
    return val < 0 ? 0xFFFD : val;
}

void FontRunIterator::consume() {
    SkASSERT(fCurrent < fEnd);
    SkASSERT(!fLanguage || this->endOfCurrentRun() <= fLanguage->endOfCurrentRun());
    SkUnichar u = utf8_next(&fCurrent, fEnd);
    // If the starting typeface can handle this character, use it.
    if (fFont.unicharToGlyph(u)) {
        fCurrentFont = &fFont;
    // If the current fallback can handle this character, use it.
    } else if (fFallbackFont.getTypeface() && fFallbackFont.unicharToGlyph(u)) {
        fCurrentFont = &fFallbackFont;
    // If not, try to find a fallback typeface
    } else {
        const char* language = fLanguage ? fLanguage->currentLanguage() : nullptr;
        int languageCount = fLanguage ? 1 : 0;
        sk_sp<SkTypeface> candidate(fFallbackMgr->matchFamilyStyleCharacter(
            fRequestName, fRequestStyle, &language, languageCount, u));
        if (candidate) {
            fFallbackFont.setTypeface(std::move(candidate));
            fCurrentFont = &fFallbackFont;
        } else {
            fCurrentFont = &fFont;
        }
    }

    while (fCurrent < fEnd) {
        const char* prev = fCurrent;
        u = utf8_next(&fCurrent, fEnd);

        // Do not switch font on whitespace
        if (u_iscntrl(u) || u_isWhitespace(u))
            continue;

        // End run if not using initial typeface and initial typeface has this character.
        if (fCurrentFont->getTypeface() != fFont.getTypeface() && fFont.unicharToGlyph(u)) {
            fCurrent = prev;
            return;
        }

        // End run if current typeface does not have this character and some other font does.
        if (!fCurrentFont->unicharToGlyph(u)) {
            const char* language = fLanguage ? fLanguage->currentLanguage() : nullptr;
            int languageCount = fLanguage ? 1 : 0;
            sk_sp<SkTypeface> candidate(fFallbackMgr->matchFamilyStyleCharacter(
                fRequestName, fRequestStyle, &language, languageCount, u));
            if (candidate) {
                fCurrent = prev;
                return;
            }
        }
    }
}
