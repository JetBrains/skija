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

        namespace Paragraph {
            namespace TextBox {
                jclass cls;
                jmethodID ctor;

                void onLoad(JNIEnv* env) {
                    jclass local = env->FindClass("org/jetbrains/skija/paragraph/Paragraph$TextBox");
                    cls  = static_cast<jclass>(env->NewGlobalRef(local));
                    ctor = env->GetMethodID(cls, "<init>", "(FFFFI)V");
                }

                void onUnload(JNIEnv* env) {
                    env->DeleteGlobalRef(cls);
                }
            }
        }

        namespace TextStyle {
            namespace Decoration {
                jclass cls;
                jmethodID ctor;

                void onLoad(JNIEnv* env) {
                    jclass local = env->FindClass("org/jetbrains/skija/paragraph/TextStyle$Decoration");
                    cls  = static_cast<jclass>(env->NewGlobalRef(local));
                    ctor = env->GetMethodID(cls, "<init>", "(ZZZIIIF)V");
                }

                void onUnload(JNIEnv* env) {
                    env->DeleteGlobalRef(cls);
                }
            }

            namespace Shadow {
                jclass cls;
                jmethodID ctor;

                void onLoad(JNIEnv* env) {
                    jclass local = env->FindClass("org/jetbrains/skija/paragraph/TextStyle$Shadow");
                    cls  = static_cast<jclass>(env->NewGlobalRef(local));
                    ctor = env->GetMethodID(cls, "<init>", "(IFFD)V");
                }

                void onUnload(JNIEnv* env) {
                    env->DeleteGlobalRef(cls);
                }
            }

            namespace FontFeature {
                jclass cls;
                jmethodID ctor;

                void onLoad(JNIEnv* env) {
                    jclass local = env->FindClass("org/jetbrains/skija/paragraph/TextStyle$FontFeature");
                    cls  = static_cast<jclass>(env->NewGlobalRef(local));
                    ctor = env->GetMethodID(cls, "<init>", "(Ljava/lang/String;I)V");
                }

                void onUnload(JNIEnv* env) {
                    env->DeleteGlobalRef(cls);
                }
            }
        }
    }
}