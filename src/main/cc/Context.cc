#include <jni.h>
#include "RefCnt.hh"
#include "GrContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_skija_Context_nMakeGL(JNIEnv* env, jclass jclass) {
    return sk_sp_ref(GrContext::MakeGL());
}

extern "C" JNIEXPORT void JNICALL Java_skija_Context_nFlush(JNIEnv* env, jclass jclass, long ptr) {
    GrContext* context = reinterpret_cast<GrContext*>(static_cast<uintptr_t>(ptr));
    context->flush();
}