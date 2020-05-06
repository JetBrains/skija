#include <iostream>
#include <jni.h>
#include "SkColorFilter.h"
#include "SkShader.h"
#include "SkGradientShader.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nWithColorFilter
  (JNIEnv* env, jclass jclass, jlong ptr, jlong filterPtr) {
    SkShader* instance = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(ptr));
    SkColorFilter* filter = reinterpret_cast<SkColorFilter*>(static_cast<uintptr_t>(filterPtr));
    SkShader* newPtr = instance->makeWithColorFilter(sk_ref_sp(filter)).release();
    return reinterpret_cast<jlong>(newPtr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nLinearGradient
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    SkPoint pts[2] {SkPoint::Make(x0, y0), SkPoint::Make(x1, y1)};
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeLinear(pts, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nLinearGradientCS
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    SkPoint pts[2] {SkPoint::Make(x0, y0), SkPoint::Make(x1, y1)};
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeLinear(pts, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nRadialGradient
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat r, jintArray colorsArray, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeRadial(SkPoint::Make(x, y), r, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nRadialGradientCS
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat r, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeRadial(SkPoint::Make(x, y), r, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nTwoPointConicalGradient
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat r0, jfloat x1, jfloat y1, jfloat r1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeTwoPointConical(SkPoint::Make(x0, y0), r0, SkPoint::Make(x1, y1), r1, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nTwoPointConicalGradientCS
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat r0, jfloat x1, jfloat y1, jfloat r1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeTwoPointConical(SkPoint::Make(x0, y0), r0, SkPoint::Make(x1, y1), r1, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nSweepGradient
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat start, jfloat end, jfloat y1, jfloat r1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeSweep(x, y, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode, start, end, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nSweepGradientCS
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat start, jfloat end, jfloat y1, jfloat r1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt, jint flags, jfloatArray matrixArray) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkMatrix* localMatrix = arrayToMatrix(env, matrixArray).get();
    SkShader* ptr = SkGradientShader::MakeSweep(x, y, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, start, end, static_cast<uint32_t>(flags), localMatrix).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nEmpty(JNIEnv* env, jclass jclass) {
    SkShader* ptr = SkShaders::Empty().release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nColor(JNIEnv* env, jclass jclass, jint color) {
    SkShader* ptr = SkShaders::Color(color).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nColorCS(JNIEnv* env, jclass jclass, jfloat r, jfloat g, jfloat b, jfloat a, jlong colorSpacePtr) {
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkShader* ptr = SkShaders::Color(SkColor4f{r, g, b, a}, sk_ref_sp<SkColorSpace>(colorSpace)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nBlend(JNIEnv* env, jclass jclass, jint blendModeInt, jlong dstPtr, jlong srcPtr) {
    SkShader* dst = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(dstPtr));
    SkShader* src = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(srcPtr));
    SkBlendMode blendMode = static_cast<SkBlendMode>(blendModeInt);
    SkShader* ptr = SkShaders::Blend(blendMode, sk_ref_sp<SkShader>(dst), sk_ref_sp<SkShader>(src)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nLerp(JNIEnv* env, jclass jclass, jfloat t, jlong dstPtr, jlong srcPtr) {
    SkShader* dst = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(dstPtr));
    SkShader* src = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(srcPtr));
    SkShader* ptr = SkShaders::Lerp(t, sk_ref_sp<SkShader>(dst), sk_ref_sp<SkShader>(src)).release();
    return reinterpret_cast<jlong>(ptr);
}