
#include <cstring>
#include <iostream>
#include <jni.h>
#include "SkData.h"
#include "SkSerialProcs.h"
#include "SkTextBlob.h"
#include "interop.hh"

static void unrefTextBlob(SkTextBlob* ptr) {
    ptr->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefTextBlob));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nBounds
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkRect bounds = instance->bounds();
    return skija::Rect::fromSkRect(env, instance->bounds());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_TextBlob__1nGetUniqueId
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    return instance->uniqueID();
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextBlob__1nGetIntercepts
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat lower, jfloat upper, jlong paintPtr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    std::vector<float> bounds {lower, upper};
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    int len = instance->getIntercepts(bounds.data(), nullptr, paint);
    std::vector<float> intervals(len);
    instance->getIntercepts(bounds.data(), intervals.data(), paint);
    return javaFloatArray(env, intervals);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nMakeFromPosH
  (JNIEnv* env, jclass jclass, jshortArray glyphsArr, jfloatArray xposArr, jfloat ypos, jlong fontPtr) {
    jsize len = env->GetArrayLength(glyphsArr);
    jshort* glyphs = env->GetShortArrayElements(glyphsArr, nullptr);
    jfloat* xpos = env->GetFloatArrayElements(xposArr, nullptr);
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));

    SkTextBlob* instance = SkTextBlob::MakeFromPosTextH(glyphs, len * sizeof(jshort), xpos, ypos, *font, SkTextEncoding::kGlyphID).release();

    env->ReleaseShortArrayElements(glyphsArr, glyphs, 0);
    env->ReleaseFloatArrayElements(xposArr, xpos, 0);

    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nMakeFromPos
  (JNIEnv* env, jclass jclass, jshortArray glyphsArr, jfloatArray posArr, jlong fontPtr ) {
    jsize len = env->GetArrayLength(glyphsArr);
    jshort* glyphs = env->GetShortArrayElements(glyphsArr, nullptr);
    jfloat* pos = env->GetFloatArrayElements(posArr, nullptr);
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));

    SkTextBlob* instance = SkTextBlob::MakeFromPosText(glyphs, len * sizeof(jshort), reinterpret_cast<SkPoint*>(pos), *font, SkTextEncoding::kGlyphID).release();

    env->ReleaseShortArrayElements(glyphsArr, glyphs, 0);
    env->ReleaseFloatArrayElements(posArr, pos, 0);

    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nMakeFromRSXform
  (JNIEnv* env, jclass jclass, jshortArray glyphsArr, jfloatArray xformArr, jlong fontPtr ) {
    jsize len = env->GetArrayLength(glyphsArr);
    jshort* glyphs = env->GetShortArrayElements(glyphsArr, nullptr);
    jfloat* xform = env->GetFloatArrayElements(xformArr, nullptr);
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));

    SkTextBlob* instance = SkTextBlob::MakeFromRSXform(glyphs, len * sizeof(jshort), reinterpret_cast<SkRSXform*>(xform), *font, SkTextEncoding::kGlyphID).release();

    env->ReleaseShortArrayElements(glyphsArr, glyphs, 0);
    env->ReleaseFloatArrayElements(xformArr, xform, 0);

    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nSerializeToData
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkData* data = instance->serialize({}).release();
    return reinterpret_cast<jlong>(data);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkTextBlob* instance = SkTextBlob::Deserialize(data->data(), data->size(), {}).release();
    return reinterpret_cast<jlong>(instance);
}

// Must match SkTextBlobPriv.h
//
// Extended Textblob runs have more data after the Pos[] array:
//
//    -------------------------------------------------------------------------
//    ... | RunRecord | Glyphs[] | Pos[] | TextSize | Clusters[] | Text[] | ...
//    -------------------------------------------------------------------------
class RunRecordClone {
public:
    SkFont    fFont;
    uint32_t  fCount;
    SkPoint   fOffset;
    uint32_t  fFlags;

    uint16_t* glyphBuffer() const {
        // Glyphs are stored immediately following the record.
        return reinterpret_cast<uint16_t*>(const_cast<RunRecordClone*>(this) + 1);
    }

    SkScalar* posBuffer() const {
        // Position scalars follow the (aligned) glyph buffer.
        return reinterpret_cast<SkScalar*>(reinterpret_cast<uint8_t*>(this->glyphBuffer()) +
                                           SkAlign4(fCount * sizeof(uint16_t)));
    }

    uint32_t* textSizePtr() const {
        // textSize follows the position buffer.
        return (uint32_t*)(&this->posBuffer()[fCount * ScalarsPerGlyph(positioning())]);
    }

    uint32_t textSize() const {
        return isExtended() ? *this->textSizePtr() : 0;
    }

    uint32_t* clusterBuffer() const {
        // clusters follow the textSize.
        return isExtended() ? 1 + this->textSizePtr() : nullptr;
    }

    char* textBuffer() const {
        return isExtended()
               ? reinterpret_cast<char*>(this->clusterBuffer() + fCount)
               : nullptr;
    }

    uint8_t positioning() const {
        return fFlags & 0x3; // kPositioning_Mask
    }

    bool isExtended() const {
        return fFlags & 0x8; // kExtended_Flag
    }

    static unsigned ScalarsPerGlyph(uint8_t pos) {
        const uint8_t gScalarsPerPositioning[] = {
            0,  // kDefault_Positioning
            1,  // kHorizontal_Positioning
            2,  // kFull_Positioning
            4,  // kRSXform_Positioning
        };
        return gScalarsPerPositioning[pos];
    }
};

extern "C" JNIEXPORT jshortArray JNICALL Java_org_jetbrains_skija_TextBlob__1nGetGlyphs
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    std::vector<jshort> glyphs;
    size_t stored = 0;
    while (iter.next(&run)) {
        glyphs.resize(stored + run.fGlyphCount);
        memcpy(&glyphs[stored], run.fGlyphIndices, run.fGlyphCount * sizeof(uint16_t));
        stored += run.fGlyphCount;
    }
    return javaShortArray(env, glyphs);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextBlob__1nGetPositions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    std::vector<jfloat> positions;
    size_t stored = 0;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        unsigned scalarsPerGlyph = RunRecordClone::ScalarsPerGlyph(runRecord->positioning());
        positions.resize(stored + run.fGlyphCount * scalarsPerGlyph);
        memcpy(&positions[stored], runRecord->posBuffer(), run.fGlyphCount * scalarsPerGlyph * sizeof(SkScalar));
        stored += run.fGlyphCount * scalarsPerGlyph;
    }
    return javaFloatArray(env, positions);
}

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_TextBlob__1nGetClusters
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    std::vector<jint> clusters;
    size_t stored = 0;
    // uint32_t cluster8 = 0;
    uint32_t runStart16 = 0;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (!runRecord->isExtended())
            return nullptr;
        
        skija::UtfIndicesConverter conv(runRecord->textBuffer(), runRecord->textSize());
        clusters.resize(stored + run.fGlyphCount);
        uint32_t* clusterBuffer = runRecord->clusterBuffer();
        for (int i = 0; i < run.fGlyphCount; ++i)
            clusters[stored + i] = runStart16 + conv.from8To16(clusterBuffer[i]);
        runStart16 += conv.from8To16(runRecord->textSize());
        // memcpy(&clusters[stored], runRecord->clusterBuffer(), run.fGlyphCount * sizeof(uint32_t));
        
        stored += run.fGlyphCount;
    }
    return javaIntArray(env, clusters);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nGetTightBounds
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    auto bounds = SkRect::MakeEmpty();
    SkRect tmpBounds;
    SkGlyphID glyph;
    SkPoint pos;
    bool first = false;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2) // kFull_Positioning
            return nullptr;
        
        runRecord->fFont.measureText(runRecord->glyphBuffer(), run.fGlyphCount * sizeof(uint16_t), SkTextEncoding::kGlyphID, &tmpBounds, nullptr);
        SkScalar* posBuffer = runRecord->posBuffer();
        tmpBounds.offset(posBuffer[0], posBuffer[1]);
        if (first) {
            bounds.setLTRB(tmpBounds.fLeft, tmpBounds.fTop, tmpBounds.fRight, tmpBounds.fBottom);
            first = false;
        } else
            bounds.join(tmpBounds);
    }
    return skija::Rect::fromSkRect(env, bounds);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nGetBlockBounds
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    SkScalar right = 0;
    SkScalar bottom = 0;
    SkFontMetrics metrics;
    SkGlyphID glyph;
    SkScalar width;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2) // kFull_Positioning
            return nullptr;

        SkScalar* posBuffer = runRecord->posBuffer();
        const SkFont& font = runRecord->fFont;
        font.getMetrics(&metrics);
        bottom = std::max(bottom, posBuffer[1] + metrics.fDescent);
        
        font.getWidths(&run.fGlyphIndices[run.fGlyphCount - 1], 1, &width);
        right = std::max(right, posBuffer[run.fGlyphCount * 2 - 2] + width);
    }
    return skija::Rect::fromSkRect(env, SkRect {0, 0, right, bottom});
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nGetFirstBaseline
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2) // kFull_Positioning
            return nullptr;

        return javaFloat(env, runRecord->posBuffer()[1]);
    }
    return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nGetLastBaseline
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    SkScalar baseline = 0;
    while (iter.next(&run)) {
        // run.fGlyphIndices points directly to runRecord.glyphBuffer(), which comes directly after RunRecord itself
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2) // kFull_Positioning
            return nullptr;

        baseline = std::max(baseline, runRecord->posBuffer()[1]);
    }
    return javaFloat(env, baseline);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_TextBlob__1nGetOffsetAtCoord
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    uint32_t runStart16 = 0;

    while (iter.next(&run)) {
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2 || !runRecord->isExtended()) // kFull_Positioning
            return -1;
        skija::UtfIndicesConverter conv(runRecord->textBuffer(), runRecord->textSize());
        SkScalar* posBuffer = runRecord->posBuffer();
        
        // check if we need to skip this line
        const SkFont& font = runRecord->fFont;
        SkFontMetrics metrics;
        font.getMetrics(&metrics);
        SkScalar lineBottom = posBuffer[1] + metrics.fDescent;
        if (y > lineBottom) {
            runStart16 += conv.from8To16(runRecord->textSize());
            continue;
        }

        // check if we ended prev line after last char, except for the first li
        SkScalar lineTop = posBuffer[1] + metrics.fAscent;
        if (lineTop > 0 && y < lineTop)
            return runStart16;

        // check last glyph first, since its right boundary is calculated differently
        SkScalar lastGlyphWidth;
        font.getWidths(&run.fGlyphIndices[run.fGlyphCount - 1], 1, &lastGlyphWidth);
        SkScalar lastGlyphCenter = posBuffer[run.fGlyphCount * 2 - 2] + lastGlyphWidth / 2;
        if (x > lastGlyphCenter) {
            runStart16 += conv.from8To16(runRecord->textSize());
            continue;
        }

        // have to look in this run
        SkScalar glyphLeft = posBuffer[0];
        uint32_t idx = 0;
        for (; idx < run.fGlyphCount - 1; ++idx) {
            SkScalar glyphRight = posBuffer[(idx + 1) * 2];
            SkScalar glyphCenter = (glyphLeft + glyphRight) / 2;
            if (x < glyphCenter)
                break;
            glyphLeft = glyphRight;
        }
        return runStart16 + conv.from8To16(runRecord->clusterBuffer()[idx]);
    }

    return runStart16;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nGetCoordAtOffset
  (JNIEnv* env, jclass jclass, jlong ptr, jint target16) {
    SkTextBlob* instance = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkTextBlob::Iter iter(*instance);
    SkTextBlob::Iter::Run run;
    uint32_t runStart16 = 0;
    SkScalar prevGlyphLeft = 0;
    SkScalar prevGlyphBaseline = 0;

    while (iter.next(&run)) {
        auto runRecord = reinterpret_cast<const RunRecordClone*>(run.fGlyphIndices) - 1;
        if (runRecord->positioning() != 2 || !runRecord->isExtended()) // kFull_Positioning
            return nullptr;
        const SkFont& font = runRecord->fFont;
        skija::UtfIndicesConverter conv(runRecord->textBuffer(), runRecord->textSize());
        SkScalar* posBuffer = runRecord->posBuffer();
        uint32_t* clusterBuffer = runRecord->clusterBuffer();
        uint32_t offset16, prevOffset16 = 0;
        
        for (int glyphIdx = 0; glyphIdx < run.fGlyphCount + 1; ++glyphIdx) {
            uint32_t offset8;
            SkScalar glyphLeft;
            if (glyphIdx < run.fGlyphCount) {
                offset8 = clusterBuffer[glyphIdx] - clusterBuffer[0];
                glyphLeft = posBuffer[glyphIdx * 2];
            } else {
                // one step after last glyph
                offset8 = runRecord->textSize();
                SkScalar lastGlyphWidth;
                font.getWidths(&run.fGlyphIndices[run.fGlyphCount - 1], 1, &lastGlyphWidth);
                glyphLeft = prevGlyphLeft + lastGlyphWidth;
            }

            offset16 = conv.from8To16(offset8);

            // between prev and this: approximate
            if (target16 - runStart16 < offset16) {
                float ratio = (float) ((target16 - runStart16) - prevOffset16) / (float) (offset16 - prevOffset16);
                return skija::Point::fromSkPoint(env, {(glyphLeft - prevGlyphLeft) * ratio + prevGlyphLeft, posBuffer[1]});
            }

            // exactly this
            if (target16 - runStart16 == offset16)
                return skija::Point::fromSkPoint(env, {glyphLeft, posBuffer[1]});

            prevGlyphLeft = glyphLeft;
            prevOffset16 = offset16;
        }

        // to next run
        runStart16 += offset16;
        prevGlyphBaseline = posBuffer[1];
    }
    
    // no run matched, should not happen
    std::cout << "WARN: no run matched " << prevGlyphLeft << " " << prevGlyphBaseline << std::endl;
    return nullptr; // skija::Point::fromSkPoint(env, {prevGlyphLeft, prevGlyphBaseline});
}