#include <jni.h>
#include "interop.hh"
#include "SkString.h"

static void deleteString(SkString* instance) {
    // std::cout << "Deleting [SkString " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ManagedString__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteString));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ManagedString__1nMake
  (JNIEnv* env, jclass jclass, jstring textStr) {
    SkString* text = new SkString(skString(env, textStr));
    return reinterpret_cast<jlong>(text);
}
