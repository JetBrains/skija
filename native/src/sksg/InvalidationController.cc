#include <jni.h>
#include "../interop.hh"
#include "SkSGInvalidationController.h"

using namespace sksg;

static void deleteInvalidationController(InvalidationController* controller) {
    delete controller;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_sksg_InvalidationController__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteInvalidationController));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_sksg_InvalidationController__1nMake
  (JNIEnv* env, jclass jclass) {
    InvalidationController* instance = new InvalidationController();
    return reinterpret_cast<jlong>(instance);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_sksg_InvalidationController__1nInvalidate
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat left, jfloat top, jfloat right, jfloat bottom, jfloatArray matrixArr) {
    InvalidationController* instance = reinterpret_cast<InvalidationController*>(static_cast<uintptr_t>(ptr));
    SkRect bounds {left, top, right, bottom};
    std::unique_ptr<SkMatrix> matrix = skMatrix(env, matrixArr);
    instance->inval(bounds, *matrix.get());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_sksg_InvalidationController__1nGetBounds
  (JNIEnv* env, jclass jclass, jlong ptr) {
    InvalidationController* instance = reinterpret_cast<InvalidationController*>(static_cast<uintptr_t>(ptr));
    return skija::Rect::fromSkRect(env, instance->bounds());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_sksg_InvalidationController__1nReset
  (JNIEnv* env, jclass jclass, jlong ptr) {
    InvalidationController* instance = reinterpret_cast<InvalidationController*>(static_cast<uintptr_t>(ptr));
    instance->reset();
}
