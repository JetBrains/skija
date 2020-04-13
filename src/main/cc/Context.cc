#include <jni.h>
#include "GrContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Context_nMakeGL(JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(GrContext::MakeGL().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Context_nFlush(JNIEnv* env, jclass jclass, long ptr) {
    GrContext* context = reinterpret_cast<GrContext*>(static_cast<uintptr_t>(ptr));
    context->flush();
}