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

extern "C" JNIEXPORT jintArray JNICALL Java_org_jetbrains_skija_TextLine__1nGetClusters
  (JNIEnv* env, jclass jclass, jlong ptr) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));
    std::vector<jint> clusters(instance->fGlyphCount);
    size_t idx = 0;
    for (auto& run: instance->fRuns) {
        memcpy(clusters.data() + idx, run.fClusters.data(), run.fGlyphCount * sizeof(uint32_t));
        idx += run.fGlyphCount;
    }
    SkASSERTF(idx == instance->fGlyphCount, "TextLine.cc: idx = %d != instance->fGlyphCount = %d", idx, instance->fGlyphCount);
    return javaIntArray(env, clusters);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_TextLine__1nGetOffsetAtCoord
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));

    if (instance->fRuns.empty())
        return 0;

    for (auto& run: instance->fRuns) {
        const SkPoint* pos = run.fPos;
        SkScalar glyphLeft = pos[0].fX;
        uint32_t idx = 0;
        for (; idx < run.fGlyphCount; ++idx) {
            SkScalar glyphRight = idx < run.fGlyphCount - 1 ? pos[idx + 1].fX : run.fPosition + run.fWidth;

            if (SkScalarNearlyEqual(glyphRight, glyphLeft))
                continue;

            SkScalar glyphCenter = (glyphLeft + glyphRight) / 2;
            if (x < glyphCenter)
                return run.fClusters[idx];

            glyphLeft = glyphRight;
        }
    }

    return instance->fRuns.back().fEnd16;
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_TextLine__1nGetCoordAtOffset
  (JNIEnv* env, jclass jclass, jlong ptr, jint offset16) {
    TextLine* instance = reinterpret_cast<TextLine*>(static_cast<uintptr_t>(ptr));

    for (auto& run: instance->fRuns) {
        if (offset16 > run.fEnd16)
            continue;

        for (size_t idx = 0; idx < run.fGlyphCount; ++idx) {
            SkScalar left = run.fPos[idx].fX;
            size_t this16 = run.fClusters[idx];
            if (this16 == offset16)
                return left;
            size_t next16 = idx < run.fGlyphCount - 1 ? run.fClusters[idx + 1] : run.fEnd16;
            if (offset16 > next16)
                continue;
            SkScalar right = idx < run.fGlyphCount - 1 ? run.fPos[idx + 1].fX : run.fPosition + run.fWidth;
            if (offset16 == next16)
                return right;
            float ratio = (offset16 - this16) / (float) (next16 - this16);
            return left + (right - left) * ratio;
        }
    }

    return instance->fWidth;
}