#pragma once
#include "SkShaper.h"

class FontRunIterator final: public SkShaper::FontRunIterator {
public:
    FontRunIterator(const char* utf8, size_t utf8Bytes,
                    const SkFont& font, sk_sp<SkFontMgr> fallbackMgr,
                    const char* requestName, SkFontStyle requestStyle,
                    const SkShaper::LanguageRunIterator* lang)
        : fCurrent(utf8), fBegin(utf8), fEnd(fCurrent + utf8Bytes)
        , fFallbackMgr(std::move(fallbackMgr))
        , fFont(font)
        , fFallbackFont(fFont)
        , fCurrentFont(nullptr)
        , fRequestName(requestName)
        , fRequestStyle(requestStyle)
        , fLanguage(lang)
    {
        fFont.setTypeface(font.refTypefaceOrDefault());
        fFallbackFont.setTypeface(nullptr);
    }

    FontRunIterator(const char* utf8, size_t utf8Bytes,
                       const SkFont& font, sk_sp<SkFontMgr> fallbackMgr)
        : FontRunIterator(utf8, utf8Bytes, font, std::move(fallbackMgr),
                             nullptr, font.refTypefaceOrDefault()->fontStyle(), nullptr)
    {}

    void consume() override;

    size_t endOfCurrentRun() const override {
        return fCurrent - fBegin;
    }

    bool atEnd() const override {
        return fCurrent == fEnd;
    }

    const SkFont& currentFont() const override {
        return *fCurrentFont;
    }

private:
    char const * fCurrent;
    char const * const fBegin;
    char const * const fEnd;
    sk_sp<SkFontMgr> const fFallbackMgr;
    SkFont fFont;
    SkFont fFallbackFont;
    SkFont* fCurrentFont;
    char const * const fRequestName;
    SkFontStyle const fRequestStyle;
    SkShaper::LanguageRunIterator const * const fLanguage;
};