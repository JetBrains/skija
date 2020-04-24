#include <iostream>
#include <array>
#include <memory>
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

IRectClass* iRectClass = nullptr;
void maybeInitIRectClass(JNIEnv* env) {
    if (iRectClass == nullptr) {
        iRectClass = new IRectClass;
        iRectClass->cls = env->FindClass("org/jetbrains/skija/IRect");
        iRectClass->left = env->GetFieldID(iRectClass->cls, "left", "I");
        iRectClass->top = env->GetFieldID(iRectClass->cls, "top", "I");
        iRectClass->right = env->GetFieldID(iRectClass->cls, "right", "I");
        iRectClass->bottom = env->GetFieldID(iRectClass->cls, "bottom", "I");
    }
}

std::unique_ptr<SkIRect> objToIRect(JNIEnv* env, jobject obj) {
    if (obj == nullptr)
        return std::unique_ptr<SkIRect>(nullptr);
    else {
        maybeInitIRectClass(env);
        return std::unique_ptr<SkIRect>(new SkIRect{
            env->GetIntField(obj, iRectClass->left), 
            env->GetIntField(obj, iRectClass->top), 
            env->GetIntField(obj, iRectClass->right), 
            env->GetIntField(obj, iRectClass->bottom)
        });
    }
}