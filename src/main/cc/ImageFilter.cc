#include <iostream>
#include <jni.h>
#include "SkColorFilter.h"
#include "SkImageFilter.h"
#include "SkImageFilters.h"
#include "SkPoint3.h"
#include "SkRect.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nAlphaThreshold
  (JNIEnv* env, jclass jclass, jlong regionPtr, jfloat innerMin, jfloat outerMax, jlong inputPtr, jobject cropObj) {
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::AlphaThreshold(*region, innerMin, outerMax, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nArithmetic
  (JNIEnv* env, jclass jclass, jfloat k1, jfloat k2, jfloat k3, jfloat k4, jboolean enforcePMColor, jlong bgPtr, jlong fgPtr, jobject cropObj) {
    SkImageFilter* bg = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(bgPtr));
    SkImageFilter* fg = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(fgPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Arithmetic(k1, k2, k3, k4, enforcePMColor, sk_ref_sp(bg), sk_ref_sp(fg), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nBlur
  (JNIEnv* env, jclass jclass, jfloat sigmaX, jfloat sigmaY, jint tileModeInt, jlong inputPtr, jobject cropObj) {
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Blur(sigmaX, sigmaY, tileMode, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nColorFilter
  (JNIEnv* env, jclass jclass, jlong colorFilterPtr, jlong inputPtr, jobject cropObj) {
    SkColorFilter* colorFilter = reinterpret_cast<SkColorFilter*>(static_cast<uintptr_t>(colorFilterPtr));
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::ColorFilter(sk_ref_sp(colorFilter), sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nCompose
  (JNIEnv* env, jclass jclass, jlong outerPtr, jlong innerPtr) {
    SkImageFilter* outer = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(outerPtr));
    SkImageFilter* inner = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(innerPtr));
    SkImageFilter* ptr = SkImageFilters::Compose(sk_ref_sp(outer), sk_ref_sp(inner)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDisplacementMap
  (JNIEnv* env, jclass jclass, jint xChanInt, jint yChanInt, jfloat scale, jlong displacementPtr, jlong colorPtr, jobject cropObj) {
    SkColorChannel xChan = static_cast<SkColorChannel>(xChanInt);
    SkColorChannel yChan = static_cast<SkColorChannel>(yChanInt);
    SkImageFilter* displacement = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(displacementPtr));
    SkImageFilter* color = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(colorPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::DisplacementMap(xChan, yChan, scale, sk_ref_sp(displacement), sk_ref_sp(color), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadow
  (JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jint color, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::DropShadow(dx, dy, sigmaX, sigmaY, color, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDropShadowOnly
  (JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jfloat sigmaX, jfloat sigmaY, jint color, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::DropShadowOnly(dx, dy, sigmaX, sigmaY, color, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nImage
  (JNIEnv* env, jclass jclass, jlong imagePtr, jfloat l0, jfloat t0, jfloat r0, jfloat b0, jfloat l1, jfloat t1, jfloat r1, jfloat b1, jint filterQualityInt) {
    SkImage* image = reinterpret_cast<SkImage*>(static_cast<uintptr_t>(imagePtr));
    SkFilterQuality filterQuality = static_cast<SkFilterQuality>(filterQualityInt);
    SkImageFilter* ptr = SkImageFilters::Image(sk_ref_sp(image), SkRect{l0, t0, r0, b0}, SkRect{l1, t1, r1, b1}, filterQuality).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nMagnifier
  (JNIEnv* env, jclass jclass, jfloat l, jfloat t, jfloat r, jfloat b, jfloat inset, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Magnifier(SkRect{l, t, r, b}, inset, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nMatrixConvolution
  (JNIEnv* env, jclass jclass, jint kernelW, jint kernelH, jfloatArray kernelArray, jfloat gain, jfloat bias, jint offsetX, jint offsetY, jint tileModeInt, jboolean convolveAlpha, jlong inputPtr, jobject cropObj) {
    jfloat* kernel = env->GetFloatArrayElements(kernelArray, 0);
    SkTileMode tileMode = static_cast<SkTileMode>(tileModeInt);
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::MatrixConvolution(SkISize{kernelW, kernelH}, kernel, gain, bias, SkIPoint{offsetX, offsetY}, tileMode, convolveAlpha, sk_ref_sp(input), crop).release();
    env->ReleaseFloatArrayElements(kernelArray, kernel, 0);
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nMatrixTransform
  (JNIEnv* env, jclass jclass, jfloatArray matrixArray, jint filterQualityInt, jlong inputPtr) {
    std::unique_ptr<SkMatrix> matrix = arrayToMatrix(env, matrixArray);
    SkFilterQuality filterQuality = static_cast<SkFilterQuality>(filterQualityInt);
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkImageFilter* ptr = SkImageFilters::MatrixTransform(*matrix, filterQuality, sk_ref_sp(input)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nMerge
  (JNIEnv* env, jclass jclass, jlongArray filtersArray, jobject cropObj) {
    jlong* f = env->GetLongArrayElements(filtersArray, 0);
    jsize len = env->GetArrayLength(filtersArray);
    sk_sp<SkImageFilter>* filters = new sk_sp<SkImageFilter>[len];
    for (int i = 0; i < len; ++i) {
        SkImageFilter* fi = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(f[i]));
        filters[i] = sk_ref_sp(fi);
    }
    env->ReleaseLongArrayElements(filtersArray, f, 0);
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Merge(filters, len, crop).release();
    delete[] filters;
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nOffset
  (JNIEnv* env, jclass jclass, jfloat dx, jfloat dy, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Offset(dx, dy, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nPaint
  (JNIEnv* env, jclass jclass, jlong paintPtr, jobject cropObj) {
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Paint(*paint, crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nPicture
  (JNIEnv* env, jclass jclass, jlong picturePtr, jfloat l, jfloat t, jfloat r, jfloat b) {
    SkPicture* picture = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(picturePtr));
    SkImageFilter* ptr = SkImageFilters::Picture(sk_ref_sp(picture), SkRect{l, t, r, b}).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nTile
  (JNIEnv* env, jclass jclass, jfloat l0, jfloat t0, jfloat r0, jfloat b0, jfloat l1, jfloat t1, jfloat r1, jfloat b1, jlong inputPtr) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkImageFilter* ptr = SkImageFilters::Tile(SkRect{l0, t0, r0, b0}, SkRect{l1, t1, r1, b1}, sk_ref_sp(input)).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nXfermode
  (JNIEnv* env, jclass jclass, jint blendModeInt, jlong bgPtr, jlong fgPtr, jobject cropObj) {
    SkBlendMode blendMode = static_cast<SkBlendMode>(blendModeInt);
    SkImageFilter* bg = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(bgPtr));
    SkImageFilter* fg = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(fgPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Xfermode(blendMode, sk_ref_sp(bg), sk_ref_sp(fg), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDilate
  (JNIEnv* env, jclass jclass, float rx, jfloat ry, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Dilate(rx, ry, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nErode
  (JNIEnv* env, jclass jclass, float rx, jfloat ry, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::Erode(rx, ry, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDistantLitDiffuse
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat z, jint lightColor, jfloat surfaceScale, jfloat kd, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::DistantLitDiffuse(SkPoint3{x, y, z}, lightColor, surfaceScale, kd, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nPointLitDiffuse
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat z, jint lightColor, jfloat surfaceScale, jfloat kd, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::PointLitDiffuse(SkPoint3{x, y, z}, lightColor, surfaceScale, kd, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nSpotLitDiffuse
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat z0, jfloat x1, jfloat y1, jfloat z1, jfloat falloffExponent, jfloat cutoffAngle, jint lightColor, jfloat surfaceScale, jfloat kd, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::SpotLitDiffuse(SkPoint3{x0, y0, z0}, SkPoint3{x1, y1, z1}, falloffExponent, cutoffAngle, lightColor, surfaceScale, kd, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nDistantLitSpecular
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat z, jint lightColor, jfloat surfaceScale, jfloat ks, jfloat shininess, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::DistantLitSpecular(SkPoint3{x, y, z}, lightColor, surfaceScale, ks, shininess, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nPointLitSpecular
  (JNIEnv* env, jclass jclass, jfloat x, jfloat y, jfloat z, jint lightColor, jfloat surfaceScale, jfloat ks, jfloat shininess, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::PointLitSpecular(SkPoint3{x, y, z}, lightColor, surfaceScale, ks, shininess, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_ImageFilter_nSpotLitSpecular
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat z0, jfloat x1, jfloat y1, jfloat z1, jfloat falloffExponent, jfloat cutoffAngle, jint lightColor, jfloat surfaceScale, jfloat ks, jfloat shininess, jlong inputPtr, jobject cropObj) {
    SkImageFilter* input = reinterpret_cast<SkImageFilter*>(static_cast<uintptr_t>(inputPtr));
    SkIRect* crop = objToIRect(env, cropObj).get();
    SkImageFilter* ptr = SkImageFilters::SpotLitSpecular(SkPoint3{x0, y0, z0}, SkPoint3{x1, y1, z1}, falloffExponent, cutoffAngle, lightColor, surfaceScale, ks, shininess, sk_ref_sp(input), crop).release();
    return reinterpret_cast<jlong>(ptr);
}