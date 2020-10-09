#pragma once
#include <jni.h>
#include <memory>
#include <vector>
#include "SkFontMetrics.h"
#include "SkFontStyle.h"
#include "SkMatrix.h"
#include "SkRefCnt.h"
#include "SkRect.h"
#include "SkRRect.h"
#include "SkString.h"

namespace java {
    namespace lang {
        namespace Float {
            extern jclass cls;
            extern jmethodID ctor;
        }

        namespace String {
            extern jclass cls;
        }
    }
    namespace util {
        namespace function {
            namespace BooleanSupplier {
                extern jclass cls;
                extern jmethodID apply;
            }
        }
    }
}

namespace skija {
    namespace Color4f {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace Drawable {
        extern jclass cls;
        extern jmethodID onDraw;
        extern jmethodID onGetBounds;
    }
    
    namespace FontAxisInfo {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace FontFamilyName {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace FontMetrics {
        extern jclass cls;
        extern jmethodID ctor;
        jobject toJava(JNIEnv* env, const SkFontMetrics& m);
    }

    namespace FontStyle {
        SkFontStyle fromJava(jint style);
        jint toJava(const SkFontStyle& fs);
    }

    namespace FontVariation {
        extern jclass cls;
        extern jmethodID ctor;
        extern jfieldID tag;
        extern jfieldID value;
    }

    namespace ImageInfo {
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace IPoint {
        extern jclass cls;
        extern jmethodID ctor;
        jobject make(JNIEnv* env, float x, float y);
        jobject fromSkIPoint(JNIEnv* env, const SkIPoint& p);
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
        extern jclass cls;
        extern jmethodID ctor;
    }

    namespace PathSegment {
        extern jclass cls;
        extern jmethodID ctorDone;
        extern jmethodID ctorMoveClose;
        extern jmethodID ctorLine;
        extern jmethodID ctorQuad;
        extern jmethodID ctorConic;
        extern jmethodID ctorCubic;
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

        std::unique_ptr<SkRect> toSkRect(JNIEnv* env, jobject rect);
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

    namespace RSXform {
        extern jclass cls;
        extern jmethodID ctor;
    }
}

std::unique_ptr<SkMatrix> skMatrix(JNIEnv* env, jfloatArray arr);

SkString skString(JNIEnv* env, jstring str);
jstring javaString(JNIEnv* env, const SkString& str);

jobject javaFloat(JNIEnv* env, float val);

jbyteArray   javaByteArray  (JNIEnv* env, const std::vector<jbyte>& bytes);
jshortArray  javaShortArray (JNIEnv* env, const std::vector<jshort>& shorts);
jintArray    javaIntArray   (JNIEnv* env, const std::vector<jint>& ints);
jlongArray   javaLongArray  (JNIEnv* env, const std::vector<jlong>& longs);
jfloatArray  javaFloatArray (JNIEnv* env, const std::vector<float>& floats);

std::vector<SkString> skStringVector(JNIEnv* env, jobjectArray arr);
jobjectArray javaStringArray(JNIEnv* env, const std::vector<SkString>& strings);

void deleteJBytes(void* addr, void*);
