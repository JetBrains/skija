#include <iostream>
#include <jni.h>
#include "SkTextBlob.h"
#include "interop.hh"

static void unrefTextBlob(SkTextBlob* ptr) {
    // std::cout << "Deleting [SkTextBlob " << ptr << "]" << std::endl;
    ptr->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefTextBlob));
}
extern "C" JNIEXPORT jfloatArray JNICALL Java_org_jetbrains_skija_TextBlob_nBounds(JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* blob = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkRect bounds = blob->bounds();
    return javaFloatArray(env, {bounds.left(), bounds.top(), bounds.right(), bounds.bottom()});
}