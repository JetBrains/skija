#include <iostream>
#include <jni.h>
#include "SkCanvas.h"
#include "SkRRect.h"

// static void deleteCanvas(SkCanvas* canvas) {
//     delete canvas;
// }

// extern "C" JNIEXPORT jlong JNICALL Java_skija_Canvas_nGetNativeFinalizer
//   (JNIEnv* env, jclass jclass) {
//     return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteCanvas));
// }

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawPoint
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat x, jfloat y, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    canvas->drawPoint(x, y, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawPoints
  (JNIEnv* env, jclass jclass, jlong canvasPtr, int mode, jfloatArray coords, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    SkCanvas::PointMode skMode = static_cast<SkCanvas::PointMode>(mode);
    jsize len = env->GetArrayLength(coords);
    jfloat* arr = env->GetFloatArrayElements(coords, 0); 
    SkPoint points[len / 2];
    for (int i = 0; i < len / 2; i++)
        points[i] = SkPoint { arr[i * 2], arr[i * 2 + 1] };
    env->ReleaseFloatArrayElements(coords, arr, 0);
    canvas->drawPoints(skMode, len / 2, points, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawLine
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat x0, jfloat y0, jfloat x1, jfloat y1, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    canvas->drawLine(x0, y0, x1, y1, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawArc
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat left, jfloat top, jfloat width, jfloat height, jfloat startAngle, jfloat sweepAngle, jboolean includeCenter, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    canvas->drawArc({left, top, left+width, top+height}, startAngle, sweepAngle, includeCenter, *paint);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawRectInscribed
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jfloat left, jfloat top, jfloat width, jfloat height, jfloatArray jradii, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));

    float right = left + width;
    float bottom = top + height;
    
    switch (env->GetArrayLength(jradii)) {
        case 0:
            canvas->drawRect({left, top, right, bottom}, *paint);
            break;
        case 1:
            {
                jfloat* radii = env->GetFloatArrayElements(jradii, 0);
                canvas->drawRRect(SkRRect::MakeRectXY({left, top, right, bottom}, radii[0], radii[0]), *paint);
                env->ReleaseFloatArrayElements(jradii, radii, 0);
            }
            break;
        case 2:
            {
                jfloat* radii = env->GetFloatArrayElements(jradii, 0);
                canvas->drawOval({left, top, right, bottom}, *paint);
                env->ReleaseFloatArrayElements(jradii, radii, 0);
            }
            break;
        case 4:
            {
                jfloat* radii = env->GetFloatArrayElements(jradii, 0);
                SkVector vradii[4] = {{radii[0], radii[0]}, {radii[1], radii[1]}, {radii[2], radii[2]}, {radii[3], radii[3]}};
                SkRRect rrect = SkRRect::MakeEmpty();
                rrect.setRectRadii({left, top, right, bottom}, vradii);
                canvas->drawRRect(rrect, *paint);
                env->ReleaseFloatArrayElements(jradii, radii, 0);
            }
            break;
        case 8:
            {
                jfloat* radii = env->GetFloatArrayElements(jradii, 0);
                SkVector vradii[4] = {{radii[0], radii[1]}, {radii[2], radii[3]}, {radii[4], radii[5]}, {radii[6], radii[7]}};
                SkRRect rrect = SkRRect::MakeEmpty();
                rrect.setRectRadii({left, top, right, bottom}, vradii);
                canvas->drawRRect(rrect, *paint);
                env->ReleaseFloatArrayElements(jradii, radii, 0);
            }
            break;
    }
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nClear(JNIEnv* env, jclass jclass, jlong ptr, jlong color) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(ptr));
    canvas->clear(color);
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawPaint
  (JNIEnv* env, jclass jclass, jlong canvasPtr, jlong paintPtr) {
    SkCanvas* canvas = reinterpret_cast<SkCanvas*>(static_cast<uintptr_t>(canvasPtr));
    SkPaint* paint = reinterpret_cast<SkPaint*>(static_cast<uintptr_t>(paintPtr));
    canvas->drawPaint(*paint);
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