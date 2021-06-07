#include <jni.h>
#include "../interop.hh"
#include "interop.hh"
#include "SkSVGSVG.h"
#include "SkSVGRenderContext.h"

extern "C" JNIEXPORT jint JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetTag
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGNode* instance = reinterpret_cast<SkSVGNode*>(static_cast<uintptr_t>(ptr));
    return static_cast<jint>(instance->tag());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetX
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    return skija::svg::SVGLength::toJava(env, instance->getX());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetY
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    return skija::svg::SVGLength::toJava(env, instance->getY());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetWidth
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    return skija::svg::SVGLength::toJava(env, instance->getWidth());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetHeight
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    return skija::svg::SVGLength::toJava(env, instance->getHeight());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetPreserveAspectRatio
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    return skija::svg::SVGPreserveAspectRatio::toJava(env, instance->getPreserveAspectRatio());
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetViewBox
  (JNIEnv* env, jclass jclass, jlong ptr) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkTLazy<SkSVGViewBoxType> viewBox = instance->getViewBox();
    return viewBox.isValid() ? skija::Rect::fromSkRect(env, *viewBox.get()) : nullptr;
}

extern "C" JNIEXPORT jobject JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nGetIntrinsicSize
  (JNIEnv* env, jclass jclass, jlong ptr, float width, float height, float dpi) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkSVGLengthContext lc({width, height}, dpi);
    SkSize size = instance->intrinsicSize(lc);
    return skija::Point::fromSkPoint(env, {size.width(), size.height()});
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetX
  (JNIEnv* env, jclass jclass, jlong ptr, float value, int unit) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkSVGLength lenght(value, static_cast<SkSVGLength::Unit>(unit));
    instance->setX(lenght);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetY
  (JNIEnv* env, jclass jclass, jlong ptr, float value, int unit) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkSVGLength lenght(value, static_cast<SkSVGLength::Unit>(unit));
    instance->setY(lenght);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetWidth
  (JNIEnv* env, jclass jclass, jlong ptr, float value, int unit) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkSVGLength lenght(value, static_cast<SkSVGLength::Unit>(unit));
    instance->setWidth(lenght);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetHeight
  (JNIEnv* env, jclass jclass, jlong ptr, float value, int unit) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    SkSVGLength lenght(value, static_cast<SkSVGLength::Unit>(unit));
    instance->setHeight(lenght);
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetPreserveAspectRatio
  (JNIEnv* env, jclass jclass, jlong ptr, jint align, jint scale) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    instance->setPreserveAspectRatio(SkSVGPreserveAspectRatio { static_cast<SkSVGPreserveAspectRatio::Align>(align),
                                                                static_cast<SkSVGPreserveAspectRatio::Scale>(scale) });
}

extern "C" JNIEXPORT void JNICALL Java_org_jetbrains_skija_svg_SVGSVG__1nSetViewBox
  (JNIEnv* env, jclass jclass, jlong ptr, float l, float t, float r, float b) {
    SkSVGSVG* instance = reinterpret_cast<SkSVGSVG*>(static_cast<uintptr_t>(ptr));
    instance->setViewBox(SkRect::MakeLTRB(l, t, r, b));
}
