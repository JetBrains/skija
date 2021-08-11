#include <array>
#include <cstring>
#include "interop.hh"
#include <iostream>
#include <jni.h>
#include <memory>
#include "shaper/interop.hh"
#include "src/utils/SkUTF.h"
#include "paragraph/interop.hh"

namespace java {
    namespace io {
        namespace OutputStream {
            jclass cls;
            jmethodID write;
            jmethodID flush;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("java/io/OutputStream");
                write = env->GetMethodID(cls, "write", "([BII)V");
                flush = env->GetMethodID(cls, "flush", "()V");
            }
        }
    }

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

        namespace RuntimeException {
            jclass cls;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("java/lang/RuntimeException");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
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

        namespace Throwable {
            jmethodID printStackTrace;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("java/lang/Throwable");
                printStackTrace = env->GetMethodID(cls, "printStackTrace", "()V");
            }

            bool exceptionThrown(JNIEnv* env) {
                if (env->ExceptionCheck()) {
                    auto th = skija::AutoLocal<jthrowable>(env, env->ExceptionOccurred());
                    env->CallVoidMethod(th.get(), printStackTrace);
                    env->ExceptionCheck(); // ignore
                    return true;
                } else
                    return false;
            }
        }
    }

    namespace util {
        namespace Iterator {
            jclass cls;
            jmethodID next;
            jmethodID hasNext;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("java/util/Iterator");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                next = env->GetMethodID(cls, "next", "()Ljava/lang/Object;");
                hasNext = env->GetMethodID(cls, "hasNext", "()Z");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace function {
            namespace BooleanSupplier {
                jclass cls;
                jmethodID apply;

                void onLoad(JNIEnv* env) {
                    jclass local = env->FindClass("java/util/function/BooleanSupplier");
                    cls   = static_cast<jclass>(env->NewGlobalRef(local));
                    apply = env->GetMethodID(cls, "getAsBoolean", "()Z");
                }

                void onUnload(JNIEnv* env) {
                    env->DeleteGlobalRef(cls);
                }
            }
        }
    }

    void onLoad(JNIEnv* env) {
        io::OutputStream::onLoad(env);
        lang::Float::onLoad(env);
        lang::RuntimeException::onLoad(env);
        lang::String::onLoad(env);
        lang::Throwable::onLoad(env);
        util::Iterator::onLoad(env);
        util::function::BooleanSupplier::onLoad(env);
    }

    void onUnload(JNIEnv* env) {
        util::function::BooleanSupplier::onUnload(env);
        util::Iterator::onUnload(env);
        lang::String::onUnload(env);
        lang::RuntimeException::onUnload(env);
        lang::Float::onUnload(env);
    }
}

namespace skija {
    namespace AnimationFrameInfo {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/AnimationFrameInfo");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(IIZIZIILorg/jetbrains/skija/IRect;)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject toJava(JNIEnv* env, const SkCodec::FrameInfo& i) {
            SkBlendMode blend;
            switch (i.fBlend) {
                case SkCodecAnimation::Blend::kSrcOver:
                    blend = SkBlendMode::kSrcOver;
                    break;
                case SkCodecAnimation::Blend::kSrc:
                    blend = SkBlendMode::kSrc;
                    break;
            }
            jobject res = env->NewObject(cls, ctor,
                                         i.fRequiredFrame,
                                         i.fDuration,
                                         i.fFullyReceived,
                                         static_cast<jint>(i.fAlphaType),
                                         i.fHasAlphaWithinBounds,
                                         static_cast<jint>(i.fDisposalMethod),
                                         static_cast<jint>(blend),
                                         IRect::fromSkIRect(env, i.fFrameRect));
            return java::lang::Throwable::exceptionThrown(env) ? nullptr : res;
        }
    }

    namespace Color4f {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Color4f");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FFFF)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace Drawable {
        jclass cls;
        jmethodID onDraw;
        jmethodID onGetBounds;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Drawable");
            cls = static_cast<jclass>(env->NewGlobalRef(local));
            onDraw = env->GetMethodID(cls, "_onDraw", "(J)V");
            onGetBounds = env->GetMethodID(cls, "onGetBounds", "()Lorg/jetbrains/skija/Rect;");
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

    namespace FontFeature {
        jclass cls;
        jmethodID ctor;
        jfieldID tag;
        jfieldID value;
        jfieldID start;
        jfieldID end;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontFeature");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;I)V");
            tag   = env->GetFieldID(cls, "_tag",   "I");
            value = env->GetFieldID(cls, "_value", "I");
            start = env->GetFieldID(cls, "_start", "J");
            end   = env->GetFieldID(cls, "_end",   "J");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        std::vector<SkShaper::Feature> fromJavaArray(JNIEnv* env, jobjectArray featuresArr) {
            jsize featuresLen = featuresArr == nullptr ? 0 : env->GetArrayLength(featuresArr);
            std::vector<SkShaper::Feature> features(featuresLen);
            for (int i = 0; i < featuresLen; ++i) {
                skija::AutoLocal<jobject> featureObj(env, env->GetObjectArrayElement(featuresArr, i));
                features[i] = {static_cast<SkFourByteTag>(env->GetIntField(featureObj.get(), skija::FontFeature::tag)),
                               static_cast<uint32_t>(env->GetIntField(featureObj.get(), skija::FontFeature::value)),
                               static_cast<size_t>(env->GetLongField(featureObj.get(), skija::FontFeature::start)),
                               static_cast<size_t>(env->GetLongField(featureObj.get(), skija::FontFeature::end))};
            }
            return features;
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

        jobject toJava(JNIEnv* env, const SkFontMetrics& m) {
            float f1, f2, f3, f4;
            return env->NewObject(cls, ctor,
                m.fTop,
                m.fAscent,
                m.fDescent,
                m.fBottom,
                m.fLeading,
                m.fAvgCharWidth,
                m.fMaxCharWidth,
                m.fXMin,
                m.fXMax,
                m.fXHeight,
                m.fCapHeight,
                m.hasUnderlineThickness(&f1) ? javaFloat(env, f1) : nullptr,
                m.hasUnderlinePosition(&f2)  ? javaFloat(env, f2) : nullptr,
                m.hasStrikeoutThickness(&f3) ? javaFloat(env, f3) : nullptr,
                m.hasStrikeoutPosition(&f4)  ? javaFloat(env, f4) : nullptr);
        }
    }

    namespace FontMgr {
        jclass cls;
        
        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontMgr");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
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

    namespace FontVariationAxis {
        jclass    cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/FontVariationAxis");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(IFFFZ)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace ImageInfo {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/ImageInfo");    
            cls   = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(IIIIJ)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject toJava(JNIEnv* env, const SkImageInfo& info) {
            return env->NewObject(cls, ctor,
                info.width(),
                info.height(),
                static_cast<jint>(info.colorType()),
                static_cast<jint>(info.alphaType()),
                reinterpret_cast<jlong>(info.refColorSpace().release()));
        }
    }

    namespace IPoint {
        jclass    cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/IPoint");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(II)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }

        jobject make(JNIEnv* env, jint x, jint y) {
            return env->NewObject(cls, ctor, x, y);
        }

        jobject fromSkIPoint(JNIEnv* env, const SkIPoint& p) {
            return env->NewObject(cls, ctor, p.fX, p.fY);
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
            jobject res = env->CallStaticObjectMethod(cls, makeLTRB, rect.fLeft, rect.fTop, rect.fRight, rect.fBottom);
            return java::lang::Throwable::exceptionThrown(env) ? nullptr : res;
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

    namespace Path {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Path");
            cls          = static_cast<jclass>(env->NewGlobalRef(local));
            ctor         = env->GetMethodID(cls, "<init>", "(J)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
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
        jfieldID x;
        jfieldID y;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/Point");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FF)V");
            x = env->GetFieldID(cls, "_x", "F");
            y = env->GetFieldID(cls, "_y", "F");
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

        jobjectArray fromSkPoints(JNIEnv* env, const std::vector<SkPoint>& ps) {
            jobjectArray res = env->NewObjectArray((jsize) ps.size(), cls, nullptr);
            for (int i = 0; i < ps.size(); ++i) {
                skija::AutoLocal<jobject> pointObj(env, fromSkPoint(env, ps[i]));
                env->SetObjectArrayElement(res, i, pointObj.get());
            }
            return res;
        }
    }

    namespace PaintFilterCanvas {
        JavaVM* _vm;
        jmethodID onFilterId;

        void onLoad(JNIEnv* env) {
            env->GetJavaVM(&_vm);
            jclass local = env->FindClass("org/jetbrains/skija/PaintFilterCanvas");
            onFilterId = env->GetMethodID(local, "onFilter", "(J)Z");
        }

        void onUnload(JNIEnv* env) {
        }

        bool onFilter(jobject obj, SkPaint& paint) {
            JNIEnv *env;
            _vm->AttachCurrentThread((void **) &env, NULL);
            jboolean result = env->CallBooleanMethod(obj, onFilterId, reinterpret_cast<jlong>(&paint));
            _vm->DetachCurrentThread();
            return result;
        }

        jobject attach(JNIEnv* env, jobject obj) {
            return env->NewGlobalRef(obj);
        }

        void detach(jobject obj) {
            JNIEnv *env;
            _vm->AttachCurrentThread((void **) &env, NULL);
            env->DeleteGlobalRef(obj);
            _vm->DetachCurrentThread();
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

        std::unique_ptr<SkRect> toSkRect(JNIEnv* env, jobject rectObj) {
            if (rectObj == nullptr)
                return std::unique_ptr<SkRect>(nullptr);
            else {
                SkRect* rect = new SkRect();
                rect->setLTRB(env->GetFloatField(rectObj, left), 
                              env->GetFloatField(rectObj, top), 
                              env->GetFloatField(rectObj, right), 
                              env->GetFloatField(rectObj, bottom));
                if (java::lang::Throwable::exceptionThrown(env))
                    return std::unique_ptr<SkRect>(nullptr);
                return std::unique_ptr<SkRect>(rect);
            }
        }

        jobject fromLTRB(JNIEnv* env, float left, float top, float right, float bottom) {
            jobject res = env->CallStaticObjectMethod(cls, makeLTRB, left, top, right, bottom);
            return java::lang::Throwable::exceptionThrown(env) ? nullptr : res;
        }

        jobject fromSkRect(JNIEnv* env, const SkRect& rect) {
            return fromLTRB(env, rect.fLeft, rect.fTop, rect.fRight, rect.fBottom);
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

            return nullptr;
        }
    }

   namespace RSXform {
        jclass cls;
        jmethodID ctor;

        void onLoad(JNIEnv* env) {
            jclass local = env->FindClass("org/jetbrains/skija/RSXform");
            cls  = static_cast<jclass>(env->NewGlobalRef(local));
            ctor = env->GetMethodID(cls, "<init>", "(FFFF)V");
        }

        void onUnload(JNIEnv* env) {
            env->DeleteGlobalRef(cls);
        }
    }

    namespace SamplingMode {
        SkSamplingOptions unpack(jlong val) {
            if (0x8000000000000000 & val) {
                val = val & 0x7FFFFFFFFFFFFFFF;
                float* ptr = reinterpret_cast<float*>(&val);
                return SkSamplingOptions(SkCubicResampler {ptr[1], ptr[0]});
            } else {
                int32_t filter = (int32_t) ((val >> 32) & 0xFFFFFFFF);
                int32_t mipmap = (int32_t) (val & 0xFFFFFFFF);
                return SkSamplingOptions(static_cast<SkFilterMode>(filter), static_cast<SkMipmapMode>(mipmap));
            }
        }
    }

    namespace SurfaceProps {
        jmethodID _getFlags;
        jmethodID _getPixelGeometryOrdinal;

        void onLoad(JNIEnv* env) {
            jclass cls = env->FindClass("org/jetbrains/skija/SurfaceProps");
            _getFlags = env->GetMethodID(cls, "_getFlags", "()I");
            _getPixelGeometryOrdinal = env->GetMethodID(cls, "_getPixelGeometryOrdinal", "()I");
        }

        std::unique_ptr<SkSurfaceProps> toSkSurfaceProps(JNIEnv* env, jobject surfacePropsObj) {
            if (surfacePropsObj == nullptr)
                return std::unique_ptr<SkSurfaceProps>(nullptr);
            uint32_t flags = static_cast<uint32_t>(env->CallIntMethod(surfacePropsObj, _getFlags));
            if (java::lang::Throwable::exceptionThrown(env))
                std::unique_ptr<SkSurfaceProps>(nullptr);
            SkPixelGeometry geom = static_cast<SkPixelGeometry>(env->CallIntMethod(surfacePropsObj, _getPixelGeometryOrdinal));
            if (java::lang::Throwable::exceptionThrown(env))
                std::unique_ptr<SkSurfaceProps>(nullptr);
            return std::make_unique<SkSurfaceProps>(flags, geom);
        }
    }

    namespace impl {
        namespace Native {
            jfieldID _ptr;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/impl/Native");
                _ptr = env->GetFieldID(cls, "_ptr", "J");
            }

            void* fromJava(JNIEnv* env, jobject obj, jclass cls) {
                if (env->IsInstanceOf(obj, cls)) {
                    jlong ptr = env->GetLongField(obj, skija::impl::Native::_ptr);
                    return reinterpret_cast<void*>(static_cast<uintptr_t>(ptr));
                }
                return nullptr;
            }
        }
    }

    void onLoad(JNIEnv* env) {
        AnimationFrameInfo::onLoad(env);
        Color4f::onLoad(env);
        Drawable::onLoad(env);
        FontFamilyName::onLoad(env);
        FontFeature::onLoad(env);
        FontMetrics::onLoad(env);
        FontVariation::onLoad(env);
        FontVariationAxis::onLoad(env);
        ImageInfo::onLoad(env);
        IPoint::onLoad(env);
        IRect::onLoad(env);
        Path::onLoad(env);
        PathSegment::onLoad(env);
        Point::onLoad(env);
        PaintFilterCanvas::onLoad(env);
        Rect::onLoad(env);
        RRect::onLoad(env);
        RSXform::onLoad(env);
        SurfaceProps::onLoad(env);
        
        impl::Native::onLoad(env);
    }

    void onUnload(JNIEnv* env) {
        RSXform::onUnload(env);
        RRect::onUnload(env);
        Rect::onUnload(env);
        PaintFilterCanvas::onUnload(env);
        Point::onUnload(env);
        PathSegment::onUnload(env);
        Path::onUnload(env);
        IRect::onUnload(env);
        IPoint::onUnload(env);
        ImageInfo::onUnload(env);
        FontVariationAxis::onUnload(env);
        FontVariation::onUnload(env);
        FontMetrics::onUnload(env);
        FontFeature::onUnload(env);
        FontFamilyName::onUnload(env);
        Drawable::onUnload(env);
        Color4f::onUnload(env);
        AnimationFrameInfo::onUnload(env);
    }
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

std::unique_ptr<SkM44> skM44(JNIEnv* env, jfloatArray matrixArray) {
    if (matrixArray == nullptr)
        return std::unique_ptr<SkM44>(nullptr);
    else {
        jfloat* m = env->GetFloatArrayElements(matrixArray, 0);
        SkM44* ptr = new SkM44(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], m[9], m[10], m[11], m[12], m[13], m[14], m[15]);
        env->ReleaseFloatArrayElements(matrixArray, m, 0);
        return std::unique_ptr<SkM44>(ptr);
    }
}

// bytes  range             bits  byte 1      byte 2      byte 3      byte 4      byte 5      byte 6
//
// Normal UTF-8:
//
// 1      U+     0..    7F  7     0xxxxxxx                     
// 2      U+    80..   7FF  11    110xxxxx    10xxxxxx                 
// 3      U+   800..  FFFF  16    1110xxxx    10xxxxxx    10xxxxxx             
// 4      U+ 10000..10FFFF  21    11110xxx    10xxxxxx    10xxxxxx    10xxxxxx             
//
// Modified UTF-8 (Java):
//
// 1      U+     1..    7F  7     0xxxxxxx                     
// 2      U+     0                11000000    10000000                 
// 2      U+    80..   7FF  11    110xxxxx    10xxxxxx                 
// 3      U+   800..  FFFF  16    1110xxxx    10xxxxxx    10xxxxxx             
// 6      U+ 10000..10FFFF  20    11101101    1010xxxx    10xxxxxx    11101101    1011xxxx    10xxxxxx  (+ 0x10000)

size_t utfToUtf8(unsigned char *data, size_t len) {
    size_t read_offset = 0;
    size_t write_offset = 0;
    while (read_offset < len) {
        unsigned char byte1 = data[read_offset];

        // single-byte U+0001..007F
        if ((byte1 & 0b10000000) == 0) {
            data[write_offset] = byte1;
            read_offset += 1;
            write_offset += 1;
            continue;
        }

        SkASSERT(read_offset + 1 < len);
        unsigned char byte2 = data[read_offset + 1];

        // two-byte U+0000
        if (byte1 == 0b11000000 && byte2 == 0b10000000) {
            data[write_offset] = 0;
            read_offset += 2;
            write_offset += 1;
            continue;
        }

        // two-byte U+0080..07FF
        if ((byte1 & 0b11100000) == 0b11000000) {
            SkASSERT((byte2 & 0b11000000) == 0b10000000);
            data[write_offset] = byte1;
            data[write_offset + 1] = byte2;
            read_offset += 2;
            write_offset += 2;
            continue;
        }

        SkASSERT(read_offset + 2 < len);
        unsigned char byte3 = data[read_offset + 2];

        // Six-byte modified UTF-8
        // 11101101    1010xxxx    10xxxxxx    11101101    1011xxxx    10xxxxxx
        if (byte1 == 0b11101101 && (byte2 & 0b11110000) == 0b10100000) {
            SkASSERT(read_offset + 5 < len);
            unsigned char byte4 = data[read_offset + 3];
            unsigned char byte5 = data[read_offset + 4];
            unsigned char byte6 = data[read_offset + 5];
            SkASSERT((byte3 & 0b11000000) == 0b10000000);
            SkASSERT(byte4 == 0b11101101);
            SkASSERT((byte5 & 0b11110000) == 0b10110000);
            SkASSERT((byte6 & 0b11000000) == 0b10000000);
            uint32_t codepoint = (((byte2 & 0b00001111) << 16) |
                                  ((byte3 & 0b00111111) << 10) | 
                                  ((byte5 & 0b00001111) << 6) | 
                                   (byte6 & 0b00111111))
                                 + 0x10000;
            // Four-byte UTF-8
            // 11110xxx    10xxxxxx    10xxxxxx    10xxxxxx
            data[write_offset]     = 0b11110000 | ((codepoint >> 18) & 0b00000111);
            data[write_offset + 1] = 0b10000000 | ((codepoint >> 12) & 0b00111111);
            data[write_offset + 2] = 0b10000000 | ((codepoint >> 6) & 0b00111111);
            data[write_offset + 3] = 0b10000000 | (codepoint & 0b00111111);

            read_offset += 6;
            write_offset += 4;
            continue;
        }

        // three-byte U+0800..FFFF
        if ((byte1 & 0b11110000) == 0b11100000) {
            SkASSERT((byte2 & 0b11000000) == 0b10000000);
            SkASSERT((byte3 & 0b11000000) == 0b10000000);
            data[write_offset] = byte1;
            data[write_offset + 1] = byte2;
            data[write_offset + 2] = byte3;
            read_offset += 3;
            write_offset += 3;
            continue;
        }
    }

    return write_offset;
}

SkString skString(JNIEnv* env, jstring s) {
    if (s == nullptr) {
        return SkString();
    } else {
        jsize utfUnits = env->GetStringUTFLength(s);
        jsize utf16Units = env->GetStringLength(s);
        SkString res(utfUnits);
        env->GetStringUTFRegion(s, 0, utf16Units, res.writable_str());
        size_t utf8Units = utfToUtf8((unsigned char *) res.writable_str(), utfUnits);
        res.resize(utf8Units);
        return res;
    }
}

jstring javaString(JNIEnv* env, const SkString& str) {
    return javaString(env, str.c_str(), str.size());
}

jstring javaString(JNIEnv* env, const char* chars, size_t len) {
    if (!chars || !len)
        return nullptr;
    int utf16Units = SkUTF::UTF8ToUTF16(nullptr, 0, chars, len);
    auto utf16 = std::unique_ptr<uint16_t[]>(new uint16_t[utf16Units]);
    SkUTF::UTF8ToUTF16(utf16.get(), utf16Units, chars, len);
    return env->NewString(utf16.get(), utf16Units);
}

jstring javaString(JNIEnv* env, const char* chars) {
    return chars ? javaString(env, chars, strlen(chars)) : nullptr;
}

jobject javaFloat(JNIEnv* env, SkScalar val) {
    return env->NewObject(java::lang::Float::cls, java::lang::Float::ctor, val);
}

jlong packTwoInts(int32_t a, int32_t b) {
    return (uint64_t (a) << 32) | b;
}

jlong packIPoint(SkIPoint p) {
    return packTwoInts(p.fX, p.fY);
}

jlong packISize(SkISize p) {
    return packTwoInts(p.fWidth, p.fHeight);
}

jbyteArray javaByteArray(JNIEnv* env, const std::vector<jbyte>& bytes) {
    jbyteArray res = env->NewByteArray((jsize) bytes.size());
    env->SetByteArrayRegion(res, 0, (jsize) bytes.size(), bytes.data());
    return res;
}

jshortArray javaShortArray(JNIEnv* env, const std::vector<jshort>& shorts) {
    jshortArray res = env->NewShortArray((jsize) shorts.size());
    env->SetShortArrayRegion(res, 0, (jsize) shorts.size(), shorts.data());
    return res;
}

jintArray javaIntArray(JNIEnv* env, const std::vector<jint>& ints) {
    jintArray res = env->NewIntArray((jsize) ints.size());
    env->SetIntArrayRegion( res, 0, (jsize) ints.size(), ints.data());
    return res;
}

jlongArray javaLongArray(JNIEnv* env, const std::vector<jlong>& longs) {
    jlongArray res = env->NewLongArray((jsize) longs.size());
    env->SetLongArrayRegion(res, 0, (jsize) longs.size(), longs.data());
    return res;
}

jfloatArray javaFloatArray(JNIEnv* env, const std::vector<jfloat>& floats) {
    jfloatArray res = env->NewFloatArray((jsize) floats.size());
    env->SetFloatArrayRegion(res, 0, (jsize) floats.size(), floats.data());
    return res;
}

std::vector<SkString> skStringVector(JNIEnv* env, jobjectArray arr) {
    if (arr == nullptr) {
        return std::vector<SkString>(0);
    } else {
        jsize len = env->GetArrayLength(arr);
        std::vector<SkString> res(len);
        for (jint i = 0; i < len; ++i) {
            jstring str = static_cast<jstring>(env->GetObjectArrayElement(arr, i));
            res[i] = skString(env, str);
            env->DeleteLocalRef(str);
        }
        return res;
    }
}

jobjectArray javaStringArray(JNIEnv* env, const std::vector<SkString>& strings) {
    jobjectArray res = env->NewObjectArray((jsize) strings.size(), java::lang::String::cls, nullptr);
    for (jint i = 0; i < (jsize) strings.size(); ++i) {
        skija::AutoLocal<jstring> str(env, javaString(env, strings[i]));
        env->SetObjectArrayElement(res, i, str.get());
    }
    return res;
}

void deleteJBytes(void* addr, void*) {
    delete[] (jbyte*) addr;
}

skija::UtfIndicesConverter::UtfIndicesConverter(const char* chars8, size_t len8):
  fStart8(chars8),
  fPtr8(chars8),
  fEnd8(chars8 + len8),
  fPos16(0)
{}

skija::UtfIndicesConverter::UtfIndicesConverter(const SkString& str):
  skija::UtfIndicesConverter::UtfIndicesConverter(str.c_str(), str.size())
{}

size_t skija::UtfIndicesConverter::from16To8(uint32_t i16) {
    if (i16 >= fPos16) {
        // if new i16 >= last fPos16, continue from where we started
    } else {
        fPtr8 = fStart8;
        fPos16 = 0;
    }

    while (fPtr8 < fEnd8 && fPos16 < i16) {
        SkUnichar u = SkUTF::NextUTF8(&fPtr8, fEnd8);
        fPos16 += (uint32_t) SkUTF::ToUTF16(u);
    }

    return fPtr8 - fStart8;
}

uint32_t skija::UtfIndicesConverter::from8To16(size_t i8) {
    if (i8 >= (size_t) (fPtr8 - fStart8)) {
        // if new i8 >= last fPtr8, continue from where we started
    } else {
        fPtr8 = fStart8;
        fPos16 = 0;
    }

    while (fPtr8 < fEnd8 && (size_t) (fPtr8 - fStart8) < i8) {
        SkUnichar u = SkUTF::NextUTF8(&fPtr8, fEnd8);
        fPos16 += (uint32_t) SkUTF::ToUTF16(u);
    }

    return fPos16;
}
