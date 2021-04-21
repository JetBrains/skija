#include <cstring>
#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "SkShaper.h"
#include "TextLine.hh"

static void unrefTextLine(TextLine* ptr) {
    ptr->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextLine__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefTextLine));
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetAscent
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fAscent;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetCapHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fCapHeight;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetXHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fXHeight;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetDescent
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fDescent;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetLeading
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fLeading;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return instance->fWidth;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    return -instance->fAscent + instance->fDescent + instance->fLeading;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextLine__1nGetTextBlob
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    if (instance->fBlob == nullptr)
        return 0;
    instance->fBlob->ref();
    return reinterpret_cast<jlong>(instance->fBlob.get());
}

extern "C" JNIEXPORT jshortArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetGlyphs
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jshort> glyphs(instance->fGlyphCount);
    size_t idx = 0;
    for (auto& run: instance->fRuns) {
        memcpy(glyphs.data() + idx, run.fGlyphs, run.fGlyphCount * sizeof(uint16_t));
        idx += run.fGlyphCount;
    }
    SkASSERTF(idx == instance->fGlyphCount, "TextLine.cc: idx = %d != instance->fGlyphCount = %d", idx, instance->fGlyphCount);
    return javaShortArray(env, glyphs);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetPositions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jfloat> positions(2 * instance->fGlyphCount);
    size_t idx = 0;
    for (auto& run: instance->fRuns) {
        memcpy(positions.data() + idx, run.fPos, run.fGlyphCount * sizeof(SkPoint));
        idx += 2 * run.fGlyphCount;
    }
    SkASSERTF(idx == 2 * instance->fGlyphCount, "TextLine.cc: idx = %d != 2 * instance->fGlyphCount = %d", idx, 2 * instance->fGlyphCount);
    return javaFloatArray(env, positions);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetRunPositions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jfloat> positions(instance->fRuns.size());
    for (size_t idx = 0; idx < positions.size(); ++idx)
        positions[idx] = instance->fRuns[idx].fPosition;
    return javaFloatArray(env, positions);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetBreakPositions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jfloat> positions;
    for (auto& run: instance->fRuns)
        positions.insert(positions.end(), run.fBreakPositions.begin(), run.fBreakPositions.end());
    return javaFloatArray(env, positions);
}

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetBreakOffsets
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jint> offsets;
    for (auto& run: instance->fRuns)
        offsets.insert(offsets.end(), run.fBreakOffsets.begin(), run.fBreakOffsets.end());
    return javaIntArray(env, offsets);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_TextLine__1nGetOffsetAtCoord
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));

    if (instance->fRuns.empty())
        return 0;

    for (auto& run: instance->fRuns) {
        SkScalar breakLeft = run.fBreakPositions[0];
        for (uint32_t idx = 0; idx < run.fBreakPositions.size() - 1; ++idx) {
            SkScalar breakRight = run.fBreakPositions[idx + 1];
            if (x < (breakLeft + breakRight) / 2)
                return run.fBreakOffsets[idx];
            breakLeft = breakRight;
        }
    }

    return (jint) instance->fRuns.back().fBreakOffsets.back();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_TextLine__1nGetLeftOffsetAtCoord
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));

    if (instance->fRuns.empty())
        return 0;

    for (auto& run: instance->fRuns) {
        for (uint32_t idx = 0; idx < run.fBreakPositions.size() - 1; ++idx) {
            SkScalar breakRight = run.fBreakPositions[idx + 1];
            if (x < breakRight)
                return run.fBreakOffsets[idx];
        }
    }

    return (jint) instance->fRuns.back().fBreakOffsets.back();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetCoordAtOffset
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset16) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));

    for (auto& run: instance->fRuns) {
        if (offset16 > run.fBreakOffsets.back())
            continue;

        for (uint32_t idx = 0; idx < run.fBreakPositions.size() - 1; ++idx) {
            if (offset16 < run.fBreakOffsets[idx] && idx > 0)
                return run.fBreakPositions[idx - 1];
            if (offset16 <= run.fBreakOffsets[idx])
                return run.fBreakPositions[idx];
        }
    }

    return instance->fWidth;
}