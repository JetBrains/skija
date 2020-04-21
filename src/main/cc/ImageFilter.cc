#include <iostream>
#include <jni.h>
#include "SkImageFilter.h"
#include "SkImageFilters.h"
#include "SkRect.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadow(JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jlong color, jlong inputPtr) {
    sk_sp<SkImageFilter> input;
    if (inputPtr != 0)
        input = sk_ref_sp(reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr)));
    SkImageFilter* ptr = SkImageFilters::DropShadow(dx, dy, sigmaX, sigmaY, color, input).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadowCrop(JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jlong color, jlong inputPtr, jint cropL, jint cropT, jint cropR, jint cropB) {
    sk_sp<SkImageFilter> input;
    if (inputPtr != 0)
        input = sk_ref_sp(reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr)));
    SkIRect crop = SkIRect::MakeLTRB(cropL, cropT, cropR, cropB);
    SkImageFilter* ptr = SkImageFilters::DropShadow(dx, dy, sigmaX, sigmaY, color, input, &crop).release();
    return reinterpret_cast<jlong>(ptr);
}