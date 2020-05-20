#include <algorithm>
#include <iostream>
#include <vector>
#include <jni.h>
#include "SkPath.h"
#include "interop.hh"

static void deletePath(SkPath* path) {
    // std::cout << "Deleting [SkPath " << path << "]" << std::endl;
    delete path;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_nInit(JNIEnv* env, jclass jclass) {
    SkPath* obj = new SkPath();
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_nGetNativeFinalizer(JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deletePath));
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsInterpolatable(JNIEnv* env, jclass jclass, jlong ptr, jlong comparePtr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* compare = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(comparePtr));
    return instance->isInterpolatable(*compare);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_nInterpolate(JNIEnv* env, jclass jclass, jlong ptr, jlong endingPtr, jfloat weight) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* ending = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(endingPtr));
    SkPath* out = new SkPath();
    if (instance->interpolate(*ending, weight, out)) {
        return reinterpret_cast<jlong>(out);
    } else {
        delete out;
        return 0;
    }
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nSetFillType(JNIEnv* env, jclass jclass, jlong ptr, jint fillType) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->setFillType(static_cast<SkPathFillType>(fillType));
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetConvexityType(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getConvexityType());
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetConvexityTypeOrUnknown(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->getConvexityTypeOrUnknown());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nSetConvexityType(JNIEnv* env, jclass jclass, jlong ptr, jint convexityInt) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPathConvexityType convexity = static_cast<SkPathConvexityType>(convexityInt);
    instance->setConvexityType(convexity);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nIsOval(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkRect bounds;
    if (instance->isOval(&bounds))
        return skija::Rect::fromSkRect(env, bounds);
    else
        return nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nIsRRect(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkRRect rrect;
    if (instance->isRRect(&rrect))
        return skija::RoundedRect::fromSkRRect(env, rrect);
    else
        return nullptr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nReset(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->reset();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRewind(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rewind();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsEmpty(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->isEmpty();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsLastContourClosed(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->isLastContourClosed();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsFinite(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->isFinite();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nSetIsVolatile(JNIEnv* env, jclass jclass, jlong ptr, jboolean isVolatile) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->setIsVolatile(isVolatile);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsLineDegenerate(JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jboolean exact) {
    return SkPath::IsLineDegenerate({x0, y0}, {x1, y1}, exact);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsQuadDegenerate(JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jboolean exact) {
    return SkPath::IsQuadDegenerate({x0, y0}, {x1, y1}, {x2, y2}, exact);
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsCubicDegenerate(JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat x3, jfloat y3, jboolean exact) {
    return SkPath::IsCubicDegenerate({x0, y0}, {x1, y1}, {x2, y2}, {x3, y3}, exact);
}

extern "C" JNIEXPORT jobjectArray JNICALL Java_org_jetbrains_skija_Path_nIsLine(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPoint line[2];
    if (instance->isLine(line)) {
        jobjectArray res = env->NewObjectArray(2, skija::Point::cls, nullptr);
        env->SetObjectArrayElement(res, 0, skija::Point::fromSkPoint(env, line[0]));
        env->SetObjectArrayElement(res, 1, skija::Point::fromSkPoint(env, line[1]));
        return res;
    } else
        return nullptr;
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nCountPoints(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->countPoints();
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nGetPoint(JNIEnv* env, jclass jclass, jlong ptr, jint index) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPoint p = instance->getPoint(index);
    return skija::Point::fromSkPoint(env, p);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetPoints(JNIEnv* env, jclass jclass, jlong ptr, jobjectArray pointsArray, jint max) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    std::vector<SkPoint> p(std::min(max, instance->countPoints()));
    int count = instance->getPoints(p.data(), max);
    for (int i = 0; i < max && i < count; ++ i)
        env->SetObjectArrayElement(pointsArray, i, skija::Point::fromSkPoint(env, p[i]));
    return count;
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nCountVerbs(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->countVerbs();
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetVerbs(JNIEnv* env, jclass jclass, jlong ptr, jbyteArray verbsArray, jint max) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    jbyte* verbs = env->GetByteArrayElements(verbsArray, 0);
    int count = instance->getVerbs(reinterpret_cast<uint8_t *>(verbs), max);
    env->ReleaseByteArrayElements(verbsArray, verbs, 0);
    return count;
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nApproximateBytesUsed(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->approximateBytesUsed();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nSwap(JNIEnv* env, jclass jclass, jlong ptr, jlong otherPtr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* other = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(otherPtr));
    instance->swap(*other);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nGetBounds(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return skija::Rect::fromSkRect(env, instance->getBounds());
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nUpdateBoundsCache(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->updateBoundsCache();
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nComputeTightBounds(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return skija::Rect::fromSkRect(env, instance->computeTightBounds());
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nConservativelyContainsRect(JNIEnv* env, jclass jclass, jlong ptr, float l, float t, float r, float b) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkRect rect {l, t, r, b};
    return instance->conservativelyContainsRect(rect);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nIncReserve(JNIEnv* env, jclass jclass, jlong ptr, int extraPtCount) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->incReserve(extraPtCount);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nShrinkToFit(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->shrinkToFit();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nMoveTo(JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->moveTo(x, y);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRMoveTo(JNIEnv* env, jclass jclass, jlong ptr, jfloat dx, jfloat dy) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rMoveTo(dx, dy);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nLineTo(JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->lineTo(x, y);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRLineTo(JNIEnv* env, jclass jclass, jlong ptr, jfloat dx, jfloat dy) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rLineTo(dx, dy);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nQuadTo(JNIEnv* env, jclass jclass, long ptr, jfloat x1, jfloat y1, jfloat x2, jfloat y2) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->quadTo(x1, y1, x2, y2);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRQuadTo(JNIEnv* env, jclass jclass, long ptr, jfloat dx1, jfloat dy1, jfloat dx2, jfloat dy2) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rQuadTo(dx1, dy1, dx2, dy2);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nConicTo(JNIEnv* env, jclass jclass, long ptr, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat w) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->conicTo(x1, y1, x2, y2, w);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRConicTo(JNIEnv* env, jclass jclass, long ptr, jfloat dx1, jfloat dy1, jfloat dx2, jfloat dy2, jfloat w) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rConicTo(dx1, dy1, dx2, dy2, w);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nCubicTo(JNIEnv* env, jclass jclass, long ptr, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat x3, jfloat y3) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->cubicTo(x1, y1, x2, y2, x3, y3);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nRCubicTo(JNIEnv* env, jclass jclass, long ptr, jfloat dx1, jfloat dy1, jfloat dx2, jfloat dy2, jfloat dx3, jfloat dy3) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rCubicTo(dx1, dy1, dx2, dy2, dx3, dy3);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nArcTo(JNIEnv* env, jclass jclass, long ptr, jfloat left, jfloat top, jfloat right, jfloat bottom, jfloat startAngle, jfloat sweepAngle, jboolean forceMoveTo) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->arcTo({left, top, right, bottom}, startAngle, sweepAngle, forceMoveTo);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nTangentArcTo(JNIEnv* env, jclass jclass, long ptr, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat radius) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->arcTo(x1, y1, x2, y2, radius);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nEllipticalArcTo(JNIEnv* env, jclass jclass, long ptr, jfloat rx, jfloat ry, jfloat xAxisRotate, jint size, jint direction, jfloat x, float y) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->arcTo(rx, ry, xAxisRotate, static_cast<SkPath::ArcSize>(size), static_cast<SkPathDirection>(direction), x, y);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nREllipticalArcTo(JNIEnv* env, jclass jclass, long ptr, jfloat rx, jfloat ry, jfloat xAxisRotate, jint size, jint direction, jfloat dx, float dy) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->rArcTo(rx, ry, xAxisRotate, static_cast<SkPath::ArcSize>(size), static_cast<SkPathDirection>(direction), dx, dy);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nClosePath(JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->close();
}

extern "C" JNIEXPORT jobjectArray Java_org_jetbrains_skija_Path_nConvertConicToQuads
  (JNIEnv* env, jclass jclass, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jfloat w, jint pow2) {
    std::vector<SkPoint> pts(1 + 2 * (1 << pow2));
    int count = SkPath::ConvertConicToQuads({x0, y0}, {x1, y1}, {x2, y2}, w, pts.data(), pow2);
    jobjectArray res = env->NewObjectArray(count, skija::Point::cls, nullptr);
    for (int i = 0; i < count; ++i) {
        env->SetObjectArrayElement(res, i, skija::Point::fromSkPoint(env, pts[i]));
    }
    return res;
}

extern "C" JNIEXPORT jobject Java_org_jetbrains_skija_Path_nIsRect
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkRect rect;
    if (instance->isRect(&rect))
        return skija::Rect::fromSkRect(env, rect);
    else
        return nullptr;
}

extern "C" JNIEXPORT void Java_org_jetbrains_skija_Path_nAddRect
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat l, jfloat t, jfloat r, jfloat b, jint dirInt, jint start) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPathDirection dir = static_cast<SkPathDirection>(dirInt);
    instance->addRect({l, t, r, b}, dir, start);
}

extern "C" JNIEXPORT void Java_org_jetbrains_skija_Path_nAddOval
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat l, jfloat t, jfloat r, jfloat b, jint dirInt, jint start) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPathDirection dir = static_cast<SkPathDirection>(dirInt);
    instance->addOval({l, t, r, b}, dir, start);
}

extern "C" JNIEXPORT void Java_org_jetbrains_skija_Path_nAddCircle
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y, jfloat r, jint dirInt) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPathDirection dir = static_cast<SkPathDirection>(dirInt);
    instance->addCircle(x, y, r, dir);
}

extern "C" JNIEXPORT void Java_org_jetbrains_skija_Path_nAddArc
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat l, jfloat t, jfloat r, jfloat b, jfloat startAngle, jfloat sweepAngle) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->addArc({l, t, r, b}, startAngle, sweepAngle);
}

extern "C" JNIEXPORT void Java_org_jetbrains_skija_Path_nAddRoundedRect
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat l, jfloat t, jfloat r, jfloat b, jfloatArray radii, jint dirInt, jint start) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkRRect rrect = skija::RoundedRect::toSkRRect(env, l, t, r, b, radii);
    SkPathDirection dir = static_cast<SkPathDirection>(dirInt);
    instance->addRRect(rrect, dir, start);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nAddPoly
  (JNIEnv* env, jclass jclass, jlong ptr, jfloatArray coords, jboolean close) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    jsize len = env->GetArrayLength(coords);
    jfloat* arr = env->GetFloatArrayElements(coords, 0);
    instance->addPoly(reinterpret_cast<SkPoint*>(arr), len / 2, close);
    env->ReleaseFloatArrayElements(coords, arr, 0);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nAddPath
  (JNIEnv* env, jclass jclass, jlong ptr, jlong srcPtr, jboolean extend) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* src = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(srcPtr));
    SkPath::AddPathMode mode = extend ? SkPath::AddPathMode::kExtend_AddPathMode : SkPath::AddPathMode::kAppend_AddPathMode;
    instance->addPath(*src, mode);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nAddPathOffset
  (JNIEnv* env, jclass jclass, jlong ptr, jlong srcPtr, jfloat dx, jfloat dy, jboolean extend) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* src = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(srcPtr));
    SkPath::AddPathMode mode = extend ? SkPath::AddPathMode::kExtend_AddPathMode : SkPath::AddPathMode::kAppend_AddPathMode;
    instance->addPath(*src, dx, dy, mode);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nAddPathTransform
  (JNIEnv* env, jclass jclass, jlong ptr, jlong srcPtr,
   jfloat scaleX, jfloat skewX,  jfloat transX,
   jfloat skewY,  jfloat scaleY, jfloat transY,
   jfloat persp0, jfloat persp1, jfloat persp2,
   jboolean extend) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* src = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(srcPtr));
    SkMatrix matrix = SkMatrix::MakeAll(scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2);
    SkPath::AddPathMode mode = extend ? SkPath::AddPathMode::kExtend_AddPathMode : SkPath::AddPathMode::kAppend_AddPathMode;
    instance->addPath(*src, matrix, mode);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nReversePath
  (JNIEnv* env, jclass jclass, jlong ptr, jlong srcPtr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* src = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(srcPtr));
    instance->reverseAddPath(*src);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nOffset
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat dx, jfloat dy, jlong dstPtr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* dst = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(dstPtr));
    instance->offset(dx, dy, dst);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nTransform
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat dx, jfloat dy,
   jfloat scaleX, jfloat skewX,  jfloat transX,
   jfloat skewY,  jfloat scaleY, jfloat transY,
   jfloat persp0, jfloat persp1, jfloat persp2,
   jlong dstPtr, jboolean pcBool) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPath* dst = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(dstPtr));
    SkMatrix matrix = SkMatrix::MakeAll(scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2);
    SkApplyPerspectiveClip pc = pcBool ? SkApplyPerspectiveClip::kYes : SkApplyPerspectiveClip::kNo;
    instance->transform(matrix, dst, pc);
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_nGetLastPt
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    SkPoint out;
    if (instance->getLastPt(&out))
        return skija::Point::fromSkPoint(env, out);
    else
        return nullptr;
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nSetLastPt
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->setLastPt(x, y);
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetSegmentMasks
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->getSegmentMasks();
}

static void deletePathIter(SkPath::Iter* iter) {
    // std::cout << "Deleting [SkPath " << path << "]" << std::endl;
    delete iter;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_00024Iter_nInit
  (JNIEnv* env, jclass jclass, jlong pathPtr, jboolean forceClose) {
    SkPath* path = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(pathPtr));
    SkPath::Iter* iter = new SkPath::Iter(*path, forceClose);
    return reinterpret_cast<jlong>(iter);
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_00024Iter_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deletePathIter));
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_Path_00024Iter_nNext
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath::Iter* instance = reinterpret_cast<SkPath::Iter*>(static_cast<uintptr_t>(ptr));
    SkPoint pts[4];
    SkPath::Verb verb = instance->next(pts);
    jobject segment = env->NewObject(skija::Path::Segment::cls, skija::Path::Segment::ctor, static_cast<jint>(verb));
    if (verb != SkPath::Verb::kDone_Verb)
        env->SetObjectField(segment, skija::Path::Segment::p0, skija::Point::make(env, pts[0].fX, pts[0].fY));
    if (verb == SkPath::Verb::kLine_Verb || verb == SkPath::Verb::kQuad_Verb || verb == SkPath::Verb::kConic_Verb || verb == SkPath::Verb::kCubic_Verb)
        env->SetObjectField(segment, skija::Path::Segment::p1, skija::Point::make(env, pts[1].fX, pts[1].fY));
    if (verb == SkPath::Verb::kQuad_Verb || verb == SkPath::Verb::kConic_Verb || verb == SkPath::Verb::kCubic_Verb)
        env->SetObjectField(segment, skija::Path::Segment::p2, skija::Point::make(env, pts[2].fX, pts[2].fY));
    if (verb == SkPath::Verb::kCubic_Verb)
        env->SetObjectField(segment, skija::Path::Segment::p3, skija::Point::make(env, pts[3].fX, pts[3].fY));
    if (verb == SkPath::Verb::kConic_Verb)
        env->SetFloatField(segment, skija::Path::Segment::conicWeight, instance->conicWeight());
    if (verb == SkPath::Verb::kLine_Verb)
        env->SetBooleanField(segment, skija::Path::Segment::isCloseLine, instance->isCloseLine());
    if (verb != SkPath::Verb::kDone_Verb)
        env->SetBooleanField(segment, skija::Path::Segment::isClosedContour, instance->isClosedContour());
    return segment;
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nContains
  (JNIEnv* env, jclass jclass, jlong ptr, jfloat x, jfloat y) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->contains(x, y);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nDump
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->dump();
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_Path_nDumpHex
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    instance->dumpHex();
}

extern "C" JNIEXPORT jbyteArray JNICALL Java_org_jetbrains_skija_Path_nWriteToMemory
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    int count = instance->writeToMemory(nullptr);
    jbyteArray bytesArray = env->NewByteArray(count);
    jbyte* bytes = env->GetByteArrayElements(bytesArray, 0);
    instance->writeToMemory(bytes);
    env->ReleaseByteArrayElements(bytesArray, bytes, 0);
    return bytesArray;
}

extern "C" JNIEXPORT jlong JNICALL Java_org_jetbrains_skija_Path_nReadFromMemory
  (JNIEnv* env, jclass jclass, jbyteArray bytesArray) {
    SkPath* instance = new SkPath();
    int count = env->GetArrayLength(bytesArray);
    jbyte* bytes = env->GetByteArrayElements(bytesArray, 0);
    if (instance->readFromMemory(bytes, count)) {
        env->ReleaseByteArrayElements(bytesArray, bytes, 0);
        return reinterpret_cast<jlong>(instance);
    } else {
        env->ReleaseByteArrayElements(bytesArray, bytes, 0);
        delete instance;
        return 0;
    }
}

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_Path_nGetGenerationID
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->getGenerationID();
}

extern "C" JNIEXPORT jboolean JNICALL Java_org_jetbrains_skija_Path_nIsValid
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkPath* instance = reinterpret_cast<SkPath*>(static_cast<uintptr_t>(ptr));
    return instance->isValid();
}