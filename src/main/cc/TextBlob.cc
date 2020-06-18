#include <iostream>
#include <jni.h>
#include "SkTextBlob.h"
#include "interop.hh"

static void unrefTextBlob(SkTextBlob* ptr) {
    // std::cout << "Deleting [SkTextBlob " << ptr << "]" << std::endl;
    ptr->unref();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_TextBlob__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&unrefTextBlob));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_TextBlob__1nBounds(JNIEnv* env, jclass jclass, jlong ptr) {
    SkTextBlob* blob = reinterpret_cast<SkTextBlob*>(static_cast<uintptr_t>(ptr));
    SkRect bounds = blob->bounds();
    return skija::Rect::fromSkRect(env, blob->bounds());
}