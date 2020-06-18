#include <iostream>
#include <jni.h>
#include "ParagraphStyle.h"

using namespace std;
using namespace skia::textlayout;

static void deleteParagraphStyle(ParagraphStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nMake
  (JNIEnv* env, jclass jclass) {
    ParagraphStyle* instance = new ParagraphStyle();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraphStyle));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetTextStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    TextStyle* textStyle = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(textStylePtr));
    instance->setTextStyle(*textStyle);
}