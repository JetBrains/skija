#include <iostream>
#include <jni.h>
#include "interop.hh"
#include "SkData.h"
#include "SkPicture.h"
#include "SkShader.h"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Picture__1nMakeFromData
  (JNIEnv* env, jclass jclass, jlong dataPtr) {
    SkData* data = reinterpret_cast<SkData*>(static_cast<uintptr_t>(dataPtr));
    SkPicture* instance = SkPicture::MakeFromData(data).release();
    return reinterpret_cast<jlong>(instance);
}

class BooleanSupplierAbort: public SkPicture::AbortCallback {
public:
    BooleanSupplierAbort(JNIEnv* env, jobject supplier) {
        this->env = env;
        this->supplier = supplier;
    }
    bool abort() override {
        bool res = env->CallBooleanMethod(supplier, java::util::function::BooleanSupplier::apply);
        if (java::lang::Throwable::exceptionThrown(env))
          return false;
        return res;
    }
private:
    JNIEnv* env;
    jobject supplier;
};

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Picture__1nPlayback
  (JNIEnv* env, jclass jclass, jlong ptr, jlong canvasPtr, jobject abort) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    if (abort == nullptr) {
        instance->playback(canvas, nullptr);
    } else {
        BooleanSupplierAbort callback(env, abort);
        instance->playback(canvas, &callback);
    }
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Picture__1nGetCullRect
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    return skija::Rect::fromSkRect(env, instance->cullRect());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Picture__1nGetUniqueId
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    return instance->uniqueID();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Picture__1nSerializeToData
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    SkData* data = instance->serialize().release();
    return reinterpret_cast<jlong>(data);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Picture__1nMakePlaceholder
  (JNIEnv* env, jclass jclass, jfloat left, jfloat top, jfloat right, jfloat bottom) {
    SkRect cull = SkRect::MakeLTRB(left, top, right, bottom);
    SkPicture* instance = SkPicture::MakePlaceholder(cull).release();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Picture__1nGetApproximateOpCount
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    return instance->approximateOpCount();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Picture__1nGetApproximateBytesUsed
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    return instance->approximateBytesUsed();
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Picture__1nMakeShader
  (JNIEnv* env, jclass jclass, jlong ptr, jint tmxValue, jint tmyValue, jint filterModeValue, jfloatArray localMatrixArr, jobject tileRectObj) {
    SkPicture* instance = reinterpret_cast<SkPicture*>(static_cast<uintptr_t>(ptr));
    SkTileMode tmx = static_cast<SkTileMode>(tmxValue);
    SkTileMode tmy = static_cast<SkTileMode>(tmyValue);
    SkFilterMode filterMode = static_cast<SkFilterMode>(filterModeValue);
    std::unique_ptr<SkMatrix> localMatrix = skMatrix(env, localMatrixArr);
    std::unique_ptr<SkRect> tileRect = skija::Rect::toSkRect(env, tileRectObj);
    SkShader* shader = instance->makeShader(tmx, tmy, filterMode, localMatrix.get(), tileRect.get()).release();
    return reinterpret_cast<jlong>(shader);
}