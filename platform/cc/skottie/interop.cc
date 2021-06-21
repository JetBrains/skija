#include <jni.h>
#include "interop.hh"

namespace skija {
    namespace skottie {
        namespace Logger {
            jclass cls;
            jmethodID log;

            void onLoad(JNIEnv* env) {
                jclass local = env->FindClass("org/jetbrains/skija/skottie/Logger");
                cls = static_cast<jclass>(env->NewGlobalRef(local));
                log = env->GetMethodID(cls, "log", "(Lorg/jetbrains/skija/skottie/LogLevel;Ljava/lang/String;Ljava/lang/String;)V");
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(cls);
            }
        }

        namespace LogLevel {
            jobject WARNING;
            jobject ERROR;

            void onLoad(JNIEnv* env) {
                jclass cls = env->FindClass("org/jetbrains/skija/skottie/LogLevel");
                jfieldID warningField = env->GetStaticFieldID(cls, "WARNING", "Lorg/jetbrains/skija/skottie/LogLevel;");
                WARNING = env->NewGlobalRef(env->GetStaticObjectField(cls, warningField));

                jfieldID errorField = env->GetStaticFieldID(cls, "ERROR", "Lorg/jetbrains/skija/skottie/LogLevel;");
                ERROR = env->NewGlobalRef(env->GetStaticObjectField(cls, errorField));
            }

            void onUnload(JNIEnv* env) {
                env->DeleteGlobalRef(WARNING);
                env->DeleteGlobalRef(ERROR);
            }
        }

        void onLoad(JNIEnv* env) {
            Logger::onLoad(env);
            LogLevel::onLoad(env);
        }

        void onUnload(JNIEnv* env) {
            Logger::onUnload(env);
            LogLevel::onUnload(env);
        }
    }
}