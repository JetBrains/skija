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

#ifdef SK_DIRECT3D
#include "d3d/GrD3DTypes.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_BackendRenderTarget__1nMakeDirect3D
  (JNIEnv* env, jclass jclass, jint width, jint height, jlong texturePtr, jint format, jint sampleCnt, jint levelCnt) {
    GrD3DTextureResourceInfo texResInfo = {};
    ID3D12Resource* resource = reinterpret_cast<ID3D12Resource*>(static_cast<uintptr_t>(texturePtr));
    texResInfo.fResource.retain(resource);
    texResInfo.fResourceState = D3D12_RESOURCE_STATE_COMMON;
    texResInfo.fFormat = static_cast<DXGI_FORMAT>(format);
    texResInfo.fSampleCount = static_cast<uint32_t>(sampleCnt);
    texResInfo.fLevelCount = static_cast<uint32_t>(levelCnt);
    GrBackendRenderTarget* instance = new GrBackendRenderTarget(width, height, texResInfo);
    return reinterpret_cast<jlong>(instance);
}
#endif //SK_DIRECT3D