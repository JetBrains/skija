#pragma once
#include "SkRefCnt.h"

#include <array>
#include <jni.h>

typedef std::array<int, sizeof(float)> IntArray;
typedef std::array<float, sizeof(float)> FloatArray;

jintArray javaIntArray(JNIEnv* env, IntArray ints);
jfloatArray javaFloatArray(JNIEnv* env, FloatArray floats);

class SkRefCntHack {
public:
    void* x;
    mutable std::atomic<int32_t> fRefCnt;
};

int32_t getRefCnt(SkRefCnt* ref);