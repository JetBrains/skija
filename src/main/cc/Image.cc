#include <iostream>
#include <jni.h>
#include "SkData.h"
#include "SkImage.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image_nFromEncoded
  (JNIEnv* env, jclass jclass, jbyteArray encodedArray, jobject subsetObj) {
    jsize encodedLen = env->GetArrayLength(encodedArray);
    jbyte* encoded = env->GetByteArrayElements(encodedArray, 0);
    sk_sp<SkData> encodedData = SkData::MakeWithCopy(encoded, encodedLen);
    env->ReleaseByteArrayElements(encodedArray, encoded, 0);

    SkIRect* subset = skija::IRect::toSkIRect(env, subsetObj).release();
    SkImage* ptr = SkImage::MakeFromEncoded(encodedData, subset).release();

    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Image_nDimensions
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkImage* instance = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(ptr));
    return (uint64_t (instance->height()) << 32) | instance->width();
}