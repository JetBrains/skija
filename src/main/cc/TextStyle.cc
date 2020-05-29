#include <iostream>
#include <jni.h>
#include <vector>
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

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextStyle_nSetFontFamilies
  (JNIEnv* env, jclass jclass, jlong ptr, jobjectArray familiesArray) {
    TextStyle* instance = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(ptr));

    std::vector<SkString> families;
    for (int i=0; i < env->GetArrayLength(familiesArray); ++i) {
        jstring family    = static_cast<jstring>(env->GetObjectArrayElement(familiesArray, i));
        jsize   len       = env->GetStringUTFLength(family);
        const char* chars = env->GetStringUTFChars(family, nullptr);
        families.push_back(SkString(chars, len));
        env->ReleaseStringUTFChars(family, chars);
    }

    instance->setFontFamilies(families);
}