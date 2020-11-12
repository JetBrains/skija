#include <iostream>
#include <jni.h>
#include <string>
#include "ParagraphBuilder.h"
#include "../interop.hh"

using namespace std;
using namespace skia::textlayout;

static void deleteParagraphBuilder(ParagraphBuilder* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nMake
  (JNIEnv* env, jclass jclass, jlong paragraphStylePtr, jlong fontCollectionPtr) {
    ParagraphStyle* paragraphStyle = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(paragraphStylePtr));
    FontCollection* fontCollection = reinterpret_cast<FontCollection*>(static_cast<uintptr_t>(fontCollectionPtr));
    ParagraphBuilder* instance = ParagraphBuilder::make(*paragraphStyle, sk_ref_sp(fontCollection)).release();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteParagraphBuilder));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nPushStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    TextStyle* textStyle = reinterpret_cast<TextStyle*>(static_cast<uintptr_t>(textStylePtr));
    instance->pushStyle(*textStyle);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nPopStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong textStylePtr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    instance->pop();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nAddText
  (JNIEnv* env, jclass jclass, jlong ptr, jstring textString) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    SkString text = skString(env, textString);
    instance->addText(text.c_str(), text.size());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nAddPlaceholder
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat width, jfloat height, jint alignment, jint baselinePosition, jfloat baseline) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    instance->addPlaceholder({width, height, static_cast<PlaceholderAlignment>(alignment), static_cast<TextBaseline>(baselinePosition), baseline});
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nSetParagraphStyle
  (JNIEnv* env, jclass jclass, jlong ptr, jlong stylePtr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    ParagraphStyle* style = reinterpret_cast<ParagraphStyle*>(static_cast<uintptr_t>(stylePtr));
    instance->setParagraphStyle(*style);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_paragraph_ParagraphBuilder__1nBuild
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphBuilder* instance = reinterpret_cast<ParagraphBuilder*>(static_cast<uintptr_t>(ptr));
    Paragraph* paragraph = instance->Build().release();
    return reinterpret_cast<jlong>(paragraph);
}