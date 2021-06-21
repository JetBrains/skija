#include <jni.h>
#include "SkPathMeasure.h"
#include "interop.hh"

static void deletePathMeasure(SkPathMeasure* instance) {
    delete instance;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deletePathMeasure));
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathMeasure__1nMake
  (JNIEnv* env, jclass jclass) {
    return reinterpret_cast<jlong>(new SkPathMeasure());
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathMeasure__1nMakePath
  (JNIEnv* env, jclass jclass, jlong pathPtr, jboolean forceClosed, jfloat resScale) {
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    return reinterpret_cast<jlong>(new SkPathMeasure(*path, forceClosed, resScale));
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_PathMeasure__1nSetPath
  (JNIEnv* env, jclass jclass, jlong ptr, jlong pathPtr, jboolean forceClosed) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    instance->setPath(path, forceClosed);
}

extern "C" JNIEXPORT jfloat JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetLength
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    return instance->getLength();
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetPosition
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat distance) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkPoint position;
    if (instance->getPosTan(distance, &position, nullptr))
        return skija::Point::fromSkPoint(env, position);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetTangent
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat distance) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkVector tangent;
    if (instance->getPosTan(distance, nullptr, &tangent))
        return skija::Point::fromSkPoint(env, tangent);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetRSXform
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat distance) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkPoint position;
    SkVector tangent;
    if (instance->getPosTan(distance, &position, &tangent))
        return env->NewObject(skija::RSXform::cls, skija::RSXform::ctor, tangent.fX, tangent.fY, position.fX, position.fY);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetMatrix
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat distance, jboolean getPosition, jboolean getTangent) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkMatrix matrix;
    int flags = 0;
    
    if (getPosition)
        flags |= SkPathMeasure::MatrixFlags::kGetPosition_MatrixFlag;
    if (getTangent)
        flags |= SkPathMeasure::MatrixFlags::kGetTangent_MatrixFlag;

    if (instance->getMatrix(distance, &matrix, static_cast<SkPathMeasure::MatrixFlags>(flags))) {
        std::vector<float> floats(9);
        matrix.get9(floats.data());
        return javaFloatArray(env, floats);
    } else
        return nullptr;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_PathMeasure__1nGetSegment
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat startD, jfloat endD, jlong dstPtr, jboolean startWithMoveTo) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    SkPath* dst = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(dstPtr));
    return instance->getSegment(startD, endD, dst, startWithMoveTo);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_PathMeasure__1nIsClosed
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    return instance->isClosed();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_PathMeasure__1nNextContour
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPathMeasure* instance = reinterpret_cast<SkPathMeasure*>(static_cast<uintptr_t>(ptr));
    return instance->nextContour();
}