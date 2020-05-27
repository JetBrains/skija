#include <iostream>
#include <jni.h>
#include "TextStyle.h"

using namespace std;
using namespace skia::textlayout;

static void deleteTextStyle(TextStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextStyle_nInit
  (JNIEnv* env, jclass jclass) {
    TextStyle* instance = new TextStyle();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextStyle_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteTextStyle));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextStyle_nSetColor
  (JNIEnv* env, jclass jclass, jlong ptr, jint color) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setColor(color);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextStyle_nSetFontSize
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat size) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontSize(size);
}