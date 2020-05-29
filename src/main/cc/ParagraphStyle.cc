#include <iostream>
#include <jni.h>
#include "ParagraphStyle.h"

static void deleteParagraphStyle(skia::textlayout::ParagraphStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ParagraphStyle_nInit
  (JNIEnv* env, jclass jclass) {
    skia::textlayout::ParagraphStyle* instance = new skia::textlayout::ParagraphStyle();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ParagraphStyle_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraphStyle));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ParagraphStyle_nSetTextStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    skia::textlayout::ParagraphStyle* instance = reinterpret_cast<skia::textlayout::ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    skia::textlayout::TextStyle* textStyle = reinterpret_cast<skia::textlayout::TextStyle*>(static_cast<uintptr_t>(textStylePtr));
    instance->setTextStyle(*textStyle);
}