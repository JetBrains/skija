#include <iostream>
#include <jni.h>
#include "SkPaint.h"

static void deletePaint(SkPaint* paint) {
    delete paint;
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Paint_nInit(JNIEnv* env, jclass jclass) {
    SkPaint* obj = new SkPaint();
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Paint_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deletePaint));
}

extern "C" JNIEXPORT void JNICALL Java_skija_Paint_nSetColor(JNIEnv* env, jclass jclass, jlong ptr, jlong color) {
    SkPaint* instance = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(ptr));
    instance->setColor(color);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Paint_nGetColor(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPaint* instance = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(ptr));
    return instance->getColor();
}