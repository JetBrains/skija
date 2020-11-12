#include <iostream>
#include <jni.h>
#include "../interop.hh"
#include "interop.hh"
#include "ParagraphStyle.h"

using namespace std;
using namespace skia::textlayout;

static void deleteParagraphStyle(ParagraphStyle* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraphStyle));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nMake
  (JNIEnv* env, jclass jclass) {
    ParagraphStyle* instance = new ParagraphStyle();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nEquals
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    ParagraphStyle* other = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(otherPtr));
    return *instance == *other;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetStrutStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    StrutStyle* res = new StrutStyle();
    *res = instance->getStrutStyle();
    return reinterpret_cast<jlong>(res);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetStrutStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong stylePtr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    StrutStyle* style = reinterpret_cast<StrutStyle*>(static_cast<uintptr_t>(stylePtr));
    instance->setStrutStyle(*style);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetTextStyle
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    const TextStyle& style = instance->getTextStyle();
    TextStyle* res = new TextStyle(style, style.isPlaceholder());
    return reinterpret_cast<jlong>(res);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetTextStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    TextStyle* textStyle = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(textStylePtr));
    instance->setTextStyle(*textStyle);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetDirection
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getTextDirection());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetDirection
  (JNIEnv* env, jclass jclass, jlong ptr, jint textDirection) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setTextDirection(static_cast<TextDirection>(textDirection));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetAlignment
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getTextAlign());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetAlignment
  (JNIEnv* env, jclass jclass, jlong ptr, jint textAlign) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setTextAlign(static_cast<TextAlign>(textAlign));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetMaxLinesCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getMaxLines());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetMaxLinesCount
  (JNIEnv* env, jclass jclass, jlong ptr, jint count) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setMaxLines(count);
}

extern "C" JNIEXPORT jstring JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetEllipsis
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return instance->ellipsized() ? javaString(env, instance->getEllipsis()) : nullptr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetEllipsis
  (JNIEnv* env, jclass jclass, jlong ptr, jstring ellipsis) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setEllipsis(skString(env, ellipsis));
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return instance->getHeight();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetHeight
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat height) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setHeight(height);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetHeightMode
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getTextHeightBehavior());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nSetHeightMode
  (JNIEnv* env, jclass jclass, jlong ptr, jint heightMode) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->setTextHeightBehavior(static_cast<TextHeightBehavior>(heightMode));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nGetEffectiveAlignment
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->effective_align());
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nIsHintingEnabled
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    return instance->hintingIsOn();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphStyle__1nDisableHinting
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphStyle* instance = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(ptr));
    instance->turnHintingOff();
}