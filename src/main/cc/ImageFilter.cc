#include <iostream>
#include <jni.h>
#include "SkImageFilter.h"
#include "SkImageFilters.h"
#include "SkRect.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadow(JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jlong color, jlong inputPtr, jobject cropObj) {
    sk_sp<SkImageFilter> input;
    if (inputPtr != 0)
        input = sk_ref_sp(reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr)));
    SkImageFilter* ptr;
    if (cropObj != nullptr) {
        SkIRect crop = skIRectFromObj(env, cropObj);
        ptr = SkImageFilters::DropShadow(dx, dy, sigmaX, sigmaY, color, input, &crop).release();
    } else
        ptr = SkImageFilters::DropShadow(dx, dy, sigmaX, sigmaY, color, input, nullptr).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadowOnly(JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jlong color, jlong inputPtr, jobject cropObj) {
    sk_sp<SkImageFilter> input;
    if (inputPtr != 0)
        input = sk_ref_sp(reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr)));
    SkImageFilter* ptr;
    if (cropObj != nullptr) {
        SkIRect crop = skIRectFromObj(env, cropObj);
        ptr = SkImageFilters::DropShadowOnly(dx, dy, sigmaX, sigmaY, color, input, &crop).release();
    } else
        ptr = SkImageFilters::DropShadowOnly(dx, dy, sigmaX, sigmaY, color, input, nullptr).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nBlur(JNIEnv* env, jclass jclass, jfloat sigmaX, jfloat sigmaY, jint tileModeOrd, jlong inputPtr, jobject cropObj) {
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeOrd);

    sk_sp<SkImageFilter> input;
    if (inputPtr != 0)
        input = sk_ref_sp(reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr)));
    
    SkImageFilter* ptr;
    if (cropObj != nullptr) {
        SkIRect crop = skIRectFromObj(env, cropObj);
        ptr = SkImageFilters::Blur(sigmaX, sigmaY, tileMode, input, &crop).release();
    } else
        ptr = SkImageFilters::Blur(sigmaX, sigmaY, tileMode, input, nullptr).release();
    
    return reinterpret_cast<jlong>(ptr);
}