#include <iostream>
#include <jni.h>
#include "GrDirectContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_DirectContext__1nMakeGL
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(GrDirectContext::MakeGL().release());
}

#ifdef SK_METAL
#include "mtl/GrMtlBackendContext.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_DirectContext__1nMakeMetal
  (JNIEnv* env, jclass jclass, long devicePtr, long queuePtr) {
    GrMtlBackendContext backendContext = {};
    GrMTLHandle device = reinterpret_cast<GrMTLHandle>(static_cast<uintptr_t>(devicePtr));
    GrMTLHandle queue = reinterpret_cast<GrMTLHandle>(static_cast<uintptr_t>(queuePtr));
    backendContext.fDevice.retain(device);
    backendContext.fQueue.retain(queue);
    sk_sp<GrDirectContext> instance = GrDirectContext::MakeMetal(backendContext);
    return reinterpret_cast<jlong>(instance.release());
}
#endif

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

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_DirectContext__1nAbandon
  (JNIEnv* env, jclass jclass, jlong ptr, jint flags) {
    GrDirectContext* context = reinterpret_cast<GrDirectContext*>(static_cast<uintptr_t>(ptr));
    context->abandonContext();
}
