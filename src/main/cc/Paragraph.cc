#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "Paragraph.h"

using namespace std;
using namespace skia::textlayout;

static void deleteParagraph(Paragraph* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Paragraph_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraph));
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Paragraph_nGetMaxWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    return instance->getMaxWidth();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Paragraph_nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    return instance->getHeight();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Paragraph_nGetMinIntrinsicWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    return instance->getMinIntrinsicWidth();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Paragraph_nGetMaxIntrinsicWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    return instance->getMaxIntrinsicWidth();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Paragraph_nLineNumber
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    return instance->lineNumber();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Paragraph_nLayout
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat width) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    instance->layout(width);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Paragraph_nPaint
  (JNIEnv* env, jclass jclass, jlong ptr, jlong canvasPtr, jfloat x, jfloat y) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    instance->paint(canvas, x, y);
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Paragraph_nGetLineMetrics
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Paragraph* instance = reinterpret_cast<Paragraph*>(static_cast<uintptr_t>(ptr));
    std::vector<LineMetrics> res;
    instance->getLineMetrics(res);
    jobjectArray resArray = env->NewObjectArray(res.size(), skija::LineMetrics::cls, nullptr);
    for (int i = 0; i < res.size(); ++i) {
      LineMetrics lm = res[i];
      jobject lmObj = env->NewObject(skija::LineMetrics::cls, skija::LineMetrics::ctor, lm.fStartIndex, lm.fEndIndex, lm.fEndExcludingWhitespaces, lm.fEndIncludingNewline, lm.fHardBreak, lm.fAscent, lm.fDescent, lm.fUnscaledAscent, lm.fHeight, lm.fWidth, lm.fLeft, lm.fBaseline, lm.fLineNumber);
      env->SetObjectArrayElement(resArray, i, lmObj);
    }
    return resArray;
}
