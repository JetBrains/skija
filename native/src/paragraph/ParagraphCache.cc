#include <iostream>
#include <jni.h>
#include "ParagraphCache.h"
#include "ParagraphStyle.h"

using namespace skia::textlayout;

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nAbandon
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    instance->abandon();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nReset
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    instance->reset();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nUpdateParagraph
  (JNIEnv* env, jclass jclass, jlong ptr, jlong paragraphPtr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    ParagraphImpl* paragraph = reinterpret_cast<ParagraphImpl*>(static_cast<uintptr_t>(paragraphPtr));
    return instance->updateParagraph(paragraph);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nFindParagraph
  (JNIEnv* env, jclass jclass, jlong ptr, jlong paragraphPtr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    ParagraphImpl* paragraph = reinterpret_cast<ParagraphImpl*>(static_cast<uintptr_t>(paragraphPtr));
    return instance->findParagraph(paragraph);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nPrintStatistics
  (JNIEnv* env, jclass jclass, jlong ptr, jlong paragraphPtr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    instance->printStatistics();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nSetEnabled
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    instance->turnOn(value);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_paragraph_ParagraphCache__1nGetCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    ParagraphCache* instance = reinterpret_cast<ParagraphCache*>(static_cast<uintptr_t>(ptr));
    return instance->count();
}
