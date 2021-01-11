#include <jni.h>
#include "../interop.hh"
#include "SkStream.h"
#include "SkSGInvalidationController.h"
#include "Skottie.h"

using namespace skottie;

static void deleteAnimation(Animation* animation) {
    delete animation;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteAnimation));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation__1nMakeFromString
  (JNIEnv* env, jclass jclass, jstring dataStr) {
    SkString data = skString(env, dataStr);
    sk_sp<Animation> instance = Animation::Make(data.c_str(), data.size());
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation__1nMakeFromFile
  (JNIEnv* env, jclass jclass, jstring pathStr) {
    SkString path = skString(env, pathStr);
    sk_sp<Animation> instance = Animation::MakeFromFile(path.c_str());
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_skottie_Animation__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkMemoryStream stream(sk_ref_sp(data));
    sk_sp<Animation> instance = Animation::Make(&stream);
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation__1nRender
  (JNIEnv* env, jclass jclass, jlong ptr, jlong canvasPtr, jfloat left, jfloat top, jfloat right, jfloat bottom, jint flags) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkRect bounds {left, top, right, bottom};
    instance->render(canvas, &bounds, flags);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation__1nSeek
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat t, jlong icPtr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    sksg::InvalidationController* controller = reinterpret_cast<sksg::InvalidationController*>(static_cast<uintptr_t>(icPtr));
    instance->seek(t, controller);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation__1nSeekFrame
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat t, jlong icPtr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    sksg::InvalidationController* controller = reinterpret_cast<sksg::InvalidationController*>(static_cast<uintptr_t>(icPtr));
    instance->seekFrame((double) t, controller);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_skottie_Animation__1nSeekFrameTime
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat t, jlong icPtr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    sksg::InvalidationController* controller = reinterpret_cast<sksg::InvalidationController*>(static_cast<uintptr_t>(icPtr));
    instance->seekFrameTime((double) t, controller);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetDuration
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    return (jfloat) instance->duration();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetFPS
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    return (jfloat) instance->fps();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetInPoint
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    return (jfloat) instance->inPoint();
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetOutPoint
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    return (jfloat) instance->outPoint();
}

extern "C" JNIEXPORT jstring JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetVersion
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    return javaString(env, instance->version());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_skottie_Animation__1nGetSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    Animation* instance = reinterpret_cast<Animation*>(static_cast<uintptr_t>(ptr));
    const SkSize& size = instance->size();
    return skija::Point::make(env, size.fWidth, size.fHeight);
}
