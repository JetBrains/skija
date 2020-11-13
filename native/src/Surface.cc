#include <iostream>
#include <jni.h>
#include "GrDirectContext.h"
#include "SkSurface.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Surface__1nMakeFromBackendRenderTarget
  (JNIEnv* env, jclass jclass, jlong pContext, jlong pBackendRenderTarget, jint surfaceOrigin, jint colorType, jlong colorSpacePtr) {
    GrDirectContext* context = reinterpret_cast<GrDirectContext*>(static_cast<uintptr_t>(pContext));
    GrBackendRenderTarget* backendRenderTarget = reinterpret_cast<GrBackendRenderTarget*>(static_cast<uintptr_t>(pBackendRenderTarget));
    GrSurfaceOrigin grSurfaceOrigin = static_cast<GrSurfaceOrigin>(surfaceOrigin);
    SkColorType skColorType = static_cast<SkColorType>(colorType);
    sk_sp<SkColorSpace> colorSpace = sk_ref_sp<SkColorSpace>(reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr)));
    // SkSurfaceProps props{SkSurfaceProps::kLegacyFontHost_InitType};
    SkSurfaceProps props(0, SkPixelGeometry::kRGB_V_SkPixelGeometry);

    sk_sp<SkSurface> surface = SkSurface::MakeFromBackendRenderTarget(
        static_cast<GrRecordingContext*>(context),
        *backendRenderTarget,
        grSurfaceOrigin,
        skColorType,
        colorSpace,
        &props,
        /* RenderTargetReleaseProc */ nullptr,
        /* ReleaseContext */ nullptr
    );
    return reinterpret_cast<jlong>(surface.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Surface__1nMakeRasterN32Premul
  (JNIEnv* env, jclass jclass, jint width, jint height) {
    sk_sp<SkSurface> surface = SkSurface::MakeRasterN32Premul(
        width, height,
        /* const SkSurfaceProps* */ nullptr
    );
    return reinterpret_cast<jlong>(surface.release());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Surface__1nGetCanvas
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSurface* surface = reinterpret_cast<SkSurface*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(surface->getCanvas());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Surface__1nMakeImageSnapshot
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSurface* surface = reinterpret_cast<SkSurface*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(surface->makeImageSnapshot().release());
}
