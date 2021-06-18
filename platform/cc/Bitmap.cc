#include <jni.h>
#include "SkBitmap.h"
#include "SkPixelRef.h"
#include "SkSamplingOptions.h"
#include "SkShader.h"
#include "interop.hh"

static void deleteBitmap(SkBitmap* instance) {
    // std::cout << "Deleting [SkBitmap " << instance << "]" << std::endl;
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteBitmap));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nMake
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(new SkBitmap());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nMakeClone
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return reinterpret_cast<jlong>(new SkBitmap(*instance));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSwap
  (JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkBitmap* other = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(otherPtr));
    instance->swap(*other);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Bitmap__1nGetImageInfo
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return skija::ImageInfo::toJava(env, instance->info());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Bitmap__1nGetRowBytesAsPixels
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->rowBytesAsPixels();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsNull
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->isNull();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetRowBytes
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->rowBytes();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nSetAlphaType
  (JNIEnv* env, jclass jclass, jlong ptr, jint alphaType) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->setAlphaType(static_cast<SkAlphaType>(alphaType));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nComputeByteSize
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->computeByteSize();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->isImmutable();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSetImmutable
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->setImmutable();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nReset
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->reset();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nComputeIsOpaque
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return SkBitmap::ComputeIsOpaque(*instance);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nSetImageInfo
  (JNIEnv* env, jclass jclass, jlong ptr, jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong rowBytes) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
    return instance->setInfo(imageInfo, rowBytes);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nAllocPixelsFlags
  (JNIEnv* env, jclass jclass, jlong ptr, jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jint flags) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
    return instance->tryAllocPixelsFlags(imageInfo, flags);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nAllocPixelsRowBytes
  (JNIEnv* env, jclass jclass, jlong ptr, jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong rowBytes) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
    return instance->tryAllocPixels(imageInfo, rowBytes);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nInstallPixels
  (JNIEnv* env, jclass jclass, jlong ptr, jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jbyteArray pixelsArr, jlong rowBytes) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));

    jsize len = env->GetArrayLength(pixelsArr);
    jbyte* pixels = new jbyte[len];
    env->GetByteArrayRegion(pixelsArr, 0, len, pixels);
    return instance->installPixels(imageInfo, pixels, rowBytes, deleteJBytes, nullptr);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nAllocPixels
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->tryAllocPixels();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetPixelRef
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkPixelRef* pixelRef = instance->pixelRef();
    pixelRef->ref();
    return reinterpret_cast<jlong>(pixelRef);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nGetPixelRefOrigin
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkIPoint origin = instance->pixelRefOrigin();
    return packIPoint(origin);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nSetPixelRef
  (JNIEnv* env, jclass jclass, jlong ptr, jlong pixelRefPtr, jint dx, jint dy) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkPixelRef* pixelRef = reinterpret_cast<SkPixelRef*>(static_cast<uintptr_t>(pixelRefPtr));
    instance->setPixelRef(sk_ref_sp<SkPixelRef>(pixelRef), dx, dy);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nIsReadyToDraw
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->readyToDraw();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Bitmap__1nGetGenerationId
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->getGenerationID();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nNotifyPixelsChanged
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->notifyPixelsChanged();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nEraseColor
  (JNIEnv* env, jclass jclass, jlong ptr, jint color) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->eraseColor(color);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Bitmap__1nErase
  (JNIEnv* env, jclass jclass, jlong ptr, jint color, jint left, jint top, jint right, jint bottom) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    instance->erase(color, {left, top, right, bottom});
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Bitmap__1nGetColor
  (JNIEnv* env, jclass jclass, jlong ptr, jint x, jint y) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->getColor(x, y);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_Bitmap__1nGetAlphaf
  (JNIEnv* env, jclass jclass, jlong ptr, jint x, jint y) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    return instance->getAlphaf(x, y);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Bitmap__1nExtractSubset
  (JNIEnv* env, jclass jclass, jlong ptr, jlong dstPtr, jint left, jint top, jint right, jint bottom) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkBitmap* dst = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(dstPtr));
    return instance->extractSubset(dst, {left, top, right, bottom});
}

extern "C" JNIEXPORT jbyteArray JNICALL Java_org_jetbrains_skija_Bitmap__1nReadPixels
  (JNIEnv* env, jclass jclass, jlong ptr, jint width, jint height, jint colorType, jint alphaType, jlong colorSpacePtr, jlong rowBytes, jint srcX, jint srcY) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkColorSpace* colorSpace = reinterpret_cast<SkColorSpace*>(static_cast<uintptr_t>(colorSpacePtr));
    SkImageInfo imageInfo = SkImageInfo::Make(width,
                                              height,
                                              static_cast<SkColorType>(colorType),
                                              static_cast<SkAlphaType>(alphaType),
                                              sk_ref_sp<SkColorSpace>(colorSpace));
    std::vector<jbyte> pixels(std::min(height, instance->height() - srcY) * rowBytes);
    if (instance->readPixels(imageInfo, pixels.data(), rowBytes, srcX, srcY))
        return javaByteArray(env, pixels);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Bitmap__1nExtractAlpha
  (JNIEnv* env, jclass jclass, jlong ptr, jlong dstPtr, jlong paintPtr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkBitmap* dst = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(dstPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    SkIPoint offset;
    if (instance->extractAlpha(dst, paint, &offset))
        return skija::IPoint::fromSkIPoint(env, offset);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Bitmap__1nPeekPixels
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    SkPixmap pixmap;
    if (instance->peekPixels(&pixmap))
        return env->NewDirectByteBuffer(pixmap.writable_addr(), pixmap.rowBytes() * pixmap.height());
    else
        return nullptr;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Bitmap__1nMakeShader
  (JNIEnv* env, jclass jclass, jlong ptr, jint tmx, jint tmy, jlong samplingMode, jfloatArray localMatrixArr) {
    SkBitmap* instance = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(ptr));
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);
    sk_sp<SkShader> shader = instance->makeShader(static_cast<SkTileMode>(tmx), static_cast<SkTileMode>(tmy), skija::SamplingMode::unpack(samplingMode), localMatrix.get());
    return reinterpret_cast<jlong>(shader.release());
}
