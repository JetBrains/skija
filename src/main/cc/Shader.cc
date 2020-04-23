#include <iostream>
#include <jni.h>
#include "SkShader.h"
#include "SkGradientShader.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeLinearGradient
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkPoint pts[2] { SkPoint::Make(x0, y0), SkPoint::Make(x1, y1) };
    SkShader* ptr = SkGradientShader::MakeLinear(pts, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeLinearGradientCS
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkPoint pts[2] { SkPoint::Make(x0, y0), SkPoint::Make(x1, y1) };
    SkShader* ptr = SkGradientShader::MakeLinear(pts, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, 0, nullptr).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeRadialGradient
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat r, jintArray colorsArray, jfloatArray posArray, jint tileModeInt) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeRadial(SkPoint::Make(x, y), r, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeRadialGradientCS
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat r, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeRadial(SkPoint::Make(x, y), r, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, 0, nullptr).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeTwoPointConicalGradient
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat r0, jfloat x1, jfloat y1, jfloat r1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeTwoPointConical(SkPoint::Make(x0, y0), r0, SkPoint::Make(x1, y1), r1, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeTwoPointConicalGradientCS
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat r0, jfloat x1, jfloat y1, jfloat r1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeTwoPointConical(SkPoint::Make(x0, y0), r0, SkPoint::Make(x1, y1), r1, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, 0, nullptr).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeSweepGradient
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat start, jfloat end, jfloat y1, jfloat r1, jintArray colorsArray, jfloatArray posArray, jint tileModeInt) {
    int* colors = env->GetIntArrayElements(colorsArray, nullptr);
    float* pos = posArray == nullptr ? nullptr : env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeSweep(x, y, reinterpret_cast<SkColor*>(colors), pos, env->GetArrayLength(colorsArray), tileMode, start, end, 0, nullptr).release();
    env->ReleaseIntArrayElements(colorsArray, colors, 0);
    if (posArray != nullptr)
        env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeSweepGradientCS
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat start, jfloat end, jfloat y1, jfloat r1, jfloatArray colorsArray, jlong colorSpacePtr, jfloatArray posArray, jint tileModeInt) {
    float* colors = env->GetFloatArrayElements(colorsArray, nullptr);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    float* pos = env->GetFloatArrayElements(posArray, nullptr);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkShader* ptr = SkGradientShader::MakeSweep(x, y, reinterpret_cast<SkColor4f*>(colors), colorSpace, pos, env->GetArrayLength(colorsArray), tileMode, start, end, 0, nullptr).release();
    env->ReleaseFloatArrayElements(colorsArray, colors, 0);
    env->ReleaseFloatArrayElements(posArray, pos, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeEmpty(JNIEnv* env, jclass jclass) {
    SkShader* ptr = SkShaders::Empty().release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeColor(JNIEnv* env, jclass jclass, jint color) {
    SkShader* ptr = SkShaders::Color(color).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeColorCS(JNIEnv* env, jclass jclass, jfloat r, jfloat g, jfloat b, jfloat a, jlong colorSpacePtr) {
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkShader* ptr = SkShaders::Color(SkColor4f{r, g, b, a}, sk_ref_sp<SkColorSpace>(colorSpace)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeBlend(JNIEnv* env, jclass jclass, jint blendModeInt, jlong dstPtr, jlong srcPtr) {
    SkShader* dst = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(dstPtr));
    SkShader* src = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(srcPtr));
    SkBlendMode blendMode = static_cast<SkBlendMode>(blendModeInt);
    SkShader* ptr = SkShaders::Blend(blendMode, sk_ref_sp<SkShader>(dst), sk_ref_sp<SkShader>(src)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Shader_nMakeLerp(JNIEnv* env, jclass jclass, jfloat t, jlong dstPtr, jlong srcPtr) {
    SkShader* dst = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(dstPtr));
    SkShader* src = reinterpret_cast<SkShader*>(static_cast<uintptr_t>(srcPtr));
    SkShader* ptr = SkShaders::Lerp(t, sk_ref_sp<SkShader>(dst), sk_ref_sp<SkShader>(src)).release();
    return reinterpret_cast<jlong>(ptr);
}