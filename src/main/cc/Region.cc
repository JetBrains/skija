#include <iostream>
#include <jni.h>
#include "SkRegion.h"

static void deleteRegion(SkRegion* region) {
    // std::cout << "Deleting [SkRegion " << Region << "]" << std::endl;
    delete region;
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Region_nInit(JNIEnv* env, jclass jclass) {
    SkRegion* obj = new SkRegion();
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Region_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteRegion));
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSet(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* other = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->set(*other);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nIsEmpty(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->isEmpty();
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nIsRect(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->isRect();
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nIsComplex(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->isComplex();
}
    
extern "C" JNIEXPORT jintArray JNICALL Java_skija_Region_nGetBounds(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkIRect bounds = instance->getBounds();
    jintArray res = env->NewIntArray(4);
    jint* arr = env->GetIntArrayElements(res, 0);
    arr[0] = bounds.left();
    arr[1] = bounds.top();
    arr[2] = bounds.right();
    arr[3] = bounds.bottom();
    env->ReleaseIntArrayElements(res, arr, 0);
    return res;
}

extern "C" JNIEXPORT jint JNICALL Java_skija_Region_nComputeRegionComplexity(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->computeRegionComplexity();
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nGetBoundaryPath(JNIEnv* env, jclass jclass, jlong ptr, jlong pathPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    return instance->getBoundaryPath(path);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSetEmpty(JNIEnv* env, jclass jclass, jlong ptr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->setEmpty();
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSetRect(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->setRect({left, top, right, bottom});
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSetRects(JNIEnv* env, jclass jclass, jlong ptr, jintArray coords) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    int len = env->GetArrayLength(coords);
    SkIRect rects[len / 4];
    jint* arr = env->GetIntArrayElements(coords, 0); 
    for (int i = 0; i < len; i += 4)
        rects[i / 4] = {arr[i], arr[i+1], arr[i+2], arr[i+3]};
    env->ReleaseIntArrayElements(coords, arr, 0);
    return instance->setRects(rects, len / 4);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSetRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->setRegion(*region);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nSetPath(JNIEnv* env, jclass jclass, jlong ptr, jlong pathPtr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->setPath(*path, *region);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nIntersectsIRect(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->intersects({left, top, right, bottom});
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nIntersectsRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->intersects(*region);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nContainsIPoint(JNIEnv* env, jclass jclass, jlong ptr, jint x, jint y) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->contains(x, y);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nContainsIRect(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->contains({left, top, right, bottom});
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nContainsRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->contains(*region);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nQuickContains(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->quickContains({left, top, right, bottom});
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nQuickRejectIRect(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->quickReject({left, top, right, bottom});
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nQuickRejectRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->contains(*region);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Region_nTranslate(JNIEnv* env, jclass jclass, jlong ptr, jint dx, jint dy) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    instance->translate(dx, dy);
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nOpIRect(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom, jint op) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    return instance->op({left, top, right, bottom}, static_cast<SkRegion::Op>(op));
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nOpRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr, jint op) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->op(*region, static_cast<SkRegion::Op>(op));
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nOpIRectRegion(JNIEnv* env, jclass jclass, jlong ptr, jint left, jint top, jint right, jint bottom, jlong regionPtr, jint op) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->op({left, top, right, bottom}, *region, static_cast<SkRegion::Op>(op));
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nOpRegionIRect(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtr, jint left, jint top, jint right, jint bottom, jint op) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* region = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtr));
    return instance->op(*region, {left, top, right, bottom}, static_cast<SkRegion::Op>(op));
}

extern "C" JNIEXPORT jboolean JNICALL Java_skija_Region_nOpRegionRegion(JNIEnv* env, jclass jclass, jlong ptr, jlong regionPtrA, jlong regionPtrB, jint op) {
    SkRegion* instance = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(ptr));
    SkRegion* regionA = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtrA));
    SkRegion* regionB = reinterpret_cast<SkRegion*>(static_cast<uintptr_t>(regionPtrB));
    return instance->op(*regionA, *regionB, static_cast<SkRegion::Op>(op));
}
