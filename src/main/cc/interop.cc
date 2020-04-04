#include <array>
#include <jni.h>
#include "interop.hh"

jintArray javaIntArray(JNIEnv* env, IntArray ints) {
    jintArray res = env->NewIntArray(ints.size());
    env->SetIntArrayRegion(res, 0, ints.size(), ints.data());
    return res;
}

jfloatArray javaFloatArray(JNIEnv* env, FloatArray floats) {
    jfloatArray res = env->NewFloatArray(floats.size());
    env->SetFloatArrayRegion(res, 0, floats.size(), floats.data());
    return res;
}

int32_t getRefCnt(SkRefCnt* ref) {
    return reinterpret_cast<SkRefCntHack*>(ref)->fRefCnt.load(std::memory_order_relaxed);
}