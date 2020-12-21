#include <iostream>
#include <jni.h>
#include "GrDirectContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_DirectContext__1nMakeGL
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(GrDirectContext::MakeGL().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_DirectContext__1nFlush
  (JNIEnv* env, jclass jclass, jlong ptr) {
    GrDirectContext* context = reinterpret_cast<GrDirectContext*>(static_cast<uintptr_t>(ptr));
    context->flush(GrFlushInfo());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_DirectContext__1nReset
  (JNIEnv* env, jclass jclass, jlong ptr, jint flags) {
    GrDirectContext* context = reinterpret_cast<GrDirectContext*>(static_cast<uintptr_t>(ptr));
    context->resetContext((uint32_t) flags);
}