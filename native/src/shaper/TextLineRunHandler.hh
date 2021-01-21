#include <iostream>
#include <jni.h>
#include "../interop.hh"
#include "../TextLine.hh"
#include "SkShaper.h"
#include "SkTextBlob.h"

class TextLineRunHandler: public SkShaper::RunHandler {
public:
    TextLineRunHandler(const SkString& text):
      fLine(new TextLine()),
      conv(text) {
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
            info.glyphCount);
        TextLine::Run& run = fLine->fRuns.back();
        return {
            buffer.glyphs,
            buffer.points(),
            nullptr,
            run.fClusters.data(),
            {fPosition, 0}
        };
    }

    void commitRunBuffer(const RunInfo& info) override {
        TextLine::Run& run = fLine->fRuns.back();
        for (int i = 0; i < info.glyphCount; ++i)
            run.fClusters[i] = conv.from8To16(run.fClusters[i]);
        run.fEnd16 = conv.from8To16(info.utf8Range.end());
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
    SkScalar fPosition = 0;
    SkDEBUGCODE(int fLines = 0;)
};