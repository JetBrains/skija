#include <jni.h>
#include "GrContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Context__1nMakeGL(JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(GrContext::MakeGL().release());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Context__1nFlush(JNIEnv* env, jclass jclass, jlong ptr) {
    GrContext* context = reinterpret_cast<GrContext*>(static_cast<uintptr_t>(ptr));
    context->flush(GrFlushInfo());
}