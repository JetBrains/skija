#include <iostream>
#include <jni.h>
#include "SkTypeface.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
    const char* chars = env->GetStringUTFChars(path, nullptr);
    SkTypeface* ptr = SkTypeface::MakeFromFile(chars, index).release();
    env->ReleaseStringUTFChars(path, chars);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Typeface_nMakeClone(JNIEnv* env, jclass jclass, jlong typefacePtr, jobjectArray variations) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    int variationCount = env->GetArrayLength(variations);
    SkFontArguments::VariationPosition::Coordinate coordinates[variationCount];
    for (int i=0; i < variationCount; ++i) {
        jobject jvar = env->GetObjectArrayElement(variations, i);
        coordinates[i] = {
            static_cast<SkFourByteTag>(env->GetIntField(jvar, skija::FontVariation::tag)),
            env->GetFloatField(jvar, skija::FontVariation::value)
        };
        env->DeleteLocalRef(jvar);
    }
    SkFontArguments arg = SkFontArguments().setVariationDesignPosition({coordinates, variationCount});
    SkTypeface* clone = typeface->makeClone(arg).release();
    return reinterpret_cast<jlong>(clone);
}
