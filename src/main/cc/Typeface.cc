#include <iostream>
#include <jni.h>
#include "SkTypeface.h"

extern "C" JNIEXPORT jlong JNICALL Java_skija_Typeface_nMakeFromFile(JNIEnv* env, jclass jclass, jstring path, jint index) {
    const char* chars = env->GetStringUTFChars(path, nullptr);
    SkTypeface* ptr = SkTypeface::MakeFromFile(chars, index).release();
    env->ReleaseStringUTFChars(path, chars);
    return reinterpret_cast<jlong>(ptr);
}
