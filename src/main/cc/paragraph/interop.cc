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
    }
}