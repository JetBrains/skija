#include <iostream>
#include <array>
#include <jni.h>
#include "interop.hh"

jintArray javaIntArray(JNIEnv* env, IntArray ints) {
    jintArray res = env->NewIntArray(ints.size());
    env->SetIntArrayRegion(res, 0, ints.size(), ints.data());
    return res;
}

jlongArray javaLongArray(JNIEnv* env, LongArray longs) {
    jlongArray res = env->NewLongArray(longs.size());
    env->SetLongArrayRegion(res, 0, longs.size(), longs.data());
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

void unrefSkRefCnt(SkRefCnt* p) {
    if (p->unique())
        std::cout << "Deleting [" << p << "]" << std::endl;
    else
        std::cout << "Unref [" << p << "] " << getRefCnt(p) << " - 1" << std::endl;
    p->unref();
}

FontVariationClass* fontVariationClass = nullptr;

void maybeInitFontVariationClass(JNIEnv* env) {
    if (fontVariationClass == nullptr) {
        fontVariationClass = new FontVariationClass;
        fontVariationClass->cls = env->FindClass("org/jetbrains/skija/FontVariation");
        fontVariationClass->tagID = env->GetFieldID(fontVariationClass->cls, "tag", "I");
        fontVariationClass->valueID = env->GetFieldID(fontVariationClass->cls, "value", "F");
    }
}

FontAxisInfoClass* fontAxisInfoClass = nullptr;

void maybeInitFontAxisInfoClass(JNIEnv* env) {
    if (fontAxisInfoClass == nullptr) {
        fontAxisInfoClass = new FontAxisInfoClass;
        fontAxisInfoClass->cls = env->FindClass("org/jetbrains/skija/FontAxisInfo");
        fontAxisInfoClass->ctorID = env->GetMethodID(fontAxisInfoClass->cls, "<init>", "(II[BIFFF)V");
    }
}