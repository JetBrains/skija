#include <jni.h>
#include "interop.hh"

namespace skija {
    namespace paragraph {
        namespace LineMetrics {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/paragraph/LineMetrics");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(JJJJZDDDDDDDJ)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace TextBox {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/paragraph/TextBox");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(FFFFI)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace DecorationStyle {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/paragraph/DecorationStyle");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(ZZZZIIF)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace Shadow {
            jclass cls;
            jmethodID ctor;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/paragraph/Shadow");
                cls  = static_cast<jclass>(env->NewGlobalRef(local));
                ctor = env->GetMethodID(cls, "<init>", "(IFFD)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        void onLoad(JNIEnv* env) {
            LineMetrics::onLoad(env);
            TextBox::onLoad(env);
            DecorationStyle::onLoad(env);
            Shadow::onLoad(env);
        }

        void onUnload(JNIEnv* env) {
            Shadow::onUnload(env);
            DecorationStyle::onUnload(env);
            TextBox::onUnload(env);
            LineMetrics::onUnload(env);
        } 
    }
}