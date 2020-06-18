#pragma once
#include <jni.h>
#include <memory>
#include <vector>
#include "SkFontStyle.h"
#include "SkMatrix.h"
#include "SkRefCnt.h"
#include "SkRect.h"
#include "SkRRect.h"
#include "SkString.h"

jintArray javaIntArray(JNIEnv* env, std::vector<int> ints);

jlongArray javaLongArray(JNIEnv* env, std::vector<long> longs);

jfloatArray javaFloatArray(JNIEnv* env, std::vector<float> floats);

namespace skija {
    namespace FontAxisInfo {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace FontVariation {
        extern jclass cls;
        extern jfieldID tag;
        extern jfieldID value;
    }

    namespace IRect {
        extern jclass cls;
        extern jmethodID makeLTRB;
        extern jfieldID left;
        extern jfieldID top;
        extern jfieldID right;
        extern jfieldID bottom;

        jobject fromSkIRect(JNIEnv* env, const SkIRect& rect);
        std::unique_ptr<SkIRect> toSkIRect(JNIEnv* env, jobject obj);
    }

    namespace Path {
        namespace Segment {
            extern jclass cls;
            extern jmethodID ctorDone;
            extern jmethodID ctorMoveClose;
            extern jmethodID ctorLine;
            extern jmethodID ctorQuad;
            extern jmethodID ctorConic;
            extern jmethodID ctorCubic;
        }
    }

    namespace Point {
        extern jclass cls;
        extern jmethodID ctor;
        jobject make(JNIEnv* env, float x, float y);
        jobject fromSkPoint(JNIEnv* env, const SkPoint& p);
    }

    namespace Rect {
        extern jclass cls;
        extern jmethodID makeLTRB;
        extern jfieldID left;
        extern jfieldID top;
        extern jfieldID right;
        extern jfieldID bottom;

        jobject fromLTRB(JNIEnv* env, float left, float top, float right, float bottom);
        jobject fromSkRect(JNIEnv* env, const SkRect& rect);
    }

    namespace RRect {
        extern jclass cls;
        extern jmethodID makeLTRB1;
        extern jmethodID makeLTRB2;
        extern jmethodID makeLTRB4;
        extern jmethodID makeNinePatchLTRB;
        extern jmethodID makeComplexLTRB;
        extern jfieldID left;
        extern jfieldID top;
        extern jfieldID right;
        extern jfieldID bottom;
        extern jfieldID radii;

        SkRRect toSkRRect(JNIEnv* env, jfloat left, jfloat top, jfloat right, jfloat bottom, jfloatArray jradii);
        jobject fromSkRRect(JNIEnv* env, const SkRRect& rect);   
    }
}

std::unique_ptr<SkMatrix> skMatrix(JNIEnv* env, jfloatArray arr);
SkString skString(JNIEnv* env, jstring str);
jstring javaString(JNIEnv* env, const SkString& str);
SkFontStyle skFontStyle(jint style);