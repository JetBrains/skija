#pragma once
#include <jni.h>
#include <memory>
#include <vector>
#include "SkMatrix.h"
#include "SkRefCnt.h"
#include "SkRect.h"
#include "SkRRect.h"
#include "SkString.h"

jintArray javaIntArray(JNIEnv* env, std::vector<int> ints);

jlongArray javaLongArray(JNIEnv* env, std::vector<long> longs);

jfloatArray javaFloatArray(JNIEnv* env, std::vector<float> floats);

namespace skija {
    namespace FontVariation {
        extern jclass cls;
        extern jfieldID tag;
        extern jfieldID value;
    }

    namespace FontAxisInfo {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace IRect {
        extern jclass cls;
        extern jfieldID left;
        extern jfieldID top;
        extern jfieldID right;
        extern jfieldID bottom;

        std::unique_ptr<SkIRect> toSkIRect(JNIEnv* env, jobject obj);
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

    namespace RoundedRect {
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

    namespace Point {
        extern jclass cls;
        extern jmethodID ctor;
        extern jfieldID x;
        extern jfieldID y;

        jobject make(JNIEnv* env, float x, float y);
        jobject fromSkPoint(JNIEnv* env, const SkPoint& p);
    }

    namespace Path {
        namespace Segment {
            extern jclass cls;
            extern jmethodID ctor;
            extern jfieldID verb;
            extern jfieldID p0;
            extern jfieldID p1;
            extern jfieldID p2;
            extern jfieldID p3;
            extern jfieldID conicWeight;
            extern jfieldID isCloseLine;
            extern jfieldID isClosedContour;
        }
    }
}

std::unique_ptr<SkMatrix> arrayToMatrix(JNIEnv* env, jfloatArray arr);
SkString skString(JNIEnv* env, jstring str);