#include <jni.h>
#include "SkBitmap.h"
#include "interop.hh"

static void deleteBitmap(SkBitmap* instance) {
    // std::cout << "Deleting [SkBitmap " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBitmap));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nMake
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(new SkBitmap());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nMakeClone
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(new SkBitmap(*instance));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSwap
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkBitmap* other = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(otherPtr));
    instance->swap(*other);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Bitmap__1nGetImageInfo
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    const SkImageInfo& info = instance->info();
    return env->NewObject(skija::ImageInfo::cls, skija::ImageInfo::ctor,
        info.width(),
        info.height(),
        static_cast<jint>(info.colorType()),
        static_cast<jint>(info.alphaType()),
        reinterpret_cast<jlong>(info.refColorSpace().release()));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Bitmap__1nGetRowBytesAsPixels
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->rowBytesAsPixels();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsNull
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->isNull();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetRowBytes
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->rowBytes();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nSetAlphaType
  (JNIEnv* env, jclass jclass, jlong ptr, jint alphaType) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->setAlphaType(static_cast<SkAlphaType>(alphaType));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nComputeByteSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->computeByteSize();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->isImmutable();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSetImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->setImmutable();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsVolatile
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->isVolatile();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSetVolatile
  (JNIEnv* env, jclass jclass, jlong ptr, jboolean value) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->setIsVolatile(value);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nReset
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->reset();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nComputeIsOpaque
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return SkBitmap::ComputeIsOpaque(*instance);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nSetImageInfo
  (JNIEnv* env, jclass jclass, jlong ptr, jint colorType, jint alphaType, jlong colorSpacePtr, jint width, jint height, jlong rowBytes) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
    return instance->setInfo(imageInfo, rowBytes);
}


