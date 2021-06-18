#include <jni.h>
#include "SkShadowUtils.h"
#include "interop.hh"

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_ShadowUtils__1nDrawShadow
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jlong pathPtr, jfloat zPlaneX, jfloat zPlaneY, jfloat zPlaneZ,
        jfloat lightPosX, jfloat lightPosY, jfloat lightPosZ, jfloat lightRadius, jint ambientColor, jint spotColor, jint flags) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkShadowUtils::DrawShadow(canvas, *path, {zPlaneX, zPlaneY, zPlaneZ}, {lightPosX, lightPosY, lightPosZ}, lightRadius, ambientColor, spotColor, flags);
}

extern "C" JNIEXPORT int JNICALL Java_org_jetbrains_skija_ShadowUtils__1nComputeTonalAmbientColor
  (JNIEnv* env, jclass jclass, jint ambientColor, jint spotColor) {
    SkColor outAmbientColor, outSpotColor;
    SkShadowUtils::ComputeTonalColors(ambientColor, spotColor, &outAmbientColor, &outSpotColor);
    return outAmbientColor;
}

extern "C" JNIEXPORT int JNICALL Java_org_jetbrains_skija_ShadowUtils__1nComputeTonalSpotColor
  (JNIEnv* env, jclass jclass, jint ambientColor, jint spotColor) {
    SkColor outAmbientColor, outSpotColor;
    SkShadowUtils::ComputeTonalColors(ambientColor, spotColor, &outAmbientColor, &outSpotColor);
    return outSpotColor;
}