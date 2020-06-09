#include <iostream>
#include <array>
#include <memory>
#include <jni.h>
#include "interop.hh"

jintArray javaIntArray(JNIEnv* env, std::vector<int> ints) {
    jintArray res = env->NewIntArray(ints.size());
    env->SetIntArrayRegion(res, 0, ints.size(), ints.data());
    return res;
}

jlongArray javaLongArray(JNIEnv* env, std::vector<long> longs) {
    jlongArray res = env->NewLongArray(longs.size());
    env->SetLongArrayRegion(res, 0, longs.size(), longs.data());
    return res;
}

jfloatArray javaFloatArray(JNIEnv* env, std::vector<float> floats) {
    jfloatArray res = env->NewFloatArray(floats.size());
    env->SetFloatArrayRegion(res, 0, floats.size(), floats.data());
    return res;
}

namespace skija {
    namespace FontAxisInfo {
        jclass    cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontAxisInfo");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(II[BIFFF)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace FontVariation {
        jclass   cls;
        jfieldID tag;
        jfieldID value;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontVariation");    
            cls   = static_cast<jclass>(env->NewGlobalRef(local));
            tag   = env->GetFieldID(cls, "tag", "I");
            value = env->GetFieldID(cls, "value", "F");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace IRect {
        jclass cls;
        jfieldID left;
        jfieldID top;
        jfieldID right;
        jfieldID bottom;    

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/IRect");
            cls    = static_cast<jclass>(env->NewGlobalRef(local));
            left   = env->GetFieldID(cls, "left",   "I");
            top    = env->GetFieldID(cls, "top",    "I");
            right  = env->GetFieldID(cls, "right",  "I");
            bottom = env->GetFieldID(cls, "bottom", "I");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        std::unique_ptr<SkIRect> toSkIRect(JNIEnv* env, jobject obj) {
            if (obj == nullptr)
                return std::unique_ptr<SkIRect>(nullptr);
            else {
                return std::unique_ptr<SkIRect>(new SkIRect{
                    env->GetIntField(obj, left), 
                    env->GetIntField(obj, top), 
                    env->GetIntField(obj, right), 
                    env->GetIntField(obj, bottom)
                });
            }
        }
    }

    namespace LineMetrics {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/LineMetrics");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(JJJJZDDDDDDDJ)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace Paragraph {
        namespace TextBox {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/Paragraph$TextBox");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(FFFFI)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }
    }

    namespace Path {
        namespace Segment {
            jclass cls;
            jmethodID ctor;
            jfieldID verb;
            jfieldID p0;
            jfieldID p1;
            jfieldID p2;
            jfieldID p3;
            jfieldID conicWeight;
            jfieldID isCloseLine;
            jfieldID isClosedContour;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/Path$Segment");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(I)V");
                verb = env->GetFieldID(cls, "verb", "Lorg/jetbrains/skija/Path$Verb;");
                p0   = env->GetFieldID(cls, "p0", "Lorg/jetbrains/skija/Point;");
                p1   = env->GetFieldID(cls, "p1", "Lorg/jetbrains/skija/Point;");
                p2   = env->GetFieldID(cls, "p2", "Lorg/jetbrains/skija/Point;");
                p3   = env->GetFieldID(cls, "p3", "Lorg/jetbrains/skija/Point;");
                conicWeight     = env->GetFieldID(cls, "conicWeight", "F");
                isCloseLine     = env->GetFieldID(cls, "isCloseLine", "Z");
                isClosedContour = env->GetFieldID(cls, "isClosedContour", "Z");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }
    }

    namespace Point {
        jclass    cls;
        jmethodID ctor;
        jfieldID  x;
        jfieldID  y;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Point");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FF)V");
            x    = env->GetFieldID(cls, "x", "F");
            y    = env->GetFieldID(cls, "y", "F");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject make(JNIEnv* env, float x, float y) {
            return env->NewObject(cls, ctor, x, y);
        }

        jobject fromSkPoint(JNIEnv* env, const SkPoint& p) {
            return env->NewObject(cls, ctor, p.fX, p.fY);
        }
    }

    namespace Rect {
        jclass cls;
        jmethodID makeLTRB;
        jfieldID left;
        jfieldID top;
        jfieldID right;
        jfieldID bottom;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Rect");
            cls      = static_cast<jclass>(env->NewGlobalRef(local));
            makeLTRB = env->GetStaticMethodID(cls, "makeLTRB", "(FFFF)Lorg/jetbrains/skija/Rect;");
            left     = env->GetFieldID(cls, "left",   "F");
            top      = env->GetFieldID(cls, "top",    "F");
            right    = env->GetFieldID(cls, "right",  "F");
            bottom   = env->GetFieldID(cls, "bottom", "F");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject fromLTRB(JNIEnv* env, float left, float top, float right, float bottom) {
            return env->CallStaticObjectMethod(cls, makeLTRB, left, top, right, bottom);
        }

        jobject fromSkRect(JNIEnv* env, const SkRect& rect) {
            return env->CallStaticObjectMethod(cls, makeLTRB, rect.fLeft, rect.fTop, rect.fRight, rect.fBottom);
        }
    }

    namespace RoundedRect {
        jclass cls;
        jmethodID makeLTRB1;
        jmethodID makeLTRB2;
        jmethodID makeLTRB4;
        jmethodID makeNinePatchLTRB;
        jmethodID makeComplexLTRB;
        jfieldID left;
        jfieldID top;
        jfieldID right;
        jfieldID bottom;
        jfieldID radii;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/RoundedRect");
            cls      = static_cast<jclass>(env->NewGlobalRef(local));
            makeLTRB1 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFF)Lorg/jetbrains/skija/RoundedRect;");
            makeLTRB2 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFFF)Lorg/jetbrains/skija/RoundedRect;");
            makeLTRB4 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFFFFF)Lorg/jetbrains/skija/RoundedRect;");
            makeNinePatchLTRB = env->GetStaticMethodID(cls, "makeNinePatchLTRB", "(FFFFFFFF)Lorg/jetbrains/skija/RoundedRect;");
            makeComplexLTRB = env->GetStaticMethodID(cls, "makeComplexLTRB", "(FFFF[F)Lorg/jetbrains/skija/RoundedRect;");
            left     = env->GetFieldID(cls, "left",   "F");
            top      = env->GetFieldID(cls, "top",    "F");
            right    = env->GetFieldID(cls, "right",  "F");
            bottom   = env->GetFieldID(cls, "bottom", "F");
            radii    = env->GetFieldID(cls, "radii",  "[F");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        SkRRect toSkRRect(JNIEnv* env, jfloat left, jfloat top, jfloat right, jfloat bottom, jfloatArray jradii) {
            SkRect rect {left, top, right, bottom};
            SkRRect rrect = SkRRect::MakeEmpty();
            jfloat* radii = env->GetFloatArrayElements(jradii, 0);
            switch (env->GetArrayLength(jradii)) {
                case 1:
                    rrect.setRectXY(rect, radii[0], radii[0]);
                    break;
                case 2:
                    rrect.setRectXY(rect, radii[0], radii[1]);
                    break;
                case 4: {
                    SkVector vradii[4] = {{radii[0], radii[0]}, {radii[1], radii[1]}, {radii[2], radii[2]}, {radii[3], radii[3]}};
                    rrect.setRectRadii(rect, vradii);
                    break;
                }
                case 8: {
                    SkVector vradii[4] = {{radii[0], radii[1]}, {radii[2], radii[3]}, {radii[4], radii[5]}, {radii[6], radii[7]}};
                    rrect.setRectRadii(rect, vradii);
                    break;
                }
            }
            env->ReleaseFloatArrayElements(jradii, radii, 0);
            return rrect;
        }

        jobject fromSkRRect(JNIEnv* env, const SkRRect& rr) {
            const SkRect& r = rr.rect();
            switch (rr.getType()) {
                case SkRRect::Type::kEmpty_Type:
                case SkRRect::Type::kRect_Type:
                    return env->CallStaticObjectMethod(cls, makeLTRB1, r.fLeft, r.fTop, r.fRight, r.fBottom, 0);

                case SkRRect::Type::kOval_Type:
                case SkRRect::Type::kSimple_Type: {
                    float rx = rr.getSimpleRadii().fX;
                    float ry = rr.getSimpleRadii().fY;
                    if (SkScalarNearlyEqual(rx, ry))
                        return env->CallStaticObjectMethod(cls, makeLTRB1, r.fLeft, r.fTop, r.fRight, r.fBottom, rx);
                    else
                        return env->CallStaticObjectMethod(cls, makeLTRB2, r.fLeft, r.fTop, r.fRight, r.fBottom, rx, ry);
                }

                case SkRRect::Type::kNinePatch_Type:
                    return env->CallStaticObjectMethod(cls, makeNinePatchLTRB,
                        r.fLeft, r.fTop, r.fRight, r.fBottom,
                        rr.radii(SkRRect::Corner::kUpperLeft_Corner).fX,
                        rr.radii(SkRRect::Corner::kUpperLeft_Corner).fY,
                        rr.radii(SkRRect::Corner::kLowerRight_Corner).fX,
                        rr.radii(SkRRect::Corner::kLowerRight_Corner).fY);

                case SkRRect::Type::kComplex_Type:
                    std::vector<float> radii = {
                        rr.radii(SkRRect::Corner::kUpperLeft_Corner).fX,
                        rr.radii(SkRRect::Corner::kUpperLeft_Corner).fY,
                        rr.radii(SkRRect::Corner::kUpperRight_Corner).fX,
                        rr.radii(SkRRect::Corner::kUpperRight_Corner).fY,
                        rr.radii(SkRRect::Corner::kLowerRight_Corner).fX,
                        rr.radii(SkRRect::Corner::kLowerRight_Corner).fY,
                        rr.radii(SkRRect::Corner::kLowerLeft_Corner).fX,
                        rr.radii(SkRRect::Corner::kLowerLeft_Corner).fY
                    };

                    return env->CallStaticObjectMethod(cls, makeComplexLTRB, r.fLeft, r.fTop, r.fRight, r.fBottom, javaFloatArray(env, radii));
            }
        }
    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_10) != JNI_OK)
        return JNI_ERR;

    skija::FontAxisInfo::onLoad(env);
    skija::FontVariation::onLoad(env);
    skija::IRect::onLoad(env);
    skija::LineMetrics::onLoad(env);
    skija::Paragraph::TextBox::onLoad(env);
    skija::Path::Segment::onLoad(env);
    skija::Point::onLoad(env);
    skija::Rect::onLoad(env);
    skija::RoundedRect::onLoad(env);
    
    return JNI_VERSION_10;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_10) != JNI_OK)
        return;

    skija::FontAxisInfo::onUnload(env);
    skija::FontVariation::onUnload(env);
    skija::IRect::onUnload(env);
    skija::LineMetrics::onUnload(env);
    skija::Paragraph::TextBox::onUnload(env);
    skija::Path::Segment::onUnload(env);
    skija::Point::onUnload(env);
    skija::Rect::onUnload(env);
    skija::RoundedRect::onUnload(env);
}

std::unique_ptr<SkMatrix> arrayToMatrix(JNIEnv* env, jfloatArray matrixArray) {
    if (matrixArray == nullptr)
        return std::unique_ptr<SkMatrix>(nullptr);
    else {
        jfloat* m = env->GetFloatArrayElements(matrixArray, 0);
        SkMatrix* ptr = new SkMatrix();
        ptr->setAll(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8]);
        env->ReleaseFloatArrayElements(matrixArray, m, 0);
        return std::unique_ptr<SkMatrix>(ptr);
    }
}

SkString skString(JNIEnv* env, jstring s) {
    jsize       len   = env->GetStringUTFLength(s);
    const char* chars = env->GetStringUTFChars(s, nullptr);
    SkString res(chars, len);
    env->ReleaseStringUTFChars(s, chars);
    return res;
}

jstring javaString(JNIEnv* env, const SkString& str) {
    return env->NewStringUTF(str.c_str());
}

SkFontStyle skFontStyle(jint style) {
    return SkFontStyle(style & 0xFFFF, (style >> 16) & 0xFF, static_cast<SkFontStyle::Slant>((style >> 24) & 0xFF));
}