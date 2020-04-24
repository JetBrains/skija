#include <iostream>
#include <jni.h>
#include "SkColorSpace.h"
#include "interop.hh"

static void unrefColorSpace(SkColorSpace* ptr) {
    // std::cout << "Deleting [SkColorSpace " << ptr << "]" << std::endl;
    ptr->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ColorSpace_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefColorSpace));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ColorSpace_nMakeSRGB(JNIEnv* env, jclass jclass) {
    SkColorSpace* ptr = SkColorSpace::MakeSRGB().release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ColorSpace_nMakeSRGBLinear(JNIEnv* env, jclass jclass) {
    SkColorSpace* ptr = SkColorSpace::MakeSRGBLinear().release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_ColorSpace_nConvert
  (JNIEnv* env, jclass jclass, jlong fromPtr, jlong toPtr, float r, float g, float b, float a) {
    SkColorSpace* from = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(fromPtr));
    SkColorSpace* to = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(toPtr));

    skcms_TransferFunction fromFn;
    from->transferFn(&fromFn);

    skcms_TransferFunction toFn;
    to->invTransferFn(&toFn);

    float r1 = skcms_TransferFunction_eval(&toFn, skcms_TransferFunction_eval(&fromFn, r));
    float g1 = skcms_TransferFunction_eval(&toFn, skcms_TransferFunction_eval(&fromFn, g));
    float b1 = skcms_TransferFunction_eval(&toFn, skcms_TransferFunction_eval(&fromFn, b));
    float a1 = skcms_TransferFunction_eval(&toFn, skcms_TransferFunction_eval(&fromFn, a));
    return javaFloatArray(env, {r1, g1, b1, a1});
}