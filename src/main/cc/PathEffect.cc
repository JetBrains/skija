#include <iostream>
#include <jni.h>
#include "SkPathEffect.h"
#include "Sk1DPathEffect.h"
#include "Sk2DPathEffect.h"
#include "SkCornerPathEffect.h"
#include "SkDashPathEffect.h"
#include "SkDiscretePathEffect.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nSum
  (JNIEnv* env, jclass jclass, jlong firstPtr, jlong secondPtr) {
    SkPathEffect* first = reinterpret_cast<SkPathEffect*>(static_cast<uintptr_t>(firstPtr));
    SkPathEffect* second = reinterpret_cast<SkPathEffect*>(static_cast<uintptr_t>(secondPtr));
    SkPathEffect* ptr = SkPathEffect::MakeSum(sk_ref_sp(first), sk_ref_sp(second)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nCompose
  (JNIEnv* env, jclass jclass, jlong outerPtr, jlong innerPtr) {
    SkPathEffect* outer = reinterpret_cast<SkPathEffect*>(static_cast<uintptr_t>(outerPtr));
    SkPathEffect* inner = reinterpret_cast<SkPathEffect*>(static_cast<uintptr_t>(innerPtr));
    SkPathEffect* ptr = SkPathEffect::MakeCompose(sk_ref_sp(outer), sk_ref_sp(inner)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathEffect_nComputeFastBounds
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat l, jfloat t, jfloat r, jfloat b) {
    SkPathEffect* instance = reinterpret_cast<SkPathEffect*>(static_cast<uintptr_t>(ptr));
    SkRect res;
    instance->computeFastBounds(&res, SkRect{l, t, r, b});
    return skija::Rect::fromSkRect(env, res);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nPath1D
  (JNIEnv* env, jclass jclass, jlong pathPtr, jfloat advance, jfloat phase, jint styleInt) {
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkPath1DPathEffect::Style style = static_cast<SkPath1DPathEffect::Style>(styleInt);
    SkPathEffect* ptr = SkPath1DPathEffect::Make(*path, advance, phase, style).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nPath2D
  (JNIEnv* env, jclass jclass, jfloat scaleX, jfloat skewX, jfloat transX,
        jfloat skewY,  jfloat scaleY, jfloat transY,
        jfloat persp0, jfloat persp1, jfloat persp2,
        jlong pathPtr) {
    SkMatrix m = SkMatrix::MakeAll(scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2);
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkPathEffect* ptr = SkPath2DPathEffect::Make(m, *path).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nLine2D
  (JNIEnv* env, jclass jclass, jfloat width, jfloat scaleX, jfloat skewX, jfloat transX,
        jfloat skewY,  jfloat scaleY, jfloat transY,
        jfloat persp0, jfloat persp1, jfloat persp2) {
    SkMatrix m = SkMatrix::MakeAll(scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2);
    SkPathEffect* ptr = SkLine2DPathEffect::Make(width, m).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nCorner
  (JNIEnv* env, jclass jclass, jfloat radius) {
    SkPathEffect* ptr = SkCornerPathEffect::Make(radius).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nDash
  (JNIEnv* env, jclass jclass, jfloatArray intervalsArray, jfloat phase) {
    jsize len = env->GetArrayLength(intervalsArray);
    jfloat* intervals = env->GetFloatArrayElements(intervalsArray, 0);
    SkPathEffect* ptr = SkDashPathEffect::Make(intervals, len, phase).release();
    env->ReleaseFloatArrayElements(intervalsArray, intervals, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathEffect_nDiscrete
  (JNIEnv* env, jclass jclass, jfloat segLength, jfloat dev, jint seed) {
    SkPathEffect* ptr = SkDiscretePathEffect::Make(segLength, dev, static_cast<uint32_t>(seed)).release();
    return reinterpret_cast<jlong>(ptr);
}