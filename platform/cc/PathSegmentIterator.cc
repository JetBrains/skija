#include <jni.h>
#include "SkPath.h"
#include "interop.hh"

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathSegmentIterator__1nMake
  (JNIEnv* env, jclass jclass, jlong pathPtr, jboolean forceClose) {
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkPath::Iter* iter = new SkPath::Iter(*path, forceClose);
    return reinterpret_cast<jlong>(iter);
}

static void deletePathSegmentIterator(SkPath::Iter* iter) {
    // std::cout << "Deleting [SkPathSegmentIterator " << path << "]" << std::endl;
    delete iter;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_PathSegmentIterator__1nGetFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deletePathSegmentIterator));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_PathSegmentIterator__1nNext
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath::Iter* instance = reinterpret_cast<SkPath::Iter*>(static_cast<uintptr_t>(ptr));
    SkPoint pts[4];
    SkPath::Verb verb = instance->next(pts);
    jobject segment;
    switch (verb) {
        case SkPath::Verb::kDone_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorDone);
            break;
        case SkPath::Verb::kMove_Verb:
        case SkPath::Verb::kClose_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorMoveClose, static_cast<jint>(verb), pts[0].fX, pts[0].fY, instance->isClosedContour());
            break;
        case SkPath::Verb::kLine_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorLine, pts[0].fX, pts[0].fY, pts[1].fX, pts[1].fY, instance->isCloseLine(), instance->isClosedContour());
            break;
        case SkPath::Verb::kQuad_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorQuad, pts[0].fX, pts[0].fY, pts[1].fX, pts[1].fY, pts[2].fX, pts[2].fY, instance->isClosedContour());
            break;
        case SkPath::Verb::kConic_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorConic, pts[0].fX, pts[0].fY, pts[1].fX, pts[1].fY, pts[2].fX, pts[2].fY, instance->conicWeight(), instance->isClosedContour());
            break;
        case SkPath::Verb::kCubic_Verb:
            segment = env->NewObject(skija::PathSegment::cls, skija::PathSegment::ctorConic, pts[0].fX, pts[0].fY, pts[1].fX, pts[1].fY, pts[2].fX, pts[2].fY, pts[3].fX, pts[3].fY, instance->isClosedContour());
            break;
    }
    return segment;
}
