#include <iostream>
#include <jni.h>
#include "SkCanvas.h"

// static void deleteCanvas(SkCanvas* canvas) {
//     delete canvas;
// }

// extern "C" JNIEXPORT jlong JNICALL Java_skija_Canvas_nGetNativeFinalizer
//   (JNIEnv* env, jclass jclass) {
//     return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteCanvas));
// }

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawLine
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    canvas->drawLine(x0, y0, x1, y1, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawRect
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat left, jfloat top, jfloat right, jfloat bottom, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    SkRect rect = { left, top, right, bottom };
    canvas->drawRect(rect, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawOval
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat left, jfloat top, jfloat right, jfloat bottom, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    SkRect rect = { left, top, right, bottom };
    canvas->drawOval(rect, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nClear(JNIEnv* env, jclass jclass, jlong ptr, jlong color) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr));
    canvas->clear(color);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nConcat(JNIEnv* env, jclass jclass, jlong ptr,
        jfloat scaleX, jfloat skewX,  jfloat transX,
        jfloat skewY,  jfloat scaleY, jfloat transY,
        jfloat persp0, jfloat persp1, jfloat persp2) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr));
    SkMatrix m = SkMatrix::MakeAll(scaleX, skewX, transX, skewY, scaleY, transY, persp0, persp1, persp2);
    canvas->concat(m);
}

extern "C" JNIEXPORT jint JNICALL Java_skija_Canvas_nSave(JNIEnv* env, jclass jclass, jlong ptr) {
    return reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr))->save();
}

extern "C" JNIEXPORT jint JNICALL Java_skija_Canvas_nGetSaveCount(JNIEnv* env, jclass jclass, jlong ptr) {
    return reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr))->getSaveCount();
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nRestore(JNIEnv* env, jclass jclass, jlong ptr) {
    reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr))->restore();
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nRestoreToCount(JNIEnv* env, jclass jclass, jlong ptr, jint saveCount) {
    reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr))->restoreToCount(saveCount);
}