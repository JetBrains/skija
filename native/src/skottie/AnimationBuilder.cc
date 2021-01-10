#include <jni.h>
#include "../interop.hh"
#include "SkStream.h"
#include "Skottie.h"
#include "SkFontMgr.h"

using namespace skottie;

static void deleteBuilder(Animation::Builder* builder) {
    delete builder;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBuilder));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nCreateInstance
  (JNIEnv* env, jclass jclass, jint flags) {
    return reinterpret_cast<jlong>(new Animation::Builder(flags));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nSetFontManager
  (JNIEnv* env, jclass jclass, jlong ptr, jlong fontMgrPtr) {
    Animation::Builder* instance = reinterpret_cast<Animation::Builder*>(static_cast<uintptr_t>(ptr));
    sk_sp<SkFontMgr> fontMgr = sk_ref_sp(reinterpret_cast<SkFontMgr*>(static_cast<uintptr_t>(fontMgrPtr)));
    instance->setFontManager(fontMgr);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nSetLogger
  (JNIEnv* env, jclass jclass, jlong ptr, jlong loggerPtr) {
    Animation::Builder* instance = reinterpret_cast<Animation::Builder*>(static_cast<uintptr_t>(ptr));
    sk_sp<skottie::Logger> logger = sk_ref_sp(reinterpret_cast<skottie::Logger*>(static_cast<uintptr_t>(loggerPtr)));
    instance->setLogger(logger);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nMake
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkString* data = reinterpret_cast<SkString*>(static_cast<uintptr_t>(dataPtr));
    sk_sp<Animation> instance = Animation::Make(data->c_str(), data->size());
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nMakeFromFile
  (JNIEnv* env, jclass jclass, jstring pathStr) {
    SkString path = skString(env, pathStr);
    sk_sp<Animation> instance = Animation::MakeFromFile(path.c_str());
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation_00024Builder__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkMemoryStream stream(sk_ref_sp(data));
    sk_sp<Animation> instance = Animation::Make(&stream);
    return reinterpret_cast<jlong>(instance.release());
}
