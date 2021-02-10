#include <iostream>
#include <jni.h>
#include "SkData.h"
#include "SkImage.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image__1nMakeFromEncoded
  (JNIEnv* env, jclass jclass, jbyteArray encodedArray) {
    jsize encodedLen = env->GetArrayLength(encodedArray);
    jbyte* encoded = env->GetByteArrayElements(encodedArray, 0);
    sk_sp<SkData> encodedData = SkData::MakeWithCopy(encoded, encodedLen);
    env->ReleaseByteArrayElements(encodedArray, encoded, 0);

    sk_sp<SkImage> image = SkImage::MakeFromEncoded(encodedData);

    return reinterpret_cast<jlong>(image.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image__1nGetDimensions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkImage* instance = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(ptr));
    return packTwoInts(instance->width(), instance->height());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image__1nEncodeToData
  (JNIEnv* env, jclass jclass, jlong ptr, jint format, jint quality) {
    SkImage* instance = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(ptr));
    SkData* data = instance->encodeToData(static_cast<SkEncodedImageFormat>(format), quality).release();
    return reinterpret_cast<jlong>(data);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image__1nMakeShader
  (JNIEnv* env, jclass jclass, jlong ptr, jint tmx, jint tmy, jint filterMode, jint mipmapMode, jfloatArray localMatrixArr) {
    SkImage* instance = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(ptr));
    SkSamplingOptions sampling(static_cast<SkFilterMode>(filterMode), static_cast<SkMipmapMode>(mipmapMode));
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);
    sk_sp<SkShader> shader = instance->makeShader(static_cast<SkTileMode>(tmx), static_cast<SkTileMode>(tmy), sampling, localMatrix.get());
    return reinterpret_cast<jlong>(shader.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image__1nMakeShaderCubic
  (JNIEnv* env, jclass jclass, jlong ptr, jint tmx, jint tmy, jfloat B, jfloat C, jfloatArray localMatrixArr) {
    SkImage* instance = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(ptr));
    SkSamplingOptions sampling(SkCubicResampler{B, C});
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);
    sk_sp<SkShader> shader = instance->makeShader(static_cast<SkTileMode>(tmx), static_cast<SkTileMode>(tmy), sampling, localMatrix.get());
    return reinterpret_cast<jlong>(shader.release());
}
