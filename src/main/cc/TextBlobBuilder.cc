#include <iostream>
#include <jni.h>
#include "SkTextBlob.h"
#include "interop.hh"

static void deleteTextBlobBuilder(SkTextBlobBuilder* ptr) {
    delete ptr;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteTextBlobBuilder));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nMake
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(new SkTextBlobBuilder());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nBuild
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlobBuilder* instance = reinterpret_cast<SkTextBlobBuilder*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(instance->make().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nAppendRun
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontPtr, jshortArray glyphsArr, jfloat x, jfloat y, jobject boundsObj) {
    SkTextBlobBuilder* instance = reinterpret_cast<SkTextBlobBuilder*>(static_cast<uintptr_t>(ptr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    jsize len = env->GetArrayLength(glyphsArr);
    std::unique_ptr<SkRect> bounds = skija::Rect::toSkRect(env, boundsObj);
    SkTextBlobBuilder::RunBuffer run = instance->allocRun(*font, len, x, y, bounds.get());
    env->GetShortArrayRegion(glyphsArr, 0, len, reinterpret_cast<jshort*>(run.glyphs));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nAppendRunPosH
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontPtr, jshortArray glyphsArr, jfloatArray xsArr, jfloat y, jobject boundsObj) {
    SkTextBlobBuilder* instance = reinterpret_cast<SkTextBlobBuilder*>(static_cast<uintptr_t>(ptr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    jsize len = env->GetArrayLength(glyphsArr);
    std::unique_ptr<SkRect> bounds = skija::Rect::toSkRect(env, boundsObj);
    SkTextBlobBuilder::RunBuffer run = instance->allocRunPosH(*font, len, y, bounds.get());
    env->GetShortArrayRegion(glyphsArr, 0, len, reinterpret_cast<jshort*>(run.glyphs));
    env->GetFloatArrayRegion(xsArr, 0, len, reinterpret_cast<jfloat*>(run.pos));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nAppendRunPos
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontPtr, jshortArray glyphsArr, jfloatArray posArr, jobject boundsObj) {
    SkTextBlobBuilder* instance = reinterpret_cast<SkTextBlobBuilder*>(static_cast<uintptr_t>(ptr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    jsize len = env->GetArrayLength(glyphsArr);
    std::unique_ptr<SkRect> bounds = skija::Rect::toSkRect(env, boundsObj);
    SkTextBlobBuilder::RunBuffer run = instance->allocRunPos(*font, len, bounds.get());
    env->GetShortArrayRegion(glyphsArr, 0, len, reinterpret_cast<jshort*>(run.glyphs));
    env->GetFloatArrayRegion(posArr, 0, len * 2, reinterpret_cast<jfloat*>(run.pos));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_TextBlobBuilder__1nAppendRunRSXform
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontPtr, jshortArray glyphsArr, jfloatArray xformArr) {
    SkTextBlobBuilder* instance = reinterpret_cast<SkTextBlobBuilder*>(static_cast<uintptr_t>(ptr));
    SkFont* font = reinterpret_cast<SkFont*>(static_cast<uintptr_t>(fontPtr));
    jsize len = env->GetArrayLength(glyphsArr);
    SkTextBlobBuilder::RunBuffer run = instance->allocRunRSXform(*font, len);
    env->GetShortArrayRegion(glyphsArr, 0, len, reinterpret_cast<jshort*>(run.glyphs));
    env->GetFloatArrayRegion(xformArr, 0, len * 4, reinterpret_cast<jfloat*>(run.pos));
}