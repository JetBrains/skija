#include <jni.h>
#include "SkPixelRef.h"
#include "interop.hh"

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_PixelRef__1nGetWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    return instance->width();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_PixelRef__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    return instance->height();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PixelRef__1nGetRowBytes
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    return instance->rowBytes();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_PixelRef__1nGetGenerationId
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    return instance->getGenerationID();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_PixelRef__1nNotifyPixelsChanged
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    instance->notifyPixelsChanged();
}
extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_PixelRef__1nIsImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    return instance->isImmutable();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_PixelRef__1nSetImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPixelRef* instance = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(ptr));
    instance->setImmutable();
}