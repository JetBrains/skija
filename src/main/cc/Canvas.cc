#include <iostream>
#include <jni.h>
#include "Canvas.hh"

Canvas::Canvas() {}
Canvas::~Canvas() {}

static void deleteCanvas(Canvas* paint) {
    delete paint;
}


extern "C" JNIEXPORT jlong JNICALL Java_skija_Canvas_nInit
  (JNIEnv* env, jclass jclass) {
    Canvas* obj = new Canvas();
    return reinterpret_cast<jlong>(obj);
}

extern "C" JNIEXPORT jlong JNICALL Java_skija_Canvas_nGetNativeFinalizer
  (JNIEnv* env, jclass jclass) {
    return static_cast<jlong>(reinterpret_cast<uintptr_t>(&deleteCanvas));
}

extern "C" JNIEXPORT void JNICALL Java_skija_Canvas_nDrawRect
  (JNIEnv* env, jclass jclass, jlong canvas, jfloat left, jfloat top, jfloat right, jfloat bottom, jlong paint_ptr) {
    std::cout << "Canvas::nDrawRect(canvas:" << canvas << ", " << left << ", " << top << ", " << right << ", " << bottom << ", paint:" << paint_ptr << ")" << std::endl;
}