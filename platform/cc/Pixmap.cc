#include <jni.h>
#include "interop.hh"
#include "SkPixmap.h"

static void deletePixmap(SkPixmap *pixmap) {
    delete pixmap;
}

extern "C" {
    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Pixmap__1nGetFinalizer
      (JNIEnv *env, jclass klass) {
        return ptrToJlong(&deletePixmap);
    }

    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Pixmap__1nMakeNull
      (JNIEnv *env, jclass klass) {
        return ptrToJlong(new SkPixmap());
    }

    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Pixmap__1nMake
      (JNIEnv *env, jclass klass,
      jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong pixelsPtr, jint rowBytes) {
        SkColorSpace* colorSpace = jlongToPtr<SkColorSpace*>(colorSpacePtr);
        SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
        return ptrToJlong(new SkPixmap(
            imageInfo, jlongToPtr<void*>(pixelsPtr), rowBytes));
    }

    JNIEXPORT void JNICALL Java_org_jetbrains_skija_Pixmap__1nReset
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        pixmap->reset();
    }

    JNIEXPORT void JNICALL Java_org_jetbrains_skija_Pixmap__1nResetWithInfo
      (JNIEnv *env, jclass klass, jlong ptr,
      jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong pixelsPtr, jint rowBytes) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkColorSpace* colorSpace = jlongToPtr<SkColorSpace*>(colorSpacePtr);
        SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
        pixmap->reset(imageInfo, jlongToPtr<void*>(pixelsPtr), rowBytes);
    }

    JNIEXPORT void JNICALL Java_org_jetbrains_skija_Pixmap__1nSetColorSpace
      (JNIEnv *env, jclass klass, jlong ptr, jlong colorSpacePtr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkColorSpace* colorSpace = jlongToPtr<SkColorSpace*>(colorSpacePtr);
        pixmap->setColorSpace(sk_ref_sp<SkColorSpace>(colorSpace));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nExtractSubset
      (JNIEnv *env, jclass klass, jlong ptr,
      jlong subsetPtr, jint l, jint t, jint w, jint h) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkPixmap* dst = jlongToPtr<SkPixmap*>(subsetPtr);
        return pixmap->extractSubset(dst, { l, t, w, h });
    }

    JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Pixmap__1nGetInfo
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        const SkImageInfo& imageInfo = pixmap->info();
        return skija::ImageInfo::toJava(env, imageInfo);
    }

    JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Pixmap__1nGetRowBytes
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jint>(pixmap->rowBytes());
    }

    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Pixmap__1nGetAddr
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return ptrToJlong(pixmap->addr());
    }

    JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Pixmap__1nGetRowBytesAsPixels
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jint>(pixmap->rowBytesAsPixels());
    }

    JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Pixmap__1nComputeByteSize
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jint>(pixmap->computeByteSize());
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nComputeIsOpaque
      (JNIEnv *env, jclass klass, jlong ptr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jboolean>(pixmap->computeIsOpaque());
    }

    JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Pixmap__1nGetColor
      (JNIEnv *env, jclass klass, jlong ptr, jint x, jint y) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jint>(pixmap->getColor(x, y));
    }

    JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Pixmap__1nGetAlphaF
      (JNIEnv *env, jclass klass, jlong ptr, jint x, jint y) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jfloat>(pixmap->getAlphaf(x, y));
    }

    JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Pixmap__1nGetAddrAt
      (JNIEnv *env, jclass klass, jlong ptr, jint x, jint y) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return reinterpret_cast<jlong>(pixmap->addr(x, y));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nReadPixels
      (JNIEnv *env, jclass klass, jlong ptr,
      jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong pixelsPtr, jint rowBytes) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkColorSpace* colorSpace = jlongToPtr<SkColorSpace*>(colorSpacePtr);
        SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
        return static_cast<jboolean>(pixmap->readPixels(imageInfo, jlongToPtr<void*>(pixelsPtr), rowBytes));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nReadPixelsFromPoint
      (JNIEnv *env, jclass klass, jlong ptr,
      jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong pixelsPtr, jint rowBytes,
      jint srcX, jint srcY) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkColorSpace* colorSpace = jlongToPtr<SkColorSpace*>(colorSpacePtr);
        SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
        return static_cast<jboolean>(pixmap->readPixels(imageInfo, jlongToPtr<void*>(pixelsPtr), rowBytes, srcX, srcY));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nReadPixelsToPixmap
      (JNIEnv *env, jclass klass, jlong ptr, jlong dstPixmapPtr) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkPixmap* dstPixmap = jlongToPtr<SkPixmap*>(dstPixmapPtr);
        return static_cast<jboolean>(pixmap->readPixels(*dstPixmap));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nReadPixelsToPixmapFromPoint
      (JNIEnv *env, jclass klass, jlong ptr, jlong dstPixmapPtr, jint srcX, jint srcY) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkPixmap* dstPixmap = jlongToPtr<SkPixmap*>(dstPixmapPtr);
        return static_cast<jboolean>(pixmap->readPixels(*dstPixmap, srcX, srcY));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nScalePixels
      (JNIEnv *env, jclass klass, jlong ptr, jlong dstPixmapPtr, jlong samplingOptions) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        SkPixmap* dstPixmap = jlongToPtr<SkPixmap*>(dstPixmapPtr);
        return static_cast<jboolean>(pixmap->scalePixels(*dstPixmap, skija::SamplingMode::unpack(samplingOptions)));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nErase
      (JNIEnv *env, jclass klass, jlong ptr, jint color) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jboolean>(pixmap->erase(color));
    }

    JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Pixmap__1nEraseSubset
      (JNIEnv *env, jclass klass, jlong ptr, jint color, jint l, jint t, jint w, jint h) {
        SkPixmap* pixmap = jlongToPtr<SkPixmap*>(ptr);
        return static_cast<jboolean>(pixmap->erase(color, { l, t, w, h }));
    }
}
