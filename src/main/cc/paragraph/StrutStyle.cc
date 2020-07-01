#include <iostream>
#include <jni.h>
#include <vector>
#include "../interop.hh"
#include "interop.hh"
#include "ParagraphStyle.h"

using namespace std;
using namespace skia::textlayout;

static void deleteStrutStyle(StrutStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteStrutStyle));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nMake
  (JNIEnv* env, jclass jclass) {
    StrutStyle* instance = new StrutStyle();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    StrutStyle* other = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(otherPtr));
    return *instance == *other;
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetFontFamilies
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return javaStringArray(env, instance->getFontFamilies());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetFontFamilies
  (JNIEnv* env, jclass jclass, jlong ptr, jobjectArray familiesArr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontFamilies(skStringVector(env, familiesArr));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetFontStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return skija::FontStyle::toJava(instance->getFontStyle());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetFontStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jint style) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontStyle(skija::FontStyle::fromJava(style));
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetFontSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getFontSize();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetFontSize
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat size) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setFontSize(size);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getHeight();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetHeight
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat height) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setHeight(height);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nGetLeading
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getLeading();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetLeading
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat leading) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setLeading(leading);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nIsEnabled
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getStrutEnabled();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetEnabled
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setStrutEnabled(value);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nIsHeightForced
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getForceStrutHeight();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetHeightForced
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setForceStrutHeight(value);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nIsHeightOverridden
  (JNIEnv* env, jclass jclass, jlong ptr) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getHeightOverride();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_StrutStyle__1nSetHeightOverridden
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    StrutStyle* instance = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(ptr));
    instance->setHeightOverride(value);
}
