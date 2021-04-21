#include <iostream>
#include <jni.h>
#include "../interop.hh"
#include "../TextLine.hh"
#include "SkShaper.h"
#include "SkTextBlob.h"

class TextLineRunHandler: public SkShaper::RunHandler {
public:
    TextLineRunHandler(const SkString& text,
                       std::shared_ptr<UBreakIterator> graphemeIter):
      fLine(new TextLine()),
      conv(text),
      fGraphemeIter(graphemeIter)
    {
    }

    void beginLine() override {
    }

    void runInfo(const RunInfo& info) override {
        fLine->fGlyphCount += info.glyphCount;
        SkFontMetrics metrics;
        info.fFont.getMetrics(&metrics);
        fLine->fAscent = std::min(fLine->fAscent, metrics.fAscent);
        fLine->fCapHeight = std::max(fLine->fCapHeight, metrics.fCapHeight);
        fLine->fXHeight = std::max(fLine->fXHeight, metrics.fXHeight);
        fLine->fDescent = std::max(fLine->fDescent, metrics.fDescent);
        fLine->fLeading = std::max(fLine->fLeading, metrics.fLeading);
        fLine->fWidth += info.fAdvance.fX;
    }

    void commitRunInfo() override {
        SkDEBUGCODE(++fLines;)
    }

    Buffer runBuffer(const RunInfo& info) override {
        const SkTextBlobBuilder::RunBuffer& buffer = fBuilder.allocRunPos(info.fFont, info.glyphCount);
        fLine->fRuns.emplace_back(
            info.fFont,
            info.fBidiLevel,
            fPosition,
            info.fAdvance.fX,
            info.glyphCount,
            buffer.points());
        TextLine::Run& run = fLine->fRuns.back();
        if (fGlyphOffsets.capacity() < info.glyphCount)
            fGlyphOffsets.resize(info.glyphCount);
        return {
            buffer.glyphs,
            buffer.points(),
            nullptr,
            fGlyphOffsets.data(),
            {fPosition, 0}
        };
    }

    void commitRunBuffer(const RunInfo& info) override {
        TextLine::Run& run = fLine->fRuns.back();
        int32_t glyph = 0;
        int32_t graphemesInGlyph = 1;
        SkScalar glyphLeft = run.fPos[glyph].fX;
        run.fBreakOffsets.reserve(info.utf8Range.size() + 1);
        run.fBreakPositions.reserve(info.utf8Range.size() + 1);

        // Only record grapheme clusters boundaries
        for (int32_t offset = fGlyphOffsets[0]; offset <= info.utf8Range.end(); offset = ubrk_following(fGraphemeIter.get(), offset)) {
            run.fBreakOffsets.push_back(conv.from8To16(offset));

            // if grapheme clusters includes multiple glyphs, skip over them
            while (glyph < info.glyphCount && fGlyphOffsets[glyph] < offset)
                ++glyph;

            // if one glyph includes multiple grapheme clusters (ligature, e.g. <->), accumulate
            if ((glyph < info.glyphCount ? fGlyphOffsets[glyph] : info.utf8Range.end()) > offset)
                ++graphemesInGlyph;
            
            // when boundaries meet, distribute break positions evenly inside glyph
            else {
                SkScalar glyphRight = glyph < info.glyphCount ? run.fPos[glyph].fX : fPosition + info.fAdvance.fX;
                SkScalar step = (glyphRight - glyphLeft) / graphemesInGlyph;
                for (int i = 0; i < graphemesInGlyph; ++i)
                    run.fBreakPositions.push_back(glyphLeft + step * (i + 1));
                graphemesInGlyph = 1;
                glyphLeft = glyphRight;
            }
        }
        fPosition += info.fAdvance.fX;
    }

    void commitLine() override {
    }

    sk_sp<TextLine> makeLine() {
        SkASSERTF(fLines == 1, "TextLineRunHandler: Expected single line, got %d", fLines);

        sk_sp<SkTextBlob> blob = fBuilder.make();
        if (nullptr == blob.get())
            return fLine;
        SkTextBlob::Iter iter(*blob);
        SkTextBlob::Iter::Run blobRun;
        int runIdx = 0;
        while (iter.next(&blobRun)) {
            // from SkTextBlobPriv.h
            // -----------------------------------------------------------------------------
            // | SkTextBlob | RunRecord | Glyphs[] | Pos[] | RunRecord | Glyphs[] | Pos[] | ...
            // -----------------------------------------------------------------------------
            int glyphCount = blobRun.fGlyphCount;
            const uint16_t* glyphIndices = blobRun.fGlyphIndices;
            const SkPoint* positions = reinterpret_cast<const SkPoint*>(reinterpret_cast<const uint8_t*>(blobRun.fGlyphIndices) +
                                                                        SkAlign4(glyphCount * sizeof(uint16_t)));
            for (int consumed = 0; consumed < glyphCount;) {
                auto& run = fLine->fRuns[runIdx];
                run.fGlyphs = glyphIndices + consumed;
                run.fPos = positions + consumed;
                runIdx += 1;
                consumed += run.fGlyphCount;
            }
        }
        fLine->fBlob = blob;
        return fLine;
    }

private:
    sk_sp<TextLine> fLine;
    SkTextBlobBuilder fBuilder;
    skija::UtfIndicesConverter conv;
    std::shared_ptr<UBreakIterator> fGraphemeIter;
    std::vector<uint32_t> fGlyphOffsets;
    SkScalar fPosition = 0;
    SkDEBUGCODE(int fLines = 0;)
};