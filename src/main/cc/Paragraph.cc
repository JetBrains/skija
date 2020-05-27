#include <iostream>
#include <jni.h>
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