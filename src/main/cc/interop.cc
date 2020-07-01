#include <iostream>
#include <array>
#include <memory>
#include <jni.h>
#include "interop.hh"
#include "paragraph/interop.hh"

namespace java {
    namespace lang {
        namespace Float {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("java/lang/Float");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(F)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace String {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("java/lang/String");
                cls = static_cast<jclass>(env->NewGlobalRef(local));
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }
    }
}

namespace skija {
    namespace FontAxisInfo {
        jclass    cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontAxisInfo");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(IILjava/lang/String;IFFF)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace FontFamilyName {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontFamilyName");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace FontMetrics {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontMetrics");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FFFFFFFFFFFLjava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;Ljava/lang/Float;)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace FontStyle {
        SkFontStyle fromJava(jint style) {
            return SkFontStyle(style & 0xFFFF, (style >> 16) & 0xFF, static_cast<SkFontStyle::Slant>((style >> 24) & 0xFF));
        }

        jint toJava(const SkFontStyle& fs) {
            return (static_cast<int>(fs.slant()) << 24)| (fs.width() << 16) | fs.weight();
        }
    }

    namespace FontVariation {
        jclass    cls;
        jmethodID ctor;
        jfieldID  tag;
        jfieldID  value;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontVariation");    
            cls   = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(IF)V");
            tag   = env->GetFieldID(cls, "_tag", "I");
            value = env->GetFieldID(cls, "_value", "F");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace IRect {
        jclass cls;
        jmethodID makeLTRB;
        jfieldID left;
        jfieldID top;
        jfieldID right;
        jfieldID bottom;    

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/IRect");
            cls      = static_cast<jclass>(env->NewGlobalRef(local));
            makeLTRB = env->GetStaticMethodID(cls, "makeLTRB", "(IIII)Lorg/jetbrains/skija/IRect;");
            left     = env->GetFieldID(cls, "_left",   "I");
            top      = env->GetFieldID(cls, "_top",    "I");
            right    = env->GetFieldID(cls, "_right",  "I");
            bottom   = env->GetFieldID(cls, "_bottom", "I");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject fromSkIRect(JNIEnv* env, const SkIRect& rect) {
            return env->CallStaticObjectMethod(cls, makeLTRB, rect.fLeft, rect.fTop, rect.fRight, rect.fBottom);
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

    namespace PathSegment {
        jclass cls;
        jmethodID ctorDone;
        jmethodID ctorMoveClose;
        jmethodID ctorLine;
        jmethodID ctorQuad;
        jmethodID ctorConic;
        jmethodID ctorCubic;

        void onLoad(JNIEnv* env) {
            jclass local  = env->FindClass("org/jetbrains/skija/PathSegment");
            cls           = static_cast<jclass>(env->NewGlobalRef(local));
            ctorDone      = env->GetMethodID(cls, "<init>", "()V");
            ctorMoveClose = env->GetMethodID(cls, "<init>", "(IFFZ)V");
            ctorLine      = env->GetMethodID(cls, "<init>", "(FFFFZZ)V");
            ctorQuad      = env->GetMethodID(cls, "<init>", "(FFFFFFZ)V");
            ctorConic     = env->GetMethodID(cls, "<init>", "(FFFFFFFZ)V");
            ctorCubic     = env->GetMethodID(cls, "<init>", "(FFFFFFFFZ)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace Point {
        jclass    cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Point");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FF)V");
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
            left     = env->GetFieldID(cls, "_left",   "F");
            top      = env->GetFieldID(cls, "_top",    "F");
            right    = env->GetFieldID(cls, "_right",  "F");
            bottom   = env->GetFieldID(cls, "_bottom", "F");
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

    namespace RRect {
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
            jclass local = env->FindClass("org/jetbrains/skija/RRect");
            cls      = static_cast<jclass>(env->NewGlobalRef(local));
            makeLTRB1 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFF)Lorg/jetbrains/skija/RRect;");
            makeLTRB2 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFFF)Lorg/jetbrains/skija/RRect;");
            makeLTRB4 = env->GetStaticMethodID(cls, "makeLTRB", "(FFFFFFFF)Lorg/jetbrains/skija/RRect;");
            makeNinePatchLTRB = env->GetStaticMethodID(cls, "makeNinePatchLTRB", "(FFFFFFFF)Lorg/jetbrains/skija/RRect;");
            makeComplexLTRB = env->GetStaticMethodID(cls, "makeComplexLTRB", "(FFFF[F)Lorg/jetbrains/skija/RRect;");
            left     = env->GetFieldID(cls, "_left",   "F");
            top      = env->GetFieldID(cls, "_top",    "F");
            right    = env->GetFieldID(cls, "_right",  "F");
            bottom   = env->GetFieldID(cls, "_bottom", "F");
            radii    = env->GetFieldID(cls, "_radii",  "[F");
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

    java::lang::Float::onLoad(env);
    java::lang::String::onLoad(env);

    skija::FontAxisInfo::onLoad(env);
    skija::FontFamilyName::onLoad(env);
    skija::FontMetrics::onLoad(env);
    skija::FontVariation::onLoad(env);
    skija::IRect::onLoad(env);
    skija::PathSegment::onLoad(env);
    skija::Point::onLoad(env);
    skija::Rect::onLoad(env);
    skija::RRect::onLoad(env);

    skija::paragraph::LineMetrics::onLoad(env);
    skija::paragraph::TextBox::onLoad(env);
    skija::paragraph::DecorationStyle::onLoad(env);
    skija::paragraph::Shadow::onLoad(env);
    skija::paragraph::FontFeature::onLoad(env);
    
    return JNI_VERSION_10;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_10) != JNI_OK)
        return;

    java::lang::Float::onUnload(env);
    java::lang::String::onUnload(env);

    skija::FontAxisInfo::onUnload(env);
    skija::FontFamilyName::onUnload(env);
    skija::FontMetrics::onUnload(env);
    skija::FontVariation::onUnload(env);
    skija::IRect::onUnload(env);
    skija::PathSegment::onUnload(env);
    skija::Point::onUnload(env);
    skija::Rect::onUnload(env);
    skija::RRect::onUnload(env);

    skija::paragraph::LineMetrics::onUnload(env);
    skija::paragraph::TextBox::onUnload(env);
    skija::paragraph::DecorationStyle::onUnload(env);
    skija::paragraph::Shadow::onUnload(env);
    skija::paragraph::FontFeature::onUnload(env);
}

std::unique_ptr<SkMatrix> skMatrix(JNIEnv* env, jfloatArray matrixArray) {
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
    // TODO fix UTF-8

    // void ParagraphStyle::setEllipsis(const std::u16string& ellipsis) {
    //     icu::UnicodeString unicode;
    //     unicode.setTo((UChar*)ellipsis.data());
    //     std::string str;
    //     unicode.toUTF8String(str);
    //     fEllipsis = SkString(str.c_str());
    // }

    jsize       len   = env->GetStringUTFLength(s);
    const char* chars = env->GetStringUTFChars(s, nullptr);
    SkString res(chars, len);
    env->ReleaseStringUTFChars(s, chars);
    return res;
}

jstring javaString(JNIEnv* env, const SkString& str) {
    return env->NewStringUTF(str.c_str());
}

jobject javaFloat(JNIEnv* env, float val) {
    return env->NewObject(java::lang::Float::cls, java::lang::Float::ctor, val);
}

jshortArray javaShortArray(JNIEnv* env, const std::vector<short>& shorts) {
    jshortArray res = env->NewShortArray(shorts.size());
    env->SetShortArrayRegion(res, 0, shorts.size(), shorts.data());
    return res;
}

jintArray javaIntArray(JNIEnv* env, const std::vector<int>& ints) {
    jintArray res = env->NewIntArray(ints.size());
    env->SetIntArrayRegion(res, 0, ints.size(), ints.data());
    return res;
}

jlongArray javaLongArray(JNIEnv* env, const std::vector<long>& longs) {
    jlongArray res = env->NewLongArray(longs.size());
    env->SetLongArrayRegion(res, 0, longs.size(), longs.data());
    return res;
}

jfloatArray javaFloatArray(JNIEnv* env, const std::vector<float>& floats) {
    jfloatArray res = env->NewFloatArray(floats.size());
    env->SetFloatArrayRegion(res, 0, floats.size(), floats.data());
    return res;
}

std::vector<SkString> skStringVector(JNIEnv* env, jobjectArray arr) {
    size_t len = env->GetArrayLength(arr);
    std::vector<SkString> res(len);
    for (int i = 0; i < len; ++i) {
        jstring str = static_cast<jstring>(env->GetObjectArrayElement(arr, i));
        res[i] = skString(env, str);
    }
    return res;
}

jobjectArray javaStringArray(JNIEnv* env, const std::vector<SkString>& strings) {
    jobjectArray res = env->NewObjectArray(strings.size(), java::lang::String::cls, nullptr);
    for (int i = 0; i < strings.size(); ++i)
        env->SetObjectArrayElement(res, i, javaString(env, strings[i]));
    return res;
}