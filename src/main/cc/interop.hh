#pragma once
#include <array>
#include <memory>
#include <jni.h>
#include "SkRefCnt.h"
#include "SkRect.h"
#include "SkMatrix.h"

typedef std::array<int, sizeof(int)> IntArray;
typedef std::array<long, sizeof(long)> LongArray;
typedef std::array<float, sizeof(float)> FloatArray;

jintArray javaIntArray(JNIEnv* env, IntArray ints);
jlongArray javaLongArray(JNIEnv* env, LongArray longs);
jfloatArray javaFloatArray(JNIEnv* env, FloatArray floats);

typedef struct {
    jclass cls;
    jfieldID tagID;
    jfieldID valueID;
} FontVariationClass;

extern FontVariationClass* fontVariationClass;

void maybeInitFontVariationClass(JNIEnv* env);


typedef struct {
    jclass cls;
    jmethodID ctorID;
} FontAxisInfoClass;

extern FontAxisInfoClass* fontAxisInfoClass;

void maybeInitFontAxisInfoClass(JNIEnv* env);

typedef struct {
    jclass cls;
    jfieldID left;
    jfieldID top;
    jfieldID right;
    jfieldID bottom;
} IRectClass;

extern IRectClass* iRectClass;

std::unique_ptr<SkIRect> objToIRect(JNIEnv* env, jobject obj);
std::unique_ptr<SkMatrix> arrayToMatrix(JNIEnv* env, jfloatArray arr);

typedef struct {
    jclass cls;
    jmethodID makeLTRB;
    jfieldID left;
    jfieldID top;
    jfieldID right;
    jfieldID bottom;
} RectClass;

extern RectClass* rectClass;

jobject javaRect(JNIEnv* env, float left, float top, float right, float bottom);
jobject javaRect(JNIEnv* env, const SkRect& rect);