#include <iostream>
#include <jni.h>
#include "ParagraphBuilder.h"

using namespace std;
using namespace skia::textlayout;

static void deleteParagraphBuilder(ParagraphBuilder* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nInit
  (JNIEnv* env, jclass jclass, jlong paragraphStylePtr, jlong fontCollectionPtr) {
    ParagraphStyle* paragraphStyle = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(paragraphStylePtr));
    FontCollection* fontCollection = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(fontCollectionPtr));
    ParagraphBuilder* instance = ParagraphBuilder::make(*paragraphStyle, sk_ref_sp(fontCollection)).release();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraphBuilder));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nPushStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    TextStyle* textStyle = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(textStylePtr));
    instance->pushStyle(*textStyle);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nPopStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    instance->pop();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nAddText
  (JNIEnv* env, jclass jclass, jlong ptr, jstring text) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    const char* textChars = env->GetStringUTFChars(text, nullptr);
    size_t textLen = env->GetStringUTFLength(text);
    instance->addText(textChars, textLen);
    env->ReleaseStringUTFChars(text, textChars);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ParagraphBuilder_nBuild
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    Paragraph* paragraph = instance->Build().release();
    return reinterpret_cast<jlong>(paragraph);
}