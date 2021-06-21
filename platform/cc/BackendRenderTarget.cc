#include <iostream>
#include <jni.h>
#include "GrBackendSurface.h"

static void deleteBackendRenderTarget(GrBackendRenderTarget* rt) {
    // std::cout << "Deleting [GrBackendRenderTarget " << rt << "]" << std::endl;
    delete rt;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BackendRenderTarget__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBackendRenderTarget));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BackendRenderTarget__1nMakeGL
  (JNIEnv* env, jclass jclass, jint width, jint height, jint sampleCnt, jint stencilBits, jint fbId, jint fbFormat) {
    GrGLFramebufferInfo glInfo = { static_cast<unsigned int>(fbId), static_cast<unsigned int>(fbFormat) };
    GrBackendRenderTarget* instance = new GrBackendRenderTarget(width, height, sampleCnt, stencilBits, glInfo);
    return reinterpret_cast<jlong>(instance);
}

#ifdef SK_METAL
extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BackendRenderTarget__1nMakeMetal
  (JNIEnv* env, jclass jclass, jint width, jint height, jlong texturePtr) {
    GrMTLHandle texture = reinterpret_cast<GrMTLHandle>(static_cast<uintptr_t>(texturePtr));
    GrMtlTextureInfo fbInfo;
    fbInfo.fTexture.retain(texture);
    GrBackendRenderTarget* instance = new GrBackendRenderTarget(width, height, fbInfo);
    return reinterpret_cast<jlong>(instance);
}
#endif