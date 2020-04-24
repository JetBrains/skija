#pragma once
#include <array>
#include <memory>
#include <jni.h>
#include "SkRefCnt.h"
#include "SkRect.h"

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

std::unique_ptr<SkIRect> objToIRect(JNIEnv* env, jobject obj);