#include <iostream>
#include <jni.h>
#include "SkFont.h"
#include "interop.hh"

static void deleteFont(SkFont* font) {
    // std::cout << "Deleting [SkFont " << font << "]" << std::endl;
    delete font;
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_SkFont_nInit(JNIEnv* env, jclass jclass, jlong typefacePtr, jfloat size) {
    SkTypeface* typeface = reinterpret_cast<SkTypeface*>(static_cast<uintptr_t>(typefacePtr));
    SkFont* obj = new SkFont(sk_sp<SkTypeface>(SkRef(typeface)), size);
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_SkFont_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteFont));
}