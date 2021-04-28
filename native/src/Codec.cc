#include <iostream>
#include <jni.h>
#include "SkBitmap.h"
#include "SkCodec.h"
#include "SkData.h"
#include "interop.hh"

static void deleteCodec(SkCodec* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Codec__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteCodec));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Codec__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    std::unique_ptr<SkCodec> instance = SkCodec::MakeFromData(sk_ref_sp(data));
    return reinterpret_cast<jlong>(instance.release());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Codec__1nGetImageInfo
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return skija::ImageInfo::toJava(env, instance->getInfo());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Codec__1nGetSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return packISize(instance->dimensions());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Codec__1nGetEncodedOrigin
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getOrigin());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Codec__1nGetEncodedImageFormat
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getEncodedFormat());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Codec__1nReadPixels
  (JNIEnv* env, jclass jclass, jlong ptr, jlong bitmapPtr, jint frame, jint priorFrame) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    SkBitmap* bitmap = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(bitmapPtr));
    SkCodec::Options opts;
    opts.fFrameIndex = frame;
    opts.fPriorFrame = priorFrame;
    SkCodec::Result result = instance->getPixels(bitmap->info(), bitmap->getPixels(), bitmap->rowBytes(), &opts);
    return static_cast<jint>(result);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Codec__1nGetFrameCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return instance->getFrameCount();
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Codec__1nGetFrameInfo
  (JNIEnv* env, jclass jclass, jlong ptr, jint frame) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    SkCodec::FrameInfo info;
    instance->getFrameInfo(frame, &info);
    return skija::AnimationFrameInfo::toJava(env, info);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Codec__1nGetFramesInfo
  (JNIEnv* env, jclass jclass, jlong ptr, jint frame) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    SkCodec::FrameInfo info;
    std::vector<SkCodec::FrameInfo> frames = instance->getFrameInfo();
    jobjectArray res = env->NewObjectArray(frames.size(), skija::AnimationFrameInfo::cls, nullptr);
    if (java::lang::Throwable::exceptionThrown(env))
        return nullptr;
    for (int i = 0; i < frames.size(); ++i) {
        skija::AutoLocal<jobject> infoObj(env, skija::AnimationFrameInfo::toJava(env, frames[i]));
        env->SetObjectArrayElement(res, i, infoObj.get());
    }
    return res;
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Codec__1nGetRepetitionCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkCodec* instance = reinterpret_cast<SkCodec*>(static_cast<uintptr_t>(ptr));
    return instance->getRepetitionCount();
}