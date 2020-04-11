#include <iostream>
#include <jni.h>
#include "SkTypeface.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_skija_SkTypeface_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
    const char* chars = env->GetStringUTFChars(path, nullptr);
    SkTypeface* ptr = SkTypeface::MakeFromFile(chars, index).release();
    env->ReleaseStringUTFChars(path, chars);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_SkTypeface_nMakeClone(JNIEnv* env, jclass jclass, jlong typefacePtr, jobjectArray variations) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    int variationCount = env->GetArrayLength(variations);
    maybeInitFontVariationClass(env);
    SkFontArguments::VariationPosition::Coordinate coordinates[variationCount];
    for (int i=0; i < variationCount; ++i) {
        jobject jvar = env->GetObjectArrayElement(variations, i);
        coordinates[i] = {
            static_cast<SkFourByteTag>(env->GetIntField(jvar, fontVariationClass->tagID)),
            env->GetFloatField(jvar, fontVariationClass->valueID)
        };
    }
    SkFontArguments arg = SkFontArguments().setVariationDesignPosition({coordinates, variationCount});
    SkTypeface* clone = typeface->makeClone(arg).release();
    return reinterpret_cast<jlong>(clone);
}
