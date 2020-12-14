#pragma once
#include <jni.h>
#include <memory>
#include <vector>
#include "SkFontMetrics.h"
#include "SkFontStyle.h"
#include "SkMatrix.h"
#include "SkM44.h"
#include "SkPaint.h"
#include "SkRefCnt.h"
#include "SkRect.h"
#include "SkRRect.h"
#include "SkShaper.h"
#include "SkString.h"

namespace java {
    namespace lang {
        namespace Float {
            extern jclass cls;
            extern jmethodID ctor;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace String {
            extern jclass cls;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }
    }

    namespace util {
        namespace Iterator {
            extern jclass cls;
            extern jmethodID next;
            extern jmethodID hasNext;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
        }

        namespace function {
            namespace BooleanSupplier {
                extern jclass cls;
                extern jmethodID apply;
                void onLoad(JNIEnv* env);
                void onUnload(JNIEnv* env);
            }
        }
    }

    void onLoad(JNIEnv* env);
    void onUnload(JNIEnv* env);
}

namespace skija {
    namespace Color4f {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace Drawable {
        extern jclass cls;
        extern jmethodID onDraw;
        extern jmethodID onGetBounds;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }
    
    namespace FontFamilyName {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace FontFeature {
        extern jclass cls;
        extern jmethodID ctor;
        extern jfieldID tag;
        extern jfieldID value;
        extern jfieldID start;
        extern jfieldID end;

        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
        std::vector<SkShaper::Feature> fromJavaArray(JNIEnv* env, jobjectArray featuresArr);
    }

    namespace FontMetrics {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
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
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace FontVariationAxis {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace ImageInfo {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace IPoint {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
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
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
        jobject fromSkIRect(JNIEnv* env, const SkIRect& rect);
        std::unique_ptr<SkIRect> toSkIRect(JNIEnv* env, jobject obj);
    }

    namespace Path {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace PathSegment {
        extern jclass cls;
        extern jmethodID ctorDone;
        extern jmethodID ctorMoveClose;
        extern jmethodID ctorLine;
        extern jmethodID ctorQuad;
        extern jmethodID ctorConic;
        extern jmethodID ctorCubic;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace Point {
        extern jclass cls;
        extern jmethodID ctor;
        extern jfieldID x;
        extern jfieldID y;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
        jobject make(JNIEnv* env, float x, float y);
        jobject fromSkPoint(JNIEnv* env, const SkPoint& p);
        jobjectArray fromSkPoints(JNIEnv* env, const std::vector<SkPoint>& ps);
    }

    namespace PaintFilterCanvas {
        extern jmethodID onFilterId;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
        bool onFilter(jobject obj, SkPaint& paint);
        jobject attach(JNIEnv* env, jobject obj);
        void detach(jobject obj);
    }

    namespace Rect {
        extern jclass cls;
        extern jmethodID makeLTRB;
        extern jfieldID left;
        extern jfieldID top;
        extern jfieldID right;
        extern jfieldID bottom;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
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
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
        SkRRect toSkRRect(JNIEnv* env, jfloat left, jfloat top, jfloat right, jfloat bottom, jfloatArray jradii);
        jobject fromSkRRect(JNIEnv* env, const SkRRect& rect);   
    }

    namespace RSXform {
        extern jclass cls;
        extern jmethodID ctor;
        void onLoad(JNIEnv* env);
        void onUnload(JNIEnv* env);
    }

    namespace impl {
        namespace Native {
            extern jfieldID _ptr;
            void onLoad(JNIEnv* env);
            void onUnload(JNIEnv* env);
            void* fromJava(JNIEnv* env, jobject obj, jclass cls);
        }
    }

    void onLoad(JNIEnv* env);
    void onUnload(JNIEnv* env);

    class UtfIndicesConverter {
    public:
        UtfIndicesConverter(const char* chars8, size_t len8);
        UtfIndicesConverter(const SkString& s);

        const char* fStart8;
        const char* fPtr8;
        const char* fEnd8;
        uint32_t fPos16;

        size_t from16To8(uint32_t i16);
        uint32_t from8To16(size_t i8);
    };
}

std::unique_ptr<SkMatrix> skMatrix(JNIEnv* env, jfloatArray arr);
std::unique_ptr<SkM44> skM44(JNIEnv* env, jfloatArray arr);

SkString skString(JNIEnv* env, jstring str);
jstring javaString(JNIEnv* env, const SkString& str);
jstring javaString(JNIEnv* env, const char* chars, size_t len);
jstring javaString(JNIEnv* env, const char* chars);

jobject javaFloat(JNIEnv* env, float val);
jlong packTwoInts(int32_t a, int32_t b);
jlong packIPoint(SkIPoint p);
jlong packISize(SkISize s);

jbyteArray   javaByteArray  (JNIEnv* env, const std::vector<jbyte>& bytes);
jshortArray  javaShortArray (JNIEnv* env, const std::vector<jshort>& shorts);
jintArray    javaIntArray   (JNIEnv* env, const std::vector<jint>& ints);
jlongArray   javaLongArray  (JNIEnv* env, const std::vector<jlong>& longs);
jfloatArray  javaFloatArray (JNIEnv* env, const std::vector<float>& floats);

std::vector<SkString> skStringVector(JNIEnv* env, jobjectArray arr);
jobjectArray javaStringArray(JNIEnv* env, const std::vector<SkString>& strings);

void deleteJBytes(void* addr, void*);

// On top level to allow instance sharing accross template instantiations.
static jfieldID ptrFieldId = nullptr;
template <typename T>
T* getNativePtr(JNIEnv* env, jobject native) {
    if (!ptrFieldId) {
        jclass nativeClass = env->FindClass("org/jetbrains/skija/impl/Native");
        ptrFieldId = env->GetFieldID(nativeClass, "_ptr", "J");
    }
    return reinterpret_cast<T*>(
        static_cast<uintptr_t>(native ? env->GetLongField(native, ptrFieldId) : 0));
}
