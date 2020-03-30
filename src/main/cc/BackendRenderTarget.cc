#include <iostream>
#include <jni.h>
#include "GrBackendSurface.h"

static void deleteBackendRenderTarget(GrBackendRenderTarget* rt) {
    std::cout << "Deleting [GrBackendRenderTarget " << rt << "]" << std::endl;
    delete rt;
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_BackendRenderTarget_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBackendRenderTarget));
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_BackendRenderTarget_nNewGL
  (JNIEnv* env, jclass jclass, jint width, jint height, jint sampleCnt, jint stencilBits, jlong fbId, jlong fbFormat) {
    GrGLFramebufferInfo glInfo = { static_cast<unsigned int>(fbId), static_cast<unsigned int>(fbFormat) };
    GrBackendRenderTarget* obj = new GrBackendRenderTarget(width, height, sampleCnt, stencilBits, glInfo);
    return reinterpret_cast<jlong>(obj);
}