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
        FontVariationClass* c = new FontVariationClass;
        jclass localClass = env->FindClass("org/jetbrains/skija/FontVariation");
        c->cls = static_cast<jclass>(env->NewGlobalRef(localClass));
        c->tagID = env->GetFieldID(c->cls, "tag", "I");
        c->valueID = env->GetFieldID(c->cls, "value", "F");
        fontVariationClass = c;
    }
}

FontAxisInfoClass* fontAxisInfoClass = nullptr;

void maybeInitFontAxisInfoClass(JNIEnv* env) {
    if (fontAxisInfoClass == nullptr) {
        FontAxisInfoClass* c = new FontAxisInfoClass;
        jclass localClass = env->FindClass("org/jetbrains/skija/FontAxisInfo");
        c->cls = static_cast<jclass>(env->NewGlobalRef(localClass));
        c->ctorID = env->GetMethodID(c->cls, "<init>", "(II[BIFFF)V");
        fontAxisInfoClass = c;
    }
}

IRectClass* iRectClass = nullptr;
void maybeInitIRectClass(JNIEnv* env) {
    if (iRectClass == nullptr) {
        IRectClass* c = new IRectClass;
        jclass localClass = env->FindClass("org/jetbrains/skija/IRect");
        c->cls = static_cast<jclass>(env->NewGlobalRef(localClass));
        c->left = env->GetFieldID(c->cls, "left", "I");
        c->top = env->GetFieldID(c->cls, "top", "I");
        c->right = env->GetFieldID(c->cls, "right", "I");
        c->bottom = env->GetFieldID(c->cls, "bottom", "I");
        iRectClass = c;
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

RectClass* rectClass = nullptr;
void maybeInitRectClass(JNIEnv* env) {
    if (rectClass == nullptr) {
        RectClass* c = new RectClass();
        jclass localClass = env->FindClass("org/jetbrains/skija/Rect");
        c->cls = static_cast<jclass>(env->NewGlobalRef(localClass));
        c->makeLTRB = env->GetStaticMethodID(c->cls, "makeLTRB", "(FFFF)Lorg/jetbrains/skija/Rect;");
        c->left = env->GetFieldID(c->cls, "left", "F");
        c->top = env->GetFieldID(c->cls, "top", "F");
        c->right = env->GetFieldID(c->cls, "right", "F");
        c->bottom = env->GetFieldID(c->cls, "bottom", "F");
        rectClass = c;
    }
}

jobject javaRect(JNIEnv* env, float left, float top, float right, float bottom) {
    maybeInitRectClass(env);
    return env->CallStaticObjectMethod(rectClass->cls, rectClass->makeLTRB, left, top, right, bottom);
}

jobject javaRect(JNIEnv* env, const SkRect& rect) {
    maybeInitRectClass(env);
    return env->CallStaticObjectMethod(rectClass->cls, rectClass->makeLTRB, rect.fLeft, rect.fTop, rect.fRight, rect.fBottom);
}